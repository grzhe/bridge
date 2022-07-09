// code by jph
package ch.alpine.bridge.ref.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import ch.alpine.bridge.ref.util.FieldOuterParam.NestedParam;
import ch.alpine.tensor.Scalar;

class RandomFieldsAssignmentTest {
  @Test
  void test() {
    Set<Scalar> set = new HashSet<>();
    FieldOuterParam fieldOuterParam = new FieldOuterParam();
    FieldsAssignment fieldsAssignment = RandomFieldsAssignment.of(fieldOuterParam, () -> {
      set.add(fieldOuterParam.ratio);
    });
    assertEquals(set.size(), 0);
    fieldsAssignment.randomize(300);
    assertTrue(100 < set.size());
  }

  @Test
  void testGrid() {
    AtomicInteger atomicInteger = new AtomicInteger();
    NestedParam nestedParam = new NestedParam();
    FieldsAssignment fieldsAssignment = RandomFieldsAssignment.of(nestedParam, () -> {
      atomicInteger.getAndIncrement();
    });
    assertEquals(atomicInteger.get(), 0);
    fieldsAssignment.randomize(2);
    assertEquals(atomicInteger.get(), 2);
    fieldsAssignment.randomize(10);
    assertEquals(atomicInteger.get(), 2 + 4);
  }

  @Test
  void testColor() {
    Set<Color> set_color = new HashSet<>();
    Set<LocalTime> set_time = new HashSet<>();
    ColorParam colorParam = new ColorParam();
    FieldsAssignment fieldsAssignment = RandomFieldsAssignment.of(colorParam, () -> {
      set_color.add(colorParam.color);
      set_time.add(colorParam.localTime);
    });
    assertEquals(set_color.size(), 0);
    assertEquals(set_time.size(), 0);
    int n = 20;
    Random random = new Random(3);
    fieldsAssignment.randomize(random, n);
    assertEquals(set_color.size(), n);
    assertEquals(set_time.size(), n);
    random = new Random(3);
    fieldsAssignment.randomize(random, n);
    assertEquals(set_color.size(), n);
    assertEquals(set_time.size(), n);
  }
}
