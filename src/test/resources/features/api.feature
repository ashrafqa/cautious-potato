@smoke @regression
Feature: JsonPlaceholder API

  Scenario: Get post by id
    Given the JsonPlaceholder API is available
    And I set the request header "Content-Type" to "application/json"
    When I request "/posts/1"
    Then the response status should be 200
    And the response field "id" should be 1
    And the response field "userId" should be 1
