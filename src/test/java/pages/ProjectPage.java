package pages;

import elements.Input;
import elements.Select;
import elements.TextArea;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import models.TestCase;
import models.TestSuite;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

import static org.testng.Assert.assertEquals;

@Log4j2
public class ProjectPage extends BasePage {
    public static String URL = "project/";
    public static String PROJECT_NAME = ".subheader";
    public static final By TEST_REPOSITORY_PAGE_TITLE = By.xpath("//*[contains(text(),'Test repository')]");
    public static final By CREATE_NEW_CASE_BUTTON = By.cssSelector(".btn.mr-2.btn-primary");
    public static final By SAVE_NEW_TEST_CASE_BUTTON = By.xpath("//*[(text() = 'Save')]");
    public static final By TEST_CASE = By.cssSelector(".case-row-title");
    public static final By DELETE_TEST_CASE_BUTTON = By.cssSelector("button[title='Delete case']");
    public static final By DELETE_CONFIRMATION_BUTTON = By.xpath("//button[contains(@class, 'btn-danger') and contains(text(),'Delete')]");
    public static final By TRASH_BIN_BUTTON = By.xpath("//*[@class='fa fa-trash']");
    public static final By ADD_SUITE_BUTTON = By.cssSelector(".btn.mr-3.btn-primary");
    public static final By CONFIRM_CREATING_SUITE_BUTTON = By.id("saveButton");
    public static final By DELETE_SUITE_BUTTON = By.xpath("//*[contains(text(),'Delete suite')]");
    public static final By TEST_SUITE_NAME_TITLE = By.cssSelector(".suite-header");
    public static final By TEST_PLANS_PAGE = By.xpath("//*[contains(@class, 'submenu-item-text') and contains(text(),'Test Plans')]");
    public static final By TEST_RUN_PAGE = By.xpath("//*[contains(@class, 'submenu-item-text') and contains(text(),'Test Runs')]");
    public static final By START_RUN_BUTTON = By.xpath("//*[contains(@class, 'btn btn-primary') and contains(text(),'Start run')]");


    public ProjectPage(WebDriver driver) {
        super(driver);
    }

    @Step("Validation that the project is opened")
    public ProjectPage isPageOpened() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(TEST_REPOSITORY_PAGE_TITLE));
        return this;
    }

    @Step("Open page with {projectName} project")
    public ProjectPage openPage(String projectName) {
        driver.get(URN + URL + projectName);
        isPageOpened();
        return this;
    }

    @Step("Click button to create test case")
    public ProjectPage clickOnNewCaseCreatingButton() {
        driver.findElement(CREATE_NEW_CASE_BUTTON).click();
        return this;
    }

    @Step("Setting parameters for a new test case")
    public ProjectPage addTestCaseParameters(TestCase testCase) {
        new Input(driver, "Title").write(testCase.getTitle());
        new Input(driver, "Description").write(testCase.getDescription());
        new Select(driver, "Status").select(testCase.getStatus());
        new Select(driver, "Suite").select(testCase.getSuite());
        new Select(driver, "Severity").select(testCase.getSeverity());
        new Select(driver, "Priority").select(testCase.getPriority());
        new Select(driver, "Type").select(testCase.getType());
        new Select(driver, "Milestone").select(testCase.getMilestone());
        new Select(driver, "Behavior").select(testCase.getBehavior());
        new Select(driver, "Automation status").select(testCase.getAutomationStatus());
        new Input(driver, "Pre-conditions").write(testCase.getPreConditions());
        new Input(driver, "Post-conditions").write(testCase.getPostConditions());
        return this;
    }

    @Step("Click on \"Save\" button")
    public ProjectPage clickOnSaveTestCaseButton() {
        driver.findElement(SAVE_NEW_TEST_CASE_BUTTON).click();
        return this;
    }

    @Step("Validation that new case is crated")
    public boolean validateThatNewCaseIsCreated(String testCase) {
        boolean condition = false;
        for (int i = 0; i <= driver.findElements(TEST_CASE).size(); i++) {
            String testCaseName = driver.findElement(TEST_CASE).getText();
            if (testCaseName.equals(testCase)) {
                condition = true;
            }
        }
        return condition;
    }

    @Step("Delete test case \"{testCase}\"")
    public ProjectPage deleteTestCase(String testCase) {
        List<WebElement> testCases = driver.findElements(TEST_CASE);
        for (WebElement element : testCases) {
            String testCaseName = element.getText();
            log.info("Test case: " + testCaseName);
            if (testCaseName.equals(testCase)) {
                element.click();
                driver.findElement(DELETE_TEST_CASE_BUTTON).click();
                driver.findElement(DELETE_CONFIRMATION_BUTTON).click();
                log.info(String.format("Test case \"%s\" was deleted", testCaseName));
            }
        }
        return this;
    }

    @Step("Validation that the case \"{testCase}\" does not exist anymore")
    public ProjectPage validateThatCaseDoesNotExist(String testCase) {

        List<WebElement> testCases = driver.findElements(TEST_CASE);
        int count = 0;
        for (WebElement element : testCases) {
            String testCaseName = element.getText();
            log.info("Test case: " + testCaseName);
            if (testCaseName.equals(testCase)) {
                log.error(String.format("Test case \"%s\" still exists", testCase));
                count++;
            }
        }
        Assert.assertEquals(count, 0);
        return this;
    }

    @Step("Validation that project is opened")
    public ProjectPage validateThatProjectIsOpened(String projectName) {
        assertEquals(driver.findElement(By.cssSelector(PROJECT_NAME)).getText(), projectName.toUpperCase());
        return this;
    }

    public ProjectPage refreshPage() {
        driver.navigate().refresh();
        return this;
    }

    @Step("Click button to create test suite")
    public ProjectPage clickOnNewSuiteCreatingButton() {
        driver.findElement(ADD_SUITE_BUTTON).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(CONFIRM_CREATING_SUITE_BUTTON));
        return this;
    }

    @Step("Setting parameters for a new test suite")
    public ProjectPage addTestSuiteParameters(TestSuite testSuite) {
        new Input(driver, "Suite name").write(testSuite.getSuiteName());
        new TextArea(driver, "Preconditions").write(testSuite.getPreconditions());
        new TextArea(driver, "Description").write(testSuite.getDescription());
        return this;
    }

    @Step("Click on \"Create Suite\" button")
    public ProjectPage clickOnSaveButton() {
        driver.findElement(CONFIRM_CREATING_SUITE_BUTTON).click();
        return this;
    }

    @Step("Validation that new suite is created")
    public boolean validateThatNewSuiteIsCreated(String testSuite) {
        boolean condition = false;
        for (int i = 0; i <= driver.findElements(TEST_SUITE_NAME_TITLE).size(); i++) {
            String testSuiteName = driver.findElement(TEST_SUITE_NAME_TITLE).getText();
            if (testSuiteName.equals(testSuite)) {
                condition = true;
            }
        }
        return condition;
    }

    @Step("Delete test suite {name}")
    public ProjectPage deleteSuite(String name) {
        List<WebElement> trash = driver.findElements(TEST_SUITE_NAME_TITLE);
        for (WebElement element : trash) {
            String testSuiteName = element.getText();
            log.info("Test suite: " + testSuiteName);
            if (testSuiteName.equals(name)) {
                WebElement trashBinIconElement = driver.findElement(TRASH_BIN_BUTTON);
                JavascriptExecutor executor = (JavascriptExecutor)driver;
                executor.executeScript("arguments[0].click();", trashBinIconElement);
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DELETE_SUITE_BUTTON));
                driver.findElement(DELETE_SUITE_BUTTON).click();
                log.info(String.format("Test suite '%s' was deleted", testSuiteName));
            }
        }
        return this;
    }

    @Step("Validation that the suite {testSuite} does not exist anymore")
    public ProjectPage validateThatSuiteDoesNotExist(String testSuite) {

        List<WebElement> trash = driver.findElements(TEST_SUITE_NAME_TITLE);
        int count = 0;
        for (WebElement element : trash) {
            String testSuiteName = element.getText();
            log.info("Test suite: " + testSuiteName);
            if (testSuiteName.equals(testSuite)) {
                log.error(String.format("Test suite '%s' still exists", testSuite));
                count++;
            }
        }
        Assert.assertEquals(count, 0);
        return this;
    }

    public TestPlanPage goToTestPlanPage() {
        driver.findElement(TEST_PLANS_PAGE).click();
        return new TestPlanPage(driver);
    }

    public TestRunPage goToTestRunPage() {
        driver.findElement(TEST_RUN_PAGE).click();
        return new TestRunPage(driver);
    }
}
