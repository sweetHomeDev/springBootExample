package jp.co.sb.sample.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jp.co.sb.sample.bean.Payload1Json;
import jp.co.sb.sample.bean.RequestBean;
import jp.co.sb.sample.bean.ResponseBean;
import jp.co.sb.sample.errorException.ACSServerErrorException;
import jp.co.sb.sample.errorException.BadRequestException;
import jp.co.sb.sample.errorResponse.ErrorDetail;
import jp.co.sb.sample.errorResponse.ErrorResponseBody;
import jp.co.sb.sample.service.SpringRestApiSampleService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/location-latestcell/v1")
@RestController
@Slf4j
public class SpringRestApiSampleController {

	@Autowired
	SpringRestApiSampleService sampleService;
	
	@PostMapping("/devices/logs")
	@ResponseBody
	public ResponseEntity<?> sample(@RequestHeader(value="X-Access-Key", required = false) String accessKey, 
			@Valid @RequestBody Payload1Json payload1Json) {
		
		if (StringUtils.isEmpty(accessKey)) {
			log.error("AccessKey値が設定されてないです。");
			throw new BadRequestException("ACSERR010", "AccessKey is null.");
		}

		// requestデータ設定
		RequestBean requestBean = new RequestBean();
		requestBean.setAccessKey(accessKey);
		requestBean.setBody(payload1Json);
		
		HttpHeaders header = setResponseHeader();

		// 処理
		ResponseBean responseBean = sampleService.checkAccessMsn(requestBean, header);

		return new ResponseEntity<String>(responseBean.getBody(), responseBean.getHeaders(), responseBean.getStatus());
	}
	
	@RequestMapping(value="/healthcheck", method = { GET, POST })
	@ResponseBody
	public ResponseEntity<?> healthCheck() {
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> BadRequestException(BadRequestException ex, WebRequest request) {
		HttpHeaders header = setResponseHeader();
		ErrorResponseBody errorResponseBody 
			= setErrorResponseBody(ex.code, ex.msg, "Bad Request");
	    return new ResponseEntity<Object>(errorResponseBody, header, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<?> requestJsonFormatException(Exception ex, WebRequest request) {
		HttpHeaders header = setResponseHeader();
		ErrorResponseBody errorResponseBody 
			= setErrorResponseBody("ACSERR020", "Json format of body is not correct.", "Bad Request");
	    return new ResponseEntity<Object>(errorResponseBody, header, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ACSServerErrorException.class)
	public ResponseEntity<?> ACSServerErrorException(ACSServerErrorException ex, WebRequest request) {
		HttpHeaders header = setResponseHeader();
		ErrorResponseBody errorResponseBody 
			= setErrorResponseBody(ex.code, ex.msg, "Server Error");
	    return new ResponseEntity<Object>(errorResponseBody, header, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private HttpHeaders setResponseHeader() {
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		return header;
	}
	
	private ErrorResponseBody setErrorResponseBody(String code, String msg, String reason) {
		ErrorDetail ErrorDetail = new ErrorDetail();
		ErrorDetail.setCode(code);
		ErrorDetail.setMessage(msg);
		ErrorDetail.setReason(reason);
		ErrorResponseBody errorResponseBody = new ErrorResponseBody();
		errorResponseBody.setError(ErrorDetail);
		return errorResponseBody;
	}
	
}
