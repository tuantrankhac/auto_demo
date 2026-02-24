package constant;

import java.io.File;
import org.openqa.selenium.By;

public class GlobalConstants {
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String UPLOAD_FILE_FOLDER = PROJECT_PATH + File.separator + "uploadFiles" + File.separator;
	public static final String DOWNLOAD_FILE_FOLDER = PROJECT_PATH + File.separator + "downloadFiles";
	public static final String BROWNSER_LOG_FOLDER = PROJECT_PATH + File.separator + "brownserLog";
	public static final String DRAG_DROP_HTML5_FOLDER = PROJECT_PATH + File.separator + "dragDropHTML5";
	public static final String REPORTNG_SCREENSHOT = PROJECT_PATH + File.separator + "reportNGImage" + File.separator;
	public static final long SHORT_TIMEOUT = 5;
	public static final long LONG_TIMEOUT = 12;
	public static final long RYTRY_TIMEOUT = 3;
	public static final By LOADING_ICON = By.xpath("//div[@class='global-loading fade-in']");

}