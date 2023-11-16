package edu.ncsu.csc.itrust2.models.enums;

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
    SNACK("Snack")
    ;

    /** Code of the status */
    private final String meal;

    /**
     * Create a MealType from the String meal.
     *
     * @param meal Meal of a type
     */
    MealType(final String meal) {
        this.meal = meal;
    }
}
