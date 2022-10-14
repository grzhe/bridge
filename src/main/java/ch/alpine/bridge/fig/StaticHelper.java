// code by gjoel, jph
package ch.alpine.bridge.fig;

import java.awt.image.BufferedImage;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.qty.UnitConvert;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/* package */ enum StaticHelper {
  ;

  /** @param bufferedImage
   * @param visualSet
   * @param domain
   * @param yhi with unit of domain negated
   * @return */
  public static VisualImage create(BufferedImage bufferedImage, VisualSet visualSet, Tensor domain, Scalar yhi) {
    Unit unitX = visualSet.getAxisX().getUnit();
    ScalarUnaryOperator suoX = UnitConvert.SI().to(unitX);
    Clip clipX = Clips.interval(suoX.apply(domain.Get(0)), suoX.apply(Last.of(domain)));
    // ---
    Unit unitY = visualSet.getAxisY().getUnit();
    ScalarUnaryOperator suoY = UnitConvert.SI().to(unitY);
    Clip clipY = Clips.interval(suoY.apply(yhi.zero()), suoY.apply(yhi));
    // ---
    VisualImage visualImage = new VisualImage(bufferedImage, clipX, clipY);
    visualImage.getAxisX().setLabel(visualSet.getAxisX().getLabel());
    visualImage.getAxisY().setLabel(visualSet.getAxisY().getLabel());
    return visualImage;
  }
}
