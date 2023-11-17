package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.repositories.PersonnelRepository;
import edu.ncsu.csc.itrust2.repositories.UserRepository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class PersonnelService extends UserService {

    private final PersonnelRepository repository;

    public PersonnelService(UserRepository userRepository, PersonnelRepository repository) {
        super(userRepository);
        this.repository = repository;
    }

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }
}
