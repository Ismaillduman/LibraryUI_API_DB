package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;

import io.restassured.response.Response;

public class UIStepDefs extends BasePage {
    LoginPage loginPage = new LoginPage();

    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String userType) {
        loginPage.login(userType);
        BrowserUtil.waitFor(3);
    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String page) {
        navigateModule(page);
        BrowserUtil.waitFor(1);
    }


}
