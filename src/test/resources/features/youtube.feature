Feature: YouTube search and playback

  @smoke @regression
  Scenario: Search for Selenium WebDriver
    Given I open YouTube
    When I search for "Selenium WebDriver"
    Then I should see video results
    And the page title should contain "selenium"

  @regression
  Scenario: Play a video from search results
    Given I open YouTube
    When I search for "Selenium WebDriver"
    And I open the first video result
    Then the video should be playing

  @smoke @regression
  Scenario: Play a video from a direct link
    When I open the YouTube video "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    Then the video should be playing
