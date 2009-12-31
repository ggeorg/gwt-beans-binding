/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */
package org.gwt.beansbinding.ui.client.adapters;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @deprecated use {@link HasTextAdapterProvider} instead.
 */
@Deprecated
public final class TextBoxAdapterProvider implements BeanAdapterProvider {

  private static final String TEXT_P = "text";

  public static final class Adapter extends BeanAdapterBase {
    private TextBox textBox;
    private Handler handler;
    private String cachedText;

    private HandlerRegistration changeHandlerReg;

    private Adapter(TextBox textBox) {
      super(TEXT_P);
      this.textBox = textBox;
    }

    public String getText() {
      return textBox.getText();
    }

    public void setText(String text) {
      textBox.setText(text);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
     */
    @Override
    protected void listeningStarted() {
      handler = new Handler();
      cachedText = getText();
      changeHandlerReg = textBox.addChangeHandler(handler);
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
      handler = null;
    }

    private class Handler implements ChangeHandler {
      public void onChange(ChangeEvent event) {
        String oldText = cachedText;
        cachedText = getText();
        firePropertyChange(oldText, cachedText);
      }
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.db.client.beansbinding.ext.BeanAdapterProvider#createAdapter(java.lang.Object,
   *      java.lang.String)
   */
  public BeanAdapter createAdapter(Object source, String property) {
    if (!providesAdapter(source.getClass(), property)) {
      throw new IllegalArgumentException();
    }
    return new Adapter((TextBox) source);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.db.client.beansbinding.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == TextBox.class) ? TextBoxAdapterProvider.Adapter.class
        : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.db.client.beansbinding.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    return (type == TextBox.class) && property.intern() == TEXT_P;
  }

}
