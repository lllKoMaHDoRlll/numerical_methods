import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        double tau = 1;
        int n = 20;
        ArrayList<Double> arr_approx_double_precision = (new SecondTask2Sem(tau/2, 3, n * 2)).solve(new ArrayList<>());
        (new SecondTask2Sem(tau, 3, n)).solve(arr_approx_double_precision);
    }
}