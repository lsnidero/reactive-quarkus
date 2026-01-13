package it.redhat.kaprecar.service;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.KaprecarComputation;
import it.redhat.kaprecar.entity.IterationEntity;
import it.redhat.kaprecar.entity.NumbersComputedEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ApplicationScoped
public class CacheService {
    private static final Logger LOG = Logger.getLogger(CacheService.class);

    public Uni<Void> insertInCache(KaprecarComputation computation) {
        final Pattern compile = Pattern.compile("([0-9]+) - ([0-9]+)");

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

        return numbersComputedEntity.persist().replaceWithVoid();
    }
}
