import java.util.ArrayList;

public class FourthTask2Sem {
    private int N;
    private double a;
    private double b;
    private double h;
    private double y_0, y_N;

    public double f(double x) {
        return 2 * x;
    }

    public double p(double x) {
        return 1;
    }

    public double analytic(double x) {
        return Math.exp(x + 1) / (Math.exp(2) - 1) - Math.exp(-1 * x + 1) / (Math.exp(2) - 1) - 2 * x;
    }

    public FourthTask2Sem(double a, double b, int N, double y_0, double y_N) {
        this.a = a;
        this.b = b;
        this.N = N;
        this.h = (b - a) / N;
        this.y_0 = y_0;
        this.y_N = y_N;
    }

    public static ArrayList<Double> TDMA(ArrayList<ArrayList<Double>> A_, ArrayList<Double> b_) {
        ArrayList<ArrayList<Double>> A = new ArrayList<>();
        ArrayList<Double> b = new ArrayList<>(b_);
        for (int i = 0; i < A_.size(); i++) {
            A.add(new ArrayList<>(A_.get(i)));
        }

        ArrayList<Double> alphas = new ArrayList<>();
        ArrayList<Double> betas = new ArrayList<>();

        alphas.add((-1 * A.get(0).get(1)) / A.get(0).get(0));
        betas.add(b.get(0) / A.get(0).get(0));

        for (int i = 1; i < A.size() - 1; i++) {
            alphas.add((-1 * A.get(i).get(i+1)) / (A.get(i).get(i) + A.get(i).get(i-1) * alphas.getLast()));
            betas.add((b.get(i) - A.get(i).get(i-1) * betas.getLast()) / (A.get(i).get(i) + A.get(i).get(i-1) * alphas.get(i-1)));
        }

        betas.add((b.getLast() - A.getLast().get(A.size() - 2) * betas.getLast()) / (A.getLast().getLast() + A.getLast().get(A.size() - 2) * alphas.getLast()));

        ArrayList<Double> x = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            x.add(0.0);
        }
        x.set(A.size() - 1, betas.getLast());
        for (int i = A.size() - 2; i >= 0; i--) {
            x.set(i, alphas.get(i) * x.get(i + 1) + betas.get(i));
        }
        return x;
    }

    public void solve() {
        ArrayList<ArrayList<Double>> A = new ArrayList<>();
        ArrayList<Double> F = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
            A.add(new ArrayList<>());
            F.add(0.0);
            for (int j = 0; j < N + 1; j++) {
                A.get(i).add(0.0);
            }
        }

        A.get(0).set(0, 1.0);
        F.set(0, y_0);
        A.get(N).set(N, 1.0);
        F.set(N, y_N);

        for (int i = 1; i < N; i++) {
            double x_i_prev = a + (i - 1) * h;
            double x_i = a + i * h;
            double x_i_next = a + (i + 1) * h;

            A.get(i).set(
                    i-1,
                    (240/(h*h*h*h) - 20/(h*h) * p(x_i_prev) + p(x_i) * p(x_i_prev))
                            /((240/(h*h*h*h) - 20/(h*h) * p(x_i_next) + p(x_i) * p(x_i_next)))
            );
            A.get(i).set(
                    i,
                    -1 * (480/(h*h*h*h) + 200/(h*h) * p(x_i) + 2 * p(x_i) * p(x_i))
                            /((240/(h*h*h*h) - 20/(h*h) * p(x_i_next) + p(x_i) * p(x_i_next)))
            );
            A.get(i).set(
                    i+1,
                    1.0
            );
            F.set(
                    i,
                    (h*h)/((240/(h*h*h*h) - 20/(h*h) * p(x_i_next) + p(x_i) * p(x_i_next)))
                            * ((f(x_i_prev) + f(x_i_next)) * (20 - p(x_i) * h * h) + f(x_i)
                                * (-240 + 2 * p(x_i) * h * h))
            );

            A.get(i).set(i-1, 1.0);
            A.get(i).set(i, -2 - p(x_i) * h * h);
            A.get(i).set(i+1, 1.0);
            F.set(i,f(x_i) * h * h);
        }

        ArrayList<Double> y_approximation = TDMA(A, F);
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y_analytic = new ArrayList<>();
        for (int i = 0; i < N + 1; i++) {
            x.add(a + i * h);
            y_analytic.add(analytic(x.getLast()));
        }
        System.out.println(y_approximation.size());
        System.out.println(x.size());
        System.out.println(y_analytic.size());


        Plotter plotter = new Plotter(x, y_analytic, y_approximation, new ArrayList<>(), 0.0, 0);
        plotter.show_plots();
    }
}
