package it.redhat.kaprecar.service;

import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.IterationDetails;
import it.redhat.kaprecar.domain.KaprecarComputation;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@ApplicationScoped
public class CalculateService {
    private static final Logger LOG = Logger.getLogger(CalculateService.class);

    private static final int KAPREKAR = 6174;

    public Uni<KaprecarComputation> calculateIterations(int number) {
        LOG.infof("Calculating kaprecar computations for number %s", number);
        return Uni.createFrom().item(number).map(n -> {
            List<IterationDetails> details = new ArrayList<>();
            LOG.debugf("number %s will converge to Kaprekar constant in ...", number);
            int iterations = convergeToKaprekar(number, 0, details);
            LOG.debugf("...%s iterations!", iterations);
            return new KaprecarComputation(number, iterations, details);
        });
    }

    private int convergeToKaprekar(int number, int iteration, List<IterationDetails> details) {
        if (number == KAPREKAR) {
            return iteration;
        }
        char[] arr = String.valueOf(number).toCharArray();

        List<String> digits = IntStream.range(0, arr.length).mapToObj(i -> String.valueOf(arr[i])).toList();

        String descNumber = digits.stream().sorted(Comparator.reverseOrder()).collect(Collectors.joining());
        String ascNumber = digits.stream().sorted().collect(Collectors.joining());
        int major = Integer.parseInt(descNumber);
        int minor = Integer.parseInt(ascNumber);
        int result = major - minor;

        LOG.debugf("\t%s - %s = %s ", major, minor, result);

        final int nextIteration = iteration + 1;
        details.add(new IterationDetails(nextIteration, String.format("%d - %d", major, minor), result));

        return convergeToKaprekar(result, nextIteration, details);
    }

}
