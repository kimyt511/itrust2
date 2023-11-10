package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.Procedure;
import edu.ncsu.csc.iTrust2.models.User;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    public boolean existsByLabtech ( User labtech );

    public List<Procedure> findByLabtech ( User labtech );
    public boolean existsByPatient ( User patient );

    public List<Procedure> findByPatient ( User patient );
    public boolean existsByHcp ( User hcp );

    public List<Procedure> findByHcp ( User hcp );

    public boolean existsByHcpAndPatient (User hcp, User patient);

    public List<Procedure> findByHcpAndPatient(User hcp, User patient);

}
