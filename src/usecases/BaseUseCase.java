package usecases;

import report.ReportWriter;

public abstract class BaseUseCase {

    /**
     * Коллбек на показ меню после выполнения операции
     */
    protected final TaskCompletedCallback taskCompletedCallback;

    /**
     * Базовый конструктор для юзкейса
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     */
    public BaseUseCase(TaskCompletedCallback taskCompletedCallback) {
        this.taskCompletedCallback = taskCompletedCallback;
    }

    /**
     * Показать меню по завершении работы
     */
    protected void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
