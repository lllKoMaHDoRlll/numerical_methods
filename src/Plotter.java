import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.None;

import java.awt.*;
import java.util.ArrayList;

public class Plotter {
    private final ArrayList<Double> arr_x;
    private final ArrayList<Double> arr_solution;
    private final ArrayList<Double> arr_approximation;
    private final ArrayList<Double> arr_approximation_double_precision;

    private final int max_diff_i;
    private final double max_diff;

    public Plotter(ArrayList<Double> arr_x_, ArrayList<Double> arr_solution_, ArrayList<Double> arr_approximation_, ArrayList<Double> arr_approximaiton_double_precision_, double max_diff_, int max_diff_i_) {
        arr_x = arr_x_;
        arr_solution = arr_solution_;
        arr_approximation = arr_approximation_;
        arr_approximation_double_precision = arr_approximaiton_double_precision_;
        max_diff = max_diff_;
        max_diff_i = max_diff_i_;
    }

    public static void show_hyper_plots(ArrayList<Double> x, ArrayList<ArrayList<Double>> ys, ArrayList<ArrayList<Double>> solution) {
        for (int i = 0; i < ys.size(); i++) {
            XYChart chart = new XYChart(800, 600);
            chart.getStyler().setZoomEnabled(true);
            chart.getStyler().setZoomResetByDoubleClick(false);
            chart.getStyler().setZoomResetByButton(true);
            chart.getStyler().setZoomSelectionColor(new Color(0,0 , 192, 128));
            chart.getStyler().setAnnotationLineStroke(new BasicStroke(1.0f));

            System.out.println(x.size());
            System.out.println(ys.get(i).size());

            XYSeries series_solution = chart.addSeries("solution", x, solution.get(i));
            XYSeries series_approximation = chart.addSeries("approximation", x, ys.get(i));

            series_solution.setMarker(new None());
            series_approximation.setMarker(new None());
            series_solution.setLineStyle(SeriesLines.DASH_DASH);

            new SwingWrapper<>(chart).displayChart();
        }
    }

    public void show_plots() {
        XYChart chart = new XYChart(800, 600);
        chart.getStyler().setZoomEnabled(true);
        chart.getStyler().setZoomResetByDoubleClick(false);
        chart.getStyler().setZoomResetByButton(true);
        chart.getStyler().setZoomSelectionColor(new Color(0,0 , 192, 128));

        chart.getStyler().setAnnotationLineStroke(new BasicStroke(1.0f));
        XYSeries series_solution = chart.addSeries("solution", arr_x, arr_solution);
        XYSeries series_approximation = chart.addSeries("approximation", arr_x, arr_approximation);
        if (arr_approximation_double_precision.size() > 0) { // TODO: сделать размерность векторов одинаковой
            int arr_length = arr_approximation.size() - 1;
            for (int i = 0; i < arr_length; i++) {
                double average_x = (arr_x.get(2 * i) + arr_x.get(2 * i + 1))/2;
                double average_solution = (arr_solution.get(2 * i) + arr_solution.get(2 * i + 1))/2;
                double average_approximation = (arr_solution.get(2 * i) + arr_solution.get(2 * i + 1))/2;

                arr_x.add(2* i + 1, average_x);
                arr_solution.add(2* i + 1, average_solution);
                arr_approximation.add(2* i + 1, average_approximation);
            }
            arr_x.add(arr_x.getLast());
            arr_solution.add(arr_solution.getLast());
            arr_approximation.add(arr_approximation.getLast());

            XYSeries series_approximation_double_precision = chart.addSeries("approximation_double_precision", arr_x, arr_approximation_double_precision);
            series_approximation_double_precision.setMarker(new None());
            series_approximation_double_precision.setLineStyle(SeriesLines.DASH_DOT);
        }

        series_solution.setMarker(new None());
        series_approximation.setMarker(new None());
        series_solution.setLineStyle(SeriesLines.DASH_DASH);

//        double[] max_diff_arr_x = {arr_x.get(max_diff_i), arr_x.get(max_diff_i)};
//        double[] max_diff_arr_y = {arr_approximation.get(max_diff_i), arr_solution.get(max_diff_i)};
//        chart.addSeries("Max difference: ".concat(Double.toString(max_diff)), max_diff_arr_x, max_diff_arr_y);
//        if (arr_approximation_double_precision.size() > 0) {
            new SwingWrapper<>(chart).displayChart();
//        }
    }
}
