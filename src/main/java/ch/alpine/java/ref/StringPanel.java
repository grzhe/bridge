// code by jph
package ch.alpine.java.ref;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.undo.UndoManager;

import ch.alpine.java.ref.gui.FieldPanel;

/* package */ class StringPanel extends FieldPanel {
  private static final Color LABEL = new Color(51, 51, 51);
  private static final int MASK = KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK;
  private static final int UNDO = KeyEvent.CTRL_DOWN_MASK;
  private static final int REDO = MASK;
  // ---
  protected final JTextField jTextField;
  private String fallbackValue = null;

  public StringPanel(FieldWrap fieldWrap, Object value) {
    super(fieldWrap);
    jTextField = Objects.isNull(value) //
        ? new JTextField()
        : new JTextField(fallbackValue = fieldWrap.toString(value));
    UndoManager undoManager = new UndoManager();
    jTextField.getDocument().addUndoableEditListener(undoManager);
    jTextField.setFont(FieldPanel.FONT);
    jTextField.setForeground(LABEL);
    jTextField.addActionListener(l -> nofifyIfValid(jTextField.getText()));
    jTextField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
        case KeyEvent.VK_ESCAPE: {
          if (Objects.nonNull(fallbackValue))
            jTextField.setText(fallbackValue);
          break;
        }
        case KeyEvent.VK_Z: {
          int modifiers = keyEvent.getModifiersEx();
          if ((modifiers & MASK) == UNDO && undoManager.canUndo())
            try {
              undoManager.undo();
            } catch (Exception exception) {
              exception.printStackTrace();
            }
          if ((modifiers & MASK) == REDO && undoManager.canRedo())
            try {
              undoManager.redo();
            } catch (Exception exception) {
              exception.printStackTrace();
            }
          break;
        }
        default:
          break;
        }
      }

      @Override
      public void keyReleased(KeyEvent keyEvent) {
        indicateGui();
      }
    });
    jTextField.addFocusListener(new FocusListener() {
      private String _value;

      @Override
      public void focusGained(FocusEvent focusEvent) {
        _value = jTextField.getText();
      }

      @Override
      public void focusLost(FocusEvent focusEvent) {
        String string = jTextField.getText();
        if (!string.equals(_value))
          nofifyIfValid(_value = string);
      }
    });
    indicateGui();
    addListener(string -> fallbackValue = string);
  }

  @Override
  public JComponent getJComponent() {
    return jTextField;
  }

  protected void nofifyIfValid(String string) {
    if (isValid(string))
      notifyListeners(string);
  }

  private boolean isValid(String string) {
    Object object = fieldWrap().toValue(string);
    return Objects.nonNull(object) //
        && fieldWrap().isValidValue(object);
  }

  protected void indicateGui() {
    boolean isOk = isValid(jTextField.getText());
    jTextField.setBackground(isOk //
        ? Color.WHITE
        : FAIL);
  }
}
