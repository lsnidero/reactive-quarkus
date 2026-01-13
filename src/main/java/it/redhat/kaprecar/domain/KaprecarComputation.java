package it.redhat.kaprecar.domain;

import java.util.List;

public record KaprecarComputation(int number, int iterations, List<IterationDetails> details) {
}
