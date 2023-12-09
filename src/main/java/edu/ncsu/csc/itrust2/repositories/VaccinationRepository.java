package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Vaccine;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findByPatient(User patient);

    List<Vaccination> findByVaccine(Vaccine vaccine);
}
