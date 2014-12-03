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

package co.uproot.htabs.custom.custom.components;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JPanel;

/**
 * Custom JPanel to wrap a component which needs to be hidden but the space
 * should still be occupied. This is done by using a CardLayout in which there
 * are two cards; one is the actual component to be hidden and the other card is
 * an empty JPanel. To hide the component call
 * {@link HideableComponentWrapper#hideComponent()} which will switch the card
 * in the CardLayout to empty JPanel. The component can be made invisible and
 * the space can be usable by other components; this is achieved by setting the
 * visibility of this component to false.
 */
public class HideableComponentWrapper extends JPanel {

  final private static String HIDEABLE_COMPONENT = "hideableComponent";
  final private static String EMPTY_COMPONENT = "emptyComponent";
  final private CardLayout cl;

  final private JPanel emptyComponent;

  private boolean componentVisible;

  /**
   * @param hideableComponent
   *          the component to be hidden
   * @param componentVisible
   *          the initial state of the component. <code>true</code> to make the
   *          component visible, <code>false</code> to make it invisible.
   */
  public HideableComponentWrapper(final Component hideableComponent, final boolean componentVisible) {
    this.cl = new CardLayout();
    this.setLayout(cl);
    setOpaque(false);

    this.emptyComponent = new JPanel();
    this.emptyComponent.setOpaque(false);

    add(hideableComponent, HIDEABLE_COMPONENT);
    add(this.emptyComponent, EMPTY_COMPONENT);

    if (componentVisible) {
      this.showComponent();
    } else {
      this.hideComponent();
    }
  }

  /**
   * The hideableComponent will be visible by default, to set the initial state
   * of the hideableComponent manually see
   * {@link HideableComponentWrapper#HideableComponentWrapper(Component, boolean)}
   *
   * @param hideableComponent
   *          the component to be hidden. this component is visible by default
   */
  public HideableComponentWrapper(final Component hideableComponent) {
    this(hideableComponent, false);
  }

  /**
   * @return the state of the hideableComponent, <code>true</code> if the
   *         component is visible, <code>false</code> otherwise.
   */
  public boolean isComponentVisible() {
    return this.componentVisible;
  }

  /**
   * Makes the hideableComponent invisible.
   */
  public void hideComponent() {
    cl.show(this, EMPTY_COMPONENT);
    this.componentVisible = true;
  }

  /**
   * Makes the hideableComponent visible, and calls
   * <code>setVisible(true)</code> if calling <code>isVisible()</code> on the
   * wrapper returns <code>false</code>.
   */
  public void showComponent() {
    cl.show(this, HIDEABLE_COMPONENT);
    this.componentVisible = true;
    if (!this.isVisible()) {
      this.setVisible(true);
    }
  }
}
