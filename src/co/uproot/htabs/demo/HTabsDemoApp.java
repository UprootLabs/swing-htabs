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
 * An demo application to demonstrate the usage of the htabs library.
 *
 */

package co.uproot.htabs.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import co.uproot.htabs.custom.tabbedpane.CustomTabbedPane;
import co.uproot.htabs.demo.components.ColoredIcon;
import co.uproot.htabs.demo.components.DemoTabContentPane;
import co.uproot.htabs.demo.components.DummyIcon;
import co.uproot.htabs.demo.components.ReferenceCustomTabComponent;
import co.uproot.htabs.tabmanager.TabManager;
import co.uproot.htabs.tabmanager.TabManager.Tab;

public class HTabsDemoApp {

  final public static Color COLORS[] = new Color[] {
      new Color(10, 10, 10, 190),
      new Color(10, 10, 250, 190),
      new Color(10, 250, 10, 190),
      new Color(10, 250, 250, 190),
      new Color(250, 10, 10, 190),
      new Color(250, 10, 250, 190),
      new Color(250, 250, 10, 190),
      new Color(250, 250, 250, 190),
      new Color(90, 25, 250, 190),
  };

  final private static LookAndFeelInfo lafs[] = UIManager.getInstalledLookAndFeels();
  private static int systemLafIndex = -1;
  private static int crossPlatformLafIndex = -1;

  public static void main(final String[] args) {
    systemLafIndex = getLafIndex(UIManager.getSystemLookAndFeelClassName());
    swap(lafs, 0, systemLafIndex);
    systemLafIndex = 0;
    crossPlatformLafIndex = getLafIndex(UIManager.getCrossPlatformLookAndFeelClassName());
    swap(lafs, lafs.length - 1, crossPlatformLafIndex);
    crossPlatformLafIndex = lafs.length - 1;

    updateLAF(systemLafIndex, null);
  }

  private static int getLafIndex(final String searchLafClassName) {
    for (int i = 0; i < lafs.length; i++) {
      final LookAndFeelInfo lookAndFeelInfo = lafs[i];
      final String lafClassName = lookAndFeelInfo.getClassName();
      if (lafClassName.equals(searchLafClassName)) {
        return i;
      }
    }
    return -1;
  }

  private static void updateLAF(final int lafIndex, final JFrame frame) {
    try {

      if (frame != null) {
        frame.setVisible(false);
      }

      UIManager.setLookAndFeel(lafs[lafIndex].getClassName());

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          createAndShowGUI(lafIndex);
        }
      });

    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  private static void updateStatus(final JLabel statusLabel, final String statusText) {
    statusLabel.setText("[Status] " + statusText);
  }

  private static void createAndShowGUI(final int lafIndex) {
    final JFrame f = new JFrame("Tabbed Pane Demo");
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    f.setSize((int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8));
    f.setLocationByPlatform(true);

    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLayout(new BorderLayout());

    final JTabbedPane tabbedPane = new CustomTabbedPane(SwingConstants.LEFT);
    final TabManager tabManager = new TabManager(tabbedPane, 300, JTabbedPane.SCROLL_TAB_LAYOUT);

    final JPanel radioPanel = new JPanel();
    createLAFButtons(lafIndex, f, radioPanel);

    final JPanel statusBar = new JPanel();
    statusBar.setLayout(new BorderLayout(20, 0));

    final JLabel status = new JLabel("[Status]");
    status.setBorder(new EmptyBorder(4, 0, 4, 8));
    status.setFont(status.getFont().deriveFont(Font.BOLD));
    statusBar.add(status, BorderLayout.EAST);

    final JPanel topBar = new JPanel();
    topBar.setLayout(new BorderLayout());

    final JPanel newTabButtons = new JPanel();
    newTabButtons.setLayout(new BorderLayout());

    final JButton newTabBtn = new JButton("New Tab");
    newTabBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        updateStatus(status, "New tab added");
        final String title = "New Tab";
        final int colorIndex = 7;
        final JPanel tabContent = new DemoTabContentPane(title, colorIndex, tabManager);
        tabManager.addTab(new ReferenceCustomTabComponent(title, new DummyIcon(COLORS[colorIndex])), tabContent);
      }
    });

    final JButton newSiblingBtn = new JButton("New Sibling");
    newSiblingBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        updateStatus(status, "New sibling added");
        final Tab currTab = tabManager.getActiveTab();
        final String title = "New sibling added by " + currTab.getTabTitle();
        final int colorIndex = 3;
        final JPanel tabContent = new DemoTabContentPane(title, colorIndex, tabManager);
        currTab.addSibling(null, null, new ReferenceCustomTabComponent(title, new DummyIcon(COLORS[colorIndex])),
            tabContent, null);
      }
    });

    final JButton newChildBtn = new JButton("New Child");
    newChildBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        updateStatus(status, "New child added");
        final Tab parent = tabManager.getActiveTab();
        final String title = "New Child Tab added by " + parent.getTabTitle();
        final int colorIndex = 6;
        final JPanel tabContent = new DemoTabContentPane(title, colorIndex, tabManager);
        parent.addChild(null, null, new ReferenceCustomTabComponent(title, new DummyIcon(COLORS[colorIndex])),
            tabContent, title);
      }
    });

    newTabButtons.add(newTabBtn, BorderLayout.WEST);
    newTabButtons.add(newSiblingBtn, BorderLayout.CENTER);
    newTabButtons.add(newChildBtn, BorderLayout.EAST);

    final JPanel configButtons = new JPanel();
    configButtons.setLayout(new BorderLayout());
    configButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

    final JPanel tabPlacementPanel = new JPanel();
    tabPlacementPanel.setLayout(new BoxLayout(tabPlacementPanel, BoxLayout.X_AXIS));

    final JLabel tabPlacementLabel = new JLabel("Tab Placement: ");

    final String[] tabPlacements = { "Top", "Left" };

    final JComboBox<String> tabPlacementList = new JComboBox<>(tabPlacements);
    tabPlacementList.setSelectedIndex(1);

    tabPlacementList.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final int tabPlacement = tabPlacementList.getSelectedIndex() + 1;
        tabManager.setTabPlacement(tabPlacement);
      }
    });

    tabPlacementPanel.add(tabPlacementLabel);
    tabPlacementPanel.add(tabPlacementList);
    configButtons.add(tabPlacementPanel, BorderLayout.CENTER);

    topBar.add(radioPanel, BorderLayout.WEST);
    topBar.add(configButtons, BorderLayout.CENTER);
    topBar.add(newTabButtons, BorderLayout.EAST);

    final int numTabs = 15;
    final Tab tabs[] = new Tab[numTabs];
    final Random random = new Random(0);
    for (int i = 0; i < numTabs; i++) {
      final int colorIndex = i % COLORS.length;
      final Color color = COLORS[colorIndex];
      final ColoredIcon icon = new ColoredIcon(color);
      final boolean root = random.nextDouble() > 0.7d;
      final int prevTab = random.nextInt(i + 1);
      if (root || (prevTab == i)) {
        final String title = "Tab " + i;
        final JPanel tabContent = new DemoTabContentPane(title, colorIndex, tabManager);
        final Tab tab = tabManager.addTab(new ReferenceCustomTabComponent(title, icon), tabContent);
        tab.setTabTitle(title);
        tabs[i] = tab;
      } else {
        final String title = "Tab " + i + " [child of " + prevTab + "]";
        final JPanel tabContent = new DemoTabContentPane(title, colorIndex, tabManager);
        final Tab tab = tabs[prevTab].addChild(new ReferenceCustomTabComponent(title, icon), tabContent);
        tab.setTabTitle(title);
        tabs[i] = tab;
      }
    }

    tabbedPane.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(final ChangeEvent e) {
        final Tab selectedTab = tabManager.getActiveTab();
        final String statusText = selectedTab == null ? "no more tabs" : selectedTab.getTabTitle();
        updateStatus(status, statusText);
      }
    });

    f.add(topBar, BorderLayout.NORTH);
    f.add(statusBar, BorderLayout.SOUTH);
    f.add(tabbedPane, BorderLayout.CENTER);

    f.setVisible(true);
  }

  private static JPanel createTabContent(final String text) {
    final JLabel label = new JLabel(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    final JPanel wrapper = new JPanel();
    wrapper.setLayout(new BorderLayout());
    wrapper.add(label, BorderLayout.CENTER);
    return wrapper;
  }

  private static void createLAFButtons(final int lafIndex, final JFrame f, final JPanel radioPanel) {
    final ButtonGroup bGroup = new ButtonGroup();
    for (int i = 0; i < lafs.length; i++) {
      final int index = i;
      final LookAndFeelInfo laf = lafs[index];
      String title = laf.getName();
      if (index == systemLafIndex) {
        title += " (system L&F)";
      }
      final JRadioButton lafButton = new JRadioButton(title);
      if (index == lafIndex) {
        lafButton.setSelected(true);
      }
      lafButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          updateLAF(index, f);
        }
      });
      radioPanel.add(lafButton);
      bGroup.add(lafButton);
    }
  }

  /** Swaps the `i`th and `j`th elements of a */
  public static <T> void swap(final T[] a, final int i, final int j) {
    final T temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
}
