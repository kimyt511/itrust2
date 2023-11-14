package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.ncsu.csc.iTrust2.models.Diagnosis;
import edu.ncsu.csc.iTrust2.models.OfficeVisit;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    public List<Diagnosis> findByVisit ( OfficeVisit visit );
    
    @Query(value = "SELECT d.id FROM diagnoses d " +
            "JOIN office_visit_diagnoses ovd ON d.id = ovd.diagnoses_id " +
            "JOIN office_visit v ON ovd.office_visit_id = v.id " +
            "WHERE v.patient_id = :patientId AND v.date >= DATE_SUB(CURDATE(), INTERVAL 60 DAY)" +
            "ORDER BY v.date DESC", nativeQuery = true)
    List<Long> findDiagnosisIdsForPatientLast60Days(@Param("patientId") String patientId);

}
