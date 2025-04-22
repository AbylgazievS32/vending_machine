import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private static CoinAcceptor coinAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
    }

    public static void choicePayment() {
        boolean isExis=true;
        System.out.println("выберите способ оплаты:");
        System.out.println("нажмите 1 для того что бы оплатить купюрой\n2 для оплаты монетами \nпри вводе любой другой цифры будет выбран оплата монетами");
        while (isExis) {
            try {
                int choicePayment = new Scanner(System.in).nextInt();
                isExis=false;
                switch (choicePayment) {
                    case 1:
                        System.out.println("вы выбрали способ оплаты купюрой");
                        runToBanknote();
                        break;
                    case 2:
                        System.out.println("вы выбрали способ оплаты монетами");
                        runToCoins();
                        break;
                    default:
                        System.out.println("выбран способ оплаты монетами");
                        runToCoins();
                        break;
                }
            } catch (InputMismatchException ime) {
                System.out.println("недопустимый символ!");
                isExis=true;
            }
        }
    }

    private static void runToCoins() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private static void runToBanknote() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulationForBanknote();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(products);
            }
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private int appMany() {
        int many;
        System.out.println("1 - 20 сом\n2 - 50 сом\n3 - 100coм \n4 - 200 coм");
        try {
            many = new Scanner(System.in).nextInt();
            switch (many) {
                case 1:
                    return 20;
                case 2:
                    return 50;
                case 3:
                    return 100;
                case 4:
                    return 200;
            }
        } catch (NumberFormatException nfe) {
            System.out.println("недопустимый символ");
            return 0;
        }
        return 0;
    }

    private void chooseActionForBanknote(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + appMany());
            print("Вы пополнили баланс на " + appMany());
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(products);
            }
        }
    }

    private void startSimulationForBanknote() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseActionForBanknote(allowProducts);
    }

}
