package com.dipgen.service.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtil {

	/**
	 * HTTP GET URL, retrieve byte array
	 * 
	 * @param url
	 *            URL
	 * @return byte array
	 */
	public static byte[] get(String url) {
		DefaultHttpClient httpclient = null;
		HttpGet httpGet = null;
		try {
			httpclient = new DefaultHttpClient();
			httpGet = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			return IOUtils.toByteArray(entity.getContent());
		} catch (Exception ex) {
			throw new RuntimeException("error sending HTTP GET to this URL: " + url);
		} finally {
			httpGet.releaseConnection();
		}
	}
}
