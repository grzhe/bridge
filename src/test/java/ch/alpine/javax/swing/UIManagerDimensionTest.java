// code by jph
package ch.alpine.javax.swing;

import org.junit.jupiter.api.Test;

public class UIManagerDimensionTest {
  @Test
  public void testSimple() {
    for (UIManagerDimension uiManagerDimension : UIManagerDimension.values()) {
      uiManagerDimension.get();
    }
  }
}
