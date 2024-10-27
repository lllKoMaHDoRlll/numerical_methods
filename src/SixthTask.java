import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SixthTask {
    private static final int n = 1000;
    private static final double a = 1;
    private static final double b = 1.5;
    private static final double epsilon = 0.00001;

    private static double func(double x) {
        return x;
    }

    private double integral(double x) {
        double res = 0;
        double delta = (x - a) / n;
        for (int i = 0; i < n; i++) {
            res += func(a + delta * i + delta) * delta;
        }
        return res;
    }

    private double derivative(double x) {
        return (integral(x + epsilon) - integral(x)) / epsilon;
    }

    private double newton_method (double x0) {
        double x1 = x0, x2, x3;
        x2 = x1 - (integral(x1) - b) / (derivative(x1));
        x3 = x2 - (integral(x2) - b) / (derivative(x2));;
        do {
            x1 = x2;
            x2 = x3;
            x3 = x2 - (integral(x2) - b) / (derivative(x2));
        } while (Math.abs((x3 - x2) / (1 - (x3 - x2) / (x2 - x1))) >= epsilon);
        return x3;
    }

    public void runTask() {
        double x0 = 10;
        double res = newton_method(x0);
        save_result(res);
    }

    public static void save_result (double result) {
        try {
            FileWriter writer = new FileWriter("output_5.txt");
            writer.write(Double.toString(result));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }
}
