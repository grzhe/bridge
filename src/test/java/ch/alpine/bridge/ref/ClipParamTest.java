// code by jph
package ch.alpine.bridge.ref;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clips;

class ClipParamTest {
  @Test
  void testSimple() {
    ClipParam clipParam = new ClipParam();
    List<String> list = ObjectProperties.list(clipParam);
    assertTrue(list.contains("clipReal={2, 3}"));
    assertTrue(list.contains("clipMeter={-1[m], 1[m]}"));
  }

  @Test
  void testModify() {
    ClipParam clipParam = new ClipParam();
    clipParam.clipReal = Clips.interval(10, 11);
    List<String> list = ObjectProperties.list(clipParam);
    assertTrue(list.contains("clipReal={10, 11}"));
    assertTrue(list.contains("clipMeter={-1[m], 1[m]}"));
  }

  @Test
  void testRecreate() {
    ClipParam clipParam = new ClipParam();
    clipParam.clipReal = Clips.interval(20, 21);
    Properties properties = DeprecatedObjProp.properties(clipParam);
    properties.put("clipMeter", "{9[m], 10[m]}");
    ClipParam clipParam2 = ObjectProperties.set(new ClipParam(), properties);
    assertEquals(clipParam2.clipReal, Clips.interval(20, 21));
    assertEquals(clipParam2.clipMeter, Clips.interval(Quantity.of(9, "m"), Quantity.of(10, "m")));
  }
}
