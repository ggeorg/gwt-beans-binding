/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */
package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ListBoxAdapterProvider implements BeanAdapterProvider {

  private static final String SELECTED_ITEM_P = "selectedItem".intern();
  private static final String SELECTED_ITEMS_P = "selectedItems".intern();
  private static final String SELECTED_ITEM_TEXT_P = "selectedItemText".intern();
  private static final String SELECTED_ITEMS_TEXT_P = "selectedItemsText".intern();
  private static final String SELECTED_ITEM_VALUE_P = "selectedItemValue".intern();
  private static final String SELECTED_ITEMS_VALUE_P = "selectedItemsValue".intern();

  public static final class Adapter extends BeanAdapterBase implements
      ChangeHandler {
    private ListBox list;
    private Object cachedElementOrElements;

    private HandlerRegistration changeHandlerReg;

    private Adapter(ListBox list, String property) {
      super(property);
      this.list = list;
    }

    public Object getSelectedItem() {
      return ListBoxAdapterProvider.getSelectedItem(list);
    }

    public void setSelectedItem(Object item) {
      ListBoxAdapterProvider.setSelectedItem(list, item);
      onChange(null);
    }

    public List<Object> getSelectedItems() {
      return ListBoxAdapterProvider.getSelectedItems(list);
    }

    public void setSelectedItems(List<Object> items) {
      ListBoxAdapterProvider.setSelectedItems(list, items);
      onChange(null);
    }

    public String getSelectedItemText() {
      return ListBoxAdapterProvider.getSelectedItemText(list);
    }

    public List<String> getSelectedItemsText() {
      return ListBoxAdapterProvider.getSelectedItemsText(list);
    }

    public String getSelectedItemValue() {
      return ListBoxAdapterProvider.getSelectedItemValue(list);
    }

    public List<String> getSelectedItemsValue() {
      return ListBoxAdapterProvider.getSelectedItemsValue(list);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      if (property == SELECTED_ITEM_P) {
        cachedElementOrElements = getSelectedItem();
      } else if (property == SELECTED_ITEM_TEXT_P) {
        cachedElementOrElements = getSelectedItem();
      } else if (property == SELECTED_ITEM_VALUE_P) {
        cachedElementOrElements = getSelectedItemValue();
      } else if (property == SELECTED_ITEMS_P) {
        cachedElementOrElements = getSelectedItems();
      } else if (property == SELECTED_ITEMS_TEXT_P) {
        cachedElementOrElements = getSelectedItems();
      } else if (property == SELECTED_ITEMS_VALUE_P) {
        cachedElementOrElements = getSelectedItemsValue();
      }
      changeHandlerReg = list.addChangeHandler(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStopped()
     */
    @Override
    protected void listeningStopped() {
      if (changeHandlerReg != null) {
        changeHandlerReg.removeHandler();
        changeHandlerReg = null;
      }
      cachedElementOrElements = null;
    }

    public void onChange(ChangeEvent event) {
      Object oldElementOrElements = cachedElementOrElements;
      if (property == SELECTED_ITEM_P) {
        cachedElementOrElements = getSelectedItem();
      } else if (property == SELECTED_ITEM_TEXT_P) {
        cachedElementOrElements = getSelectedItem();
      } else if (property == SELECTED_ITEM_VALUE_P) {
        cachedElementOrElements = getSelectedItemValue();
      } else if (property == SELECTED_ITEMS_P) {
        cachedElementOrElements = getSelectedItems();
      } else if (property == SELECTED_ITEMS_TEXT_P) {
        cachedElementOrElements = getSelectedItems();
      } else if (property == SELECTED_ITEMS_VALUE_P) {
        cachedElementOrElements = getSelectedItemsValue();
      }
      firePropertyChange(oldElementOrElements, cachedElementOrElements);
    }
  }

  private static Object getSelectedItem(ListBox list) {
    assert list != null;

    int index = list.getSelectedIndex();

    Object model = list.getElement().getPropertyObject("model");
    if (model instanceof ListBindingManager) {
      ListBindingManager lbm = (ListBindingManager) model;
      if (index != -1) {
        return lbm.getElement(index);
      } else {
        return null;
      }
    } else {
      return index;
    }
  }

  private static void setSelectedItem(ListBox list, Object item) {
    assert list != null;

    Object model = list.getElement().getPropertyObject("model");
    if (model instanceof ListBindingManager) {
      ListBindingManager lbm = (ListBindingManager) model;
      list.setSelectedIndex(lbm.getElements().indexOf(item));
    } else {
      list.setSelectedIndex((Integer) item);
    }
  }

  private static List<Object> getSelectedItems(ListBox list) {
    assert list != null;

    List<Object> elements = new ArrayList<Object>();

    if (list.getSelectedIndex() == -1) {
      return elements;
    }

    Object model = list.getElement().getPropertyObject("model");
    if (model instanceof ListBindingManager) {
      ListBindingManager lbm = (ListBindingManager) model;
      for (int i = 0, n = list.getItemCount(); i < n; ++i) {
        if (list.isItemSelected(i)) {
          elements.add(lbm.getElement(i));
        }
      }
    } else {
      for (int i = 0, n = list.getItemCount(); i < n; ++i) {
        if (list.isItemSelected(i)) {
          elements.add(i);
        }
      }
    }

    return elements;
  }

  private static void setSelectedItems(ListBox list, List<Object> indexes) {
    assert list != null;

    Object model = list.getElement().getPropertyObject("model");
    if (model instanceof ListBindingManager) {
      ListBindingManager lbm = (ListBindingManager) model;
      for (Object index : indexes) {
        list.setSelectedIndex(lbm.getElements().indexOf(index));
      }
    } else {
      for (Object index : indexes) {
        list.setItemSelected((Integer) index, true);
      }
    }
  }

  private static String getSelectedItemText(ListBox list) {
    assert list != null;

    int index = list.getSelectedIndex();

    if (index == -1) {
      return null;
    }

    return list.getItemText(index);
  }

  private static List<String> getSelectedItemsText(ListBox list) {
    assert list != null;

    List<String> elements = new ArrayList<String>();

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

  private static String getSelectedItemValue(ListBox list) {
    assert list != null;

    int index = list.getSelectedIndex();

    if (index == -1) {
      return null;
    }

    return list.getValue(index);
  }

  private static List<String> getSelectedItemsValue(ListBox list) {
    assert list != null;

    List<String> elements = new ArrayList<String>();

    if (list.getSelectedIndex() == -1) {
      return elements;
    }

    for (int i = 0, n = list.getItemCount(); i < n; ++i) {
      if (list.isItemSelected(i)) {
        elements.add(list.getValue(i));
      }
    }

    return elements;
  }

  /**
   * {@inheritDoc}
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

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == ListBox.class) ? ListBoxAdapterProvider.Adapter.class
        : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    if (type != ListBox.class) {
      return false;
    }

    property = property.intern();

    return property == SELECTED_ITEM_P || property == SELECTED_ITEMS_P
        || property == SELECTED_ITEM_TEXT_P
        || property == SELECTED_ITEMS_TEXT_P
        || property == SELECTED_ITEM_VALUE_P
        || property == SELECTED_ITEMS_VALUE_P;
  }

}
