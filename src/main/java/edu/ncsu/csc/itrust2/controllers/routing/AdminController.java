package edu.ncsu.csc.itrust2.controllers.routing;

import edu.ncsu.csc.itrust2.models.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to manage basic abilities for Admin roles
 *
 * @author Kai Presler-Marshall
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    /**
     * Returns the admin for the given model
     *
     * @param model model to check
     * @return role
     */
    @RequestMapping(value = "admin/index")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String index(final Model model) {
        return Role.ROLE_ADMIN.getLandingPage();
    }

    /**
     * Add or delete user
     *
     * @param model data for front end
     * @return mapping
     */
    @RequestMapping(value = "admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageUser(final Model model) {
        return "/admin/users";
    }

    /**
     * Creates the form page for the Add Hospital page
     *
     * @param model Data for the front end
     * @return Page to show to the user
     */
    @RequestMapping(value = "admin/hospitals")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageHospital(final Model model) {
        return "/admin/hospitals";
    }

    /**
     * Retrieves the form for the Drugs action
     *
     * @param model Data for front end
     * @return The page to display
     */
    @RequestMapping(value = "admin/drugs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String drugs(final Model model) {
        return "admin/drugs";
    }

    /**
     * Add code
     *
     * @param model data for front end
     * @return mapping
     */
    @RequestMapping(value = "admin/manageICDCodes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCode(final Model model) {
        return "/admin/manageICDCodes";
    }

    /**
     *
     * @param model data for front end
     * @return mapping
     */
    @RequestMapping(value = "admin/manageReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageReviews(final Model model) {
        return "/admin/manageReviews";
    }

    /**
     *
     * @param model data for front end
     * @return mapping
     */
    @RequestMapping(value = "admin/adminHcpReview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminHcpReview(final Model model) {
        return "/admin/adminHcpReview";
    }

    /**
     *
     * @param model data for front end
     * @return mapping
     */
    @RequestMapping(value = "admin/adminHospitalReview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminHospitalReview(final Model model) {
        return "/admin/adminHospitalReview";
    }

     * Retrieves the form for the Vaccine action
     *
     * @param model
     *            Data for front end
     * @return The page to display
     */
    @RequestMapping ( value = "admin/vaccines" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String vaccines ( final Model model ) {
        return "admin/vaccines";
    }

    /**
     * Add or delete LOINC
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = "admin/manageLOINC" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String manageLOINC ( final Model model ) {
        return "/admin/manageLOINC";
    }
}
