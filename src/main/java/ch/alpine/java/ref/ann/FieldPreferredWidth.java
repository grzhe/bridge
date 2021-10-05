// code by jph
package ch.alpine.java.ref.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.alpine.java.ref.gui.FieldsEditor;

/** {@link FieldsEditor} may or may not use this layout hint */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldPreferredWidth {
  int width();
}
