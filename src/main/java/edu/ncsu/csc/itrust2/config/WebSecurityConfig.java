package edu.ncsu.csc.itrust2.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    /** Login configuration for iTrust2. */
    @Bean
    public UserDetailsManager configureGlobal(DataSource dataSource) {
        // User query enabled flag also checks for locked or banned users. The
        // FailureHandler then
        // determines if the DisabledUser Exception was due to ban, lockout, or
        // true disable.
        // POSSIBLE FUTURE CHANGE: Refactor the UserSource here along the lines
        // of this:
        // http://websystique.com/springmvc/spring-mvc-4-and-spring-security-4-integration-example/
        return new JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(
                        "select username,password,enabled from user WHERE username = ?;")
                .authoritiesByUsernameQuery(
                        "select user_username, roles from user_roles where user_username=?")
                .getUserDetailsService();
    }

    /**
     * Method responsible for the Login page. Can be extended to explicitly override other automatic
     * functionality as desired.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            IPFilter ipBlockFilter, FailureHandler failureHandler, HttpSecurity http)
            throws Exception {
        final String[] patterns = new String[] {"/login*", "/DrJenkins"};
        // Add filter for banned/locked IP
        /*
         * According to
         * https://docs.spring.io/spring-security/site/docs/current/apidocs/org/
         * springframework/security/config/annotation/web/builders/HttpSecurity.
         * html#addFilter-javax.servlet.Filter- ChannelProcessingFIlter is the
         * first filter processed, so this means the IP block will be the
         * absolute first Filter.
         */
        http.addFilterBefore(ipBlockFilter, ChannelProcessingFilter.class);

        http.authorizeRequests()
                .antMatchers(new String[] {"/swagger-ui/**", "/v3/api-docs/**"})
                .permitAll()
                .antMatchers(patterns)
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(failureHandler)
                .defaultSuccessUrl("/")
                .and()
                .csrf()

                /*
                 * * Credit to https://medium.com/spektrakel
                 * -blog/angular2-and-spring-a- friend-in-
                 * security-need-is-a-friend- against-csrf-indeed- 9f83eaa9ca2e
                 * and http://docs.spring.io/spring- security/site/docs/current/
                 * reference/ html/csrf.html#csrf-cookie for information on how
                 * to make Angular work properly with CSRF protection
                 */

                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        return http.build();
    }

    /**
     * Method responsible for the Login page. Can be extended to explicitly override other automatic
     * functionality as desired.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Allow anonymous access to the 3 mappings related to resetting a
        // forgotten password
        return (web) ->
                web.ignoring()
                        .antMatchers(
                                "/api/v1/requestPasswordReset",
                                "/api/v1/resetPassword/*",
                                "/requestPasswordReset",
                                "/resetPassword",
                                "/api/v1/generateUsers",
                                "/viewEmails",
                                "/api/v1/emails");
    }

    /**
     * Bean used to generate a PasswordEncoder to hash the user-provided password.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationEventPublisher used to assist with authentication
     *
     * @return The AuthenticationEventPublisher.
     */
    @Bean
    public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }
}
