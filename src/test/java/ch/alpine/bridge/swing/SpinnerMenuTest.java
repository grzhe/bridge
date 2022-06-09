// code by jph
package ch.alpine.bridge.swing;

import javax.swing.JPopupMenu;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.mat.re.Pivots;

class SpinnerMenuTest {
  @Test
  public void testSimple() {
    SpinnerLabel<Pivots> spinnerLabel = SpinnerLabel.of(Pivots.class);
    new SpinnerMenu<>(spinnerLabel, false).design(new JPopupMenu());
    new SpinnerMenu<>(spinnerLabel, true).design(new JPopupMenu());
  }
}
