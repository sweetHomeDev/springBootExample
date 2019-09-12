package jp.co.sb.sample.errorResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class ErrorDetail {
	
    @JsonProperty("message")
    String message;
    
    @JsonProperty("reason")
    String reason;
    
    @JsonProperty("code")
    String code;
}