package edu.ncsu.csc.itrust2.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import edu.ncsu.csc.itrust2.services.FoodDiaryService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * REST API endpoints for FoodDiary model.
 * 
 * @author Jimin Yoo
 */
@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class APIFoodDiaryController extends APIController {
    
    private final FoodDiaryService foodDiaryService;

    private final LoggerUtil loggerUtil;

    private final UserService userService;

    /**
     * HCP views a diary for specific patient
     * 
     * @param username The username of the patient to view food diary.
     */
    @GetMapping("/diary/{username}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<FoodDiary> getFoodDiaries(@PathVariable final String username) {
        User patient = userService.findByName(username);
        return foodDiaryService.findByPatient(patient);
    }

    /**
     * Patients view their food diary
     * 
     */
    @GetMapping("/diary/mydiary")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> getMyFoodDiaries() {
        User patient = userService.findByName(LoggerUtil.currentUser());
        return foodDiaryService.findByPatient(patient);
    }

    /**
     * Create an FoodDiary from the RequestBody.
     * 
     * @param requestForm the FoodDiaryForm
     */
    @PostMapping("/diary/creatediary")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity createFoodDiary(@RequestBody final FoodDiaryForm requestForm) {
        try {
            final FoodDiary diary = foodDiaryService.build(requestForm);
            foodDiaryService.save(diary);
            loggerUtil.log(
                TransactionType.CREATE_FOOD_DIARY_ENTRY,
                LoggerUtil.currentUser());

            return new ResponseEntity(diary, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                errorResponse(
                    "Error occured creating food diary entry: "
                    + e.getMessage()),
                HttpStatus.BAD_REQUEST);
        }
    }
}
