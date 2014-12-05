/*
   Copyright 2014 Uproot Labs India Pvt Ltd

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package co.uproot.htabs.demo.components;

import static co.uproot.htabs.demo.HTabsDemoApp.COLORS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import co.uproot.htabs.tabmanager.TabManager;
import co.uproot.htabs.tabmanager.TabManager.Tab;

public class DemoTabContentPane extends JPanel {

  final private TabManager tabManager;

  public DemoTabContentPane(final String tabTitle, final int colorIndex, final TabManager tabManager) {
    setLayout(new GridBagLayout());
    this.tabManager = tabManager;

    final GridBagConstraints c = new GridBagConstraints();

    c.weightx = 0.1;
    c.insets = new Insets(5, 100, 5, 100);

    final JLabel contentPaneTitle = new JLabel("<html><center>Content of tab<p><big><b>" + tabTitle + "</b></big></p></center></html>");
    c.gridx = 2;
    c.gridy = 1;
    c.gridwidth = 1;
    add(contentPaneTitle, c);
    c.fill = GridBagConstraints.HORIZONTAL;

    final JPanel tabTitleEditorPanel = new JPanel();
    tabTitleEditorPanel.setLayout(new BorderLayout());
    tabTitleEditorPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black), "Title"));
    final JLabel title = new JLabel("Edit title");
    final JTextField tabTitleEditor = new JTextField(tabTitle);
    tabTitleEditor.getDocument().addDocumentListener(new DocumentListener() {

      private void updateTitle() {
        String updatedTitle = tabTitleEditor.getText();
        if (updatedTitle.length() == 0) {
          updatedTitle = "Default Title";
        }
        changeTabTitle(getReferenceCustomTabComponent(tabManager), updatedTitle);
        contentPaneTitle.setText("<html><center>Content of tab<p><big><b>" + updatedTitle + "</b></big></p></center></html>");
      }

      @Override
      public void removeUpdate(final DocumentEvent e) {
        updateTitle();
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        updateTitle();
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {
        updateTitle();
      }
    });
    tabTitleEditorPanel.add(title, BorderLayout.PAGE_START);
    tabTitleEditorPanel.add(tabTitleEditor, BorderLayout.CENTER);
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 3;
    add(tabTitleEditorPanel, c);

    final JPanel tabColorPickerPanel = new JPanel();
    tabColorPickerPanel.setLayout(new BorderLayout());
    tabColorPickerPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black), "Icon"));
    final JLabel colorTitle = new JLabel("Choose Icon");
    tabColorPickerPanel.add(colorTitle, BorderLayout.PAGE_START);
    tabColorPickerPanel.add(createColorPicker(colorIndex, this.tabManager), BorderLayout.CENTER);
    c.gridy = 3;
    add(tabColorPickerPanel, c);

  }

  private static JPanel createColorPicker(final int color, final TabManager tabManager) {
    final ButtonGroup colorGroup = new ButtonGroup();
    final JPanel colorPicker = new JPanel();
    colorPicker.setLayout(new GridLayout(3, 3));
    for (int index = 0; index < COLORS.length; index++) {
      final JPanel colorRadioButton = new JPanel();
      colorRadioButton.setLayout(new BorderLayout());

      final JLabel iconLabel = new JLabel();
      iconLabel.setIcon(new ColoredIcon(COLORS[index]));

      final JRadioButton colorButton = new JRadioButton("" + index);
      if (index == color) {
        colorButton.setSelected(true);
      }
      colorButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final JRadioButton colorButton = (JRadioButton) e.getSource();
          final int colorIndex = Integer.parseInt(colorButton.getText());
          changeTabIcon(getReferenceCustomTabComponent(tabManager), new ColoredIcon(COLORS[colorIndex]));
        }
      });
      colorRadioButton.add(colorButton, BorderLayout.LINE_START);
      colorRadioButton.add(iconLabel, BorderLayout.CENTER);
      colorPicker.add(colorRadioButton);
      colorGroup.add(colorButton);
    }
    return colorPicker;
  }

  private static ReferenceCustomTabComponent getReferenceCustomTabComponent(final TabManager tabManager) {
    final Tab tab = tabManager.getActiveTab();
    final Component tabComponent = tab.getTabComponent();
    if (tabComponent instanceof ReferenceCustomTabComponent) {
      return (ReferenceCustomTabComponent) tabComponent;
    }
    return null;
  };

  private static void changeTabTitle(final ReferenceCustomTabComponent customComponent, final String title) {
    if (customComponent == null) {
      System.err.println("Custom tab component not recognized");
    } else {
      customComponent.setTitle(title);
    }
  }

  private static void changeTabIcon(final ReferenceCustomTabComponent customComponent, final Icon icon) {
    if (customComponent == null) {
      System.err.println("Custom tab component not recognized");
    } else {
      customComponent.setIcon(icon);
    }
  }

}
