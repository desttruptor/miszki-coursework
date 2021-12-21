package usecases;

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
     * Метод для запуска последовательности действий, определенной в юзкейсе
     */
    public abstract void run();

    /**
     * Показать меню по завершении работы
     */
    protected void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
