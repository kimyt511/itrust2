package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.models.Vaccine;
import edu.ncsu.csc.itrust2.repositories.VaccineRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class VaccineService extends Service {

    private final VaccineRepository repository;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public Vaccine saveVaccine(Vaccine vaccine) {
        return repository.save(vaccine);
    }

    public Vaccine getVaccineByName(String name) {
        return repository.findByName(name);
    }

    public Vaccine getVaccineByAbbreviation(String abbreviation) {
        return repository.findByAbbreviation(abbreviation);
    }

    public Vaccine getVaccineByCptCode(String cptCode) {
        return repository.findByCptCode(cptCode);
    }

    public Vaccine findById(Long id) {
        Optional<Vaccine> result = VaccineRepository.findById(id);
        return result.orElse(null);
    }

    public List<Vaccine> getAllVaccines() {
        return repository.findAll();
    }
}
