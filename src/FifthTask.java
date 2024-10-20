import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class FifthTask {
    private final double epsilon = 0.00001;
    private final double delta = 0.00001;
    private final double phi = (1d + Math.sqrt(5)) / 2d;

    public double func(double[] x) {
        //return 0.01 * (x[0] - 2) * (x[0] - 2) + 0.02 * x[1] * x[1];
        //return Math.cos(x[0]) + Math.sin(x[1]);
        return x[0] * x[0] + x[1] * x[1] + 0.9 * Math.cos(x[0] * x[0] + x[1] * x[1]);
    }

    private double[] grad(double[] x) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            double[] x_incremented = new double[x.length];
            System.arraycopy(x, 0, x_incremented, 0, x.length);
            x_incremented[i] += delta;
            res[i] = (func(x_incremented) - func(x)) / (delta);
        }
        return res;
    }

    private double norm(double[] x) {
        double res = 0;
        for (double v : x) {
            res += v * v;
        }
        return res;
    }

    private double[] subtract(double[] vector1, double[] vector2) {
        double[] res = new double[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            res[i] = vector1[i] - vector2[i];
        }
        return res;
    }

    private double[] multiply(double[] vector, double multiplier) {
        double[] res = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            res[i] = multiplier * vector[i];
        }
        return res;
    }

    private double golden_ratio_method (double[] x) {
        double a = -1d;
        double b = 1d;
        double t1 = b - ((b - a) / phi);
        double t2 = a + ((b - a) / phi);
        double f1;
        double f2;
        do {
            f1 = func(subtract(x, multiply(grad(x), t1)));
            f2 = func(subtract(x, multiply(grad(x), t2)));

            if (f1 > f2) {
                a = t1;
                t1 = t2;
                t2 = b - (t1 - a);
            } else {
                b = t2;
                t2 = t1;
                t1 = a + (b - t2);
            }
        } while ( (b - a) >= epsilon * 2);
        return ((a + b) / 2);
    }

    public double[] gradient_descent_method(double[] x0) {
        double[] x2 = new double[x0.length];
        double[] x1 = new double[x0.length];
        System.arraycopy(x0, 0, x2, 0, x0.length);
        double lambda;
        do {
            x1 = x2;
            lambda = golden_ratio_method(x1);
            x2 = subtract(x1, multiply(grad(x1), lambda));
            System.out.print("(" + BigDecimal.valueOf(x2[0]).toPlainString() + ", " + BigDecimal.valueOf(x2[1]).toPlainString() + "), ");
        } while (Math.abs(func(x2) - func(x1)) > (epsilon * epsilon));
        return x2;
    }

    public static void save_result (double[] result) {
        try {
            FileWriter writer = new FileWriter("output_5.txt");
            writer.write(Arrays.toString(result));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    public void runTask() {
        double[] x0 = {10, 10};
        double[] res = gradient_descent_method(x0);
        save_result(res);
    }
}
