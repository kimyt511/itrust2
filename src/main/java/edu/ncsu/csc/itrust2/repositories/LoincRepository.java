package edu.ncsu.csc.itrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.Loinc;

public interface LoincRepository extends JpaRepository<Loinc, Long> {

    public boolean existsByCode ( String code );

    public Loinc findByCode ( String code );

}
