/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */
package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ListBoxAdapterProvider implements BeanAdapterProvider {

  private static final String SELECTED_ELEMENT_P = "selectedElement";
  private static final String SELECTED_ELEMENTS_P = "selectedElements";

  public static final class Adapter extends BeanAdapterBase {
    private ListBox list;
    private Handler handler;
    private Object cachedElementOrElements;

    private Adapter(ListBox list, String property) {
      super(property);
      this.list = list;
    }

    private boolean isPlural() {
      return property == SELECTED_ELEMENTS_P;
    }

    public Object getSelectedElement() {
      return ListBoxAdapterProvider.getSelectedElement(list);
    }

    public List<Object> getSelectedElements() {
      return ListBoxAdapterProvider.getSelectedElements(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      handler = new Handler();
      cachedElementOrElements = isPlural() ? getSelectedElements()
          : getSelectedElement();
      list.addChangeListener(handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStopped()
     */
    @Override
    protected void listeningStopped() {
      list.removeChangeListener(handler);
      cachedElementOrElements = null;
      handler = null;
    }

    private class Handler implements ChangeListener {
      public void onChange(Widget sender) {
        Object oldElementOrElements = cachedElementOrElements;
        cachedElementOrElements = isPlural() ? getSelectedElements()
            : getSelectedElement();
        firePropertyChange(oldElementOrElements, cachedElementOrElements);
      }
    }
  }

  private static List<Object> getSelectedElements(ListBox list) {
    assert list != null;

    List<Object> elements = new ArrayList<Object>();

    if (list.getSelectedIndex() == -1) {
      return elements;
    }

    for (int i = 0, n = list.getItemCount(); i < n; ++i) {
      if (list.isItemSelected(i)) {
        elements.add(list.getItemText(i));
      }
    }

    return elements;
  }

  private static Object getSelectedElement(ListBox list) {
    assert list != null;

    int index = list.getSelectedIndex();

    if (index == -1) {
      return null;
    }

    return list.getItemText(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#createAdapter(java.lang.Object,
   *      java.lang.String)
   */
  public BeanAdapter createAdapter(Object source, String property) {
    if (!providesAdapter(source.getClass(), property)) {
      throw new IllegalArgumentException();
    }
    return new Adapter((ListBox) source, property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == ListBox.class) ? ListBoxAdapterProvider.Adapter.class
        : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    if (type != ListBox.class) {
      return false;
    }

    property = property.intern();

    return property == SELECTED_ELEMENT_P || property == SELECTED_ELEMENTS_P;
  }

}
