package org.gwt.beansbinding.core.client.ext;

import java.beans.PropertyChangeListener;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

public interface BeanAdapter extends HasPropertyChangeSupport {

  void addPropertyChangeListener(String property,
      PropertyChangeListener listener);

  void removePropertyChangeListener(String property,
      PropertyChangeListener listener);
}
