package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;

public class PostAPITest extends TestBase {
	TestBase testBase;
	String serviceUrl;
	String apiUrl;
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;

	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		testBase = new TestBase();
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("serviceURL");

		url = serviceUrl + apiUrl; // this gets uri = https://reqres.in/api/users from config file

	}

	@Test
	public void postAPITest() throws JsonGenerationException, JsonMappingException, IOException {

		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");

		// Jackson : Java API (Java Object to JSON - Marshalling )
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader"); // Expected user object

		// java object to jason file
		mapper.writeValue(new File("E:\\JavaProjects\\restapi\\src\\main\\java\\com\\qa\\data\\users.json"), users);

		// object to json string
		String usersJsonString = mapper.writeValueAsString(users);
		System.out.println("JSON String >>>> " + usersJsonString);

		// Call the API
		closeableHttpResponse = restClient.post(url, usersJsonString, headerMap);

		// Validating response from API
		// Get Status code and check
		int status = closeableHttpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(status, RESPONSE_STATUS_CODE_201);

		// Json String
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		// convert into JSON
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response from API >>" + responseJson);

		// Converting Json to JavaObject : unmarshalling
		Users usersResObj = mapper.readValue(responseString, Users.class); // actual user object
		System.out.println("usersResObj>>>" + usersResObj);

		Assert.assertTrue(users.getName().equals(usersResObj.getName()));
		Assert.assertTrue(users.getJob().equals(usersResObj.getJob()));

		System.out.println("usersResObj ID:" + usersResObj.getId());
		System.out.println("usersResObj Created At :" + usersResObj.getCreatedAt());

	}
}
