package edu.ncsu.csc.itrust2.models.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Enum representing possible Genders for users of the system.
 *
 * @author Kai Presler-Marshall
 */
@Getter
public enum Gender {

    /** Male */
    Male("Male"),
    /** Female */
    Female("Female"),
    /** Other */
    Other("Other"),
    /** Not Specified */
    NotSpecified("Not Specified");

    /** Name of this Gender */
    private final String name;

    /**
     * Constructor for the Gender
     *
     * @param name Name of the Gender to create
     */
    Gender(final String name) {
        this.name = name;
    }

    /**
     * Returns a map from field name to value, which is more easily serialized for sending to
     * front-end.
     *
     * @return map from field name to value for each of the fields in this enum
     */
    public Map<String, Object> getInfo() {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", name());
        map.put("name", getName());
        return map;
    }

    /** Convert the Gender to a String */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Parse the String provided into an actual Gender enum.
     *
     * @param genderStr String representation of the Gender
     * @return Gender enum corresponding to the string, or NotSpecified if nothing could be matched
     */
    public static Gender parse(final String genderStr) {
        for (final Gender gender : values()) {
            if (gender.getName().equals(genderStr)) {
                return gender;
            }
        }
        return NotSpecified;
    }
}
