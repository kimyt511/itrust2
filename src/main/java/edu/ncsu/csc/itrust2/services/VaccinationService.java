package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.VaccinationForm;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.repositories.PrescriptionRepository;
import edu.ncsu.csc.itrust2.repositories.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class VaccinationService extends Service{

    private final VaccinationRepository repository;

    private final VaccineService vaccineService;

    private final UserService userService;

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public Vaccination build(final VaccinationForm form) {
        final Vaccination v = new Vaccination();
        v.setVaccine(vaccineService.findByName(form.getVaccine()));
        v.setPatient(userService.findByName(form.getPatient()));
        v.setVaccinationDay(LocalDate.parse(form.getVaccinationDay()));
        v.setComments(form.getComments());
        if (form.getId() != null) {
            v.setId(form.getId());
        }

        return v;
    }

    public List<Vaccination> findByPatient(final User patient) {
        return repository.findByPatient(patient);
    }


}
