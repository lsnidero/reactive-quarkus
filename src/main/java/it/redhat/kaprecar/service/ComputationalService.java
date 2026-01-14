package it.redhat.kaprecar.service;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.IterationDetail;
import it.redhat.kaprecar.domain.KaprecarComputation;
import it.redhat.kaprecar.entity.IterationEntity;
import it.redhat.kaprecar.entity.NumbersComputedEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class ComputationalService {
    private static final Logger LOG = Logger.getLogger(ComputationalService.class);
    private static final Pattern compile = Pattern.compile("([0-9]+) - ([0-9]+)");

    private CalculateService calculateService;

    @Inject
    @Channel("computations-out")
    Emitter<String> emitter;


    public ComputationalService(CalculateService calculateService) {
        this.calculateService = calculateService;
    }


    @Incoming("computations-in")
    Uni<Void> executeCalculation(Message<String> numberMessage) {
        LOG.infof("Received message for number ", numberMessage.getPayload());

        return Panache.withTransaction(() ->
                calculate(Integer.parseInt(numberMessage.getPayload())).replaceWithVoid());
    }


    public CompletionStage<Void> enqueue(int number) {
        return emitter.send(String.valueOf(number));
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
                return Uni.createFrom().item(calculateService.calculateIterations(number)).call(this::insertInCache);
            }
        });
    }

    public Uni<List<KaprecarComputation>> allComputed() {
        return NumbersComputedEntity.<NumbersComputedEntity>listAll().map(list -> list.stream().map(ComputationalService::fromEntity).toList());
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
