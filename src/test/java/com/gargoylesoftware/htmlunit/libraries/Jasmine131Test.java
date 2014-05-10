package com.gargoylesoftware.htmlunit.libraries;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import org.eclipse.jetty.server.Server;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(BrowserRunner.class)
public class Jasmine131Test extends WebDriverTestCase {
    private static Server SERVER_;

    /**
     * Returns the Jasmine version being tested.
     * @return the Jasmine version being tested
     */
    protected static String getVersion() {
        return "1.3.1";
    }

    @BeforeClass
    public static void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/Jasmine/" + getVersion(), null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void zzz_stopServer() throws Exception {
        SERVER_.stop();
    }

    /**
     * Overridden so it doesn't check for background threads.
     *
     * {@inheritDoc}
     */
    @After
    @Override
    public void releaseResources() {
        // nothing
    }

    protected void runTest(String testName) throws Exception {
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        try {
            final WebDriver webdriver = getWebDriver();
            webdriver.get("http://localhost:" + PORT + "/SpecRunner.html");

            WebElement status;
            while ((status = getStatus(webdriver)) == null || status.getText().indexOf("Passing") == -1) {
                Thread.sleep(100);
                if (System.currentTimeMillis() > endTime) {
                    fail("Test runs too long (longer than " + runTime / 1000 + "s)");
                }
            }
            assertThat(status.getText(), equalTo("Passing 5 specs"));
        }
        catch (final Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while ((t = t.getCause()) != null) {
                t.printStackTrace();
            }
            throw e;
        }
    }

    private WebElement getStatus(WebDriver webdriver) {
        try {
            return webdriver.findElement(By.className("bar"));
        } catch(Exception e) {
            return null;
        }
    }

    @Test
    public void core__SpecRunner() throws Exception {
        runTest("core: SpecRunner");
    }
}
