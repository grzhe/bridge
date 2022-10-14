//// code by gjoel, jph
//package ch.alpine.bridge.fig;
//
//import java.util.function.Function;
//
//import org.jfree.chart.JFreeChart;
//
//import ch.alpine.bridge.lang.Unicode;
//import ch.alpine.tensor.Scalar;
//
///** inspired by
// * <a href="https://reference.wolfram.com/language/ref/Histogram.html">Histogram</a> */
//public enum Histogram {
//  ;
//  /** @param visualSet
//   * @return */
//  public static JFreeChart of(VisualSet visualSet) {
//    return of(visualSet, false);
//  }
//
//  /** @param visualSet
//   * @param stacked
//   * @return
//   * @see StackedHistogram */
//  /* package */ static JFreeChart of(VisualSet visualSet, boolean stacked) {
//    return JFreeChartFactory.barChart(visualSet, stacked, Unicode::valueOf);
//  }
//
//  /** @param visualSet
//   * @param stacked
//   * @param naming for coordinates on x-axis
//   * @return */
//  public static JFreeChart of(VisualSet visualSet, boolean stacked, Function<Scalar, String> naming) {
//    return JFreeChartFactory.barChart(visualSet, stacked, naming);
//  }
//}
