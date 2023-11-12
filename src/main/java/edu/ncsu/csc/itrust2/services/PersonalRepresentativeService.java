package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.PersonalRepresentativeForm;
import edu.ncsu.csc.itrust2.models.PersonalRepresentative;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentativeRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class PersonalRepresentativeService extends Service {

    private final PersonalRepresentativeRepository personalRepresentativeRepository;

    private final UserService userService;

    @Override
    protected JpaRepository getRepository() { return personalRepresentativeRepository; }

    public List<PersonalRepresentative> findByPatient(final User patient) {
        return personalRepresentativeRepository.findByPatient(patient);
    }

    public List<PersonalRepresentative> findByRepresentative(final User representative) {
        return personalRepresentativeRepository.findByRepresentative(representative);
    }

    public boolean existsByPatientAndRepresentative(final User patient, final User representative) {
        return personalRepresentativeRepository.existsByPatientAndRepresentative(patient, representative);
    }

    public PersonalRepresentative findById(final Long id) {
        return personalRepresentativeRepository.findById(id).orElse(null);
    }

    public PersonalRepresentative build(final PersonalRepresentativeForm prf) {
        final PersonalRepresentative pr = new PersonalRepresentative();

        pr.setPatient(userService.findByName(prf.getPatient()));
        pr.setRepresentative(userService.findByName(prf.getRepresentative()));
        pr.setComment(prf.getComment());

        if (prf.getId() != null) {
            pr.setId(Long.parseLong(prf.getId()));
        }

        return pr;
    }
}
