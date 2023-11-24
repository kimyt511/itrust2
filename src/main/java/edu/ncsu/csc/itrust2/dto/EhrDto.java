package edu.ncsu.csc.itrust2.dto;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EhrDto {

    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private BloodType bloodType;
    private Gender gender;

    public EhrDto(Patient p){
        this.username = p.getUsername();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.dateOfBirth = p.getDateOfBirth();
        this.bloodType = p.getBloodType();
        this.gender = p.getGender();
    }
}
