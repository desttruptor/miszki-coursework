package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Операция "проверка работоспособности межсетевого экрана"
 */
public class CheckIfFirewallWorkingUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка наличия межсетевого экрана"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     * @param reportWriter          записывает отчет о выполнении проверки в файл
     */
    public CheckIfFirewallWorkingUseCase(TaskCompletedCallback taskCompletedCallback, ReportWriter reportWriter) {
        super(taskCompletedCallback, reportWriter);
        driverMethod();
    }

    private void driverMethod() {
        String cmdResponse = null;
        try {
            cmdResponse = checkFirewallWork();
        } catch (IOException ignored) {
        }
        boolean isConnected = isEnabled(cmdResponse);
        makeReport(cmdResponse, isConnected);
        notifyTaskCompleted();
    }

    /**
     * Проверка статуса файрволла Windows
     *
     * @return запрос на сервер и загрузка JSON
     */
    private String checkFirewallWork() throws IOException {
        System.out.println("Выполняю проверку...");

        URL testUrl = null;
        try {
            testUrl = new URL("https://jsonplaceholder.typicode.com/todos/1");
        } catch (MalformedURLException ignored) {
        }

        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) testUrl.openConnection();
        } catch (IOException ignored) {
        }

        int responseCode = 0;
        try {
            responseCode = conn.getResponseCode();
        } catch (IOException ignored) {
        }

        BufferedReader messageBody = null;
        try {
            messageBody = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException ignored) {
        }
        StringBuilder responceBody = new StringBuilder();

        String s = messageBody.readLine();
        responceBody.append(s);
        while (s != null) {
            s = messageBody.readLine();
            if (s == null) {
                break;
            }
            responceBody.append(s);
        }
        return "Код ответа сервера: " + responseCode + "\n" +
                "Тело ответа сервера: " + responceBody + "\n";
    }

    /**
     * Проверка работы файрволла из ответа сервера
     *
     * @param networkResponse ответ сервера
     * @return {@code true} - файрволл активирован, {@code false} - отключен
     */
    private boolean isEnabled(String networkResponse) {
        return networkResponse.contains("Код ответа сервера: 200");
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param networkResponse ответ сервера
     * @param isEnabled       признак правильной работы файрволла
     **/
    private void makeReport(String networkResponse, boolean isEnabled) {
        if (isEnabled) {
            System.out.println("Межсетевой экран функционирует верно.\n");
        } else {
            System.out.println("Межсетевой экран функционирует неверно.\n");
        }
        String sb =
                "Отчет об операции \"Проверка работы межсетевого экрана\": \n" +
                        "Работает верно: " + isEnabled + "\n" +
                        networkResponse + "\n";
        reportWriter.addToReport(sb);
    }
}
