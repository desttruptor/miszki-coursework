package usecases;

import report.ReportWriter;
import utils.ConsoleCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Операция "проверка наличия межсетевого экрана"
 */
public class CheckFirewallUseCase {
    /**
     * Коллбек на показ меню после выполнения операции
     */
    private final TaskCompletedCallback taskCompletedCallback;

    /**
     * Класс для записи отчета о проверках
     */
    private final ReportWriter reportWriter;

    /**
     * Конструктор для операции "проверка наличия межсетевого экрана"
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     *      * @param reportWriter записывает отчет о выполнении проверки в файл
     */
    public CheckFirewallUseCase(TaskCompletedCallback taskCompletedCallback, ReportWriter reportWriter) {
        this.taskCompletedCallback = taskCompletedCallback;
        this.reportWriter = reportWriter;
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
     * @return возврат консоли Windows
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
                "Отчет об операции \"Проверка наличия межсетевого экрана\": " +
                        "Межсетевой экран включен: " + isConnected + "\n" +
                        "Подробный отчет: " + cmdResponse + "\n";
        reportWriter.addToReport(sb);
    }

    /**
     * Показать меню по завершении работы
     */
    private void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
