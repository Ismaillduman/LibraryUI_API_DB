package com.library.steps;

import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

public class LoginStepDefs {
    LoginPage loginPage=new LoginPage();
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
       LibraryAPI_Util.getToken(userType);
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        given().accept(contentType);




    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String string) {

    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer int1) {

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String string) {

    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String string) {

    }
}
