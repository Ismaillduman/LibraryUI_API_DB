package com.library.steps;

import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class LoginStepDefs {
    LoginPage loginPage = new LoginPage();
    RequestSpecification request;
    String pathId;
    String token;
    Response response;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
       // token = LibraryAPI_Util.getToken(userType);

        String email = ConfigurationReader.getProperty("librarian_username");
        String password = ConfigurationReader.getProperty("librarian_password");
        token = LibraryAPI_Util.getToken(email, password);

    }

    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        request = given().log().uri().header("x-library-token", token)
                .and().accept(ContentType.JSON);


    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endPoint) {

        response = request.when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint);

    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        assertThat(response.contentType(), is(contentType));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {
        assertThat(response.path(field), everyItem(is(notNullValue())));


    }

    @And("Path param is {string}")
    public void pathParamIs(String idValue) {
         pathId=idValue;
         request.pathParam("id",pathId);
    }

    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String idValue) {
        assertThat(response.path(idValue),is(pathId));
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String> fields) {
        for (String eachField : fields) {
            assertThat(response.path(eachField),is(notNullValue()));
        }
    }
}
