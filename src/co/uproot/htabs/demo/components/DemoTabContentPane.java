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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DemoTabContentPane extends JPanel {

  public DemoTabContentPane(final String tabTitle, final int colorIndex, final ReferenceCustomTabComponent customTabComponent) {
    setLayout(new GridBagLayout());

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

    tabTitleEditorPanel.setBorder(createTitleBorder("Title"));

    final JTextField tabTitleEditor = new JTextField(tabTitle);
    tabTitleEditor.getDocument().addDocumentListener(new DocumentListener() {

      private void updateTitle() {
        String updatedTitle = tabTitleEditor.getText();
        if (updatedTitle.length() == 0) {
          updatedTitle = "Default Title";
        }
        customTabComponent.setText(updatedTitle);
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
    tabTitleEditorPanel.add(tabTitleEditor, BorderLayout.CENTER);
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 3;
    add(tabTitleEditorPanel, c);

    final JPanel tabColorPickerPanel = new JPanel();
    tabColorPickerPanel.setLayout(new BorderLayout());
    tabColorPickerPanel.setBorder(createTitleBorder("Icon"));
    tabColorPickerPanel.add(createColorPicker(colorIndex, customTabComponent), BorderLayout.CENTER);
    c.gridy = 3;
    add(tabColorPickerPanel, c);

  }

  private static Border createTitleBorder(final String headingText) {
    final Border titlePanelBorder = BorderFactory.createCompoundBorder(
        new TitledBorder(BorderFactory.createLineBorder(Color.gray), headingText),
        new EmptyBorder(20, 20, 20, 20)
      );
    return titlePanelBorder;
  }

  private static JPanel createColorPicker(final int color, final ReferenceCustomTabComponent customTabComponent) {
    final ButtonGroup colorGroup = new ButtonGroup();
    final JPanel colorPicker = new JPanel();
    colorPicker.setLayout(new GridLayout(3, 3));
    for (int index = 0; index < COLORS.length; index++) {
      final JPanel colorRadioButton = new JPanel();
      colorRadioButton.setLayout(new BorderLayout());
      colorRadioButton.setBorder(new EmptyBorder(20, 0, 20, 0));

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
          customTabComponent.setIcon(new ColoredIcon(COLORS[colorIndex]));
        }
      });
      colorButton.setBorder(new EmptyBorder(0, 10, 0, 10));
      colorRadioButton.add(colorButton, BorderLayout.LINE_START);
      colorRadioButton.add(iconLabel, BorderLayout.CENTER);
      colorPicker.add(colorRadioButton);
      colorGroup.add(colorButton);
    }
    return colorPicker;
  }

}
