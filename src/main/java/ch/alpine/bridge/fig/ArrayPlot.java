// code by jph
package ch.alpine.bridge.fig;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.UnaryOperator;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Rescale;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/ArrayPlot.html">ArrayPlot</a> */
public class ArrayPlot extends BarLegendPlot {
  private static final UnaryOperator<Clip> TRANSLATION = Clips.translation(RationalScalar.HALF.negate());

  public static Showable of(Tensor matrix, CoordinateBoundingBox cbb, ScalarTensorFunction colorDataGradient) {
    return new ArrayPlot(matrix, cbb, colorDataGradient);
  }

  /** @param matrix
   * @param colorDataGradient
   * @return */
  public static Showable of(Tensor matrix, ScalarTensorFunction colorDataGradient) {
    return of(matrix, CoordinateBoundingBox.of( //
        TRANSLATION.apply(Clips.positive(Unprotect.dimension1(matrix))), //
        TRANSLATION.apply(Clips.positive(matrix.length()))), //
        colorDataGradient);
  }

  // ---
  private final BufferedImage bufferedImage;
  private final CoordinateBoundingBox cbb;
  private final Clip clip;

  private ArrayPlot( //
      Tensor matrix, //
      CoordinateBoundingBox cbb, //
      ScalarTensorFunction colorDataGradient) {
    super(colorDataGradient);
    MatrixQ.require(matrix);
    Rescale rescale = new Rescale(matrix);
    this.bufferedImage = ImageFormat.of(rescale.result().map(colorDataGradient));
    this.cbb = cbb;
    this.clip = rescale.clip();
  }

  @Override // from Showable
  public void render(ShowableConfig showableConfig, Graphics2D graphics) {
    Point2D.Double ul = showableConfig.toPoint2D(Tensors.of( //
        cbb.getClip(0).min(), //
        cbb.getClip(1).max()));
    Point2D.Double dr = showableConfig.toPoint2D(Tensors.of( //
        cbb.getClip(0).max(), //
        cbb.getClip(1).min()));
    int width = (int) Math.floor(dr.getX() - ul.getX()) + 1;
    int height = (int) Math.floor(dr.getY() - ul.getY()) + 1;
    if (0 < width && 0 < height)
      graphics.drawImage(bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), //
          (int) ul.getX(), //
          (int) ul.getY(), null);
  }

  @Override // from Showable
  public Optional<CoordinateBoundingBox> fullPlotRange() {
    return Optional.of(cbb);
  }

  @Override
  protected Clip clip() {
    return clip;
  }
}
