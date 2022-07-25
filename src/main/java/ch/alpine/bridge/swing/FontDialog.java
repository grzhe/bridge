// code by jph
package ch.alpine.bridge.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import ch.alpine.bridge.awt.RenderQuality;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldInteger;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.bridge.ref.util.PanelFieldsEditor;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

public class FontDialog extends JDialog {
  private static final String DEMO = "Abc123!";

  @ReflectionMarker
  public static class FontParam {
    @FieldSelectionCallback("names")
    public String name; // "Dialog"
    public FontStyle style; // PLAIN
    @FieldInteger
    @FieldClip(min = "0", max = "Infinity")
    @FieldSelectionArray({ "12", "14", "16", "18", "20", "22", "25" })
    public Scalar size; // 12

    /** @param font */
    public FontParam(Font font) {
      name = font.getName();
      style = FontStyle.values()[font.getStyle()];
      size = RealScalar.of(font.getSize());
    }

    @ReflectionMarker
    public static List<String> names() {
      GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
      return Arrays.stream(graphicsEnvironment.getAvailableFontFamilyNames()).collect(Collectors.toList());
    }

    /** Experimentation has shown that a font name unknown to the graphics environment
     * will cause a fallback to a known font
     * 
     * @return font as specified by this instance */
    public Font toFont() {
      return new Font(name, style.ordinal(), size.number().intValue());
    }
  }

  // ---
  private final JComponent jComponent = new JComponent() {
    @Override
    protected void paintComponent(Graphics _g) {
      Dimension dimension = getSize();
      Point point = new Point(dimension.width / 2, dimension.height / 2);
      Graphics2D graphics = (Graphics2D) _g;
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, dimension.width, dimension.height);
      RenderQuality.setQuality(graphics);
      graphics.setFont(fontParam.toFont());
      FontMetrics fontMetrics = graphics.getFontMetrics();
      int ascent = fontMetrics.getAscent();
      int stringWidth = fontMetrics.stringWidth(DEMO);
      graphics.setColor(Color.DARK_GRAY);
      graphics.drawString(DEMO, point.x - stringWidth / 2, point.y + ascent / 2);
    }
  };
  private final FontParam fontParam;

  public FontDialog(Component component, final Font font_fallback, Consumer<Font> consumer) {
    super(JOptionPane.getFrameForComponent(component));
    setTitle("Font selection");
    fontParam = new FontParam(font_fallback);
    // ---
    JPanel jPanel = new JPanel(new BorderLayout());
    jComponent.setPreferredSize(new Dimension(200, 60));
    jPanel.add(jComponent, BorderLayout.NORTH);
    // ---
    {
      PanelFieldsEditor panelFieldsEditor = new PanelFieldsEditor(fontParam);
      panelFieldsEditor.addUniversalListener( //
          () -> {
            jComponent.repaint();
            consumer.accept(fontParam.toFont());
          });
      jPanel.add(panelFieldsEditor.getJPanel(), BorderLayout.CENTER);
    }
    jPanel.add(new JLabel("\u3000"), BorderLayout.WEST);
    jPanel.add(new JLabel("\u3000"), BorderLayout.EAST);
    {
      JToolBar jToolBar = new JToolBar();
      jToolBar.setFloatable(false);
      jToolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
      {
        JButton jButton = new JButton("Done");
        jButton.addActionListener(actionEvent -> {
          dispose();
          consumer.accept(fontParam.toFont());
        });
        jToolBar.add(jButton);
      }
      jToolBar.addSeparator();
      {
        JButton jButton = new JButton("Cancel");
        jButton.addActionListener(actionEvent -> {
          consumer.accept(font_fallback);
          dispose();
        });
        jToolBar.add(jButton);
      }
      jPanel.add(jToolBar, BorderLayout.SOUTH);
    }
    StaticHelper.configure(this, jPanel);
    addWindowListener(new WindowAdapter() {
      /** function is called when [x] is pressed by user */
      @Override
      public void windowClosing(WindowEvent windowEvent) {
        // propagate fallback value
        consumer.accept(font_fallback);
      }
    });
  }
}
