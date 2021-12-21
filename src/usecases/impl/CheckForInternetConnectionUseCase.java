package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    @Override
    public void run() {
        String cmdResponse = checkConnection();
        makeReport(isConnected(cmdResponse));
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
        String line;
        try {
            Process p = r.exec(ConsoleCommands.PING_GOOGLE);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "cp866"));
            while ((line = reader.readLine()) != null) {
                out.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Что-то пошло не так.");
            notifyTaskCompleted();
        } catch (InterruptedException ignored) {
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
     * @param isConnected признак подключения к интернету
     **/
    private void makeReport(boolean isConnected) {
        String result;
        if (isConnected) {
            result = "ПК подключен к интернету.\n";
        } else {
            result = "ПК не подключен к интернету.\n";
        }
        System.out.println(result);
        String sb = "Отчет об операции \"Проверка подключения к интернету\": \n" + result;
        ReportWriter.addToReport(sb);
    }

}
