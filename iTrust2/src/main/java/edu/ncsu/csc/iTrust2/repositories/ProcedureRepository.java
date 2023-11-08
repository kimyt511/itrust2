package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.itrust2.models.Procedure;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    public boolean existsByLabtech ( String labtech );

    public Procedure findByLabtech ( String labtech );
    public boolean existsByPatient ( String patient );

    public Procedure findByPatient ( String patient );

}
