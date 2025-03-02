import java.util.ArrayList;

public class SecondTask2Sem {
//    3-шаговый метод Адамса, неявный
    private double tau;
    private double u_0;
    private int n;
    private final int m = 3;
    private final double newton_epsilon = 0.001;
    private final double partial_epsilon = 0.001;
    private final ArrayList<Double> y = new ArrayList<>();

    public SecondTask2Sem(double tau, double u_0, int n) {
        this.tau = tau;
        this.u_0 = u_0;
        this.n = n;
        this.y.add(u_0);
    }

    public static double func(double t, double u) {
        return (t + 1) * (t + 1) + 1 + (2 * u * (t+1))/(1 + (t + 1)*(t+1));
    }

    public static double analytic_solution(double t) {
        return (t + 1.5) * (1 + (t + 1) * (t + 1));
    }

    private double adams_iter_eq(double y_n) {
        return y_n / this.tau
                - 0.375 * func(this.tau * this.y.size(), y_n)
                - (double) 1/24 * (
                        19 * func(this.tau * (this.y.size() - 1), this.y.get(this.y.size()-1))
                        - 5 * func(this.tau * (this.y.size() - 2), this.y.get(this.y.size()-2))
                        + func(this.tau * (this.y.size() - 3), this.y.get(this.y.size()-3))
        ) - this.y.getLast() / this.tau;
    }

    private double partial_adams(double x_0) {
        return (this.adams_iter_eq(x_0 + this.partial_epsilon) - this.adams_iter_eq(x_0)) / this.partial_epsilon;
    }

    private double newton_method(double y_0) {
        double x_k = y_0;
        while (Math.abs(adams_iter_eq(x_k)) > this.newton_epsilon) {
            x_k -= adams_iter_eq(x_k)/partial_adams(x_k);
            System.out.println(adams_iter_eq(x_k));
        }
        return x_k;
    }

    private void runge_kutta_method() {
        for (int i = 1; i < this.m; i++) {
            double y_i = this.tau * func((i - 1) * this.tau, this.y.get(i-1)) + this.y.getLast();
            this.y.add(y_i);
        }
    }

    private void adams_method() {
        for (int i = m; i < n; i++) {
            double y_i = this.newton_method(this.y.get(i - 1));
            this.y.add(y_i);
        }
    }

    public ArrayList<Double> solve(ArrayList<Double> doubled_precision_arr_approx) {
        this.runge_kutta_method();
        System.out.println(this.y.size());
        this.adams_method();

        ArrayList<Double> arr_x = new ArrayList<>();
        ArrayList<Double> arr_solution = new ArrayList<>();
        ArrayList<Double> arr_approx = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            arr_x.add(i * this.tau);
            arr_solution.add(analytic_solution(i * this.tau));
            arr_approx.add(this.y.get(i));
        }

        if (doubled_precision_arr_approx.size() > 0) {
            Plotter plotter = new Plotter(arr_x, arr_solution, arr_approx, doubled_precision_arr_approx, 0, 0);
            plotter.show_plots();
        }
        return arr_approx;

    }
}
