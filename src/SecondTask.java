import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class SecondTask {
    static double epsilon = 0.000_1;
    static double delta = 0.00001;
    private double[][] A;

    SecondTask() {
        A = load_matrix();
    }

    public static double[][] load_matrix () {
        try {
            File file = new File("input_2.txt");
            Scanner reader = new Scanner(file);

            int n = Integer.parseInt(reader.nextLine());
            double[][] matrix = new double[n][n + 1];
            for (int i = 0; i < n; i++) {
                String[] lines = reader.nextLine().split(" ");
                for (int j = 0; j < n + 1; j++) {
                    matrix[i][j] = Double.parseDouble(lines[j]);
                }
            }
            reader.close();
            return matrix;

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file");
            return new double[0][];
        }
    }

    public static void save_result (double[] result) {
        try {
            FileWriter writer = new FileWriter("output_2.txt");
            writer.write(Arrays.toString(result));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    public static void print_matrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public double energy_functional (double[] y) { // функционал энергии
        double result = 0;
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result += A[i][j] * y[i] * y[j];
            }
            result += -2 * A[i][n] * y[i];
        }
        return result;
    }

    public static double get_norm (double[] a) {
        double result = 0;
        for (double ai : a) {
            result += ai * ai; // раньше была манхэттонская норма
        }
        return Math.sqrt(result);
    }

    public double[] function_min_coordinate_descent(double[] x0) {
        int n = A.length;
        double[] x1 = x0.clone();
        System.out.print("( " + String.format("%.5f", x0[0]) + ", " + String.format("%.5f", x0[1]) + "), ");
        while (true) {

            for (int i = 0; i < n; i++) {
                double xi = function_min_golden_ratio(x0, i, x0[i] - 100000 * epsilon, x0[i] + 100000 * epsilon);
                x1[i] = xi;
                System.out.print("( " + String.format("%.5f", x1[0]) + ", " + String.format("%.5f", x1[1]) + "), ");
            }
            double delta = get_norm(x1) - get_norm(x0);
            if (Math.abs(energy_functional(x1) - energy_functional(x0)) < (epsilon / 10000)) { // критерий останова
                break;
            }
            x0 = x1.clone();

        }
        return x1;
    }

    public double function_min_dividing_segments_in_half(double[] x0, int i, double a, double b) {
        double[] y = new double[x0.length];
        for (int j = 0; j < x0.length; j++) {
            y[j] = x0[j];
        }
        do {
            double a_ = a + (b - a) / 5;
            double b_ = b - (b - a) / 5;

            y[i] = a_;
            double res_1 = energy_functional(y);
            y[i] = b_;
            double res_2 = energy_functional(y);

            if (res_1 > res_2) a = a_;
            else b = b_;
        } while ((b - a) / 2 >= epsilon / 1000);
        return (a + b) / 2;
    }

    public double function_min_golden_ratio(double[] x0, int i, double a, double b) {
        final double phi = (1 + Math.sqrt(5)) / 2;
        double a_ = b - ((b - a) / phi);
        double b_ = a + ((b - a) / phi);

        do {
            x0[i] = a_;
            double res_1 = energy_functional(x0);
            x0[i] = b_;
            double res_2 = energy_functional(x0);
            if (res_1 > res_2) {
                a = a_;
                a_ = b_;
                b_ = b - (a_ - a);
            } else {
                b = b_;
                b_ = a_;
                a_ = a + (b - b_);
            }
        } while ((b - a) / 2 >= epsilon);
        return (a + b) / 2;
    }

    public void runTask () {
        print_matrix(A);
        double[] x0 = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            x0[i] = -1;
        }

        double[] res = function_min_coordinate_descent(x0);

        save_result(res);

    }
}
