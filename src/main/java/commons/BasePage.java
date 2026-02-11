package commons;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import constant.GlobalConstants;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BasePage {

	public static BasePage getBasePageObject() {
		return new BasePage();
	}

	public void openPageUrl(WebDriver driver, String pageUrl) {
		Allure.step("Mở trang URL: " + pageUrl);
		driver.get(pageUrl);
	}

	// ======================= Get Title =========================

	public String getPageTitle(WebDriver driver) {
		return Allure.step("Lấy tiêu đề trang hiện tại", () -> driver.getTitle());
	}

	public String getPageTitleByJS(WebDriver driver) {
		return Allure.step("Lấy tiêu đề trang hiện tại bằng Javascript", () -> {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			return (String) jsExecutor.executeScript("return document.title;");
		});
	}

	public String getPageUrl(WebDriver driver) {
		return Allure.step("Lấy URL trang hiện tại", () -> driver.getCurrentUrl());
	}

	public String getPageSourceCode(WebDriver driver) {
		return driver.getPageSource();
	}

	// ======================= Back =========================

	public void backToPageByNavigate(WebDriver driver) {
		Allure.step("Quay lại trang trước");
		driver.navigate().back();
	}

	public void backToPageByJS(WebDriver driver) {
		Allure.step("Quay lại trang trước bằng Javascript");
		((JavascriptExecutor) driver).executeScript("window.history.back();");
	}

	// ======================= Forward =========================

	public void forwardToPageByNavigate(WebDriver driver) {
		Allure.step("Chuyển đến trang tiếp theo");
		driver.navigate().forward();
	}

	public void forwardToPageByJS(WebDriver driver) {
		Allure.step("Chuyển đến trang tiếp theo bằng Javascript");
		((JavascriptExecutor) driver).executeScript("window.history.forward();");
	}

	// ======================= Refresh =========================

	public void refreshPageByNavigate(WebDriver driver) {
		Allure.step("Làm mới trang bằng navigate.refresh");
		driver.navigate().refresh();
		sleepInMiliSecond(500);
	}

	public void refreshPageByGetUrl(WebDriver driver) {
		Allure.step("Làm mới trang bằng get Url hiện tại");
		driver.get(driver.getCurrentUrl());
		sleepInMiliSecond(500);
	}

	public void refreshPageByJS(WebDriver driver) {
		Allure.step("Làm mới trang bằng JS");
		((JavascriptExecutor) driver).executeScript("location.reload();");
		sleepInMiliSecond(500);
	}

	public void refreshPageByActions(WebDriver driver) {
		Allure.step("Làm mới trang bằng Actions");
		Actions action = new Actions(driver);
		action.sendKeys(Keys.F5).perform();
		sleepInMiliSecond(500);
	}


	// ======================= Cookies =========================

	public Set<Cookie> getAllCookies(WebDriver driver) {
		return Allure.step("Lấy tất cả cookies domain hiện tại: ", () -> {
			return driver.manage().getCookies();
		});
	}


	public String getCookieByJS(WebDriver driver) {
		Allure.step("Lấy tất cả cookies bằng Javascript");
		return (String) ((JavascriptExecutor) driver).executeScript("return document.cookie;");
	}


	public Map<String, Object> getAllCookiesByCDP(WebDriver driver) {
		Allure.step("Lấy tất cả cookies thông qua CDP");
		try {
			if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
				org.openqa.selenium.devtools.DevTools devTools = 
					((org.openqa.selenium.chrome.ChromeDriver) driver).getDevTools();
				devTools.createSession();
				// domains: Network or Browser, depending on Selenium version
				Map<String, Object> cookies = 
					(Map<String, Object>) ((org.openqa.selenium.chrome.ChromeDriver) driver)
						.executeCdpCommand("Network.getAllCookies", new java.util.HashMap<>());
				return cookies;
			} else {
				Allure.step("Driver không hỗ trợ CDP (chỉ ChromeDriver)");
				return java.util.Collections.emptyMap();
			}
		} catch (Exception e) {
			Allure.step("Không thể lấy cookies qua CDP: " + e.getMessage());
			return java.util.Collections.emptyMap();
		}
	}

	public void setAllCookies(WebDriver driver, Set<Cookie> allCookies) {
		for (Cookie cookie : allCookies) {
			driver.manage().addCookie(cookie);
		}
	}






	public Alert waitForAlertPresence(WebDriver driver) {
		Allure.step("Đợi cho alert present và switch vào alert");
		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(3));
		explicitWait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		return alert;

	}

	public void acceptAlert(WebDriver driver) {
		Allure.step("Chấp nhận alert");
		waitForAlertPresence(driver).accept();
		switchToDefaultContent(driver);
	}

	public void cancelAlert(WebDriver driver) {
		Allure.step("Hủy alert");
		waitForAlertPresence(driver).dismiss();
		switchToDefaultContent(driver);

	}

	public String getAlertText(WebDriver driver) {
		return Allure.step("Get Alert text: ", () -> {
			String alertText = waitForAlertPresence(driver).getText();
			return alertText;
		});
	}

	public void sendkeyToAlert(WebDriver driver, String textValue) {
		Allure.step("Sendkeys vào textbox trong Alert: " + textValue);
		waitForAlertPresence(driver).sendKeys(textValue);
		sleepInMiliSecond(500);
	}

	public void handleUnexpectedAlert(WebDriver driver, String actionAlert) {
		Allure.step("Xử lý alert xuất hiện ngẫu nhiên");
		try {
			String action = actionAlert != null ? actionAlert.trim().toLowerCase() : "";
			if ("accept".equals(action)) {
				acceptAlert(driver);
			} else if ("cancel".equals(action)) {
				cancelAlert(driver);
			}
		} catch (Exception e) {
			Allure.step("Không có alert nào hiển thị!");
		}

	}



	private By getByXpath(String xpathLocator) {
		try {
			return By.xpath(xpathLocator);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public WebElement getWebElement(WebDriver driver, String xpathLocator) {
		try {
			// Chờ tối đa 10s cho đến khi element xuất hiện trong DOM
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
		} catch (TimeoutException e) {
			System.err.println("Lỗi: Không tìm thấy element sau 10s: " + xpathLocator);
			// Có thể chụp ảnh màn hình ở đây nếu muốn
		} catch (Exception e) {
			System.err.println("Lỗi hệ thống khi tìm element: " + e.getMessage());
		}
		return null; // Chỉ trả về null khi thực sự đã đợi mà không thấy
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
		} catch (NoSuchElementException e) {
			return null;
		}

	}

	public void clickToElement(WebDriver driver, String xpathLocator) {
		Allure.step("Click vào element: " + xpathLocator);
		waitForElementClickable(driver, xpathLocator);
		getWebElement(driver, xpathLocator).click();
		sleepInMiliSecond(500);
	}

	public void clickToElement(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Click vào element động với tham số");
		waitForElementClickable(driver, xpathLocator, params);
		getWebElement(driver, getDynamicLocator(xpathLocator, params)).click();
		sleepInMiliSecond(500);
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, String xpathLocator) {
		Allure.step("Clear dữ liệu trong textbox");
		getWebElement(driver, xpathLocator).clear();
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Clear dữ liệu trong textbox");
		xpathLocator = getDynamicLocator(xpathLocator, params);
		getWebElement(driver, xpathLocator).clear();
	}

	public void sendkeyToElement(WebDriver driver, String xpathLocator, String textValue) {
		Allure.step("Nhập vào input: " + xpathLocator + " với giá trị: " + textValue);
		try {
			waitForElementVisible(driver, xpathLocator);
			getWebElement(driver, xpathLocator).clear();
			getWebElement(driver, xpathLocator).sendKeys(textValue);
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
		}
	}

	public void sendkeyToElementAreaTextbox(WebDriver driver, String xpathLocator, String textValue) {
		try {
			waitForElementClickable(driver, xpathLocator);
			getWebElement(driver, xpathLocator).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			getWebElement(driver, xpathLocator).sendKeys(Keys.BACK_SPACE);
			getWebElement(driver, xpathLocator).sendKeys(textValue);
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
		}
	}

	public void sendkeyToElementAreaTextbox(WebDriver driver, String xpathLocator, String textValue, String... params) {
		try {
			waitForElementClickable(driver, xpathLocator, params);
			getWebElement(driver, xpathLocator, params).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			getWebElement(driver, xpathLocator, params).sendKeys(Keys.BACK_SPACE);
			getWebElement(driver, xpathLocator, params).sendKeys(textValue);
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
		}

	}

	public void sendkeyToElement(WebDriver driver, String xpathLocator, String textValue, String... params) {
		Allure.step("Nhập text vào element động với giá trị: " + textValue);
		try {
			xpathLocator = getDynamicLocator(xpathLocator, params);
			getWebElement(driver, xpathLocator, params).clear();
			getWebElement(driver, xpathLocator, params).sendKeys(textValue);
		} catch (Exception e) {
		}
	}

	public String getElementText(WebDriver driver, String xpathLocator) {
		return Allure.step("Lấy text từ element: " + xpathLocator, () -> {
			waitForElementVisible(driver, xpathLocator);
			if (getWebElement(driver, xpathLocator) != null) {
				Allure.step("Value sau khi getText: " + getWebElement(driver, xpathLocator).getText());
				return getWebElement(driver, xpathLocator).getText();

			} else {
				return null;
			}
		});
	}

	public String getElementText(WebDriver driver, String xpathLocator, String... params) {
		return Allure.step("Lấy text từ element động", () -> {
			waitForElementVisible(driver, xpathLocator, params);
			if (getWebElement(driver, xpathLocator, params) != null) {
				Allure.step("Value sau khi getText: " + getWebElement(driver, xpathLocator).getText());
				return getWebElement(driver, xpathLocator, params).getText();
			} else {
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

	public void deselectOptionByVisibleText(WebDriver driver, String xpathLocator, String optionText) {
		WebElement dropdown = driver.findElement(By.xpath(xpathLocator));
		Select select = new Select(dropdown);
		if (select.isMultiple()) {
			select.deselectByVisibleText(optionText);
		} else {
			System.out.println("Không phải multi-select");
		}
	}

	public void deselectAllOption(WebDriver driver, String xpathLocator) {
		WebElement dropdown = driver.findElement(By.xpath(xpathLocator));
		Select select = new Select(dropdown);
		if (select.isMultiple()) {
			select.deselectAll();
		} else {
			System.out.println("Không phải multi-select");
		}
	}

	public void selectItemInCustomDropdown(WebDriver driver, String parentXpath, String childXpath,
			String expectedItem) {
		getWebElement(driver, parentXpath).click();
		sleepInMiliSecond(2000);

		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		List<WebElement> allItems = explicitWait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childXpath)));
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
			waitForElementVisible(driver, xpathLocator);
			try {
				return getWebElement(driver, xpathLocator).isDisplayed();
			} catch (Exception e) {
				return false;
			}
		});
	}

	public boolean isElementDisplayed(WebDriver driver, String xpathLocator, String... params) {
		return Allure.step("Kiểm tra element động có hiển thị", () -> {
			waitForElementVisible(driver, xpathLocator, params);
			try {
				return getWebElement(driver, getDynamicLocator(xpathLocator, params)).isDisplayed();
			} catch (NoSuchElementException e) {
				return false;
			} catch (Exception e) {
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


	public void waitForNumberOfWindowsToIncrease(WebDriver driver, int originalCount) {
		Allure.step("Đợi số lượng window handle tăng lên so với ban đầu: " + originalCount);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait.until(driver1 -> driver1.getWindowHandles().size() > originalCount);
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
		waitForElementVisible(driver, xpathLocator);
		return getWebElement(driver, xpathLocator).isSelected();
	}

	public boolean isElementSelected(WebDriver driver, String xpathLocator, String... params) {
		return getWebElement(driver, getDynamicLocator(xpathLocator, params)).isSelected();
	}

	public void switchToFrameByWebElement(WebDriver driver, String xpathLocator) {
		Allure.step("Chuyển vào iframe: " + xpathLocator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		WebElement iframe = explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathLocator)));
		driver.switchTo().frame(iframe);
	}

	public void switchToFrameByNameOrId(WebDriver driver, String nameOrId) {
		Allure.step("Chuyển vào iframe bằng name hoặc id: " + nameOrId);
		driver.switchTo().frame(nameOrId);
	}

	public void switchToFrameByIndex(WebDriver driver, int index) {
		Allure.step("Chuyển vào iframe theo index: " + index);
		driver.switchTo().frame(index);
	}

	public void switchToDefaultContent(WebDriver driver) {
		Allure.step("Chuyển về nội dung mặc định (thoát iframe)");
		driver.switchTo().defaultContent();
	}

	public void hoverMouseToElement(WebDriver driver, String xpathLocator) {
		Allure.step("Hover chuột vào element: " + xpathLocator);
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, xpathLocator)).perform();
	}

	public void hoverMouseToElement(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Hover chuột vào element động");
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, getDynamicLocator(xpathLocator, params))).perform();
	}

	public void hightlightElement(WebDriver driver, String xpathLocator) {
		WebElement element = getWebElement(driver, xpathLocator);
		String originalStyle = element.getAttribute("style");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1])", element,
				"border: 2px solid red; border-style: dashed;");
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
		Allure.step("Nhấn phím " + key.name() + " trên element: " + xpathLocator);
		action = new Actions(driver);
		action.sendKeys(getWebElement(driver, xpathLocator), key).perform();
	}

	public void pressKeyToElement(WebDriver driver, String xpathLocator, Keys key, String... params) {
		Allure.step("Nhấn phím " + key.name() + " trên element động");
		action = new Actions(driver);
		xpathLocator = getDynamicLocator(xpathLocator, params);
		action.sendKeys(getWebElement(driver, xpathLocator), key).perform();
	}

	public void scrollToElementOnTopByJS(WebDriver driver, String xpathLocator) {
		Allure.step("Cuộn tới element (trên) bằng JS: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator));
	}

	public void scrollToElementOnTopByJS(WebDriver driver, String xpathLocator, String... param) {
		Allure.step("Cuộn tới element động (trên) bằng JS");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, xpathLocator, param));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, String xpathLocator) {
		Allure.step("Cuộn tới element (dưới) bằng JS: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, xpathLocator));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Cuộn tới element động (dưới) bằng JS");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, xpathLocator, params));
	}

	public void scrollToBottomPageByJS(WebDriver driver) {
		Allure.step("Cuộn xuống cuối trang bằng JS");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void scrollToElementCenterByJS(WebDriver driver, String xpathLocator) {
		Allure.step("Cuộn tới element vị trí giữa bằng JS: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
				getWebElement(driver, xpathLocator));
	}

	public void scrollToElementCenterByJS(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Cuộn tới element động vị trí giữa bằng JS");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
				getWebElement(driver, xpathLocator, params));
	}

	public void setAttributeInDOM(WebDriver driver, String xpathLocator, String attributeName, String attributeValue) {
		Allure.step("Set attribute '" + attributeName + "' = '" + attributeValue + "' cho element: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + attributeValue + "');",
				getWebElement(driver, xpathLocator));
	}

	public void removeAttributeInDOM(WebDriver driver, String xpathLocator, String attributeRemove) {
		Allure.step("Xóa attribute '" + attributeRemove + "' trong DOM của element: " + xpathLocator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');",
				getWebElement(driver, xpathLocator));
	}

	public void sendkeyToElementByJS(WebDriver driver, String xpathLocator, String value) {
		Allure.step("Nhập value bằng JS vào element: " + xpathLocator + " với giá trị: " + value);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')",
				getWebElement(driver, xpathLocator));
	}

	public String getAttributeInDOMByJS(WebDriver driver, String xpathLocator, String attributeName) {
		return Allure.step(
				"Lấy attribute '" + attributeName + "' trong DOM của element: " + xpathLocator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (String) jsExecutor.executeScript(
							"return arguments[0].getAttribute('" + attributeName + "');",
							getWebElement(driver, xpathLocator));
				});
	}

	public String getElementValidationMessage(WebDriver driver, String xpathLocator) {
		return Allure.step(
				"Lấy validation message của element: " + xpathLocator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (String) jsExecutor.executeScript("return arguments[0].validationMessage;",
							getWebElement(driver, xpathLocator));
				});
	}

	public boolean isImageLoaded(WebDriver driver, String xpathLocator) {
		return Allure.step(
				"Kiểm tra image đã load: " + xpathLocator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (boolean) jsExecutor.executeScript("return arguments[0].complete "
							+ "&& typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
							getWebElement(driver, xpathLocator));
				});
	}

	public boolean isJQueryAjaxLoadedSuccess(WebDriver driver) {
		Allure.step("Chờ tất cả JQuery Ajax load thành công");
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
		Allure.step("Click ra ngoài popup tại element: " + xpathLocator);
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
			Allure.step("Element có hiển thị");
		} catch (Exception e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
		}
	}

	public void fluentWaitForElementVisible(WebDriver driver, String xpathLocator, long timeoutInSeconds,
			long pollingInMillis) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingInMillis))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(xpathLocator)));
	}

	public void waitForElementPresent(WebDriver driver, String xpathLocator) {
		Allure.step("Chờ element tồn tại trong DOM: " + xpathLocator);
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions.presenceOfElementLocated(getByXpath(xpathLocator)));
			Allure.step("Element có tồn tại trong DOM");
		} catch (Exception e) {
			System.out.println("Element không tồn tại trong DOM");
			Allure.step("Element không tồn tại trong DOM");
		}
	}

	public void fluentWaitForElementClickable(WebDriver driver, String xpathLocator, long timeoutInSeconds,
			long pollingInMillis) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingInMillis))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.elementToBeClickable(getByXpath(xpathLocator)));
	}

	public void waitForElementVisible(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Chờ element động hiển thị");
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions
					.visibilityOfElementLocated(getByXpath((getDynamicLocator(xpathLocator, params)))));
			Allure.step("Element có hiển thị");
		} catch (TimeoutException e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
		} catch (Exception e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
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
		explicitWait.until(
				ExpectedConditions.invisibilityOfElementLocated(getByXpath(getDynamicLocator(xpathLocator, params))));
	}

	public void waitForAllElementInvisible(WebDriver driver, String xpathLocator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, xpathLocator)));
	}

	public void waitForElementClickable(WebDriver driver, String xpathLocator) {
		Allure.step("Chờ element có thể click: " + xpathLocator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(xpathLocator)));
		Allure.step("Element có thể click");
	}

	public void waitForElementClickable(WebDriver driver, String xpathLocator, String... params) {
		Allure.step("Chờ element động có thể click");
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait
				.until(ExpectedConditions.elementToBeClickable(getByXpath(getDynamicLocator(xpathLocator, params))));
		Allure.step("Element có thể click");
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

		StringBuilder fullPathBuilder = new StringBuilder();
		String folderPath = GlobalConstants.UPLOAD_FILE_FOLDER;

		for (String file : fileNames) {
			String absolutePath = folderPath + file;

			// Kiểm tra file có tồn tại thực tế không trước khi truyền vào driver
			if (Files.exists(Paths.get(absolutePath))) {
				fullPathBuilder.append(absolutePath).append("\n");
			} else {
				throw new RuntimeException("File không tồn tại tại đường dẫn: " + absolutePath);
			}
		}

		String finalPaths = fullPathBuilder.toString().trim();

		// Đảm bảo element sẵn sàng trước khi tương tác
		getWebElement(driver, xpathLocator).sendKeys(finalPaths);
	}

	public void uploadFilesSequentiallyWithWait(WebDriver driver, String xpathLocatorInitialFile, String xpathLocator,
			String... fileNames) {
		Allure.step("Upload lần lượt " + fileNames.length + " file: " + String.join(", ", fileNames));

		String folderPath = GlobalConstants.UPLOAD_FILE_FOLDER;
		waitForElementPresent(driver, xpathLocator);

		for (int i = 0; i < fileNames.length; i++) {
			// Lưu số lượng file đã upload trước khi bắt đầu (để so sánh sau mỗi lần upload)
			int initialFileCount = getListWebElement(driver, xpathLocatorInitialFile).size();
			Allure.step("Số lượng file đã upload trước khi upload: " + initialFileCount);

			String fileName = fileNames[i];
			String absolutePath = folderPath + fileName;

			// Kiểm tra file tồn tại
			if (!Files.exists(Paths.get(absolutePath))) {
				throw new RuntimeException("File không tồn tại: " + absolutePath);
			}

			getWebElement(driver, xpathLocator).sendKeys(absolutePath);

			// Chờ upload thành công băng cách đợi số lượng element tăng lên
			try {
				explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
				explicitWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(getByXpath(xpathLocatorInitialFile),
						initialFileCount));
				Allure.step("Upload file " + fileName + " thành công");
			} catch (Exception e) {
				int uploadedFileCount = getListWebElement(driver, xpathLocatorInitialFile).size();
				throw new RuntimeException(
						"Upload file " + fileName + " thất bại. Số lượng file đã upload: " + uploadedFileCount);
			}
		}
	}

	public void waitLoadingIsInvisible(WebDriver driver) {
		Allure.step("Đợi cho loading icon biến mất");
		try {
			// Tìm icon loading trong thời gian rất ngắn (1s)
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
			shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(GlobalConstants.LOADING_ICON)));

			// Nếu thấy thì mới chờ cho nó biến mất (vẫn dùng timeout mặc định)
			waitForElementInvisible(driver, GlobalConstants.LOADING_ICON);
		} catch (TimeoutException e) {
			// Không tìm thấy loading icon trong 1s, bỏ qua luôn không chờ nữa
		}
	}

}