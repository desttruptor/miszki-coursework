package userinterface;

import report.ReportWriter;
import usecases.*;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Обработка логики консольного меню
 */
public class Menu {
    /**
     * Получает ввод пользователя
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Класс для записи отчета о проверках
     */
    private final ReportWriter reportWriter;

    public Menu(ReportWriter reportWriter) {
        this.reportWriter = reportWriter;
        executeMenu();
    }

    private void executeMenu() {
        displayMenu();
        executeSelected(getUserSelection());
    }

    /**
     * Показ меню
     */
    private void displayMenu() {
        String menu = "Проверка информационной безопасности.\n" +
                "Проверка межсетевого экрана:\n" +
                "1. Проверка подключения к интернету.\n" +
                "2. Проверка наличия межсетевого экрана.\n" +
                "3. Проверка работоспособности межсетевого экрана.\n" +
                "Проверка антивирусного ПО.\n" +
                "4. Проверка наличия установленного антивируса.\n" +
                "5. Проверка работоспособности антивирусного ПО.\n" +
                "6. Выход.\n";
        System.out.println(menu);
    }

    /**
     * Отображение сообщения об ошибке ввода
     */
    private void displayError() {
        System.out.println("Введено неправильное значение, попробуйте еще раз.");
    }

    /**
     * Запрос пользовательского ввода
     *
     * @return выбранный пункт меню
     */
    private int getUserSelection() {
        System.out.println("Введите цифру для запуска соответствующей ей проверки:");
        int selection = -1;

        while (!isAvailable(selection)) {
            try {
                selection = scanner.nextInt();
            } catch (NoSuchElementException inputMismatchException) {
                displayError();
                scanner.next();
            }
        }

        return selection;
    }

    /**
     * Запуск выбранного пункта меню
     *
     * @param selected выбранный пункт меню
     */
    private void executeSelected(int selected) {
        switch (selected) {
            case 1 -> new CheckForInternetConnectionUseCase(
                    this::executeMenu,
                    reportWriter
            );
            case 2 -> new CheckFirewallUseCase();
            case 3 -> new CheckIfFirewallWorkingUseCase();
            case 4 -> new CheckIfWinDefenderExistsUseCase();
            case 5 -> new CheckIfWinDefenderWorkingUseCase();
            case 6 -> System.exit(0);
        }
    }

    /**
     * Проверка что ввод соответствует числу пунктов меню
     *
     * @param selection ввод пользователя
     * @return {@code true} - соответствует, {@code false} - нет
     */
    private boolean isAvailable(int selection) {
        return selection >= 1 && selection <= 6;
    }
}
