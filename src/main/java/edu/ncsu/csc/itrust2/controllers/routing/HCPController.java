package edu.ncsu.csc.itrust2.controllers.routing;

import edu.ncsu.csc.itrust2.models.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for managing the behavior for the HCP Landing Screen
 *
 * @author Kai Presler-Marshall
 */
@Controller
@RequiredArgsConstructor
public class HCPController {

    /**
     * Returns the Landing screen for the HCP
     *
     * @param model Data from the front end
     * @return The page to display
     */
    @RequestMapping(value = "hcp/index")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String index(final Model model) {
        return Role.ROLE_HCP.getLandingPage();
    }

    /**
     * Returns the page allowing HCPs to edit patient demographics
     *
     * @return The page to display
     */
    @GetMapping("/hcp/editPatientDemographics")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editPatientDemographics() {
        return "/hcp/editPatientDemographics";
    }

    /**
     * Returns the page allowing HCPs to edit prescriptions
     *
     * @return The page to display
     */
    @GetMapping("/hcp/editPrescriptions")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editPrescriptions() {
        return "/hcp/editPrescriptions";
    }

    /**
     * Returns the ER for the given model
     *
     * @param model model to check
     * @return role
     */
    @RequestMapping(value = "hcp/records")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String emergencyRecords(final Model model) {
        return "personnel/records";
    }

    /**
     * Method responsible for HCP's Accept/Reject requested appointment functionality. This prepares
     * the page.
     *
     * @param model Data for the front end
     * @return The page to display to the user
     */
    @GetMapping("/hcp/appointmentRequests")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String requestAppointmentForm(final Model model) {
        return "hcp/appointmentRequests";
    }

    /**
     * Returns the form page for a HCP to document an OfficeVisit
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/documentOfficeVisit")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String documentOfficeVisit(final Model model) {
        return "/hcp/documentOfficeVisit";
    }

    /**
     * Returns the page of EHR
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/hcp/EmergencyHealthRecords" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String EmergencyHealthRecords ( final Model model ) {
        return "/hcp/EmergencyHealthRecords";
    }

    /**
     * Returns the page of EHR
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/hcp/viewVaccinations" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String viewVaccinations ( final Model model ) {
        return "/hcp/viewVaccinations";
    }

    /**
     * Returns the form page for a HCP to document an editProcedures
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/editProcedures")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editProcedures(final Model model) {
        return "/hcp/editProcedures";
    }

    /**
     * Returns the form page for a HCP to document an editOfficeVisit
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/editOfficeVisit")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editOfficeVisit(final Model model) {
        return "/hcp/editOfficeVisit";
    }

    /**
     * Returns the form page for a HCP to view Personal Representatives
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/hcpPersonalRepresentatives")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String hcpPersonalRepresentatives(final Model model) {
        return "/hcp/hcpPersonalRepresentatives";
    }

    /**
     * Returns the form page for a HCP to view food diary
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/viewPatientFoodDiary")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewPatientFoodDiary(final Model model) {
        return "/hcp/viewPatientFoodDiary";
    }

    /**
     * Returns the form page for a HCP to view food diary entry
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/viewDiaryEntryHCP")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewDiaryEntryHCP(final Model model) {
        return "/hcp/viewDiaryEntryHCP";
    }

    /**
     * Returns the form page for a HCP to the review page
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/hcpReview")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String hcpReview(final Model model) {
        return "/hcp/hcpReview";
    }
}
