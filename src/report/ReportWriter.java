package report;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс, который будет записывать отчет о проверках
 */
public class ReportWriter {

    private static final String FILE_NAME = "Отчет о проверках";

    public void addToReport(String report) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true)) {
            fileWriter.write("\n");
            fileWriter.write(report + "\n");
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }

    private void createNewReportFile() {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME)) {
            fileWriter.write("Файл отчета о проверках.\n");
            fileWriter.write("Отчет о проверках будет заполнен в порядке их выполнения.\n");
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }

}
