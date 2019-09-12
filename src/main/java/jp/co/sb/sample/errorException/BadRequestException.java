package jp.co.sb.sample.errorException;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
	
	public String code;
	public String msg;
	
	public BadRequestException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

}
