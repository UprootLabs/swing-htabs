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

import javax.swing.plaf.metal.MetalTabbedPaneUI;

import co.uproot.htabs.helpers.TabRects;
import co.uproot.htabs.tabmanager.TabManager;

public class CustomMetalTabbedPaneUI extends MetalTabbedPaneUI implements TabRects {
  final TabManager tabManager;

  public CustomMetalTabbedPaneUI(final TabManager tabManager) {
    this.tabManager = tabManager;
    tabManager.setTabIndent(0);
    tabManager.setTabComponentIndent(20);
  }

  @Override
  public void computeTabRects() {
    tabManager.computeTabIndents(this.tabPane.getTabCount(), rects);
  }
}
