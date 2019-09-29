package simpleMoney;

import simpleMoney.api.AccountRestService;


public class Application {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        new AccountRestService();
    }
}
