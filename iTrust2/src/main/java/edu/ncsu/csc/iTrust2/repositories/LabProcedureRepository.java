package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.LabProcedure;

public interface LabProcedureRepository extends JpaRepository<LabProcedure, Long> {

    public boolean existsByCode ( String code );

    public LabProcedure findByCode ( String code );

}
