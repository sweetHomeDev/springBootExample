package jp.co.sb.sample.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
//import java.net.HttpURLConnection;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sb.sample.bean.Payload1Json;
import jp.co.sb.sample.bean.PayloadDetail;
import jp.co.sb.sample.bean.PayloadJson;
import jp.co.sb.sample.bean.RequestBean;
import jp.co.sb.sample.bean.ResponseBean;
import jp.co.sb.sample.entity.ResultInfo;
import jp.co.sb.sample.errorException.ACSServerErrorException;
import jp.co.sb.sample.errorException.BadRequestException;
import jp.co.sb.sample.repository.ResultMsninfoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SpringRestApiSampleService {
	
	@Autowired
	ResultMsninfoRepository resultMsninfoRepository;
	
	// access check
	public ResponseBean checkAccessMsn(RequestBean requestBean, HttpHeaders header) {
		
		String accessKey = requestBean.getAccessKey();
		Payload1Json body = requestBean.getBody();
		String msn = body.payload1.msn;
		
		ResultInfo result = null;
		try {
		// DBから確認
		result = resultMsninfoRepository.findByCheckMsn(accessKey, msn);
		} catch (Exception e) {
			log.error("DBからデータ取得でエラーが発生しました。{}",e.toString());
			throw new BadRequestException("ACSERR030", "Database connect error.");
		}
		
		// 存在の場合
		if (result != null) {
			log.info("登録されている情報です。");
			ResponseBean responseBean = requestAPIM(header, result.getApi_key(), msn);
			return responseBean;
		} else {
			log.error("登録されていない情報です。");
			throw new BadRequestException("ACSERR040","Information not found in database.");
		}
	}

	private ResponseBean requestAPIM(HttpHeaders header, String apiKey, String msn) {

        //HttpURLConnection conn = null;
        HttpsURLConnection conn = null;
        
        ResponseBean responseBean = new ResponseBean();
        
        try {
        	InputStream istream = new FileInputStream("./sample.properties");
    		Properties prop = new Properties();
    		prop.load(istream);
    		
    		String apimUrl = prop.getProperty("apimurl");
    		
            URL url = new URL(apimUrl);
            
            //conn = (HttpURLConnection)url.openConnection();
            conn = (HttpsURLConnection)url.openConnection();
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            
            // headers
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("X-SBAPI-KEY", apiKey);
            
            // コネクションを開く
            conn.connect();
            
            // JSON body
            OutputStream outputStream = conn.getOutputStream();
            
	        Map<String, String> jsonMapMSN = new HashMap<>();
	        jsonMapMSN.put("msn" , msn);
	        Map<String, Object> jsonMapPayload = new HashMap<>();
	        jsonMapPayload.put("payload", jsonMapMSN);

            //JSON形式の文字列に変換する。
            JSONObject responseJsonObject = new JSONObject(jsonMapPayload);
            String jsonText = responseJsonObject.toString();
            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(jsonText);
            ps.close();

            outputStream.close();
            
            final int statusCode = conn.getResponseCode();
            responseBean.setHeaders(header);
    		responseBean.setStatus(HttpStatus.resolve(statusCode));
         
            StringBuilder result = new StringBuilder();
            //responseの読み込み
            InputStream in = null;
			//if (statusCode == HttpURLConnection.HTTP_OK) {
			if (statusCode == HttpsURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
            //final String encoding = conn.getContentEncoding();
            final InputStreamReader inReader = new InputStreamReader(in);
            final BufferedReader bufferedReader = new BufferedReader(inReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inReader.close();
            in.close();
            
            log.info("APIM response code:{}",statusCode);
            log.info("APIM response body:{}",result.toString());
            
            //if(statusCode == HttpURLConnection.HTTP_OK){
            if(statusCode == HttpsURLConnection.HTTP_OK){
            	PayloadJson payloadJson = null;
        		try {
        			payloadJson = parseJsonToBean(result.toString());
        		} catch (Exception e) {
        			log.error("Json形式のBodyをmap変換でエラーが発生しました。{}",e.toString());
        			throw new ACSServerErrorException("ACSERR060", "APIM's Body is not json format");
        		}
        		PayloadDetail payloadDetail = payloadJson.payload;
    			String r_year = payloadDetail.year;
    			String r_month = payloadDetail.month;
    			String r_day = payloadDetail.day;
    			String r_time = payloadDetail.time;
    			String r_msn = payloadDetail.msn;
    			String r_longitude = payloadDetail.longitude;
    			String r_latitude = payloadDetail.latitude;
    			String r_error_range = payloadDetail.error_range;
    			
    	        Map<String, String> r_jsonMapMSN = new HashMap<>();
    	        r_jsonMapMSN.put("time" , r_year+"-"+r_month+"-"+r_day+"T"+r_time);
    	        r_jsonMapMSN.put("msn" , r_msn);
    	        r_jsonMapMSN.put("longitude" , r_longitude);
    	        r_jsonMapMSN.put("latitude" , r_latitude);
    	        r_jsonMapMSN.put("error_range" , r_error_range);
    	        Map<String, Object> r_jsonMapPayload = new HashMap<>();
    	        r_jsonMapPayload.put("payload1", r_jsonMapMSN);
    	        
    	        responseBean.setBody(new JSONObject(r_jsonMapPayload).toString());
    			
            } else {
            	responseBean.setBody(result.toString());
            }
        } catch (Exception e) {
        	log.error("HTTP通信でエラーが発生しました。{}",e.toString());
        	throw new ACSServerErrorException("ACSERR060", "APIM's Body is not json format");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return responseBean;
    }
	
	private PayloadJson parseJsonToBean(String jsonStr) throws Exception {
		if(jsonStr == null){ throw new Exception();}
		ObjectMapper mapper = new ObjectMapper();
		PayloadJson payloadJson = mapper.readValue(jsonStr, PayloadJson.class);
		return payloadJson;
	}
	
}
