package fr.fgroup.chatai.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * UserContextHolder - Thread-safe holder for user context information.
 * 
 * This class uses ThreadLocal storage to maintain per-request user context
 * throughout the application lifecycle. The context is populated by the
 * JwtAuthenticationFilter and cleared after each request.
 * 
 * Usage:
 * - Access current user info: UserContextHolder.getContext()
 * - Get current user key: UserContextHolder.getCurrentEmployee()
 * - Get current tenant: UserContextHolder.getCurrentTenant()
 * - Check admin status: UserContextHolder.isAdmin()
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2019-04-25
 */
@Slf4j
public class UserContextHolder {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private UserContextHolder() {
    }

    /** Thread-local storage for user context */
    private static final ThreadLocal<UserContext> currentUser = new ThreadLocal<>();

    /**
     * Retrieves the current user context from thread-local storage.
     * 
     * If no context exists, creates and stores an empty context.
     * This ensures a context is always available for the current thread.
     * 
     * @return the current UserContext
     */
    public static UserContext getContext() {
        UserContext context = currentUser.get();

        if (context == null) {
            context = createEmptyContext();
            currentUser.set(context);
        }
        return currentUser.get();
    }

    /**
     * Retrieves the current user's authentication token.
     * 
     * Converts the stored "Token" format to "Bearer" format for API use.
     * 
     * @return the authentication token in "Bearer" format
     */
    public static String getToken() {
        return getContext().getToken().replace("Token", "Bearer");
    }

    /**
     * Retrieves the current tenant ID.
     * 
     * @return the current tenant ID
     */
    public static String getCurrentTenant() {
        return getContext().getTenantId();
    }

    /**
     * Retrieves the current employee/user ID.
     * 
     * @return the current employee ID
     */
    public static String getCurrentEmployee() {
        return getContext().getEmployeeId();
    }

    /**
     * Sets the user context for the current thread.
     * 
     * @param context the UserContext to set (must not be null)
     * @throws IllegalArgumentException if context is null
     */
    public static void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        currentUser.set(context);
    }

    /**
     * Creates a new empty UserContext instance.
     * 
     * @return a new empty UserContext
     */
    public static UserContext createEmptyContext() {
        return new UserContext();
    }

    /**
     * Checks if the current user has admin privileges.
     * 
     * @return true if the current user is an admin, false otherwise
     */
    public static boolean isAdmin() {
        return getContext().isAdmin();
    }

    /**
     * Clears the user context from thread-local storage.
     * 
     * This MUST be called at the end of request processing to prevent
     * memory leaks and context pollution for the next request processed by this thread.
     */
    public static void clear() {
        currentUser.remove();
    }
}
