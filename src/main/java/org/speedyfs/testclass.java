package org.speedyfs;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class testclass {
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("APS-Token", "f59ee498d07b88a4d6cc25c852eec222");
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		// HttpEntity<String> requestEntity = new HttpEntity<String>("headers", headers);
		ResponseEntity<String> entity = restTemplate.exchange("https://demo-cip.apswebapps.com/api/Program", HttpMethod.GET, requestEntity, String.class);
		System.out.println(entity);
		/*
		 * try { SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getInstance("TLS"), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); Scheme sch = new Scheme("https", 443, sf);
		 * DefaultHttpClient httpClient = new DefaultHttpClient(); httpClient.getConnectionManager().getSchemeRegistry().register(sch); HttpGet getRequest = new
		 * HttpGet("https://demo-cip.apswebapps.com/api/Program"); getRequest.addHeader("accept", "application/json"); getRequest.addHeader("APS-Token",
		 * "c7cc9f828b70a2982eeeab133e032a0a");
		 * 
		 * HttpResponse response = httpClient.execute(getRequest); System.out.println(response); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * catch (NoSuchAlgorithmException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		/*
		 * try { URL url = new URL("https://demo-cip.apswebapps.com/api/Program"); HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setRequestMethod("GET");
		 * conn.setRequestProperty("Accept", "application/json"); conn.setRequestProperty("APS-Token", "c7cc9f828b70a2982eeeab133e032a0a"); if (conn.getResponseCode() != 200) {
		 * System.out.println("sdfgsdfsd"); throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode()); } System.out.println(conn.getResponseMessage()); }
		 * catch (Exception e) { e.printStackTrace(); }
		 */
	}
}
