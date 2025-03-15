package testngpkg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Santamonica {
    ChromeDriver driver;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://santamonicaedu.in/");
    }

    @Test
    public void santa() {
        // Close popup if present
        try {
            WebElement closeButton = driver.findElement(By.xpath("//*[@id=\"launchevent\"]/div/div/div[1]/button/span"));
            if (closeButton.isDisplayed()) {
                closeButton.click();
                System.out.println("Popup closed.");
            }
        } catch (Exception e) {
            System.out.println("Popup not found or already closed.");
        }

        // Verify if the logo is present
        try {
            WebElement logo = driver.findElement(By.id("logo-img"));
            Assert.assertTrue(logo.isDisplayed(), "Logo is not displayed on the page.");
            System.out.println("Logo is present on the page.");
        } catch (Exception e) {
            System.out.println("Logo is NOT present on the page.");
            Assert.fail("Logo element not found.");
        }

        // Check for broken links
        checkBrokenLinks();

        // Navigate to the specified menu item
        try {
            WebElement menuItem = driver.findElement(By.xpath("//*[@id=\"menu-item-441\"]/a"));
            menuItem.click();
            System.out.println("Navigated to the menu item successfully.");
        } catch (Exception e) {
            System.out.println("Failed to navigate to the menu item.");
            Assert.fail("Menu item not found.");
        }
    }

    public void checkBrokenLinks() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("Total links found: " + links.size());

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url == null || url.isEmpty()) {
                System.out.println("Skipping empty or missing URL.");
                continue;
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode >= 400) {
                    System.out.println("Broken link: " + url + " | Status Code: " + responseCode);
                } else {
                    System.out.println("Valid link: " + url + " | Status Code: " + responseCode);
                }
                connection.disconnect();
            } catch (IOException e) {
                System.out.println("Error checking link: " + url);
            }
        }
    }

    @AfterTest
    public void tearDown() {
        //driver.quit();
    }
}
