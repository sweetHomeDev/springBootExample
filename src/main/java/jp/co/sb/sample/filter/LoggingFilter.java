package jp.co.sb.sample.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingFilter implements Filter {
	
	// LOG Write
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
		HttpServletRequest httpReq = ((HttpServletRequest)request);
		long start = System.currentTimeMillis();
		String body = IOUtils.toString(wrappedRequest.getReader());
		log.info("START ACS_API.");
		log.info("request Method:{}",httpReq.getMethod());
		log.info("request URI:{}",httpReq.getRequestURI());
		Enumeration<String> headnames = httpReq.getHeaderNames();
		while(headnames.hasMoreElements()){
		    String name = headnames.nextElement();
		    log.info("request Header "+name+":"+httpReq.getHeader(name));
		}
		log.info("request body:{}",body);
		wrappedRequest.resetInputStream();
		
		// Controllerへ
		chain.doFilter(wrappedRequest, response);
		
		log.info("END ACS_API. Processing time {}", DurationFormatUtils.formatPeriod(start, System.currentTimeMillis(), "mm:ss.SSS"));
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	@Override
	public void destroy() {}

	private static class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

		private byte[] rawData;
		private HttpServletRequest request;
		private ResettableServletInputStream servletStream;

		public ResettableStreamHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
			this.servletStream = new ResettableServletInputStream();
		}


		public void resetInputStream() {
			servletStream.stream = new ByteArrayInputStream(rawData);
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			if (rawData == null) {
				rawData = IOUtils.toByteArray(this.request.getReader(),"UTF-8");
				servletStream.stream = new ByteArrayInputStream(rawData);
			}
			return servletStream;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			if (rawData == null) {
				rawData = IOUtils.toByteArray(this.request.getReader(),"UTF-8");
				servletStream.stream = new ByteArrayInputStream(rawData);
			}
			return new BufferedReader(new InputStreamReader(servletStream));
		}
		

		private class ResettableServletInputStream extends ServletInputStream {

			private InputStream stream;

			@Override
			public int read() throws IOException {
				return stream.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub
			}
		}
	}
	
}