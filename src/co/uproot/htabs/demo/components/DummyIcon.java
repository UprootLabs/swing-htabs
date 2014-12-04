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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

public class DummyIcon implements Icon {
  private final int width = 16;
  private final int height = 16;

  private final BasicStroke stroke = new BasicStroke(2);

  public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
    final Graphics2D g2d = (Graphics2D) g.create();

    g2d.setColor(Color.ORANGE);

    g2d.setStroke(stroke);

    final int wb2 = (width / 2);
    final int hb2 = (height / 2);
    final int size = 3;
    g2d.fillRect(wb2 - size, hb2 - size, wb2 + size, hb2 + size);

    g2d.dispose();
  }

  public int getIconWidth() {
    return width;
  }

  public int getIconHeight() {
    return height;
  }
}
