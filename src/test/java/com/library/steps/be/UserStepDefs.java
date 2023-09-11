package com.library.steps.be;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class UserStepDefs {
    public static Map<String, Object> randomBookInformation;

    RequestSpecification givenPart;
    String globalId;

    Response response;
    ValidatableResponse thenPart;

    String token = LibraryAPI_Util.getToken();

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

       givenPart=given().log().uri()
               .header("x-library-token", LibraryAPI_Util.getToken(userType));

    }

    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(contentType);

    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endPoint) {

        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint);

        thenPart=response.then();

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {

       thenPart.statusCode(statusCode);
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {

        thenPart.contentType(contentType);
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String key) {
        // assertThat(response.path(key), everyItem(is(notNullValue())));
       // response.then().body(key, everyItem(notNullValue()));
        thenPart.body(key,everyItem(notNullValue()));

    }

    /**
     * US02 RELATED STEPS
     * @param
     */

    @And("Path param is {string}")
    public void pathParamIs(String userId) {
        globalId = userId;
        givenPart.pathParam("id", userId);
    }

    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String pathOfId) {
       // String actualId = response.jsonPath().getString(pathOfId);
       // assertThat(actualId, is(equalTo(globalId)));
        thenPart.body(pathOfId,is(globalId));
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> keys) {
        for (String eachKey : keys) {
//            Object ob = response.jsonPath().get(eachKey);
//            assertThat(ob,is(notNullValue()));
            thenPart.body(eachKey,is(notNullValue()));
        }

    }

    /**
     *  US03 RELATED STEPS
     */

    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String contentType) {
        givenPart.contentType(contentType);
    }
    Map<String,Object> randomDataMap;
    @And("I create a random {string} as request body")
    public void iCreateARandomAsRequestBody(String randomData) {

        Map<String, Object> requestBody = new LinkedHashMap<>();
        switch (randomData) {
            case "book":
                requestBody = LibraryAPI_Util.getRandomBookMap();
                break;
            case "user":
                requestBody = LibraryAPI_Util.getRandomUserMap();
                break;
            default:
                throw new RuntimeException("Wrong Content Type-->"+randomData);

        }
        System.out.println("requestBody = " + requestBody);


       randomDataMap=requestBody;
        givenPart.formParams(requestBody);
    }

    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endpoint) {
        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint)
                ;
        thenPart=response.then();

    }

    @And("the field value for {string} path should be equal to {string}")
    public void theFieldValueForPathShouldBeEqualTo(String arg0, String arg1) {

    }

    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String arg0) {
    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String arg0) {
    }

    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {
    }
}
