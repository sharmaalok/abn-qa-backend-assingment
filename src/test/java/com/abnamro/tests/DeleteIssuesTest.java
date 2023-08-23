package com.abnamro.tests;

import com.abnamro.base.BaseTest;
import com.abnamro.constants.APIHttpStatus;
import com.abnamro.utils.JsonPathValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;

public class DeleteIssuesTest extends BaseTest{

	@Test(description ="Delete an Issue by iid")
	public void deleteIssueByIid()
	{
		Response allissues = restClient.get(ISSUES_ENDPOINT, true,  true);
		allissues.then().log().all()
				.assertThat().statusCode(APIHttpStatus.OK_200.getCode());

		JsonPathValidator js = new JsonPathValidator();
		Integer iid = js.read(allissues, "$.[0].iid"); //js.readList(allissues, "$..iid"); //get all existing iid's

		restClient.delete(PROJECTS_ENDPOINT,iid,true,true)
					.then().log().all()
						.assertThat().statusCode(APIHttpStatus.NO_CONTENT_204.getCode());
	}
}
