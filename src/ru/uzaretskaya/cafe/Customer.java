package ru.uzaretskaya.cafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.uzaretskaya.cafe.utils.Random.getRandomNumber;

public class Customer {
    private final UUID id;
    private final String name;

    public Customer(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public List<Meal> makeOrder(List<Meal> menu) {
        List<Meal> mealsForOrder = new ArrayList<>();
        int countMeals = getRandomNumber(1, 3);
        for (int i = 0; i < countMeals; i++) {
            int mealIndex = getRandomNumber(0, menu.size() - 1);
            mealsForOrder.add(menu.get(mealIndex));
        }
        System.out.println(this + " ordered " + mealsForOrder);
        return mealsForOrder;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer {" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                '}';
    }

}
