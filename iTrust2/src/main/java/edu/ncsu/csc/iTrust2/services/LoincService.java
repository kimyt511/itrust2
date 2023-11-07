package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Loinc;
import edu.ncsu.csc.iTrust2.repositories.LoincRepository;

@Component
@Transactional
public class LoincService extends Service {

    @Autowired
    private LoincRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }

    public boolean existsByCode ( final String code ) {
        return repository.existsByCode( code );
    }

    public Loinc findByCode ( final String code ) {
        return repository.findByCode( code );
    }
}
