// code by jph
package ch.ethz.idsc.tensor.ref;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ethz.idsc.tensor.ref.gui.FieldPanel;

public class EnumFieldWrap extends BaseFieldWrap {
  private final Object[] enumConstants;

  public EnumFieldWrap(Field field) {
    super(field);
    enumConstants = Objects.requireNonNull(field.getType().getEnumConstants());
  }

  @Override // from FieldWrap
  public Object toValue(String string) {
    return Stream.of(enumConstants) //
        .map(Enum.class::cast) //
        .filter(object -> object.name().equals(string)) //
        .findFirst() //
        .orElse(null);
  }

  @Override // from FieldWrap
  public String toString(Object object) {
    return ((Enum<?>) object).name();
  }

  @Override
  public FieldPanel createFieldPanel(Object value) {
    return new EnumPanel(this, enumConstants, value);
  }
}
