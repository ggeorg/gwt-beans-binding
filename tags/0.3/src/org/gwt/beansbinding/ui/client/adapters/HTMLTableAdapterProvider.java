package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class HTMLTableAdapterProvider implements BeanAdapterProvider {

  private static final String SELECTED_ITEM_P = "selectedItem".intern();
  private static List<Class<? extends HTMLTable>> registry = new ArrayList<Class<? extends HTMLTable>>();

  public static void register(Class<? extends HTMLTable> implementor) {
    if (!registry.contains(implementor)) {
      registry.add(implementor);
    }
  }

  public static final class Adapter extends BeanAdapterBase implements
      ClickHandler {
    private HTMLTable table;
    private Object cachedItem;

    private HandlerRegistration clickHandlerReg;

    protected Adapter(HTMLTable table, String property) {
      super(property);
      this.table = table;
    }

    public Object getSelectedItem() {
      return cachedItem;
    }

    public void setSelectedItem(Object item) {
      Object oldValue = cachedItem;
      cachedItem = item;
      firePropertyChange(oldValue, cachedItem);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      cachedItem = getSelectedItem();
      clickHandlerReg = table.addClickHandler(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStopped()
     */
    @Override
    protected void listeningStopped() {
      if (clickHandlerReg != null) {
        clickHandlerReg.removeHandler();
        clickHandlerReg = null;
      }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    public void onClick(ClickEvent event) {
      Object oldValue = cachedItem;
      Cell cell = table.getCellForEvent(event);
      Object model = table.getElement().getPropertyObject("model");
      if (model instanceof ListBindingManager) {
        ListBindingManager lbm = (ListBindingManager) model;
        cachedItem = lbm.getElement(cell.getRowIndex());
      }
      firePropertyChange(oldValue, cachedItem);
    }
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
    return new Adapter((HTMLTable) source, property);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == HTMLTable.class || registry.contains(type))
        ? HTMLTableAdapterProvider.Adapter.class : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    return (type == HTMLTable.class || registry.contains(type))
        && property.intern() == SELECTED_ITEM_P;
  }

}
