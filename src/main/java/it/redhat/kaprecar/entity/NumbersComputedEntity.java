package it.redhat.kaprecar.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "COMPUTED_NUMBERS")
@Cacheable
public class NumbersComputedEntity extends PanacheEntity {

    @Column(name = "NUMBER", length = 4, unique = true)
    public int computedNumber;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<IterationEntity> iterations;

}
