package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.DomainObject;
import edu.ncsu.csc.itrust2.models.Drug;
import edu.ncsu.csc.itrust2.models.Vaccine;
import edu.ncsu.csc.itrust2.repositories.DrugRepository;
import edu.ncsu.csc.itrust2.repositories.PrescriptionRepository;
import edu.ncsu.csc.itrust2.repositories.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class VaccineService extends Service {

    private final VaccineRepository repository;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public boolean existsByName(final String name) {
        return repository.existsByName(name);
    }

    public Vaccine findByName(final String name) {
        return repository.findByName(name);
    }

    public Vaccine getVaccineByCptCode(final String cptCode) {
        return repository.findByCptCode(cptCode);
    }
}
