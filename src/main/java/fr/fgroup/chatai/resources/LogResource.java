package fr.fgroup.chatai.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LogResource {

  @JsonProperty("level")
  private int level;

  @JsonProperty("additional")
  private AdditionalResource[] additional;

  @JsonProperty("message")
  private String message;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("lineNumber")
  private int lineNumber;

  @JsonProperty("columnNumber")
  private int columnNumber;
}
