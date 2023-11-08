package edu.ncsu.csc.iTrust2.models.enums;

/**
 * Enum representing the status of Procedure progress.
 *
 */
public enum ProcedureStatus {
    /**
     * This field is unrelated
     */
    NONAPPLICABLE ( 0 ),
    /**
     * Assigned procedure
     */
    Assigned ( 1 ),
    /**
     * In process procedure
     */
    InProgress ( 2 ),

    /**
     * Completed procedure
     */
    Completed ( 3 ),

    ;

    /**
     * Progress of the status
     */
    private int progress;

    /**
     * Create a Status from the numerical progress.
     *
     * @param progress
     *            progress of the Status
     */
    private ProcedureStatus ( final int progress ) {
        this.progress = progress;
    }

    /**
     * Gets the numerical progress of the Status
     *
     * @return progress of the Status
     */
    public int getProgress () {
        return progress;
    }

    /**
     * Converts progress to a named procedure status.
     *
     * @param progress
     *            The progress of procedure.
     * @return The string represented by the progress.
     */
    public static String getName ( final int progress ) {
        return ProcedureStatus.parseValue( progress ).toString();
    }

    /**
     * Returns the ProcedureStatus enum that matches the given progress.
     *
     * @param progress
     *            The progress to match
     * @return Corresponding ProcedureStatus object.
     */
    public static ProcedureStatus parseValue ( final int progress ) {
        for ( final ProcedureStatus status : values() ) {
            if ( status.getProgress() == progress ) {
                return status;
            }
        }
        return ProcedureStatus.NONAPPLICABLE;
    }

}
