package edu.ncsu.csc.itrust2.services;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import edu.ncsu.csc.itrust2.repositories.ProcedureRepository;

@Component
@Transactional
@RequiredArgsConstructor
public class ProcedureService extends Service {

    @Autowired
    private ProcedureRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private OfficeVisitRepository officeVisitRepository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }


    /*
    Procedure build will be made to be in sync just as in PrescriptionService or DiagnosisService.
    Create setters and getters in relation to the procedure form.
    This method is created in other files, this was made for the sake of harmony

     */
    public Procedure build(final ProcedureForm form) {
        final Procedure pr = new Procedure();


        pr.setCode (form.getCode() );
        pr.setName( form.getName());
        pr.setHcp(form.getHcp());
        pr.setLabtech(form.getLabtech());
        pr.setPatient(form.getPatient());
        pr.setComment(form.getComment());
        pr.setPriority(form.getPriority());
        pr.setProcedureStatus(form.getProcedureStatus());
        if (form.getId() != null) {
            pr.setId(form.getId());
        }

        return pr;
    }

    public List<Procedure> findByPatient(final User patient) {
        return officeVisitRepository.findByPatient(patient).stream()
                .map(e -> findByVisit(e))
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());
    }

    public boolean existsByLabtech ( final User labtech ) {
        return repository.existsByLabtech( labtech );
    }

    public List<Procedure> findByLabtech ( final User labtech ) {
        return repository.findByLabtech( labtech );
    }

    public boolean existsByPatient ( final User patient){
        return repository.existsByPatient(patient);
    }
    public boolean existsByHcp ( final User hcp){
        return repository.existsByHcp(hcp);
    }

    public List<Procedure> findByHcp( final User hcp){
        return repository.findByHcp(hcp);
    }

    public boolean existsByHcpAndPatient (final User hcp, final User patient){ return repository.existsByHcpAndPatient(hcp, patient);}

    public List<Procedure> findByHcpAndPatient(final User hcp, final User patient){ return repository.findByHcpAndPatient(hcp, patient);}

    public List<Procedure> findByVisit(final OfficeVisit visit) {
        return repository.findByVisit(visit);
    }
}
