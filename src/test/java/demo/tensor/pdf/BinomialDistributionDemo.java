// code by jph
package demo.tensor.pdf;

import java.awt.Dimension;
import java.io.IOException;

import ch.alpine.bridge.fig.ChartUtils;
import ch.alpine.bridge.fig.JFreeChart;
import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.VisualSet;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.d.BinomialDistribution;

public enum BinomialDistributionDemo {
  ;
  public static void main(String[] args) throws IOException {
    int n = 50;
    Distribution distribution = BinomialDistribution.of(n, RationalScalar.HALF);
    PDF pdf = PDF.of(distribution);
    CDF cdf = CDF.of(distribution);
    VisualSet visualSet = new VisualSet();
    {
      Tensor domain = Range.of(0, n + 1);
      visualSet.add(domain, domain.map(pdf::at));
      visualSet.add(domain, domain.map(cdf::p_lessEquals));
    }
    JFreeChart jFreeChart = ListPlot.of(visualSet, true);
    ChartUtils.saveChartAsPNG(HomeDirectory.Pictures("binomial_distr.png"), jFreeChart, new Dimension(640, 480));
  }
}
