// code by jph
package ch.alpine.java.fig;

import java.awt.Color;
import java.io.IOException;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.sca.Cos;
import ch.alpine.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class SpectrogramTest extends TestCase {
  public void testSimple() throws IOException {
    ScalarUnaryOperator polynomial = Polynomial.of(Tensors.vector( //
        0, //
        800, //
        2800).multiply(Pi.VALUE));
    double lo = 0.32;
    double hi = 1.6;
    Tensor domain = Subdivide.of(RealScalar.of(lo), RealScalar.of(hi), (int) (8000 * (hi - lo)));
    Tensor chirp = domain.map(polynomial).map(Cos.FUNCTION);
    VisualSet visualSet = new VisualSet();
    visualSet.setPlotLabel("Spectrogram");
    visualSet.add(domain.map(s -> Quantity.of(s, "s")), chirp);
    visualSet.getAxisX().setUnit(Unit.of("ms"));
    visualSet.getAxisX().setLabel("time");
    visualSet.getAxisY().setLabel("user defined");
    visualSet.getAxisY().setUnit(Unit.of("ms^-1"));
    // visualSet.setColorDataGradient(ColorDataGradients.CLASSIC);
    JFreeChart jFreeChart = Spectrogram.of(visualSet, HannWindow.FUNCTION);
    jFreeChart.setBackgroundPaint(Color.WHITE);
    // TODO this is more like a demo
    ChartUtils.saveChartAsPNG(HomeDirectory.Pictures("spectrogram.png"), jFreeChart, 1024, 320);
  }
}
