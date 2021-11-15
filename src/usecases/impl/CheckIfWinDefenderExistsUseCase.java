package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;
import utils.ConsoleCommands;

import java.io.*;

/**
 * Операция "проверка наличия установленного антивируса"
 */
public class CheckIfWinDefenderExistsUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка наличия установленного антивируса"
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public CheckIfWinDefenderExistsUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
        driverMethod();
    }

    private void driverMethod() {
        String cmdResponse = checkDefenderStatus();
        boolean isConnected = isEnabled(cmdResponse);
        makeReport(cmdResponse, isConnected);
        notifyTaskCompleted();
    }

    /**
     * Выполняет проверку существует ли файл
     *
     * @return строка для отчета
     */
    private String checkDefenderStatus() {
        System.out.println("Выполняю проверку...");
        File f = new File("C:\\ProgramData\\Microsoft\\Windows Defender\\");
        if (!f.exists()) {
            return ("Файл Защитника Windows не обнаружен");
        }
        return ("Файл Защитника Windows обнаружен");
    }

    /**
     * Проверка установлен ли Windows Defender
     *
     * @param cmdResponse ответ команды
     * @return {@code true} - установлен, {@code false} - не установлен
     */
    private boolean isEnabled(String cmdResponse) {
        return cmdResponse.contains("Файл Защитника Windows обнаружен");
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param cmdResponse ответ команды ping
     * @param isEnabled признак подключения к интернету
     **/
    private void makeReport(String cmdResponse, boolean isEnabled) {
        if (isEnabled) {
            System.out.println("ПК защищен антивирусным ПО.\n");
        } else {
            System.out.println("ПК не защищен антивирусным ПО.\n");
        }
        String sb =
                "Отчет об операции \"Проверка наличия установленного антивируса\": \n" +
                        "На ПК установлен антивирус: " + isEnabled + "\n" +
                        "Подробный отчет: " + cmdResponse + "\n";
        ReportWriter.addToReport(sb);
    }
}
