import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class FirstTask {
    public static double[][] load_matrix () {
        try {
            File file = new File("input_1.txt");
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
            File file = new File("./output_1.txt");
            file.createNewFile();

            FileWriter writer = new FileWriter("output_1.txt");
            writer.write(Arrays.toString(result));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    public static double[] solve_equations(double[][] matrix) {
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                double multiplier = matrix[j][i] / matrix[i][i];
                for (int k = i; k < matrix[i].length; k++) {
                    matrix[j][k] -= multiplier * matrix[i][k];
                }
            }
        }

        for (int i = matrix.length - 1; i > 0; i--) {
            for (int j = i - 1; j > -1; j--) {
                double multiplier = matrix[j][i] / matrix[i][i];
                matrix[j][i] -= multiplier * matrix[i][i];
                matrix[j][matrix[j].length - 1] -= multiplier * matrix[i][matrix[i].length - 1];
            }
        }

        double[] result = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            result[i] = matrix[i][matrix[i].length - 1] / matrix[i][i];
        }
        return result;
    }

    public static void print_matrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public static void runTask () {
        double[][] matrix = load_matrix();
        print_matrix(matrix);
        double[] result = solve_equations(matrix);
        save_result(result);
    }
}
