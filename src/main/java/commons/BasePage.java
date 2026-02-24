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

	public WebElement getWebElement(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			System.err.println("Lỗi: Không tìm thấy element sau 10s: " + locator);
		} catch (Exception e) {
			System.err.println("Lỗi hệ thống khi tìm element: " + e.getMessage());
		}
		return null;
	}

	public WebElement getWebElement(WebDriver driver, By locator, String... params) {
		// Không xử lý params với By, trả lại luôn
		return getWebElement(driver, locator);
	}

	public List<WebElement> getListWebElement(WebDriver driver, By locator) {
		return driver.findElements(locator);
	}

	public void clickToElement(WebDriver driver, By locator) {
		Allure.step("Click vào element: " + locator);
		waitForElementClickable(driver, locator);
		getWebElement(driver, locator).click();
		sleepInMiliSecond(500);
	}

	public void clickToElement(WebDriver driver, By locator, String... params) {
		Allure.step("Click vào element với tham số: " + locator);
		waitForElementClickable(driver, locator);
		getWebElement(driver, locator).click();
		sleepInMiliSecond(500);
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, By locator) {
		Allure.step("Clear dữ liệu trong textbox");
		getWebElement(driver, locator).clear();
	}

	public void cleaDateEnteredToTextbox(WebDriver driver, By locator, String... params) {
		Allure.step("Clear dữ liệu trong textbox (dynamic): " + locator);
		getWebElement(driver, locator).clear();
	}

	public void sendkeyToElement(WebDriver driver, By locator, String textValue) {
		Allure.step("Nhập vào input: " + locator + " với giá trị: " + textValue);
		try {
			waitForElementVisible(driver, locator);
			getWebElement(driver, locator).clear();
			getWebElement(driver, locator).sendKeys(textValue);
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
		}
	}

	public void sendkeyToElement(WebDriver driver, By locator, String textValue, String... params) {
		Allure.step("Nhập text vào element động với giá trị: " + textValue + " - " + locator);
		try {
			getWebElement(driver, locator).clear();
			getWebElement(driver, locator).sendKeys(textValue);
		} catch (Exception e) {
		}
	}

	public String getElementText(WebDriver driver, By locator) {
		return Allure.step("Lấy text từ element: " + locator, () -> {
			waitForElementVisible(driver, locator);
			WebElement el = getWebElement(driver, locator);
			if (el != null) {
				Allure.step("Value sau khi getText: " + el.getText());
				return el.getText();
			} else {
				return null;
			}
		});
	}

	public String getElementText(WebDriver driver, By locator, String... params) {
		return Allure.step("Lấy text từ element động: " + locator, () -> {
			waitForElementVisible(driver, locator);
			WebElement el = getWebElement(driver, locator);
			if (el != null) {
				Allure.step("Value sau khi getText: " + el.getText());
				return el.getText();
			} else {
				return null;
			}
		});
	}

	public void selectDropdownByText(WebDriver driver, By locator, String textItem) {
		Allure.step("Chọn dropdown theo text: " + textItem);
		Select select = new Select(getWebElement(driver, locator));
		select.selectByVisibleText(textItem);
	}

	public void selectDropdownByText(WebDriver driver, By locator, String textItem, String... params) {
		Allure.step("Chọn dropdown động theo text: " + textItem + " - " + locator);
		Select select = new Select(getWebElement(driver, locator));
		select.selectByVisibleText(textItem);
	}

	public String getSelectedItemDefaultDropdown(WebDriver driver, By locator) {
		return Allure.step("Lấy item đã chọn trong dropdown mặc định", () -> {
			Select select = new Select(getWebElement(driver, locator));
			return select.getFirstSelectedOption().getText();
		});
	}

	public String getSelectedItemDropdown(WebDriver driver, By locator) {
		return Allure.step("Lấy item đã chọn trong dropdown", () -> {
			select = new Select(getWebElement(driver, locator));
			return select.getFirstSelectedOption().getText();
		});
	}

	public String getSelectedItemDropdown(WebDriver driver, By locator, String... params) {
		select = new Select(getWebElement(driver, locator));
		return select.getFirstSelectedOption().getText();
	}

	public boolean isDropdownMultiple(WebDriver driver, By locator) {
		Select select = new Select(getWebElement(driver, locator));
		return select.isMultiple();
	}

	public void deselectOptionByVisibleText(WebDriver driver, By locator, String optionText) {
		WebElement dropdown = getWebElement(driver, locator);
		Select select = new Select(dropdown);
		if (select.isMultiple()) {
			select.deselectByVisibleText(optionText);
		} else {
			System.out.println("Không phải multi-select");
		}
	}

	public void deselectAllOption(WebDriver driver, By locator) {
		WebElement dropdown = getWebElement(driver, locator);
		Select select = new Select(dropdown);
		if (select.isMultiple()) {
			select.deselectAll();
		} else {
			System.out.println("Không phải multi-select");
		}
	}

	public void selectItemInCustomDropdown(WebDriver driver, By parentLocator, By childLocator, String expectedItem) {
		getWebElement(driver, parentLocator).click();
		sleepInMiliSecond(2000);

		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		List<WebElement> allItems = explicitWait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(childLocator));
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

	public String getElementAttribute(WebDriver driver, By locator, String attributeName) {
		return getWebElement(driver, locator).getAttribute(attributeName);
	}

	public String getElementAttribute(WebDriver driver, By locator, String attributeName, String... params) {
		return getWebElement(driver, locator).getAttribute(attributeName);
	}

	public String getElementCssValue(WebDriver driver, By locator, String propertyName) {
		return getWebElement(driver, locator).getCssValue(propertyName);
	}

	public String getHexaColorFromRGBA(String rgbaValue) {
		return Color.fromString(rgbaValue).asHex();
	}

	public int getElementSize(WebDriver driver, By locator) {
		return getListWebElement(driver, locator).size();
	}

	public int getElementSize(WebDriver driver, By locator, String... params) {
		return getListWebElement(driver, locator).size();
	}

	public void checkToDefaultCheckboxRadio(WebDriver driver, By locator) {
		Allure.step("Chọn checkbox/radio: " + locator);
		WebElement element = getWebElement(driver, locator);
		if (!element.isSelected()) {
			element.click();
		}
	}

	public void uncheckToDefaultCheckbox(WebDriver driver, By locator) {
		Allure.step("Bỏ chọn checkbox: " + locator);
		WebElement element = getWebElement(driver, locator);
		if (element.isSelected()) {
			element.click();
		}
	}

	public boolean isElementDisplayed(WebDriver driver, By locator) {
		return Allure.step("Kiểm tra element có hiển thị: " + locator, () -> {
			waitForElementVisible(driver, locator);
			try {
				return getWebElement(driver, locator).isDisplayed();
			} catch (Exception e) {
				return false;
			}
		});
	}

	public boolean isElementDisplayed(WebDriver driver, By locator, String... params) {
		return Allure.step("Kiểm tra element động có hiển thị: " + locator, () -> {
			waitForElementVisible(driver, locator);
			try {
				return getWebElement(driver, locator).isDisplayed();
			} catch (NoSuchElementException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		});
	}

	public boolean isElementUndisplayed(WebDriver driver, By locator) {
		System.out.println("Start time = " + new Date().toString());
		overrideGlobalTimeout(driver, shortTimeout);
		List<WebElement> elements = getListWebElement(driver, locator);
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

	public boolean isElementEnabled(WebDriver driver, By locator) {
		return getWebElement(driver, locator).isEnabled();
	}

	public boolean isElementEnabled(WebDriver driver, By locator, String... params) {
		return getWebElement(driver, locator).isEnabled();
	}

	public boolean isElementSelected(WebDriver driver, By locator) {
		waitForElementVisible(driver, locator);
		return getWebElement(driver, locator).isSelected();
	}

	public boolean isElementSelected(WebDriver driver, By locator, String... params) {
		return getWebElement(driver, locator).isSelected();
	}

	public void switchToFrameByWebElement(WebDriver driver, By locator) {
		Allure.step("Chuyển vào iframe: " + locator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		WebElement iframe = explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
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

	public void hoverMouseToElement(WebDriver driver, By locator) {
		Allure.step("Hover chuột vào element: " + locator);
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, locator)).perform();
	}

	public void hoverMouseToElement(WebDriver driver, By locator, String... params) {
		Allure.step("Hover chuột vào element động: " + locator);
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, locator)).perform();
	}

	public void hightlightElement(WebDriver driver, By locator) {
		WebElement element = getWebElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1])", element,
				"border: 2px solid red; border-style: dashed;");
		sleepInMiliSecond(2000);
		jsExecutor.executeScript("arguments[0].setAttribute('style', arguments[1])", element, originalStyle);
	}

	public void clickToElementByJS(WebDriver driver, By locator) {
		Allure.step("Click vào element bằng JavaScript: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, locator));
		sleepInMiliSecond(3000);
	}

	public void pressKeyToElement(WebDriver driver, By locator, Keys key) {
		Allure.step("Nhấn phím " + key.name() + " trên element: " + locator);
		action = new Actions(driver);
		action.sendKeys(getWebElement(driver, locator), key).perform();
	}

	public void pressKeyToElement(WebDriver driver, By locator, Keys key, String... params) {
		Allure.step("Nhấn phím " + key.name() + " trên element động: " + locator);
		action = new Actions(driver);
		action.sendKeys(getWebElement(driver, locator), key).perform();
	}

	public void scrollToElementOnTopByJS(WebDriver driver, By locator) {
		Allure.step("Cuộn tới element (trên) bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
	}

	public void scrollToElementOnTopByJS(WebDriver driver, By locator, String... params) {
		Allure.step("Cuộn tới element động (trên) bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, By locator) {
		Allure.step("Cuộn tới element (dưới) bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, locator));
	}

	public void scrollToElementOnDownByJS(WebDriver driver, By locator, String... params) {
		Allure.step("Cuộn tới element động (dưới) bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, locator));
	}

	public void scrollToBottomPageByJS(WebDriver driver) {
		Allure.step("Cuộn xuống cuối trang bằng JS");
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void scrollToElementCenterByJS(WebDriver driver, By locator) {
		Allure.step("Cuộn tới element vị trí giữa bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
				getWebElement(driver, locator));
	}

	public void scrollToElementCenterByJS(WebDriver driver, By locator, String... params) {
		Allure.step("Cuộn tới element động vị trí giữa bằng JS: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
				getWebElement(driver, locator));
	}

	public void setAttributeInDOM(WebDriver driver, By locator, String attributeName, String attributeValue) {
		Allure.step("Set attribute '" + attributeName + "' = '" + attributeValue + "' cho element: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + attributeValue + "');",
				getWebElement(driver, locator));
	}

	public void removeAttributeInDOM(WebDriver driver, By locator, String attributeRemove) {
		Allure.step("Xóa attribute '" + attributeRemove + "' trong DOM của element: " + locator);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');",
				getWebElement(driver, locator));
	}

	public void sendkeyToElementByJS(WebDriver driver, By locator, String value) {
		Allure.step("Nhập value bằng JS vào element: " + locator + " với giá trị: " + value);
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')",
				getWebElement(driver, locator));
	}

	public String getAttributeInDOMByJS(WebDriver driver, By locator, String attributeName) {
		return Allure.step(
				"Lấy attribute '" + attributeName + "' trong DOM của element: " + locator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (String) jsExecutor.executeScript(
							"return arguments[0].getAttribute('" + attributeName + "');",
							getWebElement(driver, locator));
				});
	}

	public String getElementValidationMessage(WebDriver driver, By locator) {
		return Allure.step(
				"Lấy validation message của element: " + locator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (String) jsExecutor.executeScript("return arguments[0].validationMessage;",
							getWebElement(driver, locator));
				});
	}

	public boolean isImageLoaded(WebDriver driver, By locator) {
		return Allure.step(
				"Kiểm tra image đã load: " + locator,
				() -> {
					jsExecutor = (JavascriptExecutor) driver;
					return (boolean) jsExecutor.executeScript("return arguments[0].complete "
							+ "&& typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
							getWebElement(driver, locator));
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

	public static void clickOutside(WebDriver driver, By locator) {
		Allure.step("Click ra ngoài popup tại element: " + locator);
		WebElement outsideElement = driver.findElement(locator);
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

	public void waitForElementVisible(WebDriver driver, By locator) {
		Allure.step("Chờ element hiển thị: " + locator);
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			Allure.step("Element có hiển thị");
		} catch (Exception e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
		}
	}

	public void fluentWaitForElementVisible(WebDriver driver, By locator, long timeoutInSeconds,
			long pollingInMillis) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingInMillis))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitForElementPresent(WebDriver driver, By locator) {
		Allure.step("Chờ element tồn tại trong DOM: " + locator);
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions.presenceOfElementLocated(locator));
			Allure.step("Element có tồn tại trong DOM");
		} catch (Exception e) {
			System.out.println("Element không tồn tại trong DOM");
			Allure.step("Element không tồn tại trong DOM");
		}
	}

	public void fluentWaitForElementClickable(WebDriver driver, By locator, long timeoutInSeconds,
			long pollingInMillis) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingInMillis))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForElementVisible(WebDriver driver, By locator, String... params) {
		Allure.step("Chờ element động hiển thị: " + locator);
		try {
			explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
			explicitWait.until(ExpectedConditions
					.visibilityOfElementLocated(locator));
			Allure.step("Element có hiển thị");
		} catch (TimeoutException e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
		} catch (Exception e) {
			System.out.println("Không tìm thấy Element");
			Allure.step("Element không hiển thị");
		}
	}

	// ===== Shadow DOM Handling Methods =====

	public SearchContext getShadowRoot(WebDriver driver, By shadowHostLocator) {
		WebElement shadowHost = getWebElement(driver, shadowHostLocator);
		if (shadowHost == null) {
			throw new NoSuchElementException("Shadow host element not found: " + shadowHostLocator);
		}
		// shadowRoot supported in Selenium 4+
		try {
			return shadowHost.getShadowRoot();
		} catch (UnsupportedOperationException | NoSuchMethodError e) {
			// Fallback for Selenium < 4 or if not supported, use Javascript
			if (driver instanceof JavascriptExecutor) {
				Object shadowRoot = ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", shadowHost);
				if (shadowRoot instanceof SearchContext) {
					return (SearchContext) shadowRoot;
				} else if (shadowRoot instanceof WebElement) {
					return (WebElement) shadowRoot;
				}
			}
			throw new UnsupportedOperationException("Shadow DOM is not supported or could not be accessed");
		}
	}

	/**
	 * Lấy nested shadow root theo chuỗi các By tương ứng với từng shadow host lớp lồng nhau. 
	 * @param driver WebDriver đang chạy
	 * @param shadowHostLocators Mảng các By, mỗi By là shadow host từng lớp (lồng nhau theo thứ tự)
	 * @return SearchContext cuối cùng là shadowRoot của lớp lồng sâu nhất
	 */
	public SearchContext getNestedShadowRoot(WebDriver driver, By... shadowHostLocators) {
		SearchContext context = driver;
		for (By locator : shadowHostLocators) {
			WebElement shadowHost;
			if (context instanceof WebDriver) {
				shadowHost = getWebElement((WebDriver) context, locator);
			} else if (context instanceof SearchContext) {
				shadowHost = context.findElement(locator);
			} else {
				throw new UnsupportedOperationException("Unknown SearchContext: " + context);
			}
			if (shadowHost == null) {
				throw new NoSuchElementException("Không tìm thấy shadow host: " + locator);
			}
			// shadowRoot supported in Selenium 4+
			try {
				context = shadowHost.getShadowRoot();
			} catch (UnsupportedOperationException | NoSuchMethodError e) {
				// Fallback bằng JS nếu không hỗ trợ
				if (driver instanceof JavascriptExecutor) {
					Object shadowRoot = ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", shadowHost);
					if (shadowRoot instanceof SearchContext) {
						context = (SearchContext) shadowRoot;
					} else if (shadowRoot instanceof WebElement) {
						context = (WebElement) shadowRoot;
					} else {
						throw new UnsupportedOperationException("Không truy cập được shadow DOM qua JS cho: " + locator);
					}
				} else {
					throw new UnsupportedOperationException("Shadow DOM không được hỗ trợ hoặc không thể truy cập cho: " + locator);
				}
			}
		}
		return context;
	}
	
	public WebElement getElementInShadowRoot(WebDriver driver, By shadowHostLocator, By elementInShadowLocator) {
		Allure.step("Tìm element trong Shadow DOM: shadow host = " + shadowHostLocator + ", element = " + elementInShadowLocator);
		SearchContext shadowRoot = getShadowRoot(driver, shadowHostLocator);
		if (shadowRoot == null) {
			throw new NoSuchElementException("Shadow root not found for host: " + shadowHostLocator);
		}
		try {
			WebElement element = shadowRoot.findElement(elementInShadowLocator);
			Allure.step("Đã tìm thấy element trong shadow root: " + elementInShadowLocator);
			return element;
		} catch (NoSuchElementException e) {
			Allure.step("Không tìm thấy element trong shadow root: " + elementInShadowLocator);
			throw e;
		}
	}


	/**
	 * Locate an element inside nested (multi-level) shadow DOMs.
	 * Each By in shadowHostLocators represents a shadow host at a given depth.
	 * The final By is the locator for the inner element.
	 * 
	 * Usage:
	 *     WebElement element = getElementInNestedShadowRoot(driver, shadowHost1, shadowHost2, ..., targetElementLocator);
	 */
	public WebElement getElementInNestedShadowRoot(WebDriver driver, By... locators) {
		if (locators == null || locators.length < 2) {
			throw new IllegalArgumentException("You must provide at least one shadow host and one element locator.");
		}
		SearchContext context = driver;
		for (int i = 0; i < locators.length - 1; i++) {
			WebElement shadowHost;
			if (context instanceof WebDriver) {
				shadowHost = getWebElement((WebDriver) context, locators[i]);
			} else if (context instanceof SearchContext) {
				shadowHost = context.findElement(locators[i]);
			} else {
				throw new UnsupportedOperationException("Unknown SearchContext: " + context);
			}
			if (shadowHost == null) {
				throw new NoSuchElementException("Không tìm thấy shadow host: " + locators[i]);
			}
			try {
				context = shadowHost.getShadowRoot();
			} catch (UnsupportedOperationException | NoSuchMethodError e) {
				if (driver instanceof JavascriptExecutor) {
					Object shadowRoot = ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", shadowHost);
					if (shadowRoot instanceof SearchContext) {
						context = (SearchContext) shadowRoot;
					} else if (shadowRoot instanceof WebElement) {
						context = (WebElement) shadowRoot;
					} else {
						throw new UnsupportedOperationException("Không truy cập được shadow DOM qua JS cho: " + locators[i]);
					}
				} else {
					throw new UnsupportedOperationException("Shadow DOM không được hỗ trợ hoặc không thể truy cập cho: " + locators[i]);
				}
			}
		}
		// The last locator is the actual element inside the innermost shadow DOM
		try {
			WebElement element = context.findElement(locators[locators.length - 1]);
			Allure.step("Đã tìm thấy element trong nested shadow root: " + locators[locators.length - 1]);
			return element;
		} catch (NoSuchElementException e) {
			Allure.step("Không tìm thấy element trong nested shadow root: " + locators[locators.length - 1]);
			throw e;
		}
	}

	public void clickElementInShadowRoot(WebDriver driver, By shadowHostLocator, By elementInShadowLocator) {
		Allure.step("Click element trong Shadow DOM: shadow host = " + shadowHostLocator + ", element = " + elementInShadowLocator);
		WebElement element = getElementInShadowRoot(driver, shadowHostLocator, elementInShadowLocator);
		element.click();
	}

	public String getTextElementInShadowRoot(WebDriver driver, By shadowHostLocator, By elementInShadowLocator) {
		Allure.step("Lấy text element trong Shadow DOM: shadow host = " + shadowHostLocator + ", element = " + elementInShadowLocator);
		WebElement element = getElementInShadowRoot(driver, shadowHostLocator, elementInShadowLocator);
		String text = element.getText();
		Allure.step("Text lấy được: " + text);
		return text;
	}
	
	public void sendKeysToElementInShadowRoot(WebDriver driver, By shadowHostLocator, By elementInShadowLocator, String value) {
		Allure.step("SendKeys vào element trong Shadow DOM: shadow host = " + shadowHostLocator + ", element = " + elementInShadowLocator + ", value = " + value);
		WebElement element = getElementInShadowRoot(driver, shadowHostLocator, elementInShadowLocator);
		element.clear();
		element.sendKeys(value);
	}


	public void waitForAllElementVisible(WebDriver driver, By locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public void waitForElementInvisible(WebDriver driver, By locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForElementInvisible(WebDriver driver, By locator, String... params) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForAllElementInvisible(WebDriver driver, By locator) {
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locator)));
	}

	public void waitForElementClickable(WebDriver driver, By locator) {
		Allure.step("Chờ element có thể click: " + locator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
		Allure.step("Element có thể click");
	}

	public void waitForElementClickable(WebDriver driver, By locator, String... params) {
		Allure.step("Chờ element động có thể click: " + locator);
		explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
		explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
		Allure.step("Element có thể click");
	}

	public void checkToCheckboxOrRadio(WebDriver driver, By locator) {
		if (!isElementSelected(driver, locator)) {
			getWebElement(driver, locator).click();
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

	public void uploadFile(WebDriver driver, By locator, String... fileNames) {
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
		getWebElement(driver, locator).sendKeys(finalPaths);
	}

	public void uploadFilesSequentiallyWithWait(WebDriver driver, By locatorInitialFile, By locator, String... fileNames) {
		Allure.step("Upload lần lượt " + fileNames.length + " file: " + String.join(", ", fileNames));

		String folderPath = GlobalConstants.UPLOAD_FILE_FOLDER;
		waitForElementPresent(driver, locator);

		for (int i = 0; i < fileNames.length; i++) {
			int initialFileCount = getListWebElement(driver, locatorInitialFile).size();
			Allure.step("Số lượng file đã upload trước khi upload: " + initialFileCount);

			String fileName = fileNames[i];
			String absolutePath = folderPath + fileName;

			if (!Files.exists(Paths.get(absolutePath))) {
				throw new RuntimeException("File không tồn tại: " + absolutePath);
			}

			getWebElement(driver, locator).sendKeys(absolutePath);

			try {
				explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
				explicitWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locatorInitialFile, initialFileCount));
				Allure.step("Upload file " + fileName + " thành công");
			} catch (Exception e) {
				int uploadedFileCount = getListWebElement(driver, locatorInitialFile).size();
				throw new RuntimeException(
						"Upload file " + fileName + " thất bại. Số lượng file đã upload: " + uploadedFileCount);
			}
		}
	}

	public void waitLoadingIsInvisible(WebDriver driver) {
		Allure.step("Đợi cho loading icon biến mất");
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
			shortWait.until(ExpectedConditions.presenceOfElementLocated(GlobalConstants.LOADING_ICON));
			waitForElementInvisible(driver, GlobalConstants.LOADING_ICON);
		} catch (TimeoutException e) {
			// Không tìm thấy loading icon trong 1s, bỏ qua luôn không chờ nữa
		}
	}

}
