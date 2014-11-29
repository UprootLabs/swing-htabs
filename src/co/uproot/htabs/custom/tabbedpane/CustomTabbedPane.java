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
 * Custom TabbedPane to support the indentation
 * of tabs in hierarchical manner.
 */

package co.uproot.htabs.custom.tabbedpane;

import javax.swing.JTabbedPane;
import javax.swing.plaf.TabbedPaneUI;

import co.uproot.htabs.helpers.TabRects;

public class CustomTabbedPane extends JTabbedPane {

  private static final long serialVersionUID = 1L;

  public CustomTabbedPane() {
    super();
  }

  public CustomTabbedPane(final int tabPlacement) {
    super(tabPlacement);
  }

  public CustomTabbedPane(final int tabPlacement, final int tabLayoutPolicy) {
    super(tabPlacement, tabLayoutPolicy);
  }

  @Override
  public void doLayout() {
    super.doLayout();

    final TabbedPaneUI ui = getUI();
    if (ui != null) {
      if (ui instanceof TabRects) {
        ((TabRects) ui).computeTabRects();
      }
    }
  }
}
