import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FifthTask2Sem {
    private final double a, c;
    private final int Nx, Ny;
    private final double hx, hy;
    private final double epsilon;

    public static double func(double x, double y) {
        return 8 * Math.cos(x + y) * Math.cos(x + y) - 4;
    }

    public static double mu_xc(double x) {
        return Math.sin(x) * Math.sin(x);
    }

    public static double mu_xd(double x) {
        return Math.sin(x + 3) * Math.sin(x + 3);
    }

    public static double mu_ay(double y) {
        return Math.sin(y) * Math.sin(y);
    }

    public static double mu_by(double y) {
        return Math.sin(y + 2) * Math.sin(y + 2);
    }

    public static double analytic_solution(double x, double y) {
        return Math.sin(x + y) * Math.sin(x + y);
    }

    public FifthTask2Sem(double a, double b, double c, double d, int Nx, int Ny, double epsilon) {
        this.a = a;
        this.c = c;
        this.Nx = Nx;
        this.Ny = Ny;
        this.hx = (b - a) / (Nx - 1);
        this.hy = (d - c) / (Ny - 1);
        this.epsilon = epsilon;
    }

    private void save_result (ArrayList<ArrayList<Double>> result, String filename) {
        try {
            File file = new File("./" + filename);
            file.createNewFile();

            FileWriter writer = new FileWriter(filename);
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(5);
            for (int i = 0; i < result.size(); i++) {
                writer.write(result.get(i).stream().map(df::format).collect(Collectors.joining(" ")));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    private ArrayList<ArrayList<Double>> copy_matrix(ArrayList<ArrayList<Double>> matrix) {
        ArrayList<ArrayList<Double>> matrix_copy = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            matrix_copy.add(new ArrayList<>());
            for (int j = 0; j < matrix.getFirst().size(); j++) {
                matrix_copy.get(i).add(matrix.get(i).get(j));
            }
        }
        return matrix_copy;
    }

    public ArrayList<ArrayList<Double>> solve() {
        ArrayList<ArrayList<Double>> A = new ArrayList<>();
        ArrayList<ArrayList<Double>> A_solution = new ArrayList<>();
        for (int i = 0; i < Nx; i++) {
            A.add(new ArrayList<>());
            A_solution.add(new ArrayList<>());
            for (int j = 0; j < Ny; j++) {
                A.get(i).add(0.0);
                A_solution.get(i).add(analytic_solution(a + i * hx, c + j * hy));
            }
        }

        for (int i = 0; i < Nx; i++) {
            for (int j = 0; j < Ny; j++) {
                if (i == 0)               A.get(i).set(j, mu_ay(c + j * hy));
                else if (j == 0)          A.get(i).set(j, mu_xc(a + i * hx));
                else if (j == Ny - 1)     A.get(i).set(j, mu_xd(a + i * hx));
                else if (i == Nx - 1)     A.get(i).set(j, mu_by(c + j * hy));
            }
        }

        double diff;
        do {
            ArrayList<ArrayList<Double>> A_new = this.copy_matrix(A);
            diff = 0;
            for (int i = 1; i < Nx - 1; i++) {
                for (int j = 1; j < Ny - 1; j++) {
                    A_new.get(i).set(
                            j,
                            (1.0/4.0) * (A.get(i+1).get(j)
                                    + A.get(i-1).get(j)
                                    + A.get(i).get(j+1)
                                    + A.get(i).get(j-1)
                                    - hx * hx * func(a + i * hx, c + j * hy)
                            )
                    );
                    diff = Math.max(diff, Math.abs(A_new.get(i).get(j) - A.get(i).get(j)));
                }
            }
            A = A_new;
        } while (diff > this.epsilon);



        save_result(A, "output_5_2_approximation.txt");
        save_result(A_solution, "output_5_2_analytic.txt");

        return A;
    }
}
