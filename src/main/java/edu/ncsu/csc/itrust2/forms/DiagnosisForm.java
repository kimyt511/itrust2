package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.ICDCode;
import edu.ncsu.csc.itrust2.forms.ICDCodeForm;
import edu.ncsu.csc.itrust2.services.ICDCodeService;


import java.io.Serializable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class DiagnosisForm implements Serializable {

    private Long visit;
    private String note;
    private Long id;
    private String code;

    public DiagnosisForm(final Diagnosis diag) {
        /* May not be attached to a visit yet */
        if (null != diag.getVisit()) {
            visit = diag.getVisit().getId();
        }

        // Changed format to keep the code synced with other files
        //note = diag.getNote();
        setNote(diag.getNote());

        //id = diag.getId();
        setId(diag.getId());

        //code = diag.getCode().getCode();
        setCode(diag.getCode().getCode());
    }


    public Long getId() {
        return id;
    }
    public void setId(final Long id) {
        this.id = id;
    }
    public String getNote() {
        return note;
    }
    public void setNote(final String note) {
        this.note = note;
    }
    public String getCode() {
        return code;
    }
    public void setCode(final String code) {
        this.code = code;
    }

}
