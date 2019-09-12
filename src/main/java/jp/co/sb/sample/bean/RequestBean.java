package jp.co.sb.sample.bean;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

@Data
public class RequestBean {

	//private HttpServletRequest servletRequest;

	//private String servletPath;
	
	private String accessKey;

	private Payload1Json body;

	//private String type;

	//private String method;

}
