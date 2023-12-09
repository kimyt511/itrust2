package edu.ncsu.csc.itrust2.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FoodDiary form used to create diary.
 * 
 */
@Setter
@NoArgsConstructor
@Getter
public class FoodDiaryForm {

    /** The date of this food diary */
    @NotEmpty(message = "Date cannot be empty")
    private String date;

    /** The patient */
    @NotEmpty
    private String patient;

    /** Type of meal */
    @NotEmpty
    private String mealType;

    /** Name of food */
    @NotEmpty
    private String foodName;

    /** Number of servings */
    @NotNull
    private Float servings;

    /** Calories per serving */
    @NotNull
    private Float calories;
    
    /** Fat(g) per serving */
    @NotNull
    private Float fat;

    /** Sodium(mg) per serving */
    @NotNull
    private Float sodium;

    /** Carbs(g) per serving */
    @NotNull
    private Float carb;

    /** Sugar(g) per serving */
    @NotNull
    private Float sugar;

    /** Fiber(g) per serving */
    @NotNull
    private Float fiber;

    /** Protein(g) per serving */
    @NotNull
    private Float protein;

}
