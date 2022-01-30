import ru.uzaretskaya.cafe.Cafe;

public class Main {
    public static void main(String[] args) {

        Cafe cafe = new Cafe();
        cafe.open();

        try {
            Thread.sleep(1000L * 4 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cafe.close();
    }
}