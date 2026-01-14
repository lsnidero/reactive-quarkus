package it.redhat.kaprecar.domain;

import java.util.List;

public record KaprecarComputation(int number, int iterations, List<IterationDetail> details) {
    //FIXME: need to understand how to treat equals and hashcode con the list

}
