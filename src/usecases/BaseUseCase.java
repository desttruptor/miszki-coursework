package usecases;

import report.ReportWriter;

public abstract class BaseUseCase {

    /**
     * Коллбек на показ меню после выполнения операции
     */
    protected final TaskCompletedCallback taskCompletedCallback;

    /**
     * Класс для записи отчета о проверках
     */
    protected final ReportWriter reportWriter;

    /**
     * Базовый конструктор для юзкейса
     *
     * @param taskCompletedCallback коллбек на показ меню после выполнения операции
     * @param reportWriter          записывает отчет о выполнении проверки в файл
     */
    public BaseUseCase(TaskCompletedCallback taskCompletedCallback, ReportWriter reportWriter) {
        this.taskCompletedCallback = taskCompletedCallback;
        this.reportWriter = reportWriter;
    }

    /**
     * Показать меню по завершении работы
     */
    protected void notifyTaskCompleted() {
        taskCompletedCallback.onCompleteTask();
    }
}
