package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Операция "проверка подключения к интернету"
 */
public class CheckForInternetConnectionUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка подключения к интернету"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public CheckForInternetConnectionUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
    }

    public void use() {
        String cmdResponse = checkConnection();
        boolean isConnected = isConnected(cmdResponse);
        makeReport(cmdResponse, isConnected);
        notifyTaskCompleted();
    }

    /**
     * Выполняет ping 8.8.8.8 средствами Windows
     *
     * @return возврат командной строки Windows
     */
    private String checkConnection() {
        System.out.println("Выполняю проверку...");
        StringBuilder out = new StringBuilder();
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            p = r.exec(ConsoleCommands.PING_GOOGLE);
        } catch (IOException e) {
            System.out.println("Что-то пошло не так. Кажется, на вашей конфигурации данный запрос невозможен.");
            notifyTaskCompleted();
        }
        try {
            p.waitFor();
        } catch (InterruptedException ignored) {
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "cp866"));
        } catch (UnsupportedEncodingException ignored) {
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line).append("\n");
            }
        } catch (IOException ignored) {
        }

        return out.toString();
    }

    /**
     * Проверка подключения к интернету из ответа команды ping
     *
     * @param cmdResponse ответ команды ping
     * @return {@code true} - подключение есть, {@code false} - отсутствует
     */
    private boolean isConnected(String cmdResponse) {
        return !cmdResponse.contains("Превышен интервал ожидания для запроса");
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param cmdResponse ответ команды ping
     * @param isConnected признак подключения к интернету
     **/
    private void makeReport(String cmdResponse, boolean isConnected) {
        if (isConnected) {
            System.out.println("ПК подключен к интернету.\n");
        } else {
            System.out.println("ПК не подключен к интернету.\n");
        }
        String sb =
                "Отчет об операции \"Проверка подключения к интернету\": \n" +
                "Подключение ПК к интернету: " + isConnected + "\n" +
                "Подробный отчет: " + cmdResponse + "\n";
        ReportWriter.addToReport(sb);
    }
}
