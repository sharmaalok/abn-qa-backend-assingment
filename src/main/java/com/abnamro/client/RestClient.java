package com.abnamro.client;


import java.util.Map;
import com.abnamro.frameworkexception.APIFrameworkException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {

	private final RequestSpecBuilder specBuilder;
	private final String baseURI;
	private final String token;
	private final String projectId;
	private boolean isAuthorizationHeaderAdded = false;

	public RestClient(String baseURI, String token, String projectId) {
		specBuilder = new RequestSpecBuilder();
		this.baseURI = baseURI;
		this.token = token;
		this.projectId = projectId;
	}

	//Http Methods Utils

	// for get request without parameters
	public Response get(String issuesUrl, boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given(createRequestSpec(includeAuth)).log().all()
					.when()
					.get(issuesUrl);
		}
		return RestAssured.given(createRequestSpec(includeAuth)).when().get(issuesUrl);
	}

	// for get request with parameters
	public Response get(String issuesUrl, Map<String, Object> queryParams,  Map<String, String> headersMap, boolean includeAuth, boolean log) {

		if(log) {
			return RestAssured.given(createRequestSpec(headersMap, queryParams, includeAuth)).log().all()
					.when()
					.get(issuesUrl);
		}
		return RestAssured.given(createRequestSpec(headersMap, queryParams, includeAuth)).when().get(issuesUrl);
	}

	
	// for post request with request body
	public Response post(String projectUrl, String contentType, Object requestBody, boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth)).log().all()
					.when()
					.post(projectUrl+"/"+projectId+"/issues");
		}
		return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth))
				.when()
				.post(projectUrl+"/"+projectId+"/issues");
	}

	// for put request with request body
	public Response put(String projectUrl, Integer issueIid, String contentType, Object requestBody, boolean includeAuth,  boolean log) {
		if(log) {
			return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth)).log().all()
					.when()
					.put(projectUrl+"/"+projectId+"/issues/"+issueIid);
		}
		return RestAssured.given(createRequestSpec(requestBody, contentType, includeAuth))
				.when()
				.put(projectUrl+"/"+projectId+"/issues/"+issueIid);
	}

	// for delete request with request body
	public Response delete(String projectUrl,Integer issueIid,boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given(createRequestSpec(includeAuth)).log().all()
					.when()
					.delete(projectUrl+"/"+projectId+"/issues"+"/"+issueIid);
		}
		return RestAssured.given(createRequestSpec(includeAuth))
				.when()
				.delete(projectUrl+"/"+projectId+"/issues"+"/"+issueIid);
	}


	//for adding Authorisation header
	public void addAuthorizationHeader() {
		if(!isAuthorizationHeaderAdded) {
			specBuilder.addHeader("Authorization", "Bearer " + token);
			isAuthorizationHeaderAdded = true;
		}
	}


	//for adding specific header content
	private void setRequestContentType(String contentType) {//json-JSON-Json
		switch (contentType.toLowerCase()) {
			case "json":
				specBuilder.setContentType(ContentType.JSON);
				break;
			case "xml":
				specBuilder.setContentType(ContentType.XML);
				break;
			case "text":
				specBuilder.setContentType(ContentType.TEXT);
				break;
			case "multipart":
				specBuilder.setContentType(ContentType.MULTIPART);
				break;

			default:
				throw new APIFrameworkException("INVALIDCONTENTTYPE");
		}
	}

	//checking whether auth should be added
	private RequestSpecification createRequestSpec(boolean includeAuth) {
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {addAuthorizationHeader();}
		return specBuilder.build();
	}

	private RequestSpecification createRequestSpec(Map<String, String> headersMap, Map<String,Object> queryParams, boolean includeAuth) {
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {addAuthorizationHeader();}
		if(headersMap!=null) {
			specBuilder.addHeaders(headersMap);
		}
		if(queryParams!=null) {
			specBuilder.addQueryParams(queryParams);
		}
		return specBuilder.build();
	}


	private RequestSpecification createRequestSpec(Object requestBody, String contentType, boolean includeAuth) {
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {addAuthorizationHeader();}
		setRequestContentType(contentType);

		if(requestBody!=null) {
			specBuilder.setBody(requestBody);
		}
		return specBuilder.build();
	}

	
}