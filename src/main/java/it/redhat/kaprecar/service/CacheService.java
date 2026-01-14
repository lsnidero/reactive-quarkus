package it.redhat.kaprecar.service;

import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.IterationDetail;
import it.redhat.kaprecar.domain.KaprecarComputation;
import it.redhat.kaprecar.entity.IterationEntity;
import it.redhat.kaprecar.entity.NumbersComputedEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CacheService {
    private static final Logger LOG = Logger.getLogger(CacheService.class);
    private static final Pattern compile = Pattern.compile("([0-9]+) - ([0-9]+)");

    private CalculateService calculateService;

    public CacheService(CalculateService calculateService) {
        this.calculateService = calculateService;
    }


    public Uni<KaprecarComputation> calculate(int number) {
        LOG.infof("calculate %d", number);
        return findNumber(number).chain(entity -> {
            if (entity != null) {
                LOG.infof("cache found, get result for %d", number);
                KaprecarComputation kaprecarComputation = fromEntity(entity);
                return Uni.createFrom().item(kaprecarComputation);
            } else {
                LOG.infof("cache miss, calculate %d", number);
                return calculateService.calculateIterations(number).call(computation -> insertInCache(computation));
            }
        });
    }


    private Uni<NumbersComputedEntity> findNumber(int number) {
        LOG.info("findInCahe");
        return NumbersComputedEntity.findByNumber(number);
    }


    private Uni<Void> insertInCache(KaprecarComputation computation) {
        LOG.info("insertInCache");
        NumbersComputedEntity numbersComputedEntity = toEntity(computation);
        return numbersComputedEntity.persist().replaceWithVoid();
    }

    // Static converters

    private static KaprecarComputation fromEntity(NumbersComputedEntity entity) {
        List<IterationDetail> details = entity.iterations.stream().map(i -> new IterationDetail(i.index, String.format("%d - %d", i.major, i.minor), i.major - i.minor)).toList();
        return new KaprecarComputation(entity.computedNumber, details.size(), details);
    }

    private static NumbersComputedEntity toEntity(KaprecarComputation computation) {
        List<IterationEntity> iterationEntities = computation.details().stream().map(i -> {
            Matcher matcher = compile.matcher(i.operation());
            String major = "0";
            String minor = "0";
            if (matcher.find()) {
                major = matcher.group(1);
                minor = matcher.group(2);
            }
            LOG.debugf("major: %s, minor: %s", major, minor);
            IterationEntity iterationEntity = new IterationEntity();
            iterationEntity.major = Integer.parseInt(major);
            iterationEntity.minor = Integer.parseInt(minor);
            iterationEntity.index = i.index();
            return iterationEntity;

        }).toList();
        NumbersComputedEntity numbersComputedEntity = new NumbersComputedEntity();
        numbersComputedEntity.computedNumber = computation.number();
        numbersComputedEntity.iterations = iterationEntities;
        return numbersComputedEntity;
    }

}
