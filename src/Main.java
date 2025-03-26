import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        double a = 0;
        double b = 2;
        double c = 0;
        double d = 3;
        int Nx = 8;
        int Ny = 12;
        double epsilon = 0.00000001;
        ArrayList<ArrayList<Double>> solution = (new FifthTask2Sem(a, b, c, d, Nx, Ny, epsilon)).solve();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(5);
        for (int i = 0; i < solution.size(); i++) {
            for (int j = 0; j < solution.getFirst().size(); j++) {
//                System.out.println(0 + i * ((b - a) / (Nx - 1)));
//                System.out.println(c + j * ((d - c) / (Ny - 1)));
//                System.out.println(solution.get(i).get(j));
                System.out.print("(" + df.format(0 + i * ((b - a) / (Nx - 1))) + ", " + df.format(c + j * ((d - c) / (Ny - 1))) + ", " + df.format(solution.get(i).get(j)) + "), ");
            }
        }
    }
}