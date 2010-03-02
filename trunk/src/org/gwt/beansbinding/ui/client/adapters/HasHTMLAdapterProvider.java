package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHTML;

public class HasHTMLAdapterProvider implements BeanAdapterProvider {

  private static final String HTML_P = "HTML".intern();
  private static List<Class<? extends HasHTML>> registry = new ArrayList<Class<? extends HasHTML>>();

  public static void register(Class<? extends HasHTML> implementor) {
    if (!registry.contains(implementor)) {
      registry.add(implementor);
    }
  }

  public static final class Adapter extends BeanAdapterBase implements
      ChangeHandler {
    private HasHTML htmlOwner;
    private String cachedHTML;

    private HandlerRegistration changeHandlerReg;

    protected Adapter(HasHTML htmlOwner) {
      super(HTML_P);
      this.htmlOwner = htmlOwner;
    }

    
    public String getHTML() {
      return htmlOwner.getHTML();
    }

    public void setHTML(String html) {
      htmlOwner.setHTML(html);
    }

    /**
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      cachedHTML = getHTML();
      if (htmlOwner instanceof HasChangeHandlers) {
        changeHandlerReg = ((HasChangeHandlers) htmlOwner).addChangeHandler(this);
      }
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
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
     */
    public void onChange(ChangeEvent event) {
      String oldHTML = cachedHTML;
      cachedHTML = getHTML();
      firePropertyChange(oldHTML, cachedHTML);
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
    return new Adapter((HasHTML) source);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return HasHTMLAdapterProvider.Adapter.class;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    return (type == HasHTML.class || registry.contains(type))
        && property.intern() == HTML_P;
  }

}
