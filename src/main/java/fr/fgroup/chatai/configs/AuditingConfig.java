package fr.fgroup.chatai.configs;

import fr.fgroup.chatai.utils.SecurityUtils;
import fr.fgroup.chatai.utils.UserContext;
import fr.fgroup.chatai.utils.UserContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * AuditingConfig - JPA Auditing configuration for automatic audit field population.
 * 
 * This configuration enables automatic tracking of:
 * - Entity creation date and user
 * - Entity modification date and user
 * 
 * The auditor (current user) is retrieved from UserContextHolder, which gets populated
 * by the JwtAuthenticationFilter. If no user context is available, the SYSTEM account is used.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2020-06-17
 */
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class AuditingConfig {

    /**
     * Internal implementation of AuditorAware to provide the current auditor.
     * 
     * The auditor is determined from the current user context or defaults to SYSTEM.
     */
    private static class AuditorAwareImpl implements AuditorAware<String> {

        /**
         * Returns the current auditor (user) from the thread-local UserContext.
         * 
         * @return Optional containing the current auditor's key or SYSTEM if no user context
         */
        @Override
        public Optional<String> getCurrentAuditor() {
            UserContext userContext = UserContextHolder.getContext();
            if (userContext != null && userContext.getKey() != null) {
                return Optional.of(userContext.getKey());
            }

            return Optional.of(SecurityUtils.SYSTEM_ACCOUNT);
        }
    }

    /**
     * Provides the AuditorAware bean for JPA auditing.
     * 
     * @return AuditorAware implementation for tracking entity changes
     */
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}
