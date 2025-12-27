package fr.fgroup.chatai.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 *
 *
 * @author Julien Fischer
 * @since  2021-02-09
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(Map<String, Object> parameters) {
        super(parameters);
    }

}
