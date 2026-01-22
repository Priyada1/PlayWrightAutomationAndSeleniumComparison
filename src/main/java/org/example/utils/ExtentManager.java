package org.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager{

    private static ExtentReports extent;

    public static ExtentReports getExtentReports() {

        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/target/ExtentReports/extent-report.html");
            reporter.config().setReportName("Playwright Automation Report");
            reporter.config().setDocumentTitle("Playwright Automation Report");
            reporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Author", "Lead SDET");
        }

        return extent;
    }

}