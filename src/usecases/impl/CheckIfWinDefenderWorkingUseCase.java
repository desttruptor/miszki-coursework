package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.*;

/**
 * Операция "проверка работоспособности антивирусного ПО"
 */
public class CheckIfWinDefenderWorkingUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка работоспособности антивирусного ПО"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     * @param reportWriter          записывает отчет о выполнении проверки в файл
     */
    public CheckIfWinDefenderWorkingUseCase(TaskCompletedCallback taskCompletedCallback, ReportWriter reportWriter) {
        super(taskCompletedCallback, reportWriter);
        driverMethod();
    }

    private void driverMethod() {
        String cmdResponse = checkDefenderStatus();
        boolean isConnected = isRunning(cmdResponse);
        makeReport(cmdResponse, isConnected);
        notifyTaskCompleted();
    }

    /**
     * Получает список запущенных процессов Windows
     *
     * @return возврат командной строки Windows
     */
    private String checkDefenderStatus() {
        System.out.println("Выполняю проверку...");

        StringBuilder out = new StringBuilder();
        Process p;
        try {
            ProcessBuilder pb = new ProcessBuilder(ConsoleCommands.TASKLIST);
            pb.redirectErrorStream(true);
            p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "cp866"));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            p.waitFor();
        } catch (IOException e) {
            System.out.println("Что-то пошло не так. Кажется, на вашей конфигурации данный запрос невозможен.");
            notifyTaskCompleted();
        } catch (InterruptedException ignored) {
        }
        return out.toString();
    }

    /**
     * Проверка запущен ли процесс Windows Defender
     *
     * @param cmdResponse ответ командной строки
     * @return {@code true} - запущен, {@code false} - не запущен
     */
    private boolean isRunning(String cmdResponse) {
        return cmdResponse.contains("MsMpEng.exe");
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param cmdResponse ответ командной строки
     * @param isRunning   признак
     **/
    private void makeReport(String cmdResponse, boolean isRunning) {
        if (isRunning) {
            System.out.println("Защитник Windows активен.\n");
        } else {
            System.out.println("Защитник Windows не активен.\n");
        }
        String sb =
                "Отчет об операции \"Проверка работоспособности антивирусного ПО\": \n" +
                        "На ПК активен антивирус: " + isRunning + "\n" +
                        "Подробный отчет: " + cmdResponse + "\n";
        reportWriter.addToReport(sb);
    }
}
