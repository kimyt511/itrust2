package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.Procedure;
import edu.ncsu.csc.iTrust2.repositories.ProcedureRepository;

@Component
@Transactional
public class ProcedureService extends Service {

    @Autowired
    private ProcedureRepository repository;

    @Autowired
    private UserService                  userService;

    @Override
    protected JpaRepository getRepository () {
        return repository;
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

    public List<Procedure> findByPatient( final User patient){
        return repository.findByPatient(patient);
    }
}
