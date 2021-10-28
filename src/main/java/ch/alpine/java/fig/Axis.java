// code by jph
package ch.alpine.java.fig;

import java.util.Objects;
import java.util.Optional;

import ch.alpine.java.lang.PrettyUnit;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.qty.UnitConvert;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Axis.html">Axis</a> */
public class Axis {
  private String label = "";
  private Unit unit = null;
  private Clip clip = null;

  /** @param label of axis */
  public void setLabel(String string) {
    label = string;
  }

  /** @return label of axis */
  public String getLabel() {
    return label;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public Unit getUnit() {
    return unit;
  }

  // ---
  public void setClip(Clip clip) {
    this.clip = clip;
    if (Objects.isNull(unit) && Objects.nonNull(clip))
      setUnit(QuantityUnit.of(clip));
  }

  public Optional<Clip> getClip() {
    return Objects.isNull(clip) //
        ? Optional.empty()
        : Optional.of(slash(clip, UnitConvert.SI().to(unit)));
  }

  /** @param clip
   * @param monotonousOperator
   * @return Clip[monotonousOperator[clip.min], monotonousOperator[clip.max]] */
  @PackageTestAccess
  static Clip slash(Clip clip, ScalarUnaryOperator monotonousOperator) {
    return Clips.interval( //
        monotonousOperator.apply(clip.min()), //
        monotonousOperator.apply(clip.max()));
  }

  // ---
  /** @return label combined with unit */
  /* package */ String getAxisLabel() {
    return (getLabel() + " " + getUnitString()).strip();
  }

  /* package */ boolean hasUnit() {
    return Objects.nonNull(unit);
  }

  /** <p>inspired by
   * <a href="https://reference.wolfram.com/language/ref/Reals.html">Reals</a>
   * 
   * @return operator that maps a scalar value to instance of {@link RealScalar}
   * so that {@link Scalar#number()} can be invoked. */
  /* package */ ScalarUnaryOperator toReals() {
    return QuantityMagnitude.SI().in(getAxisUnit());
  }

  /* package */ String getUnitString() {
    Unit unit = getAxisUnit();
    return Unit.ONE.equals(unit) ? "" : '[' + PrettyUnit.of(unit) + ']';
  }

  private Unit getAxisUnit() {
    return Objects.isNull(unit) ? Unit.ONE : unit;
  }
}
