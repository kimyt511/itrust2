package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.PersonalRepresentative;
import edu.ncsu.csc.itrust2.models.User;

import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalRepresentativeRepository extends JpaRepository<PersonalRepresentative, Long> {

    List<PersonalRepresentative> findByPatient(@NotNull User patient);

    List<PersonalRepresentative> findByRepresentative(@NotNull User representative);

    boolean existsByPatientAndRepresentative(@NotNull User patient, @NotNull User Representative);
}
