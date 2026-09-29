package com.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiSteps {
    private Response response;
    private final Map<String, String> requestHeaders = new HashMap<>();

    @Given("the JsonPlaceholder API is available")
    public void jsonPlaceholderApiIsAvailable() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Given("I set the request header {string} to {string}")
    public void setRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    @When("I request {string}")
    public void iRequest(String path) {
        response = given()
                .headers(requestHeaders)
                .when()
                .get(path);
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("the response field {string} should be {int}")
    public void responseFieldShouldBe(String field, int value) {
        response.then().body(field, equalTo(value));
    }
}
