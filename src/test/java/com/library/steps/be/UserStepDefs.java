package com.library.steps.be;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.library.utility.LibraryAPI_Util.getRandomBookMap;
import static com.library.utility.LibraryAPI_Util.getRandomUserMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.library.steps.be.LoginStepDefs.*;

public class UserStepDefs {
    public static Map<String, Object> randomBookInformation;
    public static Map<String, Object> randomUserInformation;
    RequestSpecification requestSpecification;
    String globalId;

    Response response;

    String token = LibraryAPI_Util.getToken();

    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        requestSpecification = given().log().uri().header("x-library-token", token)
                .and().accept(ContentType.JSON);


    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endPoint) {

        response = requestSpecification.when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint);

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        assertThat(response.contentType(), is(contentType));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String key) {
        // assertThat(response.path(key), everyItem(is(notNullValue())));
        response.then().body(key, everyItem(notNullValue()));

    }

    @And("Path param is {string}")
    public void pathParamIs(String userId) {
        globalId = userId;
        requestSpecification.pathParam("id", userId);
    }

    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String pathOfId) {
        String actualId = response.jsonPath().getString(pathOfId);
        assertThat(actualId, is(equalTo(globalId)));
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> keys) {
        for (String eachKey : keys) {
//            Object ob = response.jsonPath().get(eachKey);
//            assertThat(ob,is(notNullValue()));
            response.then().body(eachKey, is(notNullValue()));
        }

    }

    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String contentType) {
        requestSpecification = requestSpecification.contentType(contentType);
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


//        randomDataMap=requestBody;
        requestSpecification.formParams(requestBody);
    }

    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endpoint) {
         requestSpecification.when().post(ConfigurationReader.getProperty("library.baseUri")+endpoint)
                 .prettyPeek();

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
