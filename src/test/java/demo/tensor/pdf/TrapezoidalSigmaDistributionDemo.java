// code by jph
package demo.tensor.pdf;

import java.awt.Color;
import java.io.IOException;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import ch.alpine.java.fig.ListPlot;
import ch.alpine.java.fig.VisualSet;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TrapezoidalDistribution;

public enum TrapezoidalSigmaDistributionDemo {
  ;
  public static void main(String[] args) throws IOException {
    Distribution dist1 = NormalDistribution.of(2, 1.2);
    Distribution dist2 = TrapezoidalDistribution.with(2, 1.2, 2.4);
    {
      Tensor domain = Subdivide.of(-3 + 2, 3 + 2, 6 * 10);
      VisualSet visualSet = new VisualSet();
      PDF pdf1 = PDF.of(dist1);
      PDF pdf2 = PDF.of(dist2);
      visualSet.add(domain, domain.map(pdf1::at));
      visualSet.add(domain, domain.map(pdf2::at));
      JFreeChart jFreeChart = ListPlot.of(visualSet, true);
      jFreeChart.setBackgroundPaint(Color.WHITE);
      ChartUtils.saveChartAsPNG(HomeDirectory.Pictures("trapzsigma.png"), jFreeChart, 640, 480);
    }
  }
}
