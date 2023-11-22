package edu.ncsu.csc.itrust2.repositories;

import java.util.List;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.models.User;

import javax.validation.constraints.NotNull;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    public boolean existsByPatient ( User patient );

    public List<Procedure> findByPatient ( User patient );

    public boolean existsByLabtech ( User labtech );

    public List<Procedure> findByLabtech ( User labtech );

    public boolean existsByHcp ( User hcp );

    public List<Procedure> findByHcp ( User hcp );

    public boolean existsByHcpAndPatient (User hcp, User patient);

    public List<Procedure> findByHcpAndPatient(User hcp, User patient);

}
