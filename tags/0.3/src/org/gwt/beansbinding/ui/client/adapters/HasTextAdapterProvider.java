package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;

public class HasTextAdapterProvider implements BeanAdapterProvider {

  private static final String TEXT_P = "text".intern();
  private static List<Class<? extends HasText>> registry = new ArrayList<Class<? extends HasText>>();

  public static void register(Class<? extends HasText> implementor) {
    if (!registry.contains(implementor)) {
      registry.add(implementor);
    }
  }

  public static final class Adapter extends BeanAdapterBase implements
      ChangeHandler {
    private HasText textOwner;
    private String cachedText;

    private HandlerRegistration changeHandlerReg;

    protected Adapter(HasText textOwner) {
      super(TEXT_P);
      this.textOwner = textOwner;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.HasText#getText()
     */
    public String getText() {
      return textOwner.getText();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
     */
    public void setText(String text) {
      textOwner.setText(text);
    }

    /**
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      cachedText = getText();
      if (textOwner instanceof HasChangeHandlers) {
        changeHandlerReg = ((HasChangeHandlers) textOwner).addChangeHandler(this);
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
      String oldText = cachedText;
      cachedText = getText();
      firePropertyChange(oldText, cachedText);
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
    return new Adapter((HasText) source);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return HasTextAdapterProvider.Adapter.class;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    return (type == HasText.class || registry.contains(type))
        && property.intern() == TEXT_P;
  }

}
