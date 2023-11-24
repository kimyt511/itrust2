package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.DiagnosisForm;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class DiagnosisService extends Service {

    private final DiagnosisRepository repository;

    private final ICDCodeService icdCodeService;

    private final OfficeVisitRepository officeVisitRepository;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public Diagnosis build(final DiagnosisForm form) {
        final Diagnosis diag = new Diagnosis();
        if(form.getVisit() != null){
            diag.setVisit(officeVisitRepository.getById(form.getVisit()));
        }
        diag.setNote(form.getNote());
        diag.setCode(icdCodeService.findByCode(form.getCode()));
        diag.setId(form.getId());

        return diag;
    }

    public List<Diagnosis> findByPatient(final User patient) {
        return officeVisitRepository.findByPatient(patient).stream()
                .map(e -> findByVisit(e))
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());
    }

    public List<Diagnosis> findByVisit(final OfficeVisit visit) {
        return repository.findByVisit(visit);
    }

    public List<Long> findByUserName ( final String patientId ) {
        return repository.findDiagnosisIdsForPatientLast60Days(patientId);
    }
}
