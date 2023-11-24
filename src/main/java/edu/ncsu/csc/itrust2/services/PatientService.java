package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.dto.PatientDto;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class PatientService extends UserService {

    private final PatientRepository repository;

    public PatientService(UserRepository userRepository, PatientRepository repository) {
        super(userRepository);
        this.repository = repository;
    }

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public List<PatientDto> findByNameContaining(String keyword){
        List<Patient> patients = repository.findByNameContaining(keyword);
        List<PatientDto> p_list = new ArrayList<PatientDto>();
        for(Patient patient:patients) {
            PatientDto p = new PatientDto(patient);
            p_list.add(p);
        }
        return p_list;
    }

    public List<PatientDto> findByUsernameContaining(String keyword){
        List<Patient> patients = repository.findByUsernameContaining(keyword);
        List<PatientDto> p_list = new ArrayList<PatientDto>();
        for(Patient patient:patients) {
            PatientDto p = new PatientDto(patient);
            p_list.add(p);
        }
        return p_list;
    }

}
