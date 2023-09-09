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
    String pathId;

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
    public void pathParamIs(String idValue) {
        pathId = idValue;
        requestSpecification.pathParam("id", pathId);
    }

    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String idValue) {
        assertThat(response.path(idValue), is(pathId));
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> fields) {
        for (String eachField : fields) {
            assertThat(response.path(eachField), is(notNullValue()));
        }
    }

    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String contentType) {
        requestSpecification = requestSpecification.contentType(contentType);
    }

    @And("I create a random {string} as request body")
    public void iCreateARandomAsRequestBody(String mapType) {
        switch (mapType) {

            case "book":
                randomBookInformation = getRandomBookMap();
                requestSpecification = requestSpecification.formParams(randomBookInformation);
                break;
            case "user":
                randomUserInformation = getRandomUserMap();
                requestSpecification = requestSpecification.formParams(randomUserInformation);
                break;
            default:
                throw new IllegalArgumentException("Wrong Content Type");

        }
    }

    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endpoint) {
        response = given().spec(requestSpecification)
                .when()
                .post(endpoint);
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
