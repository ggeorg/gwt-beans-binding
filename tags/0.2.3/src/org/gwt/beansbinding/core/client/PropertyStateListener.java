package org.gwt.beansbinding.core.client;

import java.util.EventListener;

/**
 * <code>PropertyStateListeners</code> are registered on {@link Property}
 * instances, to be notified when the state of the property changes.
 */
public interface PropertyStateListener extends EventListener {
  /**
   * Called to notify the listener that a change of state has occurred to one of
   * the Property instances upon which the listener is registered.
   * 
   * @param pse an event describing the state change, non-null
   */
  void propertyStateChanged(PropertyStateEvent pse);
}
