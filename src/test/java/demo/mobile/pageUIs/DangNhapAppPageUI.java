package demo.mobile.pageUIs;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;

public class DangNhapAppPageUI {
    public static final By LOGIN_BUTTON = AppiumBy.accessibilityId("Đăng nhập");
    public static final By ALLOW_MEDIA = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
    public static final By ALLOW_ACCESS = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    public static final By USERNAME_TEXTBOX = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)");
    public static final By PASSWORD_TEXTBOX = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(1)");
    public static final By NEXT_BUTTON = AppiumBy.accessibilityId("Tiếp tục");
    public static final By MANAGE_TICKET_SCREEN = AppiumBy.accessibilityId("Quản lý sự vụ");
    public static final String PROCESS_TAB_ZERO = "//android.view.View[normalize-space(@content-desc)='0 Đang xử lý']";
    
    // Dùng Android
    public static final String SCROLL_CONDITION_DESC = "new UiSelector().descriptionContains(\"%s\")";
    public static final String SCROLL_CONDITION_TEXT = "new UiSelector().textContains(\"%s\")";
    public static final String SCROLL_CONDITION_ID = "new UiSelector().resourceId(\"com.demo.app:id/%s\")";

    // Dùng Ios
    public static final String SCROLL_NAME_CONTAINS = "name CONTAINS '%s'";
    public static final String SCROLL_LABEL_CONTAINS = "label CONTAINS '%s'";
    public static final String SCROLL_NAME_BEGINSWITH = "name BEGINSWITH '%s'";

    public static final String DYNAMIC_TICKET = "//*[starts-with(@content-desc, 'YC-%s')]";
    public static final String DETAIL_DYNAMIC_TICKET = "//android.view.View[@content-desc=\"Mã đơn: %s\"]";


}
