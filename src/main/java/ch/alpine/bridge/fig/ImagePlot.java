// code by jph
package ch.alpine.bridge.fig;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/ArrayPlot.html">ArrayPlot</a> */
public class ImagePlot extends BaseShowable {
  public static Showable of(BufferedImage bufferedImage, CoordinateBoundingBox cbb, boolean flipY) {
    return new ImagePlot(bufferedImage, cbb, flipY);
  }

  public static Showable of(BufferedImage bufferedImage, CoordinateBoundingBox cbb) {
    return of(bufferedImage, cbb, false);
  }

  public static Showable of(BufferedImage bufferedImage) {
    return of(bufferedImage, CoordinateBoundingBox.of( //
        StaticHelper.TRANSLATION.apply(Clips.positive(bufferedImage.getWidth())), //
        StaticHelper.TRANSLATION.apply(Clips.positive(bufferedImage.getHeight()))), //
        true);
  }

  // ---
  private final BufferedImage bufferedImage;
  private final CoordinateBoundingBox cbb;
  private final boolean flipY;

  private ImagePlot(BufferedImage bufferedImage, CoordinateBoundingBox cbb, boolean flipY) {
    this.bufferedImage = bufferedImage;
    this.cbb = cbb;
    this.flipY = flipY;
  }

  @Override // from Showable
  public void render(ShowableConfig showableConfig, Graphics2D graphics) {
    Point2D ul = showableConfig.toPoint2D(Tensors.of( //
        cbb.getClip(0).min(), //
        flipY ? cbb.getClip(1).min() : cbb.getClip(1).max()));
    Point2D dr = showableConfig.toPoint2D(Tensors.of( //
        cbb.getClip(0).max(), //
        flipY ? cbb.getClip(1).max() : cbb.getClip(1).min()));
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
  public boolean flipYAxis() {
    return flipY;
  }
}
