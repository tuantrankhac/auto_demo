package commons.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.qameta.allure.Allure;

public class RetryTest implements IRetryAnalyzer {
    private int retryCount = 0;
    private final int maxRetry = 2; // retry tối đa 2 lần

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetry) {
            retryCount++;
            Allure.step("Retry lần: " + retryCount);
            return true;
        }
        return false;
    }

}
