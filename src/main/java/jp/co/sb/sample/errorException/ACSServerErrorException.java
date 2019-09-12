package jp.co.sb.sample.errorException;

@SuppressWarnings("serial")
public class ACSServerErrorException extends RuntimeException {
	
	public String code;
	public String msg;
	
	public ACSServerErrorException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

}
