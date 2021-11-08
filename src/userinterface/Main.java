package userinterface;

import report.ReportWriter;

public class Main {
    private static final boolean isRunning = true;

    public static void main(String[] args) {
        ReportWriter reportWriter = ReportWriter.getInstance();
        new Menu(reportWriter);
    }
}
