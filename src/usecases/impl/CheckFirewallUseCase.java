package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    }

    @Override
    public void run() {
        String cmdResponse = checkFirewallStatus();
        makeReport(isEnabled(cmdResponse));
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
        String line;
        try {
            Process p = r.exec(ConsoleCommands.FIREWALL_STATE);
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
     * @param isConnected признак включенного файрволла
     **/
    private void makeReport(boolean isConnected) {
        String result;
        if (isConnected) {
            result = "Межсетевой экран активен.\n";
        } else {
            result = "Межсетевой экран не активен.\n";
        }
        System.out.println(result);
        String sb = "Отчет об операции \"Проверка наличия межсетевого экрана\": \n" + result;
        ReportWriter.addToReport(sb);
    }
}
