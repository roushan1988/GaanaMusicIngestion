package com.til.prime.timesSubscription.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class HttpConnectionUtils {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HttpConnectionUtils.class);

	public <T> T requestForObject(Object data, String url, Class<T> clazz, String method){
		logger.info("Making HttpRequest url " + url + ", data: "+data+", contentType: application/json, method " + method);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		T response = null;
		try {
			switch (method) {
				case ("GET"):
					response = restTemplateUtil.getRestTemplate().getForObject(url, clazz);
					break;
				case ("POST"):
					response = restTemplateUtil.getRestTemplate().postForObject(url, data, clazz);
					break;
			}
		}catch (Exception e){
			logger.error("Got Exception For HttpRequest url " + url +", data: "+data, e);
			throw e;
		}
		logger.info("Response for url: "+url+", data: "+data+", response: "+response);
		return response;
	}

	public <T> T requestWithHeaders(Object data, Map<String, String> headerMap, String url, Class<T> clazz, String method){
		logger.info("Making HttpRequest url " + url + ", headers: "+headerMap+", data: "+data+", contentType: application/json, method " + method);
		HttpHeaders headers = new HttpHeaders();
		for(String key: headerMap.keySet()){
			headers.set(key, headerMap.get(key));
		}
		HttpEntity<Object> entity = new HttpEntity<Object>(data, headers);
		ResponseEntity<T> responseEntity = null;
		try {
			switch (method) {
				case ("GET"):
					responseEntity = restTemplateUtil.getRestTemplate().exchange(url, HttpMethod.GET, entity, clazz);
					break;
				case ("POST"):
					responseEntity = restTemplateUtil.getRestTemplate().exchange(url, HttpMethod.POST, entity, clazz);
					break;
			}
		}catch (Exception e){
			logger.error("Got Exception For HttpRequest url " + url +", data: "+data, e);
			throw e;
		}
		T response = responseEntity.getBody();
		logger.info("Response for url: "+url+", headers: "+headerMap+", data: "+data+", response: "+response);
		return response;
	}

	public String request(String data, String url, String contentType,
						  String method) throws Exception {
		logger.info("Making HttpRequest url " + url + " contentType "
				+ contentType + " method " + method);
		try {

			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Method", method);

			conn.addRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Accept", contentType);
			if (StringUtils.isNotBlank(data)) {
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes("UTF-8"));
				os.close();
			}

			String line;
			StringBuffer buffer = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				reader.close();
				logger.info("Response For HttpRequest url " + url
						+ " response " + buffer);
				return buffer.toString();

			} else {
				logger.info("Got Exception For HttpRequest url " + url
						+ " error  " + conn.getResponseCode());
				throw new Exception("Got response " + conn.getResponseCode());
			}

		} catch (Exception e) {
			logger.info("Got Exception For HttpRequest url " + url + " error  "
					+ e);
			throw e;
		}
	}

	public String request(Map<String, Object> params, String url,
						  String contentType, String method) throws Exception {
		logger.info("Making HttpRequest url " + url + " contentType "
				+ contentType + " method " + method);
		try {

			String data = prepareParams(params);
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Method", method);

			if (StringUtils.isNoneBlank(contentType)) {
				conn.addRequestProperty("Content-Type", contentType);
				conn.setRequestProperty("Accept", contentType);
			}
			if (StringUtils.isNotBlank(data)) {
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes("UTF-8"));
				os.close();
			}

			String line;
			StringBuffer buffer = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				reader.close();
				logger.info("Response For HttpRequest url " + url
						+ " response " + buffer);
				return buffer.toString();

			} else {
				logger.info("Got Exception For HttpRequest url " + url
						+ " error  " + conn.getResponseCode());
				throw new Exception("Got response " + conn.getResponseCode());
			}

		} catch (Exception e) {
			logger.info("Got Exception For HttpRequest url " + url + " error  "
					+ e);
			throw e;
		}
	}

	private String addParam(String key, String value)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		return sb.append(key).append("=")
				.append(URLEncoder.encode(String.valueOf(value), "UTF8"))
				.toString();
	}

	public String prepareParams(Map<String, Object> m) {
		String result = "";
		try {
			for (Map.Entry<String, Object> e : m.entrySet()) {
				if (StringUtils.isNotBlank(e.getValue().toString())) {
					result = result
							+ addParam(e.getKey(), String.valueOf(e.getValue()))
							+ "&";
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			result = result.substring(0, result.length() - 1);
		}
		return result;

	}

	public String requestWithHeader(Map<String, Object> params,
									Map<String, Object> header, String url, String contentType,
									String method) throws Exception {
		logger.info("Making HttpRequest url " + url + " contentType "
				+ contentType + " method " + method);
		try {

			String data = prepareParams(params);
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Method", method);

			conn.addRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Accept", contentType);

			for (Map.Entry<String, Object> e : header.entrySet()) {
				conn.setRequestProperty(e.getKey(), e.getValue().toString());
			}

			if (StringUtils.isNotBlank(data)) {
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes("UTF-8"));
				os.close();
			}

			String line;
			StringBuffer buffer = new StringBuffer();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				reader.close();
				logger.info("Response For HttpRequest url " + url
						+ " response " + buffer);
				return buffer.toString();

			} else {
				logger.info("Got Exception For HttpRequest url " + url
						+ " error  " + conn.getResponseCode());
				throw new Exception("Got response " + conn.getResponseCode());
			}

		} catch (Exception e) {
			logger.info("Got Exception For HttpRequest url " + url + " error  "
					+ e);
			throw e;
		}
	}

	public ResponseEntity<String> getViaStringTemplate(
			MultiValueMap<String, String> params, String url) {
		return restTemplateUtil.getRestTemplate().getForEntity(url,
				String.class, params);
	}

	public ResponseEntity<String> postViaStringTemplate(HttpHeaders headers,
														MultiValueMap<String, String> params, String apiUrl) {
		HttpEntity<?> httpEntity = new HttpEntity<Object>(params, headers);

		return restTemplateUtil.getRestTemplate().exchange(apiUrl,
				HttpMethod.POST, httpEntity, String.class);
	}

	public ResponseEntity<String> postViaStringTemplateSB(HttpHeaders headers,
														  MultiValueMap<String, StringBuilder> params, String apiUrl) {
		HttpEntity<?> httpEntity = new HttpEntity<Object>(params, headers);

		return restTemplateUtil.getRestTemplate().exchange(apiUrl,
				HttpMethod.POST, httpEntity, String.class);
	}

	public ResponseEntity<String> postJsonViaStringTemplate(
			HttpHeaders headers, String params, String apiUrl) {
		HttpEntity<?> httpEntity = new HttpEntity<Object>(params, headers);

		return restTemplateUtil.getRestTemplate().exchange(apiUrl,
				HttpMethod.POST, httpEntity, String.class);
	}

	public void postFormDataStringBuilder(String url,
										  Map<String, String> params, PrintWriter out, String method) {

		out.println("<html>");
		out.println("<head>");
		out.println("<TITLE>Processing payment....</TITLE>");
		out.println("<Script Language=JavaScript>");
		out.println("function processPayment()");
		out.println("{");
		out.println("document.payForm.submit();");
		out.println("}");
		out.println("</Script>");
		out.println("</head>");
		out.println("<body onLoad='JavaScript:processPayment();'>");
		out.println("<form name='payForm' action= '" + url + "' method='"
				+ method + "'>");

		for (Entry<String, String> entry : params.entrySet()) {
			out.println("<input type='hidden' name='" + entry.getKey()
					+ "' value='" + entry.getValue() + "'/><br/>");
		}
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	public void postFormData(String url, Map<String, String> params,
							 PrintWriter out, String method) {

		out.println("<html>");
		out.println("<head>");
		out.println("<TITLE>Processing payment....</TITLE>");
		out.println("<Script Language=JavaScript>");
		// out.println("<script type=\"text/javascript\" src=\"https://testpay.indiatimes.com/Hermes/js/postForm.js\"></script>");
		out.println("function processPayment()");
		out.println("{");
		out.println("document.payForm.submit();");
		out.println("}");
		out.println("</Script>");
		out.println("</head>");
		out.println("<body onLoad='JavaScript:processPayment();'>");
		// out.println("<body>");
		/*
		 * out.println("<div style="text-align: center; margin: 20px;">
		 * out.println("<img src="<%=Constant.APP_URL%>/images/ajax-loader.gif"
		 * /> out.println("</div>
		 */
		out.println("<form name='payForm' action= '" + url + "' method='"
				+ method + "'>");
		// Jan 2017 - SISA - Changed For Stringbuilder

		for (Entry<String, String> entry : params.entrySet()) {
			out.println("<input type='hidden' name='" + entry.getKey()
					+ "' value='" + entry.getValue() + "'/><br/>");
		}
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	private static final String USER_AGENT = "Mozilla/5.0";

	public String sendGET(String GET_URL) {
		try {

			URL obj = new URL(GET_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result
				return response.toString();
			} else {
				System.out.println("GET request not worked");
				return "";
			}

		} catch (Exception e) {
			return "";
		}
	}

	public String sendPOST(String POST_URL) {
		try {

			URL obj = new URL(POST_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - START
			con.setDoOutput(false);
			// For POST only - END

			int responseCode = con.getResponseCode();
			logger.info("POST Response Code :: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				logger.info("result :{}"+ response.toString());
				return response.toString();
			} else {
				logger.info("POST request not worked");
				return "";
			}

		} catch (Exception e) {
			return "";
		}
	}
}
