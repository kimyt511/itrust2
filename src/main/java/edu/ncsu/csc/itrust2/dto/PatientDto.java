package edu.ncsu.csc.itrust2.dto;

import edu.ncsu.csc.itrust2.models.Patient;
import lombok.Getter;

@Getter
public class PatientDto {
    private String firstName;

    private String lastName;

    private String username;

    public PatientDto(Patient p){
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.username = p.getUsername();
    }
}
