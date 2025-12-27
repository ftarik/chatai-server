package fr.fgroup.chatai.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Base class for all exceptions at microservice level
 *
 * @author Julien Fischer
 * @since  2021-02-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ServiceException extends Exception {

    private Map<String, Object> parameters;

}
