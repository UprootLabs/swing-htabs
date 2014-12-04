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
 * Custom TabbedPane UI for indenting the tabs.
 *
 */

package co.uproot.htabs.custom.tabbedpane.ui;

import co.uproot.htabs.helpers.TabRects;
import co.uproot.htabs.tabmanager.TabManager;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

public class CustomWindowsTabbedPaneUI extends WindowsTabbedPaneUI implements TabRects {
  final TabManager tabManager;

  public CustomWindowsTabbedPaneUI(final TabManager tabManager) {
    this.tabManager = tabManager;
  }

  @Override
  public void computeTabRects() {
    tabManager.computeTabIndents(this.tabPane.getTabCount(), rects);
  }
}
