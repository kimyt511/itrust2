package edu.ncsu.csc.itrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.itrust2.models.enums.Role;

/**
 * Controller to manage basic abilities for ER roles
 *
 * @author Amalan Iyengar
 *
 */

@Controller
public class LabtechController {

    /**
     * Returns the ER for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = "labtech/index" )
    @PreAuthorize ( "hasRole('ROLE_LABTECH')" )
    public String index ( final Model model ) {
        return Role.ROLE_LABTECH.getLandingPage();
    }

    /**
     * Manage Procedure progress
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = "labtech/manageProcedures" )
    @PreAuthorize ( "hasRole('ROLE_LABTECH')" )
    public String manageProcedures ( final Model model ) {
        return "/labtech/manageProcedures";
    }

}
