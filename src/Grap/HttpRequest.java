package Grap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	private static HttpRequest httpRequest = null;
	private String charsetCode;

	private HttpRequest() {
		charsetCode="UTF-8";
	}

	public static HttpRequest getInstance() {
		if (httpRequest == null) {
			synchronized (HttpRequest.class) {
				if (httpRequest == null)
					httpRequest = new HttpRequest();
			}
		}
		return httpRequest;
	}
	
	public String getCharset() {
		return charsetCode;
	}
	
	public void setCharset(String charset) {
		charsetCode=charset;
	}

	public String doGet(String uri) {
		HttpGet httpGet = new HttpGet(uri);
		return sendHttpGet(httpGet);
	}

	/*
	 * only one paramter's http get request
	 */
	public String doGet(String uri, String paramName, String paramValue) {
		HttpGet httpGet = new HttpGet(uri);
		// build get uri with params
		URIBuilder uriBuilder = new URIBuilder(httpGet.getURI()).setParameter(paramName, paramValue);
		try {
			httpGet.setURI(uriBuilder.build());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return sendHttpGet(httpGet);
	}

	/*
	 * mulitple paramters of http get request
	 */
	public String doGet(String uri, List<NameValuePair> parameters) {
		HttpGet httpGet = new HttpGet(uri);
		String param = null;
		try {
			param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			// build get uri with params
			httpGet.setURI(new URIBuilder(httpGet.getURI().toString() + "?" + param).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpGet(httpGet);
	}

	public String doPost(String uri) {
		HttpPost httpPost = new HttpPost(uri);
		return sendHttpPost(httpPost);
	}

	public String doPost(String uri, String reqXml) {
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader("Content-Type", "application/xml");
		StringEntity entity = null;
		try {
			entity = new StringEntity(reqXml, charsetCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpPost.setEntity(entity);// http post with xml data
		return sendHttpPost(httpPost);
	}

	/*
	 * multiple http put params
	 */
	public String doPut(String uri, List<NameValuePair> parameters) {
		HttpPut httpPut = new HttpPut(uri);
		String param = null;
		try {
			param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			httpPut.setURI(new URIBuilder(httpPut.getURI().toString() + "?" + param).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPut(httpPut);
	}

	public String doPut(String uri, List<NameValuePair> parameters, String reqXml) {
		HttpPut httpPut = new HttpPut(uri);
		String param = null;
		try {
			param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			httpPut.setURI(new URIBuilder(httpPut.getURI().toString() + "?" + param).build());
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringEntity entity = null;
		try {
			entity = new StringEntity(reqXml, charsetCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpPut.setEntity(entity);

		return sendHttpPut(httpPut);
	}

	private String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault();
			// httpPost.setConfig(config);
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, charsetCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return responseContent;
	}

	private String sendHttpGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault();
			// httpGet.setConfig(config);
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, charsetCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	private String sendHttpPut(HttpPut httpPut) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault();
			// httpPut.setConfig(config);
			response = httpClient.execute(httpPut);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, charsetCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseContent;
	}
}
