package org.gwt.beansbinding.ui.client.adapters;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public final class TextBoxAdapterProvider implements BeanAdapterProvider {

  private static final String TEXT_P = "text";

  public static final class Adapter extends BeanAdapterBase {
    private TextBox textBox;
    private Handler handler;
    private String cachedText;

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

    protected void listeningStarted() {
      handler = new Handler();
      cachedText = getText();
      textBox.addChangeListener(handler);
    }

    protected void listeningStopped() {
      textBox.removeChangeListener(handler);
      handler = null;
    }

    private class Handler implements ChangeListener {
      public void onChange(Widget sender) {
        String oldText = cachedText;
        cachedText = getText();
        firePropertyChange(oldText, cachedText);
      }
    }
    
  }

  /*
   * (non-Javadoc)
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

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.db.client.beansbinding.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == TextBox.class) ? TextBoxAdapterProvider.Adapter.class
        : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.db.client.beansbinding.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    return (type == TextBox.class) && property.intern() == TEXT_P;
  }

}
