package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.enums.MealType;

import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.gson.annotations.JsonAdapter;

/**
 * Patient's food records stored in DB
 * 
 * @author Jimin Yoo
 */
@NoArgsConstructor
@Entity
@Getter
@Table(name = "food_diary")
public class FoodDiary extends DomainObject {
    /** The date of this food diary */
    @Setter
    @NotNull @Basic
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;
    
    /** Type of meal */
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    /** Name of food */
    @Setter
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

    /** The Patient who is associated with this diary */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Set the servings
     * 
     * @param servings
     */
    public void setServings(final Float servings) {
        if (servings == null) {
            return;
        }

        if (servings <= 0) {
            throw new IllegalArgumentException(
                    "Servings must be larger than 0");
        }
        this.servings = servings;
    }

    /**
     * Set calories per serving
     * 
     * @param calories
     */
    public void setCalories(Float calories) {
        if (calories == null) {
            return;
        }

        if (calories < 0) {
            throw new IllegalArgumentException(
                "Calories must be larger than or equal to 0");
        }
        this.calories = calories;
    }

    /**
     * Set grams of fat
     * 
     * @param fat
     */
    public void setFat(Float fat) {
        if (fat == null) {
            return;
        }

        if (fat < 0) {
            throw new IllegalArgumentException(
                "Grams of fat must be larger than or equal to 0");
        }
        this.fat = fat;
    }

    /**
     * Set milligrams of sodium
     * 
     * @param sodium
     */
    public void setSodium(Float sodium) {
        if (sodium == null) {
            return;
        }

        if (sodium < 0) {
            throw new IllegalArgumentException(
                "Milligrams of sodium must be larger than or equal to 0");
        }
        this.sodium = sodium;
    }

    /**
     * Set grams of carbs
     * 
     * @param carb
     */
    public void setCarb(Float carb) {
        if (carb == null) {
            return;
        }

        if (carb < 0) {
            throw new IllegalArgumentException(
                "Grams of carbs must be larger than or equal to 0");
        }
        this.carb = carb;
    }

    /**
     * Set grams of sugars
     * 
     * @param sugar
     */
    public void setSugar(Float sugar) {
        if (sugar == null) {
            return;
        }

        if (sugar < 0) {
            throw new IllegalArgumentException(
                "Grams of sugars must be larger than or equal to 0");
        }
        this.sugar = sugar;
    }

    /**
     * Set grams of fiber
     * 
     * @param fiber
     */
    public void setFiber(Float fiber) {
        if (fiber == null) {
            return;
        }

        if (fiber < 0) {
            throw new IllegalArgumentException(
                "Grams of fiber must be larger than or equal to 0");
        }
        this.fiber = fiber;
    }

    /**
     * Set grams of protein
     * 
     * @param protein
     */
    public void setProtein(Float protein) {
        if (protein == null) {
            return;
        }

        if (protein < 0) {
            throw new IllegalArgumentException(
                "Grams of proteins must be larger than or equal to 0");
        }
        this.protein = protein;
    }
}
