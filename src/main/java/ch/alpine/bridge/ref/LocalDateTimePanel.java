// code by jph
package ch.alpine.bridge.ref;

import java.awt.Component;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.swing.JDialog;

import ch.alpine.bridge.swing.LocalDateTimeDialog;

/* package */ class LocalDateTimePanel extends DialogPanel {
  public LocalDateTimePanel(FieldWrap fieldWrap, LocalDateTime localDateTime) {
    super(fieldWrap, localDateTime);
  }

  @Override // from DialogPanel
  protected JDialog createDialog(Component component, Object value) {
    LocalDateTime fallback = Objects.isNull(value) ? LocalDateTime.now() : (LocalDateTime) value;
    return new LocalDateTimeDialog(component, fallback, this::updateAndNotify);
  }
}
