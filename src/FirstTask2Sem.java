import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FirstTask2Sem {
    private static double[][] load_matrix () {
        try {
            File file = new File("input_1_2.txt");
            Scanner reader = new Scanner(file);

            int n = Integer.parseInt(reader.nextLine());
            double[][] matrix = new double[n][n];
            for (int i = 0; i < n; i++) {
                String[] lines = reader.nextLine().split(" ");
                for (int j = 0; j < n; j++) {
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

    private static void save_result (double[] result) {
        try {
            File file = new File("./output_1_2.txt");
            file.createNewFile();

            FileWriter writer = new FileWriter("output_1_2.txt");
            writer.write(Arrays.toString(result));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    private static double[][] multiply_matrices(double[][] m1, double[][] m2) {
        double[][] res = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                double value = 0;
                for (int k = 0; k < m1[0].length; k++) {
                    value += m1[i][k] * m2[k][j];
                }
                res[i][j] = value;
            }
        }
        return res;
    }

    private static double[][] transpose (double[][] matrix) {
        double[][] res = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res[j][i] = matrix[i][j];
            }
        }
        return res;
    }

    private static double[][][] householder_reflection (double[][] matrix) {
        double[][][] res = new double[2][matrix.length][matrix.length];
        double[][] H_res = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            H_res[i][i] = 1;
        }

        for (int i = 0; i < matrix.length - 1; i++) {
            double row_sum = 0;
            for (int j = i; j < matrix.length; j++) {
                row_sum += matrix[j][i] * matrix[j][i];
            }
            double s = Math.signum(-1 * matrix[i][i]) * Math.sqrt(row_sum);
            double mu = 1 / (Math.sqrt(2 * s * (s - matrix[i][i])));
            double[][] w = new double[matrix.length][1];
            w[i][0] = (matrix[i][i] - s) * mu;
            for (int j = i + 1; j < matrix.length; j++) {
                w[j][0] = matrix[j][i] * mu;
            }

            double[][] ww = multiply_matrices(w, transpose(w));

            double[][] H = new double[matrix.length][matrix.length];
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    int e = 0;
                    if (j == k) e = 1;
                    H[j][k] = e - 2 * ww[j][k];
                }
            }
            matrix = multiply_matrices(H, matrix);
            H_res = multiply_matrices(H_res, H);
        }
        res[0] = H_res;
        res[1] = matrix;
        return res;
    }

    public static void runTask() {
        double[][] matrix = load_matrix();

        for (int i = 0; i < 10; i++) {
            double[][][] res = householder_reflection(matrix);

            double[][] Q = res[0];
            double[][] R = res[1];
            matrix = multiply_matrices(R, Q);
        }
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(matrix[i][i]);
        }
    }
}
