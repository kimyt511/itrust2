package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Vaccine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    Vaccine findByName(String name);

    Vaccine findByAbbreviation(String abbreviation);

    Vaccine findByCptCode(String cptCode);
}
