package jp.co.sb.sample.errorException;

import jp.co.sb.sample.errorResponse.ErrorDetail;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ErrorException extends RuntimeException {

    private ErrorDetail errorDetails;

    public ErrorException(String message, ErrorDetail errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }
}
