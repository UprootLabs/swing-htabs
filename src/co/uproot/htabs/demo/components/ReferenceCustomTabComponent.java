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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReferenceCustomTabComponent extends JPanel {
  private final JLabel titleLabel;

  public ReferenceCustomTabComponent(final String title, final Icon icon) {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setOpaque(false);

    final JLabel label = new JLabel(title);
    label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    label.setIcon(icon);
    titleLabel = label;
    add(label);
  }

  public String getTitle() {
    return titleLabel.getText();
  }

  public void setTitle(final String newTitle) {
    titleLabel.setText(newTitle);
  }

  public Icon getIcon() {
    return titleLabel.getIcon();
  }

  public void setIcon(final Icon newIcon) {
    titleLabel.setIcon(newIcon);
  }

}
