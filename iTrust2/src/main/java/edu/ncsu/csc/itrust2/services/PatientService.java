package edu.ncsu.csc.iTrust2.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.repositories.PatientRepository;

@Component
@Transactional
public class PatientService extends UserService {

    @Autowired
    private PatientRepository repository;

    @Override
    protected JpaRepository getRepository () {
        return repository;
    }
    
    public List<String> findByNameContaining(String keyword){
    	List<String> usernames = new ArrayList<String>();
    	List<Patient> patients = repository.findByNameContaining(keyword);
    	for(Patient patient:patients) {
    		usernames.add(patient.getUsername());
    	}
		return usernames;
    }

}
