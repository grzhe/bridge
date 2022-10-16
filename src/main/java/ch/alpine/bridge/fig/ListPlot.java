// code by gjoel, jph
package ch.alpine.bridge.fig;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Optional;

import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Sign;

/** Hint:
 * to render list plot in a custom graphics use
 * jFreeChart.draw(graphics2d, rectangle2D)
 * 
 * to export to a graphics file use
 * ChartUtils.saveChartAsPNG(file, jFreeChart, width, height);
 * 
 * to embed figure as separate panel in gui use {@link ChartPanel}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/ListPlot.html">ListPlot</a> */
public class ListPlot implements Showable {
  /** Remark:
   * We would like to make joined property of VisualRow, but JFreeChart does not support
   * this granularity.
   * 
   * Tested with up to 10 million points - a little slow but possible.
   * 
   * @param visualSet
   * @param joined for lines between coordinates, otherwise scattered points
   * @return */
  public static Showable of(Show visualSet) {
    return new ListPlot(visualSet);
  }

  // ---
  private final Show visualSet;

  private ListPlot(Show visualSet) {
    this.visualSet = visualSet;
  }

  @Override
  public void draw(Graphics2D graphics, Rectangle rectangle) {
    Optional<Clip> optionalX = visualSet.getAxisX().getOptionalClip();
    if (optionalX.isEmpty())
      optionalX = visualSet.suggestClip(0);
    Optional<Clip> optionalY = visualSet.getAxisY().getOptionalClip();
    if (optionalY.isEmpty())
      optionalY = visualSet.suggestClip(1);
    if (optionalX.isPresent() && optionalY.isPresent()) {
      Clip clipX = optionalX.orElseThrow();
      Clip clipY = optionalY.orElseThrow();
      if (Sign.isPositive(clipX.width()) && // TODO handle differently by extending clip artificially!
          Sign.isPositive(clipY.width())) {
        CoordinateBoundingBox cbb = CoordinateBoundingBox.of(clipX, clipY);
        draw(graphics, rectangle, cbb);
      }
    }
  }

  @Override
  public void draw(Graphics2D graphics, Rectangle rectangle, CoordinateBoundingBox cbb) {
    GridDrawer gridDrawer = new GridDrawer(rectangle, cbb);
    gridDrawer.render(graphics);
    // ---
    Plot scalarFigure = new Plot(rectangle, cbb);
    for (VisualRow visualRow : visualSet.visualRows()) {
      scalarFigure.render(graphics, //
          visualRow.getColor(), visualRow.getStroke(), //
          visualRow.points());
    }
  }
}
