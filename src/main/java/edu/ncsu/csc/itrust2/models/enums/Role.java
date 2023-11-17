package edu.ncsu.csc.itrust2.models.enums;

import lombok.Getter;

/**
 * For keeping track of various types of users that are known to the system. Different users have
 * different functionality.
 *
 * @author Kai Presler-Marshall
 * @author Jack MacDonald
 */
@Getter
public enum Role {

    /** Patient */
    ROLE_PATIENT(1, "patient/index"),
    /** HCP (general) */
    ROLE_HCP(2, "hcp/index"),
    /** Admin */
    ROLE_ADMIN(3, "admin/index"),
    /** Emergency responder */
    ROLE_ER(4, "er/index"),
    /** Lab Tech */
    ROLE_LABTECH(5, "labtech/index"),
    /** Optometrist */
    ROLE_OD(6, "hcp/index"),
    /** Ophthalmologist */
    ROLE_OPH(7, "hcp/index"),
    /** Virologist */
    ROLE_VIROLOGIST(8, "hcp/index"),
    ;

    /** Numeric code of the Role */
    private final int code;

    /** Landing screen the User should be shown when logging in */
    private final String landingPage;

    /**
     * Create a Role from a code and landing screen.
     *
     * @param code Code of the Role.
     * @param landingPage Landing page (upon logging in) for the User
     */
    Role(final int code, final String landingPage) {
        this.code = code;
        this.landingPage = landingPage;
    }
}
