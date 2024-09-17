import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.None;

import java.awt.*;
import java.util.ArrayList;

public class Plotter {
    private final ArrayList<Double> arr_x;
    private final ArrayList<Double> arr_solution;
    private final ArrayList<Double> arr_approximation;

    private final int max_diff_i;
    private final double max_diff;

    public Plotter(ArrayList<Double> arr_x_, ArrayList<Double> arr_solution_, ArrayList<Double> arr_approximation_, double max_diff_, int max_diff_i_) {
        arr_x = arr_x_;
        arr_solution = arr_solution_;
        arr_approximation = arr_approximation_;
        max_diff = max_diff_;
        max_diff_i = max_diff_i_;
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

        series_solution.setMarker(new None());
        series_approximation.setMarker(new None());
        series_solution.setLineStyle(SeriesLines.DASH_DASH);

        double[] max_diff_arr_x = {arr_x.get(max_diff_i), arr_x.get(max_diff_i)};
        double[] max_diff_arr_y = {arr_approximation.get(max_diff_i), arr_solution.get(max_diff_i)};
        chart.addSeries("Max difference: ".concat(Double.toString(max_diff)), max_diff_arr_x, max_diff_arr_y);
        new SwingWrapper<>(chart).displayChart();
    }
}
