package org.example.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class ExtentReportListeners implements ITestListener {

    private static ExtentReports extent = ExtentManager.getExtentReports();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail("Test failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip("Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        extentTest.get().warning("Test failed but within success percentage");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    public static void addStep(String stepDescription, String status) {
        ExtentTest test = extentTest.get();
        test.log(Status.valueOf(status), stepDescription);
    }

}