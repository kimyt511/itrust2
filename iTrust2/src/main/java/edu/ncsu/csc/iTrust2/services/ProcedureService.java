package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.iTrust2.repositories.ProcedureRepository;

@Component
@Transactional
public class ProcedureService extends Service {

    @Autowired
    private ProcedureRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public boolean existsByLabtech ( final String labtech ) {
        return repository.existsByLabtech( labtech );
    }

    public Procedure findByLabtech ( final String labtech ) {
        return repository.findByLabtech( labtech );
    }

    public boolean existsByPatient ( final String patient){
        return repository.existsByPatient(patient);
    }
    public Procedure findByPatient( final String patient){
        return repository.findByPatient(patient);
    }
}
