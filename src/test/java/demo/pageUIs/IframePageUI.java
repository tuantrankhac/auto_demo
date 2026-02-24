package demo.pageUIs;
import org.openqa.selenium.By;

public class IframePageUI {
    public static final By PARENT_FRAME = By.xpath("//iframe[@id='iframeResult']");
    public static final By ELEMTN_IN_PARENT_FRAME_TEXT = By.xpath("//p");
    public static final By CHILD_FRAME = By.xpath("//iframe[@src='demo_iframe.htm']");
    public static final By ELEMENT_IN_CHILD_FRAME_TEXT = By.xpath("//h1");
    
    public static final By PARENT_FRAME_N = By.xpath("//iframe[@id='singleframe']");
    public static final By CHILD_FRAME_N = By.xpath("//h5[text()='iFrame Demo']");



}
