package it.redhat.kaprecar.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity(name = "COMPUTED_NUMBERS")
@Cacheable
public class NumbersComputedEntity extends PanacheEntity {

    @Column(name = "NUMBER", length = 4, unique = true)
    public int computedNumber;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    public List<IterationEntity> iterations;

    public static Uni<NumbersComputedEntity> findByNumber(int number) {
        return find("computedNumber", number).firstResult();
    }


}
