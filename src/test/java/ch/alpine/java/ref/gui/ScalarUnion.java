// code by jph
package ch.alpine.java.ref.gui;

import ch.alpine.java.ref.ann.FieldClip;
import ch.alpine.java.ref.ann.FieldInteger;
import ch.alpine.java.ref.ann.FieldSelection;
import ch.alpine.java.ref.ann.FieldSlider;
import ch.alpine.java.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.qty.Quantity;

@ReflectionMarker
public class ScalarUnion {
  @FieldSlider
  @FieldClip(min = "1[m*s^-1]", max = "10[m*s^-1]")
  public Scalar scalar = Quantity.of(3, "m*s^-1");
  @FieldSlider
  @FieldClip(min = "0[%]", max = "100[%]")
  public Scalar percent = Quantity.of(10, "%");
  @FieldSlider
  @FieldInteger
  @FieldClip(min = "1", max = "10")
  public Scalar scalarInt = Quantity.of(10, "");
  public Scalar scalar1 = Quantity.of(3, "m*s^-1");
  public Scalar scalar2 = Quantity.of(3, "m*s^-1");
  @FieldClip(min = "1000[W]", max = "10000[W]")
  public Scalar quantity = Quantity.of(3, "kW");
  // @FieldSubdivide(start = "-4[m*s^-1]", end = "10[m*s^-1]", intervals = 7)
  // @FieldToolTip(text = "asd")
  @FieldClip(min = "0", max = "20")
  @FieldSelection(array = { "1[W]", "2[%]", "3[]" })
  public Scalar subdiv = Quantity.of(3, "");
  @FieldInteger
  @FieldClip(min = "10", max = "20")
  public Scalar integer = RealScalar.of(12);
}
