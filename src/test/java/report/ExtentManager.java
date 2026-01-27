package report;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;

import constant.GlobalConstants ;

public class ExtentManager {
	private static ExtentReports extent;
	private static Platform platform;
	private static String reportFileName = "Report.html";
	private static String reportPath = GlobalConstants.PROJECT_PATH + "/extentReport";
	private static String macReportFileLoc = reportPath + "/" + reportFileName;
	private static String winReportFileLoc = reportPath + "\\" + reportFileName;

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance();
		return extent;
	}

	// Create an extent report instance
	public static ExtentReports createInstance() {
		platform = getCurrentPlatform();
		String fileName = getReportFileLocation(platform);
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle("Web-SOffice HTML Report");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Web-SOffice HTML Report");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;
	}

	// Select the extent report file location based on platform
	private static String getReportFileLocation(Platform platform) {
		String reportFileLocation = null;
		switch (platform) {
			case MAC:
				reportFileLocation = macReportFileLoc;
				createReportPath(reportPath);
				System.out.println("ExtentReport Path for MAC: " + reportPath + "\n");
				break;
			case WINDOWS:
				reportFileLocation = winReportFileLoc;
				createReportPath(reportPath);
				System.out.println("ExtentReport Path for WINDOWS: " + reportPath + "\n");
				break;
			default:
				System.out.println("ExtentReport path has not been set! There is a problem!\n");
				break;
		}
		return reportFileLocation;
	}

	// Create the report path if it does not exist
	private static void createReportPath(String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				System.out.println("Directory: " + path + " is created!");
			} else {
				System.out.println("Failed to create directory: " + path);
			}
		} else {
			System.out.println("Directory already exists: " + path);
		}
	}

	// Get current platform
	private static Platform getCurrentPlatform() {
		if (platform == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				platform = Platform.LINUX;
			} else if (operSys.contains("mac")) {
				platform = Platform.MAC;
			}
		}
		return platform;
	}
}