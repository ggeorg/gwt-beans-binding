package org.gwt.beansbinding.core.client.util;

import java.beans.PropertyChangeListener;

public interface HasPropertyChangeSupport {

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);

}
