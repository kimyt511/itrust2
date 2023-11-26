package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    boolean existsByName(String name);

    Vaccine findByName(String name);

    Vaccine findByCptCode(String cptCode);
}
