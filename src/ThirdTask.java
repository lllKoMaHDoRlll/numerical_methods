import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ThirdTask {
    private int n;
    private static final int x_distribution = 100;

    public static double func(double x) {
        // return Math.pow(x, 5);
        return Math.cos(28 * x);
    }

    ThirdTask() {
        load_data();
    }

    public void load_data () {
        try {
            File file = new File("input_3.txt");
            Scanner reader = new Scanner(file);

            this.n = Integer.parseInt(reader.nextLine());
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file");
        }
    }

    private ArrayList<ArrayList<Double>> get_chebyshev_mesh() {
        ArrayList<ArrayList<Double>> mesh = new ArrayList<>();
        for (int i = 1; i < n + 1; i++) {
            double x_i = Math.cos(((2* i - 1) * Math.PI)/(2 * n));
            ArrayList<Double> point = new ArrayList<>();
            point.add(x_i);
            point.add(func(x_i));
            mesh.add(point);
        }
        return mesh;
    }

    private double fundamental_polynomials_Lagrange(ArrayList<ArrayList<Double>> mesh, int k, double x) {
        double res = 1d;
        for (int i = 0; i < n; i++) {
            if (i == k) continue;
            res *= x - mesh.get(i).getFirst();
            res /= mesh.get(k).getFirst() - mesh.get(i).getFirst();
        }
        return res;
    }

    private double Lagrange_polynomial(ArrayList<ArrayList<Double>> mesh, double x) {
        double res = 0;
        for (int i = 0; i < n; i++) {
            res += mesh.get(i).getLast() * fundamental_polynomials_Lagrange(mesh, i, x);
        }
        return res;
    }

    public void runTask() {
        ArrayList<ArrayList<Double>> mesh = get_chebyshev_mesh();

        ArrayList<Double> arr_x = new ArrayList<>();
        ArrayList<Double> arr_solution = new ArrayList<>();
        ArrayList<Double> arr_approximation = new ArrayList<>();
        double max_diff;
        int max_diff_i;

        arr_x.add(-1d);
        arr_solution.add(func(arr_x.getFirst()));
        arr_approximation.add(Lagrange_polynomial(mesh, arr_x.getFirst()));
        max_diff = Math.abs(arr_solution.getFirst() - arr_approximation.getFirst());
        max_diff_i = 0;

        for (int i = 1; i < x_distribution + 1; i++) {
            arr_x.add(-1 + (double) (2 * i) / x_distribution);
            arr_solution.add(func(arr_x.get(i)));
            arr_approximation.add(Lagrange_polynomial(mesh, arr_x.get(i)));
            double diff = Math.abs(arr_solution.get(i) - arr_approximation.get(i));
            if (diff > max_diff) {
                max_diff_i = i;
                max_diff = diff;
            }
        }

        Plotter plotter = new Plotter(arr_x, arr_solution, arr_approximation, max_diff, max_diff_i);
        plotter.show_plots();
    }
}
