package edu.ncsu.csc.iTrust2.models.enums;


public enum Priority {


    HIGHEST ( 1 ),

    HIGH ( 2 ),

    LOW ( 3 ),

    LOWEST ( 4 )

    ;

    /**
     * Code of the priority
     */
    private int code;

    /**
     * Create a Priority from the numerical code.
     *
     * @param code
     *            Code of the Priority
     */
    private Priority ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical Code of the Priority
     *
     * @return Code of the Priority
     */
    public int getCode () {
        return code;
    }

}
