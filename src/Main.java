import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Customer;
import ru.uzaretskaya.cafe.Meal;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Cafe cafe = new Cafe();

        List<Meal> menu = cafe.getMenu();

        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer("Customer " + i);
            List<Meal> mealsForOrder = customer.makeOrder(menu);
            cafe.createOrder(mealsForOrder, customer);
        }

        cafe.open();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cafe.close();
    }

}