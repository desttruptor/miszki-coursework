package usecases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Операция "проверка подключения к интернету"
 */
public class CheckForInternetConnectionUseCase {

    /**
     * Коллбек на показ меню после выполнения операции
     */
    private final TaskCompletedCallback taskCompletedCallback;

    /**
     * Процесс работы с командной строкой
     */
    private Process p;

    /**
     * @param callback коллбек на показ меню после выполнения операции
     */
    public CheckForInternetConnectionUseCase(TaskCompletedCallback callback) {
        taskCompletedCallback = callback;
    }

    /**
     * Выполняет ping 8.8.8.8 средствами Windows
     *
     * @return возврат консоли Windows по запросу
     */
    private StringBuilder checkConnection() {
        StringBuilder out = new StringBuilder();
        Runtime r = Runtime.getRuntime();
        try {
            p = r.exec(ConsoleCommands.PING_GOOGLE);
        } catch (IOException e) {
            System.out.println("Что-то пошло не так. Кажется, на вашей конфигурации данный запрос невозможен.");
        }
        try {
            p.waitFor();
        } catch (InterruptedException ignored) {
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line).append("\n");
            }
        } catch (IOException ignored) {
        }

        return out;
    }

    private void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
