package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.LabProcedure;
import edu.ncsu.csc.iTrust2.repositories.LabProcedureRepository;

@Component
@Transactional
public class LabProcedureService extends Service {

    @Autowired
    private LabProcedureRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public boolean existsByCode ( final String code ) {
        return repository.existsByCode( code );
    }

    public LabProcedure findByCode ( final String code ) {
        return repository.findByCode( code );
    }
}
