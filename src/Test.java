import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.utils.statistic.MainManager;

public class Test {
    public static void main(String[] args) {
        MainManager mainManager = new MainManager(new Cafe(), 1);

        mainManager.readStatistic();
    }
}
