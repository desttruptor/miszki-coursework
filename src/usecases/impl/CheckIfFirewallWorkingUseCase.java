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
     */
    public CheckIfFirewallWorkingUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
    }

    @Override
    public void run() {
        int cmdResponse = 0;
        try {
            cmdResponse = checkFirewallWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
        makeReport(isEnabled(cmdResponse));
        notifyTaskCompleted();
    }

    /**
     * Проверка статуса файрволла Windows
     *
     * @return код ответа сервера
     */
    private int checkFirewallWork() throws IOException {
        System.out.println("Выполняю проверку...");
        int responseCode = 0;
        try {
            URL testUrl = new URL("https://jsonplaceholder.typicode.com/todos/1");
            HttpsURLConnection conn = (HttpsURLConnection) testUrl.openConnection();
            responseCode = conn.getResponseCode();
            BufferedReader messageBody = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = messageBody.readLine();
            @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
            StringBuilder responseBody = new StringBuilder();
            responseBody.append(s);
            while (s != null) {
                s = messageBody.readLine();
                if (s == null) {
                    break;
                }
                responseBody.append(s);
            }
        } catch (MalformedURLException ignored) {
        }

        return responseCode;
    }

    /**
     * Проверка работы файрволла из ответа сервера
     *
     * @param networkResponse код ответа сервера
     * @return {@code true} - файрволл в порядке, {@code false} - не в порядке
     */
    private boolean isEnabled(int networkResponse) {
        return networkResponse == 200;
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param isEnabled признак правильной работы файрволла
     **/
    private void makeReport(boolean isEnabled) {
        String result;
        if (isEnabled) {
            result = "Межсетевой экран функционирует верно.\n";
        } else {
            result = "Межсетевой экран функционирует неверно.\n";
        }
        System.out.println(result);
        String sb =
                "Отчет об операции \"Проверка работы межсетевого экрана\": \n" + result;
        ReportWriter.addToReport(sb);
    }
}
