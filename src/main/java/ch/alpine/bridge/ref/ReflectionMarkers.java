// code by jph
package ch.alpine.bridge.ref;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ch.alpine.bridge.ref.ann.ReflectionMarker;

public enum ReflectionMarkers {
  INSTANCE;

  private final Set<Class<?>> checked = new HashSet<>();
  private final Set<Class<?>> missing = new HashSet<>();
  private final ObjectFieldVisitor objectFieldVisitor = new ObjectFieldAll() {
    @Override
    public void accept(String key, FieldWrap fieldWrap, Object object, Object value) {
      expected(fieldWrap.getField().getDeclaringClass());
    }
  };

  private ReflectionMarkers() {
    checked.add(Object.class);
  }

  /** function checks if class of given object has been analyzed
   * for the presence of {@link ReflectionMarker} annotation.
   * the annotation is also expected on all nested classes, and
   * super types.
   * 
   * @param object non-null */
  public void register(Object object) {
    Class<? extends Object> cls = object.getClass();
    if (!checked.contains(cls))
      synchronized (INSTANCE) {
        ClassHierarchy.of(cls).forEach(this::expected);
        ObjectFields.of(object, objectFieldVisitor);
      }
  }

  private void expected(Class<?> cls) {
    if (!checked.contains(cls)) {
      checked.add(cls);
      ReflectionMarker reflectionMarker = cls.getAnnotation(ReflectionMarker.class);
      if (Objects.isNull(reflectionMarker) && missing.add(cls))
        System.err.println("hint: use @ReflectionMarker on " + cls);
    }
  }

  public Set<Class<?>> missing() {
    return Collections.unmodifiableSet(missing);
  }
}
