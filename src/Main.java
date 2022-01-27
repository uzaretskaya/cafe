import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Customer;
import ru.uzaretskaya.cafe.Meal;

import java.util.List;

import static ru.uzaretskaya.cafe.utils.Random.getRandomNumber;

public class Main {
    public static void main(String[] args) {

        Cafe cafe = new Cafe();

        List<Meal> menu = cafe.getMenu();
        cafe.open();

        // test with maxCustomerCounts
        int customerCounts = 0;
        int maxCustomerCounts = 10;

        while (customerCounts < maxCustomerCounts) {
            customerCounts++;
            Customer customer = new Customer("Customer " + customerCounts);
            List<Meal> mealsForOrder = customer.makeOrder(menu);
            cafe.createOrder(mealsForOrder, customer);
            try {
                Thread.sleep(getRandomNumber(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cafe.close();

        // endless test
        /*while (true) {
            Customer customer = new Customer("Customer");
            List<Meal> mealsForOrder = customer.makeOrder(menu);
            cafe.createOrder(mealsForOrder, customer);
            try {
                Thread.sleep(getRandomNumber(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }



}