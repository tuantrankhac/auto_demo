package commons;

import java.io.File;
import java.util.Random;

import constant.GlobalConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;

import utilities.VerificationFailures;

public class BaseTest extends BrowserFactory{
    protected final Log log = LogFactory.getLog(getClass());

    protected ApiFactory apiFactory = new ApiFactory();

    public void sleepInMiliSecond(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected String generateEmail() {
		Random rand = new Random();
		return rand.nextInt(9999) + "@qa.team";
	}

	protected static int generateNumber() {
		Random rand = new Random();
		return rand.nextInt(9999);
	}

	protected boolean verifyTrue(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Có hiển thị ---------------");
		} catch (Throwable e) {
			status = false;
			log.info("---------------- Không hiển thị --------------------");
		}
		return status;
	}

	protected boolean verifyFileUploadOpenSuccess(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Upload file thành công và có thể mở ---------------");
		} catch (Throwable e) {
			status = false;
			log.info("---------------- Upload file không thành công --------------------");
		}
		return status;
	}

	protected boolean verifyImageUploadOpenSuccess(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Upload image thành công và có thể mở ---------------");
		} catch (Throwable e) {
			status = false;
			log.info("---------------- Upload image không thành công --------------------");
		}
		return status;
	}

	protected boolean verifyFalse(boolean condition) {
		boolean status = false;
		try {
			Assert.assertFalse(condition);
			log.info("----------------- Element không hiển thị -------------------");
		} catch (Throwable e) {
			status = true;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("------------------ Element có hiển thị ------------------");
		}
		return status;
	}

	protected boolean verifyEquals(Object actual, Object expected) {
		boolean status = true;
		try {
			if (actual == null) {
				return false;
			}
			log.info("Text hiện tại: " + actual + " và Text mong muốn: " + expected);
			Assert.assertEquals(actual, expected);
			log.info("------------ Passed ------------");
		} catch (Throwable e) {
			status = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("----------------- Failed -------------------");
			log.info("Text hiện tại: " + actual);
		}
		return status;
	}

	protected boolean verifyEqualsContains(String actual, String expected) {
		boolean result = actual.contains(expected);
		try {
			Assert.assertTrue(result);
			log.info("------------ Text giống nhau ------------");
		} catch (Throwable e) {
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("----------------- Text không giống nhau -------------------");
		}
		return result;
	}


	public void deleteAllFileInFolder(String folderName) {
		try {
			String pathFolderDownload = GlobalConstants.PROJECT_PATH + File.separator + folderName;
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			if (listOfFiles.length != 0) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("environment.properties")) {
						new File(listOfFiles[i].toString()).delete();
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
}
