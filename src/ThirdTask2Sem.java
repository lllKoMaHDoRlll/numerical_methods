import java.util.ArrayList;

public class ThirdTask2Sem {
    private double tau;
    private ArrayList<Double> u_0;
    private int n;
    private final int m = 2;
    private final double newton_epsilon = 0.001;
    private final double partial_epsilon = 0.001;
    private final ArrayList<ArrayList<Double>> y = new ArrayList<>();

    public ThirdTask2Sem(double tau, ArrayList<Double> u_0, int n) {
        this.tau = tau;
        this.u_0 = u_0;
        this.n = n;
        this.y.add(u_0);
    }

    public static ArrayList<Double> func(double t, ArrayList<Double> u) {
        ArrayList<Double> results = new ArrayList<>();
        results.add(-2 * u.getFirst() + 5 * u.getLast());
        results.add(-7 * u.getFirst() + 10 * u.getLast());
        return results;
    }

    public static ArrayList<Double> analytic_solution(double t) {
        ArrayList<Double> results = new ArrayList<>();
        results.add(5 * Math.exp(5 * t) - 2 * Math.exp(3 * t));
        results.add(7 * Math.exp(5 * t) - 2 * Math.exp(3 * t));
        return results;
    }

    private ArrayList<Double> adams_iter_eq(ArrayList<Double> y_n) {
        ArrayList<Double> results = new ArrayList<>();
        results.add(
                y_n.getFirst() / this.tau - (double) 5/12 * func(this.y.size() * this.tau, y_n).getFirst()
                - (double) 1/12 * (
                        8 * func(this.tau * (this.y.size() - 1), this.y.getLast()).getFirst()
                        - func(this.tau * (this.y.size() - 2), this.y.get(this.y.size() - 2)).getFirst()
                        )
                - this.y.getLast().getFirst() / this.tau
        );
        results.add(
                y_n.getLast() / this.tau - (double) 5/12 * func(this.y.size() * this.tau, y_n).getLast()
                        - (double) 1/12 * (
                        8 * func(this.tau * (this.y.size() - 1), this.y.getLast()).getLast()
                                - func(this.tau * (this.y.size() - 2), this.y.get(this.y.size() - 2)).getLast()
                )
                        - this.y.getLast().getLast() / this.tau
        );
        return results;
    }

    private ArrayList<ArrayList<Double>> partial_adams(ArrayList<Double> x_0) {
        ArrayList<ArrayList<Double>> partial_matrix = new ArrayList<>();
        for (int j = 0; j < x_0.size(); j++) {
            partial_matrix.add(new ArrayList<>());
            ArrayList<Double> x = (ArrayList<Double>) x_0.clone();
            for (int i = 0; i < x_0.size(); i++) {
                x.set(i, x.get(i) + this.partial_epsilon);
                partial_matrix.get(j).add((this.adams_iter_eq(x).get(j) - this.adams_iter_eq(x_0).get(j)) / this.partial_epsilon);
            }
        }
        return partial_matrix;
    }

    private double norm(ArrayList<Double> x) {
        double res = 0;
        for (int i = 0; i < x.size(); i++) {
            res += x.get(i) * x.get(i);
        }
        return Math.sqrt(res);
    }

    private ArrayList<Double> newton_method(ArrayList<Double> y_0) {
        ArrayList<Double> x_k = (ArrayList<Double>) y_0.clone();
        while (this.norm(adams_iter_eq(x_k)) > this.newton_epsilon) {
            ArrayList<ArrayList<Double>> partial_matrix_ = partial_adams(x_k);
            double[][] partial_matrix = new double[y_0.size()][y_0.size() + 1];
            ArrayList<Double> func_value = adams_iter_eq(x_k);
            for (int i = 0; i < partial_matrix.length; i++) {
                for (int j = 0; j < partial_matrix.length; j++) {
                    partial_matrix[i][j] = partial_matrix_.get(i).get(j);
                }
                partial_matrix[i][y_0.size()] = -1 * func_value.get(i);
            }
            ArrayList<Double> x0 = new ArrayList<>(y_0.size());
            for (int i = 0; i < y_0.size(); i++) {
                x0.add(1.0);
            }
            double[] res = FirstTask.solve_equations(partial_matrix);
            for (int i = 0; i < x_k.size(); i++) {
                x_k.set(i, x_k.get(i) + res[i]);
            }
        }
        return x_k;
    }

    private void runge_kutta_method() {
        for (int i = 1; i < this.m; i++) {
            ArrayList<Double> func_value = func((i - 1) * this.tau, this.y.get(i-1));
            ArrayList<Double> calculated_func_value = new ArrayList<>();
            for (int j = 0; j < func_value.size(); j++) {
                calculated_func_value.add(this.tau * func_value.get(j) + this.y.getLast().get(j));
            }

            this.y.add(calculated_func_value);
        }
    }

    private void adams_method() {
        for (int i = m; i < n; i++) {
            ArrayList<Double> y_i = this.newton_method(this.y.get(i - 1));
            this.y.add(y_i);
        }
    }

    public ArrayList<ArrayList<Double>> solve(ArrayList<Double> doubled_precision_arr_approx) {
        this.runge_kutta_method();
        System.out.println(this.y.size());
        this.adams_method();

        ArrayList<Double> arr_x = new ArrayList<>();
        ArrayList<ArrayList<Double>> arr_solution = new ArrayList<>();
        arr_solution.add(new ArrayList<>());
        arr_solution.add(new ArrayList<>());
        ArrayList<ArrayList<Double>> arr_approx = new ArrayList<>();
        arr_approx.add(new ArrayList<>());
        arr_approx.add(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            arr_x.add(i * this.tau);
            ArrayList<Double> sol = analytic_solution(i * this.tau);
            arr_solution.getFirst().add(sol.getFirst());
            arr_solution.getLast().add(sol.getLast());
            arr_approx.getFirst().add(this.y.get(i).getFirst());
            arr_approx.getLast().add(this.y.get(i).getLast());
        }

        Plotter.show_hyper_plots(arr_x, arr_approx, arr_solution);

//        if (doubled_precision_arr_approx.size() > 0) {
//            Plotter plotter = new Plotter(arr_x, arr_solution, arr_approx, doubled_precision_arr_approx, 0, 0);
//            plotter.show_plots();
//        }
        return arr_approx;

    }
}
