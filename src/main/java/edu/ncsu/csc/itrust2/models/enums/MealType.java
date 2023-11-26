package edu.ncsu.csc.itrust2.models.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Meal types for UC19.
 *
 * @author Jimin Yoo
 */
@Getter
public enum MealType {
    /** Breakfast */
    BREAKFAST("Breakfast"),
    /** Lunch */
    LUNCH("Lunch"),
    /** Dinner */
    DINNER("Dinner"),
    /** Snack */
    SNACK("Snack");

    /** Code of the status */
    private final String name;

    /**
     * Create a MealType from the String meal.
     *
     * @param name Meal of a type
     */
    MealType(final String name) {
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

    /** Converts the MealType to a String */
    @Override
    public String toString() {
        return getName();
    }
}
