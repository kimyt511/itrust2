package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class OfficeVisitService extends Service {

    private final OfficeVisitRepository officeVisitRepository;

    private final UserService userService;

    private final AppointmentRequestService appointmentRequestService;

    private final HospitalService hospitalService;

    private final BasicHealthMetricsService bhmService;

    private final PrescriptionService prescriptionService;

    private final DiagnosisService diagnosisService;

    private final ProcedureService procedureService;

    @Override
    protected JpaRepository getRepository() {
        return officeVisitRepository;
    }

    public List<OfficeVisit> findByHcp(final User hcp) {
        return officeVisitRepository.findByHcp(hcp);
    }

    public List<OfficeVisit> findByPatient(final User patient) {
        return officeVisitRepository.findByPatient(patient);
    }

    public List<OfficeVisit> findByHcpAndPatient(final User hcp, final User patient) {
        return officeVisitRepository.findByHcpAndPatient(hcp, patient);
    }

    public OfficeVisit build(final OfficeVisitForm ovf) {
        final OfficeVisit ov = new OfficeVisit();

        ov.setPatient(userService.findByName(ovf.getPatient()));
        ov.setHcp(userService.findByName(ovf.getHcp()));
        ov.setNotes(ovf.getNotes());

        if (ovf.getId() != null) {
            ov.setId(Long.parseLong(ovf.getId()));
        }

        final ZonedDateTime visitDate = ZonedDateTime.parse(ovf.getDate());
        ov.setDate(visitDate);

        AppointmentType at = null;
        try {
            at = AppointmentType.valueOf(ovf.getType());
        } catch (final NullPointerException npe) {
            at = AppointmentType.GENERAL_CHECKUP; /*
                                                   * If for some reason we don't
                                                   * have a type, default to
                                                   * general checkup
                                                   */
        }
        ov.setType(at);

        if (null != ovf.getPreScheduled()) {
            final List<AppointmentRequest> requests =
                    appointmentRequestService.findByHcpAndPatient(ov.getHcp(), ov.getPatient());
            try {
                final AppointmentRequest match =
                        requests.stream()
                                .filter(e -> e.getDate().equals(ov.getDate()))
                                .collect(Collectors.toList())
                                .get(0); /*
                                    * We should have one and only one
                                    * appointment for the provided HCP & patient
                                    * and the time specified
                                    */
                ov.setAppointment(match);
            } catch (final Exception e) {
                throw new IllegalArgumentException(
                        "Marked as preschedule but no match can be found" + e);
            }
        }
        ov.setHospital(hospitalService.findByName(ovf.getHospital()));
        ov.setBasicHealthMetrics(bhmService.build(ovf));

        // associate all diagnoses with this visit
        if (ovf.getDiagnoses() != null) {
            ov.setDiagnoses(
                    ovf.getDiagnoses().stream()
                            .map(diagnosisService::build)
                            .collect(Collectors.toList()));
            for (final Diagnosis d : ov.getDiagnoses()) {
                d.setVisit(ov);
            }
        }

        ov.validateDiagnoses();

        final List<PrescriptionForm> ps = ovf.getPrescriptions();
        if (ps != null) {
            ov.setPrescriptions(
                    ps.stream().map(prescriptionService::build).collect(Collectors.toList()));
        }

        final List<ProcedureForm> pc = ovf.getProcedures();
        if (pc != null) {
            ov.setProcedures(
                    pc.stream().map(procedureService::build).collect(Collectors.toList()));
        }


        final Patient p = (Patient) ov.getPatient();
        if (p == null || p.getDateOfBirth() == null) {
            return ov; // we're done, patient can't be tested against
        }
        final LocalDate dob = p.getDateOfBirth();
        int age = ov.getDate().getYear() - dob.getYear();
        // Remove the -1 when changing the dob to OffsetDateTime
        if (ov.getDate().getMonthValue() < dob.getMonthValue()) {
            age -= 1;
        } else if (ov.getDate().getMonthValue() == dob.getMonthValue()) {
            if (ov.getDate().getDayOfMonth() < dob.getDayOfMonth()) {
                age -= 1;
            }
        }

        if (age < 3) {
            ov.validateUnder3();
        } else if (age < 12) {
            ov.validateUnder12();
        } else {
            ov.validate12AndOver();
        }

        return ov;
    }
}
