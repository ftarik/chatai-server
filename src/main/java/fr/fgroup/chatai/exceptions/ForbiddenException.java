package fr.fgroup.chatai.exceptions;

import fr.fgroup.chatai.exceptions.base.GenericException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends GenericException {
  public ForbiddenException(String title, String message) {
    super(title, HttpStatus.FORBIDDEN, message);
  }
}
