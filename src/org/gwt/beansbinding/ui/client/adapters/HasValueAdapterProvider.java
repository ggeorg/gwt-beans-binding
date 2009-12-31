package org.gwt.beansbinding.ui.client.adapters;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class HasValueAdapterProvider<T> implements BeanAdapterProvider {

  private static final String VALUE_P = "value".intern();
  private static List<Class<? extends HasValue<?>>> registry = new ArrayList<Class<? extends HasValue<?>>>();

  public static <T> void register(Class<? extends HasValue<T>> implementor) {
    if (!registry.contains(implementor)) {
      registry.add(implementor);
    }
  }

  public static final class Adapter<T> extends BeanAdapterBase implements
      ValueChangeHandler<T> {
    private HasValue<T> valueOwner;
    private T cachedValue;

    private HandlerRegistration valueChangeHandlerReg;

    protected Adapter(HasValue<T> valueOwner) {
      super(VALUE_P);
      this.valueOwner = valueOwner;
    }

    public T getValue() {
      return valueOwner.getValue();
    }

    public void setValue(T value) {
      valueOwner.setValue(value, true);
    }

    @Override
    protected void listeningStarted() {
      cachedValue = getValue();
      valueChangeHandlerReg = valueOwner.addValueChangeHandler(this);
    }

    @Override
    protected void listeningStopped() {
      if (valueChangeHandlerReg != null) {
        valueChangeHandlerReg.removeHandler();
        valueChangeHandlerReg = null;
      }
    }

    public void onValueChange(ValueChangeEvent<T> event) {
      T oldValue = cachedValue;
      cachedValue = getValue();
      firePropertyChange(oldValue, cachedValue);
    }

  }

  @SuppressWarnings("unchecked")
  public BeanAdapter createAdapter(Object source, String property) {
    if (!providesAdapter(source.getClass(), property)) {
      throw new IllegalArgumentException();
    }
    return new Adapter((HasValue<Object>) source);
  }

  public Class<?> getAdapterClass(Class<?> type) {
    return (type == HasValue.class || registry.contains(type))
        ? HasValueAdapterProvider.Adapter.class : null;
  }

  public boolean providesAdapter(Class<?> type, String property) {
    return (type == HasValue.class || registry.contains(type))
        && property.intern() == VALUE_P;
  }

}
