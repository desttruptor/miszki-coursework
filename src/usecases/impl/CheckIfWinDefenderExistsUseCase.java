package usecases.impl;

import report.ReportWriter;
import usecases.BaseUseCase;
import usecases.TaskCompletedCallback;

import java.io.File;

/**
 * Операция "проверка наличия установленного антивируса"
 */
public class CheckIfWinDefenderExistsUseCase extends BaseUseCase {

    /**
     * Конструктор для операции "проверка наличия установленного антивируса"
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public CheckIfWinDefenderExistsUseCase(TaskCompletedCallback taskCompletedCallback) {
        super(taskCompletedCallback);
    }

    @Override
    public void run() {
        makeReport(checkDefenderStatus());
        notifyTaskCompleted();
    }

    /**
     * Выполняет проверку существует ли файл
     *
     * @return строка для отчета
     */
    private boolean checkDefenderStatus() {
        System.out.println("Выполняю проверку...");
        File f = new File("C:\\ProgramData\\Microsoft\\Windows Defender\\");
        return f.exists();
    }

    /**
     * Запись результатов проверки в отчет
     *
     * @param isEnabled признак подключения к интернету
     **/
    private void makeReport(boolean isEnabled) {
        String result;
        if (isEnabled) {
            result = "ПК защищен антивирусным ПО.\n";
        } else {
            result = "ПК не защищен антивирусным ПО.\n";
        }
        System.out.println(result);
        String sb = "Отчет об операции \"Проверка наличия установленного антивируса\": \n" + result;
        ReportWriter.addToReport(sb);
    }
}
