package jp.co.sb.sample.errorResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;

@Setter
public class ErrorResponseBody {

    @JsonProperty("error")
    private ErrorDetail error;
}
