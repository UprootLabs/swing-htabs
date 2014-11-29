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

/*
 * Dummy Icon used for the demo application
 *
 */

package co.uproot.htabs.demo.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class ColoredIcon implements Icon {
  private final int width = 16;
  private final int height = 16;
  private final Color color;

  public ColoredIcon(final Color color) {
    this.color = color;
  }

  public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
    final Graphics2D g2d = (Graphics2D) g.create();

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setColor(color.darker().darker());
    g2d.fillOval(0, 0, width, height);

    g2d.setColor(color);
    g2d.fillOval(3, 3, width-6, height-6);

    g2d.dispose();
  }

  public int getIconWidth() {
    return width;
  }

  public int getIconHeight() {
    return height;
  }
}
