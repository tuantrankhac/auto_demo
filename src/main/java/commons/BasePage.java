package commons;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import constant.GlobalConstants;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.*;

public class BasePage {

	public static BasePage getBasePageObject() {
		return new BasePage();
	}

	public void openPageUrl(WebDriver driver, String pageUrl) {
		Allure.step("Mở trang URL: " + pageUrl);
		driver.get(pageUrl);
	}

	public String getPageTitle(WebDriver driver) {
		return Allure.step("Lấy tiêu đề trang hiện tại", () -> driver.getTitle());
	}

	public String getPageUrl(WebDriver driver) {
		return Allure.step("Lấy URL trang hiện tại", () -> driver.getCurrentUrl());
	}

	public String getPageSourceCode(WebDriver driver) {
		return driver.getPageSource();
	}

	public void backToPage(WebDriver driver) {
		Allure.step("Quay lại trang trước");
		driver.navigate().back();
	}

	public void forwardToPage(WebDriver driver) {
		Allure.step("Chuyển đến trang tiếp theo");
		driver.navigate().forward();
	}

	public void refreshCurrentPage(WebDriver driver) {
		Allure.step("Làm mới trang hiện tại");
		driver.navigate().refresh();
	}

	public Set<Cookie> getAllCookies(WebDriver driver) {
		return driver.manage().getCookies();
	}

	public void setAllCookies(WebDriver driver, Set<Cookie> allCookies) {
		for (Cookie cookie : allCookies) {
			driver.manage().addCookie(cookie);
		}
	}


	public Alert waitForAlertPresence(WebDriver driver) {
		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));
		return explicitWait.until(ExpectedConditions.alertIsPresent());
	}

	public void acceptAlert(WebDriver driver) {
		Allure.step("Chấp nhận alert");
//		Alert alert = waitForAlertPresence(driver);
//		alert.accept();
		waitForAlertPresence(driver).accept();
	}

	public void cancelAlert(WebDriver driver) {
		Allure.step("Hủy alert");
		waitForAlertPresence(driver).dismiss();
	}

	public String getAlertText(WebDriver driver) {
		return waitForAlertPresence(driver).getText();
	}

	public void sendkeyToAlert(WebDriver driver, String textValue) {
		waitForAlertPresence(driver).sendKeys(textValue);
	}

	public void switchToWindowByID(WebDriver driver, String windowID) {
		Set<String> allWindowIDs = driver.getWindowHandles();

		for (String id : allWindowIDs) {
			System.out.println(id);
			if (!id.equals(windowID)) {
				driver.switchTo().window(id);
				break;
			}
		}
	}

	public void switchToWindowByTitle(WebDriver driver, String tabTitle) {
		Allure.step("Chuyển sang cửa sổ có tiêu đề: " + tabTitle);
		Set<String> allWindowIDs = driver.getWindowHandles();

		for (String id : allWindowIDs) {
			driver.switchTo().window(id);
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(tabTitle)) {
				break;
			}
		}
	}

	public void closeTabWithoutParent(WebDriver driver, String parentId) {
		Set<String> allWindowIDs = driver.getWindowHandles();

		for (String id : allWindowIDs) {
			if (!id.equals(parentId)) {
				driver.switchTo().window(id);
				driver.close();
			}
			driver.switchTo().window(parentId);
		}
	}

	private By getByXpath(String xpathLocator) {
		try {
			return By.xpath(xpathLocator);
		}catch (NoSuchElementException e) {
			return null;
		}
	}

	public WebElement getWebElement(WebDriver driver, String xpathLocator) {
		try {
			return driver.findElement(By.xpath(xpathLocator));
		} catch (NoSuchElementException e) {
		}catch (Exception e) {
		}
		return null;
    }

	public WebElement getWebElement(WebDriver driver, String xpathLocator, String... params) {
		xpathLocator = getDynamicLocator(xpathLocator, params);
		try {
			return driver.findElement(By.xpath(xpathLocator));
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public List<WebElement> getListWebElement(WebDriver driver, String xpathLocator) {
		return driver.findElements(getByXpath(xpathLocator));
	}

	private String getDynamicLocator(String xpathLocator, String... params) {
		try {
			return String.format(xpathLocator, (Object[]) params);
		}catch (NoSuchElementException e) {
			return null;
		}

	}

	public void clickToElement(WebDriver driver, String xpathLocator) {
		Allure.step("Click vào element: " + xpathLocator);
		waitForElementClickable(driver, xpathLocator);
		getWebElement(driver, xpathLocator).click();
	}

	public void clickToElement(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Click vào element động với tham số");
		waitForElementClickable(driver, xpathLocator, params);
		getWebElement(driver, getDynamicLocator(xpathLocator, params)).click();
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, String xpathLocator){
		Allure.step("Clear dữ liệu trong textbox");
		getWebElement(driver, xpathLocator).clear();
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, String xpathLocator, String...params){
		Allure.step("Clear dữ liệu trong textbox");
		xpathLocator = getDynamicLocator(xpathLocator, params);
		getWebElement(driver, xpathLocator).clear();
	}

	public void sendkeyToElement(WebDriver driver, String xpathLocator, String textValue) {
		Allure.step("Nhập text vào element: " + xpathLocator + " với giá trị: " + textValue);
		try {
			waitForElementVisible(driver, xpathLocator);
			getWebElement(driver, xpathLocator).clear();
			getWebElement(driver, xpathLocator).sendKeys(textValue);
		}
		catch (NoSuchElementException e) {}
		catch (Exception e) {}
	}

	public void sendkeyToElementAreaTextbox(WebDriver driver, String xpathLocator, String textValue) {
		try {
			waitForElementClickable(driver, xpathLocator);
			getWebElement(driver, xpathLocator).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			getWebElement(driver, xpathLocator).sendKeys(Keys.BACK_SPACE);
			getWebElement(driver, xpathLocator).sendKeys(textValue);
		}catch (NoSuchElementException e) {}
		catch (Exception e) {}
	}

	public void sendkeyToElementAreaTextbox(WebDriver driver, String xpathLocator, String textValue, String...params) {
		try {
			waitForElementClickable(driver, xpathLocator, params);
			getWebElement(driver, xpathLocator, params).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			getWebElement(driver, xpathLocator, params).sendKeys(Keys.BACK_SPACE);
			getWebElement(driver, xpathLocator, params).sendKeys(textValue);
		}
		catch (NoSuchElementException e) {}
		catch (Exception e) {}

	}

	public void sendkeyToElement(WebDriver driver, String xpathLocator, String textValue, String... params) {
		Allure.step("Nhập text vào element động với giá trị: " + textValue);
		try {
			xpathLocator = getDynamicLocator(xpathLocator, params);
			getWebElement(driver, xpathLocator, params).clear();
			getWebElement(driver, xpathLocator, params).sendKeys(textValue);
		} catch (Exception e) {}
	}

	public String getElementText(WebDriver driver, String xpathLocator) {
		return Allure.step("Lấy text từ element: " + xpathLocator, () -> {
			waitForElementVisible(driver, xpathLocator);
			if(getWebElement(driver, xpathLocator) != null) {
				return getWebElement(driver, xpathLocator).getText();
			}else {
				return null;
			}
		});
	}

	public String getElementText(WebDriver driver, String xpathLocator, String... params) {
		return Allure.step("Lấy text từ element động", () -> {
			waitForElementVisible(driver, xpathLocator, params);
			if(getWebElement(driver, xpathLocator, params) != null) {
				return getWebElement(driver, xpathLocator, params).getText();
			}else {
				return null;
			}
		});
	}

	public void selectDropdownByText(WebDriver driver, String xpathLocator, String textItem) {
		Allure.step("Chọn dropdown theo text: " + textItem);
		Select select = new Select(getWebElement(driver, xpathLocator));
		select.selectByVisibleText(textItem);
	}

	public void selectDropdownByText(WebDriver driver, String xpathLocator, String textItem, String... params) {
		Allure.step("Chọn dropdown động theo text: " + textItem);
		xpathLocator = getDynamicLocator(xpathLocator, params);
		Select select = new Select(getWebElement(driver, xpathLocator));
		select.selectByVisibleText(textItem);
	}

	public String getSelectedItemDefaultDropdown(WebDriver driver, String xpathLocator) {
		return Allure.step("Lấy item đã chọn trong dropdown mặc định", () -> {
			Select select = new Select(getWebElement(driver, xpathLocator));
			return select.getFirstSelectedOption().getText();
		});
	}

	public String getSelectedItemDropdown(WebDriver driver, String xpathLocator) {
		return Allure.step("Lấy item đã chọn trong dropdown", () -> {
			select = new Select(getWebElement(driver, xpathLocator));
			return select.getFirstSelectedOption().getText();
		});
	}

	public String getSelectedItemDropdown(WebDriver driver, String xpathLocator, String... params) {
		xpathLocator = getDynamicLocator(xpathLocator, params);
		select = new Select(getWebElement(driver, xpathLocator));
		return select.getFirstSelectedOption().getText();
	}

	public boolean isDropdownMultiple(WebDriver driver, String xpathLocator) {
		Select select = new Select(getWebElement(driver, xpathLocator));
		return select.isMultiple();

	}

	public void deselectOptionByVisibleText(WebDriver driver, String xpathLocator, String optionText){
		WebElement dropdown = driver.findElement(By.xpath(xpathLocator));
		Select select = new Select(dropdown);
		if (select.isMultiple()){
			select.deselectByVisibleText(optionText);
		}else {
			System.out.println("Không phải multi-select");
		}
	}

	public void deselectAllOption(WebDriver driver, String xpathLocator){
		WebElement dropdown = driver.findElement(By.xpath(xpathLocator));
		Select select = new Select(dropdown);
		if (select.isMultiple()){
			select.deselectAll();
		}else {
			System.out.println("Không phải multi-select");
		}
	}


	public void selectItemInCustomDropdown(WebDriver driver, String parentXpath, String childXpath, String expectedItem) {
		getWebElement(driver, parentXpath).click();
		sleepInMiliSecond(2000);

		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childXpath)));
		for (WebElement item : allItems) {
			if (item.getText().trim().equals(expectedItem)) {
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].scrollIntoView(true)", item);
				sleepInMiliSecond(2000);
				item.click();
				break;
			}
		}
	}

	public String getElementAttribute(WebDriver driver, String xpathLocator, String attributeName) {
		return getWebElement(driver, xpathLocator).getAttribute(attributeName);
	}

	public String getElementAttribute(WebDriver driver, String xpathLocator, String attributeName, String... params) {
		xpathLocator = getDynamicLocator(xpathLocator, params);
		return getWebElement(driver, xpathLocator).getAttribute(attributeName);
	}

	public String getElementCssValue(WebDriver driver, String xpathLocator, String propertyName) {
		return getWebElement(driver, xpathLocator).getCssValue(propertyName);
	}

	public String getHexaColorFromRGBA(String rgbaValue) {
		return Color.fromString(rgbaValue).asHex();
	}

	public int getElementSize(WebDriver driver, String xpathLocator) {
		return getListWebElement(driver, xpathLocator).size();
	}

	public int getElementSize(WebDriver driver, String xpathLocator, String... params) {
		xpathLocator = getDynamicLocator(xpathLocator, params);
		return getListWebElement(driver, xpathLocator).size();
	}

	public void checkToDefaultCheckboxRadio(WebDriver driver, String xpathLocator) {
		Allure.step("Chọn checkbox/radio: " + xpathLocator);
		WebElement element = getWebElement(driver, xpathLocator);
		if (!element.isSelected()) {
			element.click();
		}
	}

	public void uncheckToDefaultCheckbox(WebDriver driver, String xpathLocator) {
		Allure.step("Bỏ chọn checkbox: " + xpathLocator);
		WebElement element = getWebElement(driver, xpathLocator);
		if (element.isSelected()) {
			element.click();
		}
	}

	public boolean isElementDisplayed(WebDriver driver, String xpathLocator) {
		return Allure.step("Kiểm tra element có hiển thị: " + xpathLocator, () -> {
			waitForElementVisible(driver,xpathLocator);
			try {
				return getWebElement(driver, xpathLocator).isDisplayed();
			} catch (Exception e) {
				return false;
			}
		});
	}

	public boolean isElementDisplayed(WebDriver driver, String xpathLocator, String... params) {
		return Allure.step("Kiểm tra element động có hiển thị", () -> {
			waitForElementVisible(driver,xpathLocator, params);
			try {
				return getWebElement(driver, getDynamicLocator(xpathLocator, params)).isDisplayed();
			} catch (NoSuchElementException e){
				return false;
			}catch (Exception e){
				return false;
			}
		});
	}

	public boolean isElementUndisplayed(WebDriver driver, String xpathLocator) {
		System.out.println("Start time = " + new Date().toString());
		overrideGlobalTimeout(driver, shortTimeout);
		List<WebElement> elements = getListWebElement(driver, xpathLocator);
		overrideGlobalTimeout(driver, longTimeout);

		if (elements.size() == 0) {
			System.out.println("Element not in DOM");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			System.out.println("Element in DOM but not visible on UI");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else {
			System.out.println("Element in DOM and visible on UI");
			return false;
		}
	}

	public void overrideGlobalTimeout(WebDriver driver, long timeout) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
	}

	public boolean isElementEnabled(WebDriver driver, String xpathLocator) {
		return getWebElement(driver, xpathLocator).isEnabled();
	}

	public boolean isElementEnabled(WebDriver driver, String xpathLocator, String... params) {
		return getWebElement(driver, getDynamicLocator(xpathLocator, params)).isEnabled();
	}

	public boolean isElementSelected(WebDriver driver, String xpathLocator) {
		waitForElementVisible(driver,xpathLocator);
		return getWebElement(driver, xpathLocator).isSelected();
	}

	public boolean isElementSelected(WebDriver driver, String xpathLocator, String... params) {
		return getWebElement(driver, getDynamicLocator(xpathLocator, params)).isSelected();
	}

	public void switchToFrameIframe(WebDriver driver, String xpathLocator) {
		driver.switchTo().frame(getWebElement(driver, xpathLocator));
	}

	public void switchToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public void hoverMouseToElement(WebDriver driver, String xpathLocator) {
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, xpathLocator)).perform();
	}

	public void hoverMouseToElement(WebDriver driver, String xpathLocator, String... params) {
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, getDynamicLocator(xpathLocator, params))).perform();
	}

	public void hightlightElement(WebDriver driver, String xpathLocator) {
		WebElement element = getWebElement(driver, xpathLocator);
		String originalStyle = element.getAttribute("style");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1])", element, "border: 2px solid red; border-style: dashed;");
		sleepInMiliSecond(2000);
		jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1])", element, originalStyle);
	}

	public void clickToElementByJS(WebDriver driver, String xpathLocator) {
		Allure.step("Click vào element bằng JavaScript: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, xpathLocator));
		sleepInMiliSecond(3000);
	}

	public void pressKeyToElement(WebDriver driver, String xpathLocator, Keys key) {
		action = new Actions(driver);
		action.sendKeys(getWebElement(driver, xpathLocator), key).perform();
	}

	public void pressKeyToElement(WebDriver driver, String xpathLocator, Keys key, String... params) {
		action = new Actions(driver);
		xpathLocator = getDynamicLocator(xpathLocator, params);
		action.sendKeys(getWebElement(driver, xpathLocator), key).perform();
	}

	public void scrollToElementOnTopByJS(WebDriver driver, String xpathLocator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator));
	}

	public void scrollToElementOnTopByJS(WebDriver driver, String xpathLocator, String...param) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator, param));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, String xpathLocator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, String xpathLocator, String... params) {
		try {
			jsExecutor = (JavascriptExecutor) driver;
			jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator, params));
		}catch (Exception e){
        }
	}

	public void scrollToBottomPageByJS(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void setAttributeInDOM(WebDriver driver, String xpathLocator, String attributeName, String attributeValue) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + attributeValue + "');", getWebElement(driver, xpathLocator));
	}

	public void removeAttributeInDOM(WebDriver driver, String xpathLocator, String attributeRemove) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, xpathLocator));
	}

	public void sendkeyToElementByJS(WebDriver driver, String xpathLocator, String value) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", getWebElement(driver, xpathLocator));
	}

	public String getAttributeInDOMByJS(WebDriver driver, String xpathLocator, String attributeName) {
		return (String) jsExecutor.executeScript("return arguments[0].getAttribute('" + attributeName + "');", getWebElement(driver, xpathLocator));
	}

	public String getElementValidationMessage(WebDriver driver, String xpathLocator) {
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(driver, xpathLocator));
	}

	public boolean isImageLoaded(WebDriver driver, String xpathLocator) {
		return (boolean) jsExecutor.executeScript("return arguments[0].complete " +
						"&& typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
				getWebElement(driver, xpathLocator));
	}

	public boolean isJQueryAjaxLoadedSuccess(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		jsExecutor = (JavascriptExecutor) driver;
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return (Boolean) jsExecutor.executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
			}
		};
		return explicitWait.until(jQueryLoad);
	}

	public static void clickOutside(WebDriver driver, String xpathLocator) {
		// Tìm một phần tử nằm ngoài popup (thường là phần tử body)
		WebElement outsideElement = driver.findElement(By.xpath(xpathLocator));
		// Dùng JavaScript để click ra ngoài
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", outsideElement);
	}

	public void sleepInMiliSecond(long timeout) {
		Allure.step("Đợi trong: " + timeout + " milisecond");
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void waitForElementVisible(WebDriver driver, String xpathLocator) {
		Allure.step("Chờ element hiển thị: " + xpathLocator);
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(xpathLocator)));
		}catch (Exception e) {
			System.out.println("Không tìm thấy Element");
		}
	}

	public void waitForElementVisible(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Chờ element động hiển thị");
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath((getDynamicLocator(xpathLocator, params)))));
		} catch (TimeoutException e){
			System.out.println("Không tìm thấy Element");
		} catch (Exception e) {
			System.out.println("Không tìm thấy Element");
		}
    }

	public void waitForAllElementVisible(WebDriver driver, String xpathLocator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(xpathLocator)));
	}

	public void waitForElementInvisible(WebDriver driver, String xpathLocator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(xpathLocator)));
	}

	public void waitForElementInvisible(WebDriver driver, String xpathLocator, String... params) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(getDynamicLocator(xpathLocator, params))));
	}

	public void waitForAllElementInvisible(WebDriver driver, String xpathLocator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, xpathLocator)));
	}

	public void waitForElementClickable(WebDriver driver, String xpathLocator) {
		Allure.step("Chờ element có thể click: " + xpathLocator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(xpathLocator)));
	}

	public void waitForElementClickable(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Chờ element động có thể click");
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(getDynamicLocator(xpathLocator, params))));
	}


	public void checkToCheckboxOrRadio(WebDriver driver, String xpathLocator) {
		if (!isElementSelected(driver, xpathLocator)) {
			getWebElement(driver, xpathLocator).click();
		}
	}

	private long shortTimeout = GlobalConstants.SHORT_TIMEOUT;
	private long longTimeout = GlobalConstants.LONG_TIMEOUT;
	private WebDriverWait explicitWait;
	private JavascriptExecutor jsExecutor;
	private Actions action;
	private Select select;


	public void clickIfElemenIsPresent(WebDriver driver, Object locator, int timeout) {
		By by = locator instanceof By ? (By) locator : By.xpath(locator.toString());
		long endTime = System.currentTimeMillis() + timeout;

		while (System.currentTimeMillis() < endTime) {
			try {
				WebElement e = driver.findElement(by);
				if (e != null) {
					e.click();
					return;
				}
			} catch (NoSuchElementException ex) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void uploadFile(WebDriver driver, String xpathLocator, String... fileNames) {
		Allure.step("Upload file: " + String.join(", ", fileNames));
		String filePath = GlobalConstants.UPLOAD_FILE_FOLDER;
		String fullFileName = "";
		for (String file : fileNames) {
			fullFileName = fullFileName + filePath + file + "\n";
		}
		fullFileName = fullFileName.trim();
		getWebElement(driver, xpathLocator).sendKeys(fullFileName);
	}

}