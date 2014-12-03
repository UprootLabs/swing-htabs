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
 * Manages the tabs in an hierarchical manner.
 * Gives an interface for tab operations like
 * add/delete/move.
 *
 */

package co.uproot.htabs.tabmanager;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.plaf.synth.SynthTabbedPaneUI;

import co.uproot.htabs.custom.custom.components.HideableComponentWrapper;
import co.uproot.htabs.custom.tabbedpane.ui.CustomBasicTabbedPaneUI;
import co.uproot.htabs.custom.tabbedpane.ui.CustomMetalTabbedPaneUI;
import co.uproot.htabs.custom.tabbedpane.ui.CustomMotifTabbedPaneUI;
import co.uproot.htabs.custom.tabbedpane.ui.CustomSynthTabbedPaneUI;
import co.uproot.htabs.custom.tabbedpane.ui.CustomWindowsTabbedPaneUI;

import com.sun.java.swing.plaf.motif.MotifTabbedPaneUI;
import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

public class TabManager {

  private static final int INDENT = 20;
  private final int TAB_BAR_WIDTH;
  private static final int MARGIN = 5;
  private static final int COLLAPSE_BUTTON_WIDTH = 16;
  private static final int COLLAPSE_BUTTON_TOTAL_WIDTH = COLLAPSE_BUTTON_WIDTH + MARGIN;

  private final JTabbedPane tabbedPane;
  private final List<Tab> tabs = new ArrayList<>();

  public TabManager(final JTabbedPane tabbedPane, final int tabBarWidth, final int tabLayoutPolicy) {
    this.tabbedPane = tabbedPane;
    this.tabbedPane.setTabLayoutPolicy(tabLayoutPolicy);
    this.TAB_BAR_WIDTH = tabBarWidth;

    final TabbedPaneUI tabUI = this.tabbedPane.getUI();

    if (tabUI instanceof MetalTabbedPaneUI) {
      this.tabbedPane.setUI(new CustomMetalTabbedPaneUI(this));
    } else if (tabUI instanceof SynthTabbedPaneUI) {
      this.tabbedPane.setUI(new CustomSynthTabbedPaneUI(this));
    } else if (tabUI instanceof MotifTabbedPaneUI) {
      this.tabbedPane.setUI(new CustomMotifTabbedPaneUI(this));
    } else if (tabUI instanceof WindowsTabbedPaneUI) {
      this.tabbedPane.setUI(new CustomWindowsTabbedPaneUI(this));
    } else {
      this.tabbedPane.setUI(new CustomBasicTabbedPaneUI(this));
    }

  }

  public Tab addTab(final String tabTitle, final Icon tabIcon, final Component tabComponent,
      final Component tabContentPane, final String toolTip, final int index) {
    final Tab childTab = new Tab(tabTitle, tabIcon, tabComponent, tabContentPane, toolTip, null);
    this.tabs.add(childTab);
    int newIndex = index;
    if (index == -1) {
      final Component addedComponent = tabbedPane.add(tabContentPane);
      newIndex = tabbedPane.indexOfComponent(addedComponent);
    }
    tabbedPane.insertTab(tabTitle, tabIcon, tabContentPane, toolTip, newIndex);
    tabbedPane.setTabComponentAt(newIndex, childTab.tabComponent);
    return childTab;
  }

  public Tab addTab(final String tabTitle, final Icon tabIcon, final Component tabComponent,
      final Component tabContentPane, final String toolTip) {
    return addTab(tabTitle, tabIcon, tabComponent, tabContentPane, toolTip, -1);
  }

  public Tab addTab(final Component tabContentPane) {
    return addTab(null, null, null, tabContentPane, null);
  }

  public Tab addTab(final Component tabContentPane, final int index) {
    return addTab(null, null, null, tabContentPane, null, index);
  }

  public Tab addTab(final String tabTitle, final Component tabContentPane) {
    return addTab(tabTitle, null, null, tabContentPane, null);
  }

  public Tab addTab(final String tabTitle, final Icon tabIcon, final Component tabContentPane) {
    return addTab(tabTitle, tabIcon, null, tabContentPane, null);
  }

  public Tab addTab(final String tabTitle, final Icon tabIcon, final Component tabContentPane, final String toolTip) {
    return addTab(tabTitle, tabIcon, null, tabContentPane, toolTip);
  }

  public Tab addTab(final Component tabComponent, final Component tabContentPane, final String toolTip) {
    return addTab(null, null, tabComponent, tabContentPane, toolTip);
  }

  public void addTab(final Tab tab) {
    tabs.add(tab);
  }

  public void addTab(final Tab tab, final int index) {
    tabbedPane.insertTab(tab.getTabTitle(), tab.getTabIcon(), tab.getTabContentPane(), tab.getToolTip(), index);
    tabbedPane.setTabComponentAt(index, tab.getTabComponentWrapperContainer());
  }

  private int tabIndent = INDENT;

  public void setTabIndent(final int indent) {
    tabIndent = indent;
  }

  private int tabComponentIndent = INDENT;

  public void setTabComponentIndent(final int indent) {
    tabComponentIndent = indent;
  }

  private int getTabIndent(final int tabLevel) {
    final int placement = this.tabbedPane.getTabPlacement();
    return ((placement == JTabbedPane.LEFT) || (placement == JTabbedPane.RIGHT)) ? tabLevel * tabIndent : 0;
  }

  private int getTabComponentIndent(final int tabLevel) {
    final int placement = this.tabbedPane.getTabPlacement();
    return ((placement == JTabbedPane.LEFT) || (placement == JTabbedPane.RIGHT)) ? tabLevel * tabComponentIndent : 0;
  }

  private int getTabComponentIndent(final Component tabComponent) {
    final Tab tab = getTabFromTabComponent(tabs, tabComponent);
    final int tabLevel = getTabLevel(tab);
    return getTabComponentIndent(tabLevel);
  }

  private static int getTabLevel(final Tab tab) {
    return tab == null ? 0 : tab.tabLevel;
  }

  public Tab getActiveTab() {
    final Component tabContentPane = tabbedPane.getSelectedComponent();
    return getTabFromTabContentPane(tabs, tabContentPane);
  }

  private static Tab getTabFromTabComponent(final List<Tab> tabs, final Component tabComponent) {
    if (tabs != null) {
      for (final Tab t : tabs) {
        if (t.tabComponent == tabComponent) {
          return t;
        } else {
          final Tab ret = getTabFromTabComponent(t.children, tabComponent);
          if (ret != null) {
            return ret;
          }
        }
      }
    }
    return null;
  }

  public Tab getTabFromTabContentPane(final Component tabContentPane) {
    return getTabFromTabContentPane(tabs, tabContentPane);
  }

  public Tab getTabFromTabComponent(final Component tabComponent) {
    return getTabFromTabComponent(tabs, tabComponent);
  }

  private static Tab getTabFromTabContentPane(final List<Tab> tabs, final Component tabContentPane) {
    if (tabs != null) {
      for (final Tab t : tabs) {
        if (t.tabContentPane == tabContentPane) {
          return t;
        } else {
          final Tab ret = getTabFromTabContentPane(t.children, tabContentPane);
          if (ret != null) {
            return ret;
          }
        }
      }
    }
    return null;
  }

  public void computeTabIndents(final int tabCount, final Rectangle[] rects) {
    final int placement = tabbedPane.getTabPlacement();
    if (placement == JTabbedPane.LEFT) {
      for (int tabIndex = 0; tabIndex < tabCount; tabIndex++) {
        final Component tabComponent = this.tabbedPane.getTabComponentAt(tabIndex);
        final Tab tab = getTabFromTabComponent(tabs, tabComponent);
        if (tab != null) {
          final int tabIndent = getTabIndent(tab.tabLevel);
          rects[tabIndex].x = tabIndent;
          final int width = rects[tabIndex].width;
          rects[tabIndex].width = width - tabIndent;
        }
      }
    }
  }

  public void setTabPlacement(final int tabPlacement) {
    tabbedPane.setTabPlacement(tabPlacement);
    if (tabPlacement == JTabbedPane.TOP) {
      for (final Tab tab : tabs) {
        tab.expandTabTree();
      }
    } else if (tabPlacement == JTabbedPane.LEFT) {
      for (final Tab tab : tabs) {
        tab.restoreTabHierarchy();
      }
    }
  }

  public class TabComponentWrappingContainer extends JPanel {

    private static final long serialVersionUID = 1L;
    private final CollapseButton collapseButton;
    private final Component tabComponent;
    private final HideableComponentWrapper hideableComponentWraper;
    private boolean collapsed;
    private static final int MARGIN_VERT = 4;
    private static final int MARGIN_LEFT = 8;

    public TabComponentWrappingContainer(final Component tabComponenet) {
      setLayout(new BorderLayout(MARGIN, 0));
      this.collapseButton = new CollapseButton();
      this.hideableComponentWraper = new HideableComponentWrapper(this.collapseButton, false);
      this.tabComponent = tabComponenet;

      addAncestorListener(new AncestorListener() {

        private void helper(final AncestorEvent event) {
          final int indent = getComponentIndent() + MARGIN_LEFT;
          final TabComponentWrappingContainer c = (TabComponentWrappingContainer) event.getSource();
          if ((!c.getHideableComponentWrapper().isVisible()) && tabbedPane.getTabPlacement() == JTabbedPane.LEFT) {
            setBorder(BorderFactory.createEmptyBorder(MARGIN_VERT, indent + COLLAPSE_BUTTON_TOTAL_WIDTH, MARGIN_VERT, 0));
          } else {
            setBorder(BorderFactory.createEmptyBorder(MARGIN_VERT, indent, MARGIN_VERT, 0));
          }
          tabComponenet.setPreferredSize(new Dimension(TAB_BAR_WIDTH - indent, tabComponenet.getPreferredSize().height));
        }

        @Override
        public void ancestorRemoved(final AncestorEvent event) {
          this.helper(event);
        }

        @Override
        public void ancestorMoved(final AncestorEvent event) {
          this.helper(event);
        }

        @Override
        public void ancestorAdded(final AncestorEvent event) {
          this.helper(event);
        }
      });
      setOpaque(false);
      add(this.hideableComponentWraper, BorderLayout.WEST);
      add(tabComponenet, BorderLayout.CENTER);
      add(new CloseButton(), BorderLayout.EAST);
    }

    public boolean isCollapsed() {
      return this.collapsed;
    }

    public void setCollapsed(final boolean collapsed) {
      this.collapsed = collapsed;
    }

    public Component getTabComponent() {
      return this.tabComponent;
    }

    public JButton getCollapseButton() {
      return this.collapseButton;
    }

    public Component getHideableComponentWrapper() {
      return this.hideableComponentWraper;
    }

    public void showCollapseButton() {
      final int placement = tabbedPane.getTabPlacement();
      if (placement == JTabbedPane.LEFT) {
        this.hideableComponentWraper.showComponent();
      }
    }

    public void hideCollapseButton() {
      final int placement = tabbedPane.getTabPlacement();
      if (placement == JTabbedPane.LEFT) {
        if (this.hideableComponentWraper.isComponentVisible()) {
          this.hideableComponentWraper.hideComponent();
        }
      } else if (placement == JTabbedPane.TOP) {
        if (this.hideableComponentWraper.isVisible()) {
          this.hideableComponentWraper.setVisible(false);
        }
      }
    }

    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
      final int placement = tabbedPane.getTabPlacement();
      if (placement == JTabbedPane.LEFT) {
        final Tab tab = getTabFromTabComponent(this);
        if (tab.getChildren().size() > 0) {
          showCollapseButton();
        } else {
          this.hideableComponentWraper.setVisible(true);
        }
        super.setBounds(0, y, width, height);
      } else if (placement == JTabbedPane.TOP) {
        hideCollapseButton();
        super.setBounds(x, y, width, height);
      }
    }

    private int getComponentIndent() {
      return getTabComponentIndent(this);
    }

    private class CloseButton extends JButton {
      private static final long serialVersionUID = 1L;
      private static final int CLOSE_BUTTON_SIZE = 8;
      private static final int MARGIN_VERT = 4;
      private static final int MARGIN_HORIZ = 4;
      private final Color crossColor = new Color(100, 100, 100, 200);
      private Color rolloverColor = new Color(250, 50, 50, 200);

      CloseButton() {
        final Dimension closeButtonDimension = new Dimension(CLOSE_BUTTON_SIZE +2*MARGIN_HORIZ, CLOSE_BUTTON_SIZE + 2*MARGIN_VERT);
        setPreferredSize(closeButtonDimension);
        setMinimumSize(closeButtonDimension);
        setMaximumSize(closeButtonDimension);
        setToolTipText("close tab");
        setContentAreaFilled(false);
        setFocusable(false);
        addMouseListener(new MouseListener() {
          @Override
          public void mouseReleased(final MouseEvent e) {
          }

          @Override
          public void mousePressed(final MouseEvent e) {
          }

          @Override
          public void mouseClicked(final MouseEvent e) {
            final Component tabComponent = getParent();
            final Tab tab = getTabFromTabComponent(tabs, tabComponent);
            tab.removeSelf();
            final int i = tabbedPane.indexOfTabComponent(getParent());

            if (i != -1) {
              tabbedPane.remove(i);
            }

          }

          public void mouseEntered(final MouseEvent e) {
            final Component component = e.getComponent();
            if (component instanceof AbstractButton) {
              final AbstractButton button = (AbstractButton) component;
              button.getModel().setRollover(true);
            }
          }

          public void mouseExited(final MouseEvent e) {
            final Component component = e.getComponent();
            if (component instanceof AbstractButton) {
              final AbstractButton button = (AbstractButton) component;
              button.getModel().setRollover(false);
            }
          }
        });
        setRolloverEnabled(true);
      }

      @Override
      public void updateUI() {
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        // TODO: This doesn't seem to make any visual difference
        if (getModel().isPressed()) {
          g2.translate(1, 1);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(crossColor);
        if (getModel().isRollover()) {
          g2.setColor(rolloverColor);
          g2.fillRoundRect(0, 0, 2*MARGIN_HORIZ+CLOSE_BUTTON_SIZE, 2*MARGIN_VERT+CLOSE_BUTTON_SIZE, 5, 5);
          g2.setColor(Color.WHITE);
        }
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(MARGIN_HORIZ, MARGIN_VERT, MARGIN_HORIZ+CLOSE_BUTTON_SIZE, MARGIN_VERT+CLOSE_BUTTON_SIZE);
        g2.drawLine(MARGIN_HORIZ+CLOSE_BUTTON_SIZE, MARGIN_VERT, MARGIN_HORIZ, MARGIN_VERT+CLOSE_BUTTON_SIZE);
        g2.dispose();
      }
    }

    private class CollapseButton extends JButton {
      private static final long serialVersionUID = 1L;
      private final GeneralPath collapsedPolygon = makePolygon(true);
      private final GeneralPath normalPolygon = makePolygon(false);

      CollapseButton() {
        final Dimension collapseButtomDimension = new Dimension(COLLAPSE_BUTTON_WIDTH, COLLAPSE_BUTTON_WIDTH);
        setPreferredSize(collapseButtomDimension);
        setMinimumSize(collapseButtomDimension);
        setMaximumSize(collapseButtomDimension);
        setToolTipText("Collapse");
        setContentAreaFilled(false);
        setFocusable(false);
        addMouseListener(new MouseListener() {
          @Override
          public void mouseReleased(final MouseEvent e) {
          }

          @Override
          public void mousePressed(final MouseEvent e) {
          }

          @Override
          public void mouseClicked(final MouseEvent e) {
            final Component tabComponent = getParent().getParent();
            final Tab tab = getTabFromTabComponent(tabs, tabComponent);
            if (tab.isCollapsed()) {
              tab.expandTab();
            } else {
              tab.collapseTab();
            }

            repaint();
          }

          public void mouseEntered(final MouseEvent e) {
            final Component component = e.getComponent();
            if (component instanceof AbstractButton) {
              final AbstractButton button = (AbstractButton) component;
              button.getModel().setRollover(true);
            }
          }

          public void mouseExited(final MouseEvent e) {
            final Component component = e.getComponent();
            if (component instanceof AbstractButton) {
              final AbstractButton button = (AbstractButton) component;
              button.getModel().setRollover(false);
            }
          }
        });
        setRolloverEnabled(true);
      }

      @Override
      public void updateUI() {
      }

      @Override
      protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);

        g2.fill(collapsed ? collapsedPolygon : normalPolygon);
        g2.dispose();
      }

      private GeneralPath makePolygon(final boolean collapsed) {
        final ArrayList<Integer> xPoints = new ArrayList<>();
        final ArrayList<Integer> yPoints = new ArrayList<>();

        if (collapsed) {
          xPoints.add(0);
          xPoints.add(6);
          xPoints.add(0);
          yPoints.add(2);
          yPoints.add(8);
          yPoints.add(14);
        } else {
          xPoints.add(0);
          xPoints.add(6);
          xPoints.add(12);
          yPoints.add(6);
          yPoints.add(12);
          yPoints.add(6);
        }
        final GeneralPath polygon = new GeneralPath(Path2D.WIND_EVEN_ODD, xPoints.size());
        polygon.moveTo(xPoints.get(0), yPoints.get(0));

        for (int i = 1; i < xPoints.size(); i++) {
          polygon.lineTo(xPoints.get(i), yPoints.get(i));
        }

        polygon.closePath();
        return polygon;
      }
    }
  }

  public class Tab {
    private Tab parent;
    final private ArrayList<Tab> children;
    private TabComponentWrappingContainer tabComponent;
    private Component tabContentPane;
    private String tabTitle;
    private Icon tabIcon;
    private String toolTip;
    private boolean collapsed = false;
    private int tabLevel;

    Tab(final String tabTitle, final Icon tabIcon, final Component tabComponent, final Component tabContentPane,
        final String toolTip, final Tab parent) {
      this.tabTitle = tabTitle;
      this.tabIcon = tabIcon;

      if (tabComponent == null) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        final JLabel iconLabel = new JLabel();
        iconLabel.setIcon(tabIcon);
        panel.add(iconLabel);

        final JLabel label = new JLabel(tabTitle);
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panel.add(label);

        this.tabComponent = new TabComponentWrappingContainer(panel);
      } else {
        this.tabComponent = new TabComponentWrappingContainer(tabComponent);
      }

      this.tabContentPane = tabContentPane;
      this.toolTip = toolTip;
      this.parent = parent;
      this.children = new ArrayList<>();
      this.tabLevel = parent == null ? 0 : parent.tabLevel + 1;
    }

    Tab(final String tabTitle, final Icon tabIcon, final Component tabContentPane, final String toolTip,
        final Tab parent) {
      this(tabTitle, tabIcon, null, tabContentPane, toolTip, parent);
    }

    Tab(final Component tabComponent, final Component tabContentPane, final String toolTip, final Tab parent) {
      this(null, null, tabComponent, tabContentPane, toolTip, parent);
    }

    Tab(final Component tabContentPane, final Tab parent) {
      this(null, null, null, tabContentPane, null, parent);
    }

    Tab(final String tabTitle, final Component tabContentPane, final Tab parent) {
      this(tabTitle, null, null, tabContentPane, null, parent);
    }

    Tab(final String tabTitle, final Icon tabIcon, final Component tabContentPane, final Tab parent) {
      this(tabTitle, tabIcon, null, tabContentPane, null, parent);
    }

    Tab(final Component tabComponent, final Component tabContentPane, final Tab parent) {
      this(null, null, tabComponent, tabContentPane, null, parent);
    }

    public Tab getParent() {
      return this.parent;
    }

    public void setParent(final Tab parent) {
      this.parent = parent;
    }

    public List<Tab> getChildren() {
      return this.children;
    }

    public Component getTabComponent() {
      return this.tabComponent.getTabComponent();
    }

    public Component getTabComponentWrapperContainer() {
      return this.tabComponent;
    }

    public void setTabComponent(final Component tabComponent) {
      this.tabComponent = new TabComponentWrappingContainer(tabComponent);
    }

    public Component getTabContentPane() {
      return this.tabContentPane;
    }

    public void setTabContentPane(final Component tabContentPane) {
      this.tabContentPane = tabContentPane;
    }

    public String getTabTitle() {
      return this.tabTitle;
    }

    public void setTabTitle(final String tabTitle) {
      this.tabTitle = tabTitle;
    }

    public Icon getTabIcon() {
      return this.tabIcon;
    }

    public void setTabIcon(final Icon tabIcon) {
      this.tabIcon = tabIcon;
    }

    public String getToolTip() {
      return this.toolTip;
    }

    public void setToolTip(final String toolTip) {
      this.toolTip = toolTip;
    }

    public boolean isCollapsed() {
      return this.collapsed;
    }

    public void setCollapsed(final boolean collapsed) {
      if (this.tabComponent instanceof TabComponentWrappingContainer) {
        this.tabComponent.setCollapsed(collapsed);
      }
      this.collapsed = collapsed;
    }

    public int getTabLevel() {
      return this.tabLevel;
    }

    public void setTabLevel(final int tabLevel) {
      this.tabLevel = tabLevel;
    }

    private int computeIndex() {
      int res = 0;
      if (!this.isCollapsed()) {
        res += children.size();
        for (final Tab t : children) {
          if (t.children.size() > 0) {
            res += t.computeIndex();
          }
        }
      }
      return res;
    }

    public Tab addChild(final String tabTitle, final Icon tabIcon, final Component tabComponent,
        final Component tabContentPane, final String toolTip) {
      final Tab childTab = new Tab(tabTitle, tabIcon, tabComponent, tabContentPane, toolTip, this);
      this.children.add(childTab);
      this.expandTab();
      final int curTabIndex = tabbedPane.indexOfTabComponent(this.tabComponent);
      final int childIndex = curTabIndex + computeIndex();
      tabbedPane.insertTab(tabTitle, tabIcon, tabContentPane, toolTip, childIndex);
      tabbedPane.setTabComponentAt(childIndex, childTab.tabComponent);
      if (this.children.size() > 0) {
        this.tabComponent.showCollapseButton();
      }
      return childTab;
    }

    public Tab addChild(final String tabTitle, final Component tabContentPane) {
      return addChild(tabTitle, null, null, tabContentPane, null);
    }

    public Tab addChild(final String tabTitle, final Icon tabIcon, final Component tabContentPane) {
      return addChild(tabTitle, tabIcon, null, tabContentPane, null);
    }

    public Tab addChild(final String tabTitle, final Icon tabIcon, final Component tabContentPane, final String toolTip) {
      return addChild(tabTitle, tabIcon, null, tabContentPane, toolTip);
    }

    public Tab addChild(final Component tabComponent, final Component tabContentPane) {
      return addChild(null, null, tabComponent, tabContentPane, null);
    }

    public void addSibling(final String tabTitle, final Icon tabIcon, final Component tabComponent, final Component tabContentPane,
        final String toolTip) {
      if (this.parent == null) {
        addTab(tabTitle, tabIcon, tabComponent, tabContentPane, toolTip);
      } else {
        this.parent.addChild(tabTitle, tabIcon, tabComponent, tabContentPane, toolTip);
      }
    }

    public void updateChildrenLevels() {
      if (children != null) {
        for (final Tab child : children) {
          child.tabLevel = this.tabLevel + 1;
          child.updateChildrenLevels();
        }
      }
    }

    public void updateParentOfChildren(final Tab newParent) {
      if (children != null) {
        for (final Tab child : children) {
          child.parent = newParent;
        }
      }
    }

    public void removeSelf() {
      this.expandTab();
      int childIndex = this.parent == null ? tabs.indexOf(this) : this.parent.children.indexOf(this);
      final List<Tab> tabList = parent == null ? tabs : parent.children;

      if (this.children.size() > 0) {
        final Tab newParent = this.children.get(0);
        newParent.tabLevel = this.tabLevel;
        newParent.parent = this.parent;
        this.children.remove(0);
        this.updateParentOfChildren(newParent);
        newParent.children.addAll(this.children);
        newParent.updateChildrenLevels();
        tabList.add(childIndex++, newParent);
        if (newParent.children.size() > 0) {
          newParent.tabComponent.showCollapseButton();
          newParent.expandTab();
        }
      }

      tabList.remove(childIndex);
      if ((this.parent != null) && (this.parent.children.size() <= 0)) {
        this.parent.tabComponent.hideCollapseButton();
      }
    }

    public void removeAllChildren() {

    }

    /**
     * Expands only the first level of tabs in the hierarchy of current tab.
     * This expansion changes the state of the tab to expanded.
     */
    public void expandTab() {
      if (this.isCollapsed()) {
        if (this.children != null) {
          int index = tabbedPane.indexOfTabComponent(this.tabComponent);
          for (final Tab child : this.children) {
            addTab(child, ++index);
          }
        }
        this.setCollapsed(false);
      }
    }

    /**
     * Expands the tab hierarchy recursively. This does not change the state of
     * tab to expanded. This behavior is useful when the tab placement is
     * changed from vertical to horizontal. In that case we are saving the
     * previous state of the tab, so that it can be restored when the tab
     * placement is changed back to vertical.
     */
    protected void expandTabTree() {
      if (this.children != null) {
        final int index = tabbedPane.indexOfTabComponent(this.tabComponent);
        expandTabTreeHelper(index);
      }
    }

    /**
     * Expands the tab by adding the children from the specified index recursively.
     *
     * @param index
     *          index from where the child tabs need to be added.
     * @return index of the tab which was last added.
     */
    private int expandTabTreeHelper(int index) {
      for (final Tab child : this.children) {
        if (this.isCollapsed()) {
          addTab(child, ++index);
        } else {
          index++;
        }
        index = child.expandTabTree(index);
      }
      return index;
    }

    /**
     * Expands the tab hierarchy recursively.
     *
     * @param index
     *          index from where the child tabs need to be added.
     * @return index of the tab which was last added.
     */
    private int expandTabTree(int index) {
      if (this.children != null) {
        index = expandTabTreeHelper(index);
      }
      return index;
    }

    public void collapseTab() {
      if (this.children != null) {
        for (final Tab child : this.children) {
          tabbedPane.remove(child.tabContentPane);
          child.collapseTab();
        }
      }
      this.setCollapsed(true);
    }

    protected void restoreTabHierarchy() {
      if (this.isCollapsed()) {
        collapseTab();
      }
      for (final Tab t : this.children) {
        t.restoreTabHierarchy();
      }
    }

  }

}
