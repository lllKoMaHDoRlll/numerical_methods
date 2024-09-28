import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FourthTask {
    private double x0;
    private double q;
    private double epsilon;


    private double func(double x) {
        return (1 - x)/x;
    }

    public FourthTask() {
        load_data();
    }

    private void load_data () {
        try {
            File file = new File("input_4.txt");
            Scanner reader = new Scanner(file);

            x0 = Double.parseDouble(reader.nextLine());
            q = Double.parseDouble(reader.nextLine());
            epsilon = Double.parseDouble(reader.nextLine());

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file");
        }
    }

    private void save_data(double res) {
        try {
            FileWriter writer = new FileWriter("output_4.txt");
            writer.write(Double.toString(res));
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    private double vegstein_method() {
        double x1 = func(x0);
        double x0_ = x0;
        double x1_ = x1;
        double x2;
        do {
            x2 = func(x1_);
            double x2_ = (x2 * x0_ - x1 * x1_)/(x2 + x0_ - x1 - x1_);
            x0 = x1;
            x0_ = x1_;
            x1 = x2;
            x1_ = x2_;
        } while (Math.abs(x2 - x1_) > (epsilon * (1 - q)) / q);
        return x2;
    }

    public void runTask() {
        double res = vegstein_method();
        save_data(res);
    }
}
