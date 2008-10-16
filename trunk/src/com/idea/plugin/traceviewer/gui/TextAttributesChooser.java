package com.idea.plugin.traceviewer.gui;

import com.intellij.openapi.editor.markup.TextAttributes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 21 juin 2007
 * Time: 16:19:13
 * To change this template use File | Settings | File Templates.
 */
public class TextAttributesChooser extends JDialog implements ActionListener {
  protected JComponent userPane;
  private JButton okButton;
  private JButton cancelButton;
  private String okLabel = "OK";
  private String cancelLabel = "CANCEL";
  private ButtonColorChooser fgColorChooser;
  private ButtonColorChooser bgColorChooser;
  private JComboBox fontChooser;
  private TextAttributes textAttributes;

  public TextAttributesChooser(TextAttributes textAttributes) {
    super();
    if (textAttributes == null) {
      textAttributes = new TextAttributes();
    }
    this.textAttributes = textAttributes;

  }

  public void showDialog() throws Exception {
    setTitle("TextAttributes Chooser");
    setModal(true);
    setLocationRelativeTo(null);
    setUserPane();
    setButtonPane();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setVisible(true);
  }

  private void setButtonPane() {
    final JPanel buttonPanel = new JPanel();
    okButton = new JButton(okLabel);
    okButton.addActionListener(this);
    buttonPanel.add(okButton, BorderLayout.WEST);
    cancelButton = new JButton(cancelLabel);
    cancelButton.addActionListener(this);
    buttonPanel.add(cancelButton, BorderLayout.EAST);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e) {
    if (okButton == e.getSource()) {
      try {
        proceed();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    setVisible(false);
    dispose();
  }

  private void proceed() {
    textAttributes.setBackgroundColor(bgColorChooser.getSelectedColor());
    textAttributes.setForegroundColor(fgColorChooser.getSelectedColor());
    if (fontChooser.getSelectedItem().equals("Bold")) {
      textAttributes.setFontType(Font.BOLD);
      return;
    }
    if (fontChooser.getSelectedItem().equals("Italic")) {
      textAttributes.setFontType(Font.ITALIC);
      return;
    }
    textAttributes.setFontType(Font.PLAIN);
  }

  protected void setUserPane() throws Exception {
    userPane = new JPanel(new GridBagLayout());
    MyGridBagConstraints bc = new MyGridBagConstraints();

    userPane.add(new JLabel("Foreground color"), bc.addLine(1,1));
    fgColorChooser = new ButtonColorChooser("foreground", textAttributes.getForegroundColor());
    userPane.add(fgColorChooser, bc.addCol(1));

    userPane.add(new JLabel("Background color"), bc.addLine(1,1));
    bgColorChooser = new ButtonColorChooser("background", textAttributes.getBackgroundColor());
    userPane.add(bgColorChooser, bc.addCol(1));

    fontChooser = new JComboBox(new String[]{"Plain", "Bold", "Italic"});
    fontChooser.setSelectedItem(getFontFromTextAtrributes());
    userPane.add(fontChooser, bc.addLine(2,1));

    getContentPane().add(userPane, BorderLayout.CENTER);
  }


  class MyGridBagConstraints extends GridBagConstraints {


    public MyGridBagConstraints() {
      this.gridx = 0;
      this.gridy = 0;
      this.gridwidth = 0;
      this.gridheight = 0;
      this.fill = 1;
      this.ipady = 4;
    }

    public GridBagConstraints get(int gx, int gy, int gw, int gh) {
      this.gridx = gx;
      this.gridy = gy;
      this.gridwidth = gw;
      this.gridheight = gh;
      this.fill = 1;
      return this;
    }

    public GridBagConstraints addLine(int gw, int gh) {
      return get(0, gridy + gridheight, gw, gh);
    }

    public GridBagConstraints addCol(int gw) {
      return get(gridx + gridwidth, gridy, gw, gridheight);
    }
  }

  private String getFontFromTextAtrributes() {
    if (textAttributes.getFontType() == Font.BOLD) {
      return "Bold";
    }
    if (textAttributes.getFontType() == Font.ITALIC) {
      return "Italic";
    }
    return "Plain";
  }

  public TextAttributes getTextAttributes() {
    return textAttributes;
  }

  public class ButtonColorChooser extends JButton implements ActionListener {
    private String name;


    public ButtonColorChooser(String name, Color color) {
      this.name = name;
      setBackground(color);
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
      Color color = JColorChooser.showDialog(this, name, getBackground());
      setBackground(color);
    }

    public Color getSelectedColor() {
      return getBackground();
    }
  }
}



