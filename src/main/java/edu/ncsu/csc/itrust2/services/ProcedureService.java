package edu.ncsu.csc.itrust2.services;

import javax.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.repositories.ProcedureRepository;

@Component
@Transactional
public class ProcedureService extends Service {

    @Autowired
    private ProcedureRepository repository;

    @Autowired
    private UserService userService;

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
    public boolean existsByHcp ( final User hcp){
        return repository.existsByHcp(hcp);
    }

    public List<Procedure> findByHcp( final User hcp){
        return repository.findByHcp(hcp);
    }

    public boolean existsByHcpAndPatient (final User hcp, final User patient){ return repository.existsByHcpAndPatient(hcp, patient);}

    public List<Procedure> findByHcpAndPatient(final User hcp, final User patient){ return repository.findByHcpAndPatient(hcp, patient);}
}
