package userinterface;

import usecases.impl.*;

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
    public Menu() {
        executeMenu();
    }    /**
     * Юзкейс проверки подключения к интернету
     */
    private final CheckForInternetConnectionUseCase checkForInternetConnectionUseCase = new CheckForInternetConnectionUseCase(this::executeMenu);

    /**
     * Запуск работы меню
     */
    private void executeMenu() {
        displayMenu();
        executeSelected(getUserSelection());
    }    /**
     * Юзкейс проверки наличия фаерволла
     */
    private final CheckFirewallUseCase checkFirewallUseCase = new CheckFirewallUseCase(this::executeMenu);

    /**
     * Показ меню
     */
    private void displayMenu() {
        String menu = "Проверка информационной безопасности.\n" +
                "Проверка межсетевого экрана:\n" +
                "1. Проверка подключения к интернету.\n" +
                "2. Проверка наличия межсетевого экрана.\n" +
                "3. Проверка работоспособности межсетевого экрана.\n" +
                "Проверка антивирусного ПО:\n" +
                "4. Проверка наличия установленного антивируса.\n" +
                "5. Проверка работоспособности антивирусного ПО.\n" +
                "6. Выход.\n";
        System.out.println(menu);
    }    /**
     * Юзкейс проверки правильности работы фаерволла
     */
    private final CheckIfFirewallWorkingUseCase checkIfFirewallWorkingUseCase = new CheckIfFirewallWorkingUseCase(this::executeMenu);

    /**
     * Отображение сообщения об ошибке ввода
     */
    private void displayError() {
        System.out.println("Введено неправильное значение, попробуйте еще раз.");
    }    /**
     * Юзкейс проверки наличия Защитника Windows
     */
    private final CheckIfWinDefenderExistsUseCase checkIfWinDefenderExistsUseCase = new CheckIfWinDefenderExistsUseCase(this::executeMenu);

    /**
     * Запрос пользовательского ввода
     *
     * @return выбранный пункт меню
     */
    private int getUserSelection() {
        System.out.println("Введите цифру для запуска соответствующей ей проверки:");
        int selection = -1;
        while (isNotAvailable(selection)) {
            if (!isNotAvailable(selection)) {
                displayError();
            }
            try {
                selection = scanner.nextInt();
            } catch (NoSuchElementException inputMismatchException) {
                displayError();
                scanner.next();
            }
        }
        return selection;
    }    /**
     * Юзкейс проверки работы Защитника Windows
     */
    private final CheckIfWinDefenderWorkingUseCase checkIfWinDefenderWorkingUseCase = new CheckIfWinDefenderWorkingUseCase(this::executeMenu);

    /**
     * Запуск выбранного пункта меню
     *
     * @param selected выбранный пункт меню
     */
    private void executeSelected(int selected) {
        switch (selected) {
            case 1 -> checkForInternetConnectionUseCase.run();
            case 2 -> checkFirewallUseCase.run();
            case 3 -> checkIfFirewallWorkingUseCase.run();
            case 4 -> checkIfWinDefenderExistsUseCase.run();
            case 5 -> checkIfWinDefenderWorkingUseCase.run();
            case 6 -> System.exit(0);
        }
    }

    /**
     * Проверка что ввод соответствует числу пунктов меню
     *
     * @param selection ввод пользователя
     * @return {@code true} - соответствует, {@code false} - нет
     */
    private boolean isNotAvailable(int selection) {
        return selection < 1 || selection > 6;
    }










}
