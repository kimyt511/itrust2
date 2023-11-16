package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.MealType;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;

import java.time.ZonedDateTime;
import java.util.List;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class FoodDiaryService extends Service {
    
    private final FoodDiaryRepository foodDiaryRepository;

    private final UserService userService;

    @Override
    protected JpaRepository getRepository() { return foodDiaryRepository; }

    public List<FoodDiary> findByPatient(final User patient) {
        return foodDiaryRepository.findByPatient(patient);
    }

    public FoodDiary build(final FoodDiaryForm fdf) {
        final FoodDiary fd = new FoodDiary();

        fd.setPatient(userService.findByName(fdf.getPatient()));

        final ZonedDateTime diaryDate = ZonedDateTime.parse(fdf.getDate());
        if (diaryDate.isAfter(ZonedDateTime.now())) {
            throw new IllegalArgumentException(
                "Cannot add a diary after current date");
        }
        fd.setDate(diaryDate);

        MealType m = null;
        try {
            m = MealType.valueOf(fdf.getMealType());
        } catch (final NullPointerException e) {
            throw new IllegalArgumentException(
                "Meal type must be included");
        }
        fd.setMealType(m);

        fd.setFoodName(fdf.getFoodName());
        fd.setServings(fdf.getServings());
        fd.setCalories(fdf.getCalories());
        fd.setFat(fdf.getFat());
        fd.setSodium(fdf.getSodium());
        fd.setCarb(fdf.getCarb());
        fd.setSugar(fdf.getSugar());
        fd.setFiber(fdf.getFiber());
        fd.setProtein(fdf.getProtein());

        return fd;
    }
}