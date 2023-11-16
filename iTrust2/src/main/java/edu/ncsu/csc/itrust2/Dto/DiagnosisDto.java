package edu.ncsu.csc.iTrust2.Dto;

import java.time.ZonedDateTime;

import edu.ncsu.csc.iTrust2.models.Diagnosis;
import edu.ncsu.csc.iTrust2.models.ICDCode;
import edu.ncsu.csc.iTrust2.models.User;

public class DiagnosisDto {
	
	private Long id;
    private String note;
    private ICDCode code;
    private ZonedDateTime date;
    private User hcp;
    private Long VisitId;
    
    public DiagnosisDto(Diagnosis d) {
        this.id = d.getId();
        this.note = d.getNote();
        this.code = d.getCode();
        this.date = d.getVisit().getDate();
        this.hcp = d.getVisit().getHcp();
        this.VisitId = d.getVisit().getId();
    }
    
}
