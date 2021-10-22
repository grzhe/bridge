// code by jph
package ch.alpine.java.ref;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorFormat;

/* package */ class ColorPanel extends StringPanel {
  private final JButton jButton = new JButton("?");

  public ColorPanel(FieldWrap fieldWrap, Object object) {
    super(fieldWrap, object);
    jButton.setBackground(getColor());
    jButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Color color = JColorChooser.showDialog(jButton, "pick color", getColor());
        if (Objects.nonNull(color)) {
          jButton.setBackground(color);
          String string = fieldWrap.toString(color);
          jTextField.setText(string);
          notifyListeners(string);
        }
      }
    });
    jTextField.setEditable(false);
  }

  private Color getColor() {
    try {
      return ColorFormat.toColor(Tensors.fromString(jTextField.getText()));
    } catch (Exception exception) {
      // ---
    }
    return Color.WHITE;
  }

  @Override // from FieldPanel
  public JComponent getJComponent() {
    JPanel jPanel = new JPanel(new BorderLayout());
    jPanel.add(BorderLayout.CENTER, jTextField);
    jPanel.add(BorderLayout.EAST, jButton);
    return jPanel;
  }

  @Override // from FieldPanel
  public void updateJComponent(Object value) {
    Color color = (Color) value;
    String string = fieldWrap().toString(color);
    jTextField.setText(string);
    jButton.setBackground(color);
  }
}
