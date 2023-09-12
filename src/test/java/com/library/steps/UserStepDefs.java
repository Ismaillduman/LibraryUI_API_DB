package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class UserStepDefs {
    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;

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
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when()
                .get(ConfigurationReader.getProperty("library.baseUri") + endpoint)
                .prettyPeek();

        thenPart = response.then();

    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {

        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path,is(notNullValue()));
    }

    /**
     * US02 RELATED STEPS
     * @param
     */

    String id;
    @Given("Path param is {string}")
    public void path_param_is(String pathParam) {
        givenPart.pathParam("id",pathParam);
        id=pathParam;
    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String path) {
        thenPart.body(path,is(id));
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> paths) {
        thenPart.body(paths.get(0),is(notNullValue()))
                .body(paths.get(1),is(notNullValue()))
                .body(paths.get(2),is(notNullValue()));
    }

    /**
     *  US03 RELATED STEPS
     */

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
        givenPart.contentType(contentType);
    }
    Map<String,Object> randomDataMap;
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomData) {
        Map<String,Object> requestBody = new LinkedHashMap<>();

        switch (randomData){
            case "user" :
                requestBody=LibraryAPI_Util.getRandomUserMap();
                break;
            case "book" :
                requestBody=LibraryAPI_Util.getRandomBookMap();
                break;
            default:
                throw new RuntimeException("Unexpected value: "+ randomData);
        }

        System.out.println("requestBody = " + requestBody);
        randomDataMap=requestBody;
        givenPart.formParams(requestBody);

    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint)
                .prettyPeek();

        thenPart=response.then();

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String value) {
        thenPart.body(path,is(value));
    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {





    }

    /**
     * US 04 RELATED STEPS
     *
     */
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        String user_id = response.path("user_id");
        DB_Util.runQuery("select * from users where id="+user_id);
        Map<String, Object> DBUser = DB_Util.getRowMap(1);

        // DB DATA --> Remove not needed part from DBUser map


        Response apiData = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParam("id",user_id)
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_user_by_id/{id}").prettyPeek();
        JsonPath jp = apiData.jsonPath();

        Map<String, Object> APIUser=new LinkedHashMap<>();
        APIUser.put("id",jp.getString("id"));
        APIUser.put("full_name",jp.getString("full_name"));
        APIUser.put("email",jp.getString("email"));
        APIUser.put("password",jp.getString("password"));
        APIUser.put("user_group_id",jp.getString("user_group_id"));
        APIUser.put("image",jp.getString("image"));
        APIUser.put("extra_data",jp.getString("extra_data"));
        APIUser.put("status",jp.getString("status"));
        APIUser.put("is_admin",jp.getString("is_admin"));
        APIUser.put("start_date",jp.getString("start_date"));
        APIUser.put("end_date",jp.getString("end_date"));
        APIUser.put("address",jp.getString("address"));


        //Assertion
        Assert.assertEquals(APIUser,DBUser);
    }

    /**
     * US 04 UI RELATED STEPS
     */
    LoginPage loginPage;

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        loginPage=new LoginPage();
        String email = (String) randomDataMap.get("email");
        String password = (String) randomDataMap.get("password");

        loginPage.login(email,password );
        BrowserUtil.waitFor(3);
    }
    BookPage bookPage;

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        bookPage=new BookPage();
        BrowserUtil.waitFor(2);
        String UIFullName = bookPage.accountHolderName.getText();
        String APIFullName = (String) randomDataMap.get("full_name");

        Assert.assertEquals(APIFullName,UIFullName);


    }


    /**
     * US 05 RELATED STEPS
     *
     */
    String token;
    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        token = LibraryAPI_Util.getToken(email, password);
        givenPart=given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(email, password));
    }
    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        givenPart.formParams("token",token);

    }
}
