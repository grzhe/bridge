// code by jph
package ch.alpine.bridge.swing;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldInteger;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

@ReflectionMarker
public class LocalDateParam {
  @FieldInteger
  @FieldClip(min = "1900", max = "3000")
  public Scalar year;
  // ---
  public Month month;
  // ---
  @FieldClip(min = "1", max = "31")
  @FieldSelectionCallback("days")
  public Scalar day;

  public LocalDateParam(LocalDate localDate) {
    year = RealScalar.of(localDate.getYear());
    month = localDate.getMonth();
    day = RealScalar.of(localDate.getDayOfMonth());
  }

  public LocalDate toLocalDate() {
    return LocalDate.of( //
        year.number().intValue(), //
        month, //
        Math.min(day.number().intValue(), maxDays()));//
  }

  @ReflectionMarker
  public List<Scalar> days() {
    return IntStream.rangeClosed(1, maxDays()).mapToObj(RealScalar::of).collect(Collectors.toList());
  }

  /** @return maximum number of days in given month and year */
  private int maxDays() {
    return month.length(LocalDate.of(year.number().intValue(), 1, 1).isLeapYear());
  }

  @Override
  public String toString() {
    return toLocalDate().toString();
  }
}
