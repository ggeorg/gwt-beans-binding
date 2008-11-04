package org.gwt.beansbinding.core.client.ext;

import java.beans.PropertyChangeListener;

public interface BeanAdapter {

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);

  void addPropertyChangeListener(String property,
      PropertyChangeListener listener);

  void removePropertyChangeListener(String property,
      PropertyChangeListener listener);
}
