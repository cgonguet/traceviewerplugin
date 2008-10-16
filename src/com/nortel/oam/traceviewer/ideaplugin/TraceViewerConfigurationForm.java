package com.nortel.oam.traceviewer.ideaplugin;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.ui.Messages;
import com.nortel.oam.traceviewer.ideaplugin.gui.TextAttributesChooser;
import com.nortel.oam.traceviewer.ideaplugin.core.TraceStyle;
import com.nortel.oam.traceviewer.ideaplugin.TraceViewerComponent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cgonguet
 * Date: 11 juin 2007
 * Time: 15:49:53
 * To change this template use File | Settings | File Templates.
 */
public class TraceViewerConfigurationForm {
  private JTextField reposDirField;
  private JTextField defaultFileField;
  private JButton reposDirChooseButton;
  private JPanel mainPaneField;
  private JTabbedPane tabbedPane1;
  private JTextField traceFormatField;
  private JButton addButton;
  private JButton deleteButton;
  private JTable userStylesTable;
  private JButton moveUpButton;
  private JButton moveDownButton;
  private JCheckBox printByBlockCheckBox;
  private JPanel buttonsPane;
  protected TraceStylesTableModel model = new TraceStylesTableModel();

  public void setData(TraceViewerComponent data) {
    reposDirField.setText(data.getReposDir());
    defaultFileField.setText(data.getDefaultFile());
    traceFormatField.setText(data.getTraceFormat());
    printByBlockCheckBox.setSelected(data.getPrintByBlock());
    model.reset();
    for (TraceStyle traceStyle : data.getTraceStyles()) {
      if (traceStyle.regexp != null && !traceStyle.regexp.equals("")) {
        model.addTraceStyle(traceStyle);
      }
    }
  }

  public void getData(TraceViewerComponent data) {
    data.setReposDir(reposDirField.getText());
    data.setDefaultFile(defaultFileField.getText());
    data.setTraceFormat(traceFormatField.getText());
    data.setPrintByBlock(printByBlockCheckBox.isSelected());
    data.setTraceStyles(model.getTraceStyles());
  }

  public boolean isModified(TraceViewerComponent data) {
    if (reposDirField.getText() != null ? !reposDirField.getText().equals(data.getReposDir()) :
        data.getReposDir() != null) {
      return true;
    }
    if (defaultFileField.getText() != null ? !defaultFileField.getText().equals(data.getDefaultFile()) :
        data.getDefaultFile() != null) {
      return true;
    }
    if (traceFormatField.getText() != null ? !traceFormatField.getText().equals(data.getTraceFormat()) :
        data.getTraceFormat() != null) {
      return true;
    }

    if(printByBlockCheckBox.isSelected() != data.getPrintByBlock()){
      return true;
    }

    if (model.getTraceStyles() != null) {
      if (data.getTraceStyles() != null) {
        if (model.getTraceStyles().size() != data.getTraceStyles().size()) {
          return true;
        }
        Iterator<TraceStyle> dataTraceStylesIter = data.getTraceStyles().iterator();

        for (TraceStyle style : model.getTraceStyles()) {
          if (!style.equals(dataTraceStylesIter.next())) {
            return true;
          }
        }
      } else {
        return true;
      }
    } else {
      return data.getTraceStyles() == null;
    }
    return false;
  }

  public JComponent getRootComponent() {
    reposDirChooseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser choose = new JFileChooser(reposDirField.getText());
        choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (choose.showOpenDialog(mainPaneField) == JFileChooser.APPROVE_OPTION) {
          reposDirField.setText(choose.getSelectedFile().toString());
        }
      }
    });

    userStylesTable.setModel(model);
    userStylesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    TableColumn regexpCol =
            userStylesTable.getColumnModel().getColumn(TraceStylesTableModel.ColumnHeader.Regexp.ordinal());
    regexpCol.setMinWidth(100);
    regexpCol.setPreferredWidth(300);

    TableColumn styleCol =
            userStylesTable.getColumnModel().getColumn(TraceStylesTableModel.ColumnHeader.Style.ordinal());
    styleCol.setMaxWidth(150);
    styleCol.setMinWidth(25);
    styleCol.setPreferredWidth(150);
    styleCol.setCellRenderer(new TableCellRenderer() {

      public Component getTableCellRendererComponent(final JTable jTable, Object object, boolean b, boolean b1,
                                                     final int row,
                                                     final int col) {
        final JButton label = new JButton("This is an example");

        TextAttributes textAttributes = model.getTraceStyle(row).style;
        label.setForeground(textAttributes.getForegroundColor());
        label.setBackground(textAttributes.getBackgroundColor());
        label.setFont(new Font("Serif", textAttributes.getFontType(), 12));

        return label;
      }
    });

    TableColumn resumeCol =
            userStylesTable.getColumnModel().getColumn(TraceStylesTableModel.ColumnHeader.Resume.ordinal());
    resumeCol.setMaxWidth(50);
    resumeCol.setMinWidth(20);
    resumeCol.setPreferredWidth(50);

    TableColumn filterCol =
            userStylesTable.getColumnModel().getColumn(TraceStylesTableModel.ColumnHeader.Filter.ordinal());
    filterCol.setMaxWidth(50);
    filterCol.setMinWidth(20);
    filterCol.setPreferredWidth(50);

    userStylesTable.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        int row = userStylesTable.getSelectedRow();
        if (e.getClickCount() == 2
            && userStylesTable.getSelectedColumn() == TraceStylesTableModel.ColumnHeader.Style.ordinal()) {
          TraceStyle traceStyle = model.getTraceStyle(row);
          if (traceStyle != null) {
            TextAttributes textAttributes = traceStyle.style;
            TextAttributesChooser chooser = new TextAttributesChooser(textAttributes);
            try {
              chooser.showDialog();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
            textAttributes = chooser.getTextAttributes();
            model.updateTraceStyle(row, textAttributes);
          }
        }
      }
    });

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        model.addTraceStyle();
      }
    });
    moveUpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int row = userStylesTable.getSelectedRow();
        if (row == -1) {
          Messages.showErrorDialog("Select the row to move", "Error");
          return;
        }
        int newRow = model.moveUpTraceStyle(row);
        userStylesTable.setRowSelectionInterval(newRow, newRow);
      }
    });
    moveDownButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int row = userStylesTable.getSelectedRow();
        if (row == -1) {
          Messages.showErrorDialog("Select the row to move", "Error");
          return;
        }
        int newRow = model.moveDownTraceStyle(row);
        userStylesTable.setRowSelectionInterval(newRow, newRow);
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        int row = userStylesTable.getSelectedRow();
        if (row == -1) {
          Messages.showErrorDialog("Select the row to delete", "Error");
          return;
        }
        model.removeTraceStyle(row);
      }
    });

    return mainPaneField;
  }


  public static class TraceStylesTableModel extends AbstractTableModel {

    List<TraceStyle> traceStyles;

    enum ColumnHeader {
      Regexp,
      Style,
      Resume,
      Filter
    }

    public TraceStylesTableModel() {
      this.traceStyles = new ArrayList<TraceStyle>();
    }

    public int getRowCount() {
      return this.traceStyles.size();
    }

    public int getColumnCount() {
      return ColumnHeader.values().length;
    }

    public String getColumnName(int column) {
      return ColumnHeader.values()[column].toString();
    }

    public Class<?> getColumnClass(int col) {
      switch (ColumnHeader.values()[col]) {
        case Regexp:
          return String.class;
        case Style:
          return TextAttributes.class;
        case Resume:
          return Boolean.class;
        case Filter:
          return Boolean.class;
      }
      return Object.class;
    }

    public Object getValueAt(int row, int col) {
      TraceStyle traceStyle = traceStyles.get(row);
      if (traceStyle != null) {
        switch (ColumnHeader.values()[col]) {
          case Regexp:
            return traceStyle.regexp;
          case Style:
            return traceStyle.style;
          case Resume:
            return (Boolean) traceStyle.resume;
          case Filter:
            return (Boolean) traceStyle.filter;
        }
      }
      return null;
    }

    public void setValueAt(Object value, int row, int col) {
      TraceStyle traceStyle = this.traceStyles.get(row);
      if (traceStyle == null) {
        return;
      }
      switch (ColumnHeader.values()[col]) {
        case Regexp:
          traceStyle.regexp = (String) value;
          return;
        case Style:
          return;
        case Resume:
          traceStyle.resume = (Boolean) value;
          return;
        case Filter:
          traceStyle.filter = (Boolean) value;
          return;
      }
    }

    public boolean isCellEditable(int row, int col) {
      switch (ColumnHeader.values()[col]) {
        case Regexp:
          return true;
        case Style:
          return false;
        case Resume:
          return true;
        case Filter:
          return true;
      }
      return false;
    }

    public void reset() {
      traceStyles.clear();
    }

    public void addTraceStyle(TraceStyle traceStyle) {
      this.traceStyles.add(traceStyle.clone());
      fireTableDataChanged();
    }

    public void addTraceStyle() {
      addTraceStyle(new TraceStyle());
    }

    public void removeTraceStyle(int row) {
      this.traceStyles.remove(row);
      fireTableDataChanged();
    }

    public int moveUpTraceStyle(int row) {
      if (row == 0) {
        return row;
      }
      TraceStyle moved = this.traceStyles.remove(row);
      this.traceStyles.add(row - 1, moved);
      fireTableDataChanged();
      return row - 1;
    }

    public int moveDownTraceStyle(int row) {
      if (row == this.traceStyles.size() - 1) {
        return row;
      }
      TraceStyle moved = this.traceStyles.remove(row);
      this.traceStyles.add(row + 1, moved);
      fireTableDataChanged();
      return row + 1;
    }

    public void updateTraceStyle(int row, TextAttributes style) {
      TraceStyle traceStyle = this.traceStyles.get(row);
      if (traceStyle != null) {
        traceStyle.style = style;
      }
      fireTableDataChanged();
    }

    public TraceStyle getTraceStyle(int row) {
      return this.traceStyles.get(row);
    }

    public List<TraceStyle> getTraceStyles() {
      return this.traceStyles;
    }
  }


}
