package com.til.prime.timesSubscription.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

public class RestTemplateUtil {

	private Properties properties;
	private RestTemplate restTemplate;

	public RestTemplateUtil() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(2000000);
		requestFactory.setConnectTimeout(500000);
		restTemplate = new RestTemplate(requestFactory);
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String addParam(String key, String value)
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
				if (e.getValue() != null
						&& StringUtils.isNotBlank(e.getValue().toString())) {
					result = result
							+ addParam(e.getKey(), URLEncoder.encode(String.valueOf(e.getValue())))
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

}
