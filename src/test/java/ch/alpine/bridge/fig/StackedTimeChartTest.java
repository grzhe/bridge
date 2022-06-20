// code by jph
package ch.alpine.bridge.fig;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;

class StackedTimeChartTest {
  @Test
  void testEmpty() {
    VisualSet visualSet = new VisualSet();
    CascadeHelper.draw(StackedTimeChart.of(visualSet));
  }

  @Test
  void testEmptyRow() {
    VisualSet visualSet = new VisualSet();
    visualSet.add(Tensors.empty());
    CascadeHelper.draw(StackedTimeChart.of(visualSet));
  }
}
