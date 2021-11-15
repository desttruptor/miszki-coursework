package userinterface;

import report.ReportWriter;

public class Main {
    public static void main(String[] args) {
        ReportWriter.createNewReportFile();
        new Menu();
    }
}
