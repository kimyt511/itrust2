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

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public Vaccination build(final VaccinationForm form) {
        final Vaccination vaccination = new Vaccination();

        Vaccine vaccine = vaccineService.getVaccineByCptCode(form.getVaccineCptCode());
        vaccination.setVaccine(vaccine);

        User patient = userService.findByName(form.getPatient());
        vaccination.setPatient(patient);

        vaccination.setDateAdministered(form.getDateAdministered());

        String Comments = form.getComments();
        if (Comments == null) {
            Comments = "";
        }
        vaccination.setComments(Comments);

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
