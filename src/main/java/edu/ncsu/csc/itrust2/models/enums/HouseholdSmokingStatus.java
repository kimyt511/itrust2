package edu.ncsu.csc.itrust2.models.enums;

import lombok.Getter;

/**
 * Enum representing the status of smokers in a patient's house.
 *
 * @author mrgray4
 */
@Getter
public enum HouseholdSmokingStatus {

    /** This field is unrelated */
    NONAPPLICABLE(0),
    /** Non-smoking house */
    NONSMOKING(1),
    /** Outdoor smokers */
    OUTDOOR(2),

    /** Indoor smokers */
    INDOOR(3),
    ;

    /** Code of the status */
    private final int code;

    /**
     * Create a Status from the numerical code.
     *
     * @param code Code of the Status
     */
    HouseholdSmokingStatus(final int code) {
        this.code = code;
    }

    /**
     * Converts a code to a named smoking status.
     *
     * @param code The smoking code.
     * @return The string represented by the code.
     */
    public static String getName(final int code) {
        return HouseholdSmokingStatus.parseValue(code).toString();
    }

    /**
     * Returns the HouseholdSmokingStatus enum that matches the given code.
     *
     * @param code The code to match
     * @return Corresponding HouseholdSmokingStatus object.
     */
    public static HouseholdSmokingStatus parseValue(final int code) {
        for (final HouseholdSmokingStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return HouseholdSmokingStatus.NONAPPLICABLE;
    }
}
