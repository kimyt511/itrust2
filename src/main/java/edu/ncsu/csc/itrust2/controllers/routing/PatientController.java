package edu.ncsu.csc.itrust2.controllers.routing;

import edu.ncsu.csc.itrust2.models.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the Patient landing screen and basic patient information
 *
 * @author Kai Presler-Marshall
 */
@Controller
@RequiredArgsConstructor
public class PatientController {

    /**
     * Retrieves the page for the Patient to request an Appointment
     *
     * @param model Data for the front end
     * @return The page the patient should see
     */
    @GetMapping("/patient/manageAppointmentRequest")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String requestAppointmentForm(final Model model) {
        return "/patient/manageAppointmentRequest";
    }

    /**
     * Returns the form page for a patient to view all OfficeVisits
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/patient/officeVisit/viewOfficeVisits")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewOfficeVisits(final Model model) {
        return "/patient/officeVisit/viewOfficeVisits";
    }

    /**
     * Returns the form page for a patient to view all prescriptions
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/patient/officeVisit/viewPrescriptions")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewPrescriptions(final Model model) {
        return "/patient/officeVisit/viewPrescriptions";
    }

    /**
     * Landing screen for a Patient when they log in
     *
     * @param model The data from the front end
     * @return The page to show to the user
     */
    @RequestMapping(value = "patient/index")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String index(final Model model) {
        return Role.ROLE_PATIENT.getLandingPage();
    }

    /**
     * Provides the page for a User to view and edit their demographics
     *
     * @param model The data for the front end
     * @return The page to show the user so they can edit demographics
     */
    @GetMapping(value = "patient/editDemographics")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewDemographics(final Model model) {
        return "/patient/editDemographics";
    }

    /**
     * Create a page for the patient to view all diagnoses
     *
     * @param model data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping(value = "patient/officeVisit/viewDiagnoses")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewDiagnoses(final Model model) {
        return "/patient/officeVisit/viewDiagnoses";
    }

    /**
     * Create a page for the patient to review HCP and Hospital
     *
     * @param model data for front end
     * @return The page for the patient to review HCP and Hospital
     */
    @GetMapping("/patient/reviewHCPandHospital")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String reviewHCPandHospital(final Model model) {
        return "/patient/reviewHCPandHospital";
    }

    /**
     * Create a page for the patient to review page
     *
     * @param model data for front end
     * @return The page for the patient to review page
     */
    @GetMapping("/patient/reviewPage")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String reviewPage(final Model model) {
        return "/patient/reviewPage";
    }

    /**
     * Create a page for the patient to review page
     *
     * @param model data for front end
     * @return The page for the patient to review page
     */
    @GetMapping("/patient/writeReview")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String writeReview(final Model model) {
        return "/patient/writeReview";
    }

    /**
     * Create a page for the patient to view reviews of a hospital
     *
     * @param model data for front end
     * @return The page for the patient to view reviews of a hospital
     */
    @GetMapping("/patient/viewHospitalReview")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT','ROLE_HCP')")
    public String viewHospitalReview(final Model model) {
        return "/patient/viewHospitalReview";
    }

    /**
     * Create a page for the patient to view reviews of an HCP
     *
     * @param model data for front end
     * @return The page for the patient to view reviews of an HCP
     */
    @GetMapping("/patient/viewHcpReview")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT','ROLE_HCP')")
    public String viewHcpReview(final Model model) { return "/patient/viewHcpReview";
    }

    /**
     * Create a page for the patient to view reviews of an HCP
     *
     * @param model data for front end
     * @return The page for the patient to view reviews of an HCP
     */
    @GetMapping("/patient/myReviews")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String myReviews(final Model model) {
        return "/patient/myReviews";
    }

    /**
     * Create a page for the patient to view all diagnoses
     *
     * @param model data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping(value = "patient/officeVisit/viewVaccinations")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewVaccinations(final Model model) {
        return "/patient/officeVisit/viewVaccinations";
    }

    /**
     * Create a page for the patient to view Personal Representatives
     *
     * @param model data for front end
     * @return The page for the patient to view Personal Representatives
     */
    @GetMapping(value = "patient/representative/viewPersonalRepresentatives")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewPersonalRepresentatives(final Model model) {
        return "/patient/representative/viewPersonalRepresentatives";
    }

    /**
     * Create a page for the patient to view access logs of a patient in PR page
     *
     * @param model data for front end
     * @return The page for the patient to view access logs of a patient in PR page
     */
    @GetMapping(value = "patient/representative/viewAccessLogPR")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewAccessLogPR(final Model model) {
        return "/patient/representative/viewAccessLogPR";
    }

    /**
     * Create a page for the patient to view Diagnoses of a patient in PR page
     *
     * @param model data for front end
     * @return The page for the patient to view Diagnoses of a patient in PR page
     */
    @GetMapping(value = "patient/representative/viewDiagnosesPR")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewDiagnosesPR(final Model model) {
        return "/patient/representative/viewDiagnosesPR";
    }

    /**
     * Create a page for the patient to view medical records(OfficeVisit) of a patient in PR page
     *
     * @param model data for front end
     * @return The page for the patient to view medical records of a patient in PR page
     */
    @GetMapping(value = "patient/representative/viewMedicalRecordsPR")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewMedicalRecordsPR(final Model model) {
        return "/patient/representative/viewMedicalRecordsPR";
    }

    /**
     * Create a page for the patient to view appointments of a patient in PR page
     *
     * @param model data for front end
     * @return The page for the patient to view appointments of a patient in PR page
     */
    @GetMapping(value = "patient/representative/viewAppointmentPR")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewAppointmentPR(final Model model) {
        return "/patient/representative/viewAppointmentPR";
    }

    /**
     * Create a page for the patient to navigate to food diary page
     *
     * @param model data for front end
     * @return The page for the patient to navigate to food diary page
     */
    @GetMapping(value = "patient/foodDiary")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String foodDiary(final Model model) {
        return "/patient/foodDiary";
    }

    /**
     * Create a page for the patient to add food diary
     *
     * @param model data for front end
     * @return The page for the patient to add food diary
     */
    @GetMapping(value = "patient/addFoodDiary")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String addFoodDiary(final Model model) {
        return "/patient/addFoodDiary";
    }

    /**
     * Create a page for the patient to view selected food diary
     *
     * @param model data for front end
     * @return The page for the patient to view selected food diary
     */
    @GetMapping(value = "patient/viewDiaryEntry")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String viewDiaryEntry(final Model model) {
        return "/patient/viewDiaryEntry";
    }
}
