package edu.ncsu.csc.itrust2.models.enums;

import lombok.Getter;

/**
 * Enum of all of the types of appointments that are recognized by the system.
 *
 * @author Kai Presler-Marshall
 */
@Getter
public enum AppointmentType {

    /** General Checkup */
    GENERAL_CHECKUP(1),

    /** General Ophthalmology Appointment */
    GENERAL_OPHTHALMOLOGY(2),

    /** Ophthalmology Surgery */
    OPHTHALMOLOGY_SURGERY(3),
    ;

    /** Numerical code of the AppointmentType */
    private final int code;

    /**
     * Creates the AppointmentType from its code.
     *
     * @param code Code of the AppointmentType
     */
    AppointmentType(final int code) {
        this.code = code;
    }
}
