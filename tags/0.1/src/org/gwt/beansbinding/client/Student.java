package org.gwt.beansbinding.client;

import java.beans.PropertyChangeListener;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

public class Student implements BeanAdapter {
  private String firstName;

  public Student() {
    super();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

  public void addPropertyChangeListener(String property,
      PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

  public void removePropertyChangeListener(String property,
      PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

}