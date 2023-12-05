package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findByVisit(@NotNull OfficeVisit visit);

    @Query(value = "SELECT d.id FROM diagnosis d " +
            "JOIN office_visit_diagnoses ovd ON d.id = ovd.diagnoses_id " +
            "JOIN office_visit v ON ovd.office_visit_id = v.id " +
            "WHERE v.patient_id = :patientId AND v.date >= DATE_SUB(CURDATE(), INTERVAL 60 DAY)" +
            "ORDER BY v.date DESC", nativeQuery = true)
    List<Long> findDiagnosisIdsForPatientLast60Days(@Param("patientId") String patientId);

    @Query(value = "SELECT d.id FROM diagnosis d " +
            "JOIN office_visit_diagnoses ovd ON d.id = ovd.diagnoses_id " +
            "JOIN office_visit v ON ovd.office_visit_id = v.id " +
            "WHERE v.patient_id = :patientId " +
            "ORDER BY v.date DESC", nativeQuery = true)
    List<Long> findDiagnosisIdsForPatient(@Param("patientId") String patientId);

}

