package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.VaccinationForm;
import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.models.Vaccine;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.VaccinationRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class VaccinationService extends Service {

    private final VaccinationRepository repository;

    private final UserService userService;

    private final VaccineService vaccineService;

    private final OfficeVisitService officeVisitService;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public Vaccination build(VaccinationForm form) {
        Vaccination vaccination = new Vaccination();

        Vaccine vaccine = vaccineService.findById(form.getVaccineId());
        vaccination.setVaccine(vaccine);

        OfficeVisit officeVisit = officeVisitService.findById(form.getOfficeVisitId());
        vaccination.setOfficeVisit(officeVisit);

        User patient = userService.findByName(form.getPatientUsername());
        vaccination.setPatient(patient);

        vaccination.setDateAdministered(form.getDateAdministered());

        return vaccination;
    }


    public Vaccination saveVaccination(Vaccination vaccination) {
        return repository.save(vaccination);
    }

    public void deleteVaccination(Vaccination vaccination) {
        repository.delete(vaccination);
    }

    public Vaccination findById(Long id) {
        Optional<Vaccination> result = repository.findById(id);
        return result.orElse(null);
    }

    public List<Vaccination> getVaccinationsByPatient(User patient) {
        return repository.findByPatient(patient);
    }

    public List<Vaccination> getVaccinationsByOfficeVisit(OfficeVisit officeVisit) {
        return repository.findByOfficeVisit(officeVisit);
    }
}
