package edu.ncsu.csc.iTrust2.Dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import edu.ncsu.csc.iTrust2.models.Diagnosis;
import edu.ncsu.csc.iTrust2.models.ICDCode;
import edu.ncsu.csc.iTrust2.models.User;

//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DiagnosisDto {
   
   private Long id;
    private String note;
    private ICDCode code;
    private ZonedDateTime date;
    private User hcp;
    private Long visitId;
    
    public DiagnosisDto() {
       
    }
    
    
    public DiagnosisDto(Diagnosis d) {
        this.id = d.getId();
        this.note = d.getNote();
        this.code = d.getCode();
        this.date = d.getVisit().getDate();
        this.hcp = d.getVisit().getHcp();
        this.visitId = d.getVisit().getId();
    }
    
    public Long getId() {
        return id;
    }
    
    public String getNote() {
        return note;
    }

    public ICDCode getCode() {
        return code;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public User getHcp() {
        return hcp;
    }

    public Long getVisitId() {
        return visitId;
    }
}