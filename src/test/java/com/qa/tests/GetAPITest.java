package com.qa.tests;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.util.TestUtil;

public class GetAPITest extends TestBase {

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

	@Test(priority = 1)
	public void getAPITestWithoutHeaders() throws ClientProtocolException, IOException {

		restClient = new RestClient();
		closeableHttpResponse = restClient.get(url);

		// a. We get Statuscode
		int statuscode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code ::: " + statuscode);

		Assert.assertEquals(statuscode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		// b. We get JSON String
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API-->>" + responseJson);

		// Value for Page
		String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page");
		System.out.println("Value of per Page is >> " + perPageValue);
		// Single Value Assertion
		Assert.assertEquals(Integer.parseInt(perPageValue), 6);

		// Value for Total
		String totalValue = TestUtil.getValueByJPath(responseJson, "/total");
		System.out.println("Value of per total is >> " + totalValue);
		// Single Value Assertion
		Assert.assertEquals(Integer.parseInt(totalValue), 12);

		// Get the Value from JSON Array
		String lastName = TestUtil.getValueByJPath(responseJson, "/data[0]/last_name");
		String id = TestUtil.getValueByJPath(responseJson, "/data[0]/id");
		String avatar = TestUtil.getValueByJPath(responseJson, "/data[0]/avatar");
		String firstName = TestUtil.getValueByJPath(responseJson, "/data[0]/first_name");

		System.out.println("lastName : " + lastName);
		System.out.println("id : " + id);
		System.out.println("avatar : " + avatar);
		System.out.println("firstName : " + "George");

		Assert.assertEquals(lastName, "Bluth");
		Assert.assertEquals(Integer.parseInt(id), 1);
		Assert.assertEquals(avatar, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
		Assert.assertEquals(firstName, "George");

		// c . We get All Header using Hashmap
		Header[] headerArray = closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for (Header header : headerArray) {
			allHeaders.put(header.getName(), header.getValue());
		}
		System.out.println("Header Array -->" + allHeaders);
	}

	@Test(priority = 2)
	public void getAPITestWithHeaders() throws ClientProtocolException, IOException {
		restClient = new RestClient();

		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("username", "test@amazon.com");
//		headerMap.put("password", "test213");
//		headerMap.put("Auth Token", "12345");

		closeableHttpResponse = restClient.get(url, headerMap);

		// a. Status Code:
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code--->" + statusCode);

		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");

		// b. Json String:
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");

		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API---> " + responseJson);

		// single value assertion:
		// per_page:
		String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page");
		System.out.println("value of per page is-->" + perPageValue);
		Assert.assertEquals(Integer.parseInt(perPageValue), 6);

		// total:
		String totalValue = TestUtil.getValueByJPath(responseJson, "/total");
		System.out.println("value of total is-->" + totalValue);
		Assert.assertEquals(Integer.parseInt(totalValue), 12);

		// get the value from JSON ARRAY:
		String lastName = TestUtil.getValueByJPath(responseJson, "/data[0]/last_name");
		String id = TestUtil.getValueByJPath(responseJson, "/data[0]/id");
		String avatar = TestUtil.getValueByJPath(responseJson, "/data[0]/avatar");
		String firstName = TestUtil.getValueByJPath(responseJson, "/data[0]/first_name");

		System.out.println(lastName);
		System.out.println(id);
		System.out.println(avatar);
		System.out.println(firstName);

		// c. All Headers
		Header[] headersArray = closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for (Header header : headersArray) {
			allHeaders.put(header.getName(), header.getValue());
		}
		System.out.println("Headers Array-->" + allHeaders);

	}
}
