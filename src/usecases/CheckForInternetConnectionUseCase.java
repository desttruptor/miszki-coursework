package usecases;

/**
 * Операция "проверка подключения к интернету"
 */
public class CheckForInternetConnectionUseCase {

    /**
     * Коллбек на показ меню после выполнения операции
     */
    private final TaskCompletedCallback taskCompletedCallback;

    /**
     * @param callback коллбек на показ меню после выполнения операции
     */
    public CheckForInternetConnectionUseCase(TaskCompletedCallback callback) {
        taskCompletedCallback = callback;
        notifyTaskCompleted();
    }



    private void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
