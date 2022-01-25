import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Customer;
import ru.uzaretskaya.cafe.Meal;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Cafe cafe = new Cafe();

        List<Meal> menu = cafe.getMenu();

        Customer customer = new Customer("Customer 1");
        List<Meal> mealsForOrder = customer.makeOrder(menu);
        cafe.createOrder(mealsForOrder, customer);

        cafe.open();

        cafe.close();
    }

}