package jp.co.sb.sample.bean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseBean {

	private HttpHeaders headers;

	private String body;

	private HttpStatus status;

}
