package edu.ncsu.csc.itrust2.services;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.repositories.ProcedureRepository;

@Component
@Transactional
@RequiredArgsConstructor
public class ProcedureService extends Service {

    private final ProcedureRepository repository;

    private final UserService userService;

    private final OfficeVisitRepository officeVisitRepository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public Procedure build(final ProcedureForm form) {
        final Procedure pc = new Procedure();

        pc.setCode(form.getCode());
        pc.setName( form.getName());
        pc.setHcp(form.getHcp());
        pc.setLabtech(form.getLabtech());
        pc.setPatient(form.getPatient());
        pc.setComment(form.getComment());
        pc.setPriority(form.getPriority());
        pc.setProcedureStatus(form.getProcedureStatus());


        if (form.getId() != null) {
            pc.setId(form.getId());
        }

        return pc;
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
