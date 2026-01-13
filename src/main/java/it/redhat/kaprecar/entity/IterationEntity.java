package it.redhat.kaprecar.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "ITERATION_DETAILS")
@Cacheable
public class IterationEntity extends PanacheEntity {

    @Column(length = 1)
    public int index;
    @Column(length = 4)
    public int major;
    @Column(length = 4)
    public int minor;
}
