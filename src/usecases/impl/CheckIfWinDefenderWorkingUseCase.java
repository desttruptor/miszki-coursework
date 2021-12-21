package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Операция "проверка работоспособности антивирусного ПО"
 */
public class CheckIfWinDefenderWorkingUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка работоспособности антивирусного ПО"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public CheckIfWinDefenderWorkingUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
    }

    @Override
    public void run() {
        String cmdResponse = checkDefenderStatus();
        makeReport(isRunning(cmdResponse));
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
        try {
            ProcessBuilder pb = new ProcessBuilder(ConsoleCommands.TASKLIST);
            pb.redirectErrorStream(true);
            Process p = pb.start();
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
     * @param isRunning признак
     **/
    private void makeReport(boolean isRunning) {
        String result;
        if (isRunning) {
            result = "Защитник Windows активен.\n";
        } else {
            result = "Защитник Windows не активен.\n";
        }
        System.out.println(result);
        String sb = "Отчет об операции \"Проверка работоспособности антивирусного ПО\": \n" + result;
        ReportWriter.addToReport(sb);
    }
}
