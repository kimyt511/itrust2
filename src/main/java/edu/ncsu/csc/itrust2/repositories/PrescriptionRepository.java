package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatient(@NotNull User patient);

    @Query(value = "SELECT p.id FROM prescription p " +
            "JOIN office_visit_prescriptions ovp ON p.id = ovp.prescriptions_id " +
            "JOIN office_visit v ON ovp.office_visit_id = v.id " +
            "WHERE p.patient_id = :patientId AND v.date >= DATE_SUB(CURDATE(), INTERVAL 90 DAY)" +
            "ORDER BY v.date DESC", nativeQuery = true)
    List<Long> findPrescriptionIdsForPatientLast90Days(@Param("patientId") String patientId);

}
