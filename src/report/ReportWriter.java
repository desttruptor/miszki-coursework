package report;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс для записи отчета о проверках в файл.
 */
public class ReportWriter {

    private static final String FILE_NAME = "Отчет о проверках.txt";

    private static ReportWriter instance;

    /**
     * Записывает сформированный юзкейсом отчет в файл
     *
     * @param report отчет о проверке
     */
    public static void addToReport(String report) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME, true)) {
            fileWriter.write("\n");
            fileWriter.write(report + "\n");
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Создает новый отчет
     */
    public static void createNewReportFile() {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME)) {
            fileWriter.write("Файл отчета о проверках.\n");
            fileWriter.write("Отчет о проверках будет заполнен в порядке их выполнения.\n");
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }
}
