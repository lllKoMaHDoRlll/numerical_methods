import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        double tau = 1;
        int n = 10;
        ArrayList<Double> u_0 = new ArrayList<>();
        u_0.add(3.0);
        u_0.add(5.0);
        (new ThirdTask2Sem(tau, u_0, n)).solve(new ArrayList<>());
    }
}