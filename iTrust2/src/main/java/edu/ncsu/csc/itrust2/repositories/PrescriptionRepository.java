package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.ncsu.csc.iTrust2.models.Prescription;
import edu.ncsu.csc.iTrust2.models.User;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    public List<Prescription> findByPatient ( final User patient );
    
    @Query(value = "SELECT p.id FROM prescription p " +
            "JOIN office_visit_prescriptions ovp ON p.id = ovp.prescriptions_id " +
            "JOIN office_visit v ON ovp.office_visit_id = v.id " +
            "WHERE p.patient_id = :patientId AND v.date >= DATE_SUB(CURDATE(), INTERVAL 90 DAY)" + 
            "ORDER BY v.date DESC", nativeQuery = true)
    List<Long> findPrescriptionIdsForPatientLast90Days(@Param("patientId") String patientId);

}
