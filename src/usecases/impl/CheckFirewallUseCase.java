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
 * Операция "проверка наличия межсетевого экрана"
 */
public class CheckFirewallUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка наличия межсетевого экрана"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public CheckFirewallUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
        driverMethod();
    }

    private void driverMethod() {
        String cmdResponse = checkFirewallStatus();
        boolean isConnected = isEnabled(cmdResponse);
        makeReport(cmdResponse, isConnected);
        notifyTaskCompleted();
    }

    /**
     * Проверка статуса файрволла Windows
     *
     * @return возврат командной строки Windows
     */
    private String checkFirewallStatus() {
        System.out.println("Выполняю проверку...");
        StringBuilder out = new StringBuilder();
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            p = r.exec(ConsoleCommands.FIREWALL_STATE);
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
     * Проверка статуса файроволла из ответа командной строки
     *
     * @param cmdResponse ответ командной строки
     * @return {@code true} - файрволл активирован, {@code false} - отключен
     */
    private boolean isEnabled(String cmdResponse) {
        return cmdResponse.contains("Состояние                             ВКЛЮЧИТЬ\n" +
                "ОК.");
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param cmdResponse ответ командной строки
     * @param isConnected признак включенного файрволла
     **/
    private void makeReport(String cmdResponse, boolean isConnected) {
        if (isConnected) {
            System.out.println("Межсетевой экран активен.\n");
        } else {
            System.out.println("Межсетевой экран не активен.\n");
        }
        String sb =
                "Отчет об операции \"Проверка наличия межсетевого экрана\": \n" +
                        "Межсетевой экран включен: " + isConnected + "\n" +
                        "Подробный отчет: " + cmdResponse + "\n";
        ReportWriter.addToReport(sb);
    }
}
