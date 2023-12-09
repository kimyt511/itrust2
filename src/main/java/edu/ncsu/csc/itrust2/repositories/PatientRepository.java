package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "select * from user where concat(user.first_name, ' ', user.last_name) like %:keyword% ", nativeQuery = true)
    List<Patient> findByNameContaining(@Param(value = "keyword")String keyword);

    List<Patient> findByUsernameContaining(String keyword);

}
