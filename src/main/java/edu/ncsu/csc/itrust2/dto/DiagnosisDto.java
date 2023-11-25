package edu.ncsu.csc.itrust2.dto;

import com.google.gson.annotations.JsonAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.ICDCode;
import edu.ncsu.csc.itrust2.models.User;
import lombok.Getter;

import javax.persistence.Convert;
import java.time.ZonedDateTime;

@Getter
public class DiagnosisDto {

    private Long id;
    private String note;
    private ICDCode code;

    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;

    private User hcp;
    private Long visitId;

    public DiagnosisDto(Diagnosis d) {
        this.id = d.getId();
        this.note = d.getNote();
        this.code = d.getCode();
        this.date = d.getVisit().getDate();
        this.hcp = d.getVisit().getHcp();
        this.visitId = d.getVisit().getId();
    }

}