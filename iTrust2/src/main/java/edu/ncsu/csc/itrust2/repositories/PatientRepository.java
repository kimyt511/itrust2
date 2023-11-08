package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.ncsu.csc.iTrust2.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	@Query(value = "select * from user where user.first_name like %:keyword% or user.last_name like %:keyword%", nativeQuery = true)
	List<Patient> findByNameContaining(@Param(value = "keyword")String keyword);

}
