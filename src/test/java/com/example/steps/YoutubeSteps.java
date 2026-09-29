package com.example.steps;

import com.example.support.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class YoutubeSteps {
    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Given("I open YouTube")
    public void openYouTube() {
        driver().get("https://www.youtube.com/");
        acceptConsentIfPresent();
    }

    @When("I search for {string}")
    public void searchFor(String query) {
        WebElement searchBox = new WebDriverWait(driver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.name("search_query")));
        searchBox.clear();
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.ENTER);
    }

    @When("I open the first video result")
    public void openFirstVideoResult() {
        WebElement firstResult = new WebDriverWait(driver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("ytd-video-renderer a#thumbnail")));
        firstResult.click();
    }

    @When("I open the YouTube video {string}")
    public void openDirectVideo(String url) {
        driver().get(url);
        acceptConsentIfPresent();
    }

    @Then("I should see video results")
    public void shouldSeeVideoResults() {
        new WebDriverWait(driver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-video-renderer")));
    }

    @Then("the page title should contain {string}")
    public void titleShouldContain(String expected) {
        String title = driver().getTitle();
        Assert.assertTrue(title.toLowerCase().contains(expected.toLowerCase()),
                "Page title should include the search term");
    }

    @Then("the video should be playing")
    public void videoShouldBePlaying() {
        verifyVideoIsPlaying();
    }

    private void verifyVideoIsPlaying() {
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("video")));

        clickPlayerIfPresent();
        clickIfPresent(By.cssSelector("button.ytp-large-play-button"), Duration.ofSeconds(3));
        clickIfPresent(By.cssSelector("button.ytp-play-button"), Duration.ofSeconds(3));

        skipAdIfPresent();

        Boolean isPlaying = wait.until(videoIsPlaying());
        Assert.assertTrue(isPlaying, "Video should be playing (currentTime > 0 and not paused)");
    }

    private ExpectedCondition<Boolean> videoIsPlaying() {
        return d -> {
            WebElement video = d.findElement(By.cssSelector("video"));
            JavascriptExecutor js = (JavascriptExecutor) d;
            try {
                js.executeScript(
                        "arguments[0].muted = true;" +
                                "if (arguments[0].paused && arguments[0].play) {" +
                                "  const p = arguments[0].play();" +
                                "  if (p && p.catch) { p.catch(()=>{}); }" +
                                "}",
                        video
                );
            } catch (Exception ignored) {
            }
            Object currentTime = js.executeScript("return arguments[0].currentTime;", video);
            Object paused = js.executeScript("return arguments[0].paused;", video);
            return currentTime instanceof Number
                    && ((Number) currentTime).doubleValue() > 0.5
                    && Boolean.FALSE.equals(paused);
        };
    }

    private void acceptConsentIfPresent() {
        List<By> selectors = List.of(
                By.cssSelector("button#agree-button"),
                By.cssSelector("button.yt-spec-button-shape-next"),
                By.xpath("//button[contains(., 'I agree') or contains(., 'I Agree') or contains(., 'Accept all') or contains(., 'Accept All')]")
        );
        for (By by : selectors) {
            try {
                WebElement button = new WebDriverWait(driver(), Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(by));
                button.click();
                break;
            } catch (TimeoutException ignored) {
            }
        }
    }

    private void skipAdIfPresent() {
        List<By> selectors = List.of(
                By.cssSelector("button.ytp-ad-skip-button"),
                By.cssSelector("button.ytp-ad-skip-button-modern")
        );
        for (By by : selectors) {
            try {
                WebElement button = new WebDriverWait(driver(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(by));
                button.click();
                break;
            } catch (TimeoutException ignored) {
            }
        }
    }

    private void clickPlayerIfPresent() {
        List<By> selectors = List.of(
                By.cssSelector("div.html5-video-player"),
                By.cssSelector("div#player")
        );
        for (By by : selectors) {
            try {
                WebElement player = new WebDriverWait(driver(), Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(by));
                try {
                    player.click();
                } catch (WebDriverException e) {
                    ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", player);
                }
                break;
            } catch (TimeoutException ignored) {
            }
        }
    }

    private void clickIfPresent(By by, Duration timeout) {
        try {
            WebElement element = new WebDriverWait(driver(), timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            try {
                element.click();
            } catch (WebDriverException e) {
                ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", element);
            }
        } catch (TimeoutException ignored) {
        }
    }
}
