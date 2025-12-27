package fr.fgroup.chatai.exceptions.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class GenericException extends RuntimeException {
  private final String title;
  private final HttpStatus status;

  protected GenericException(String title, HttpStatus status, String message) {
    super(message);
    this.title = title;
    this.status = status;
  }

}
