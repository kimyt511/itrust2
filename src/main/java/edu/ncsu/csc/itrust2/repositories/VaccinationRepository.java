package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface VaccinationRepository  extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findByPatient(@NotNull User patient);
}
