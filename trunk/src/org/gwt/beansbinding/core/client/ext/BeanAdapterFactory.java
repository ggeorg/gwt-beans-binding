package org.gwt.beansbinding.core.client.ext;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BeanAdapterFactory {

  private static final BeanAdapterFactory INSTANCE = new BeanAdapterFactory();
  private static final List<BeanAdapterProvider> providers = new ArrayList<BeanAdapterProvider>();

  public static BeanAdapterProvider addProvider(BeanAdapterProvider provider) {
    if (providers.indexOf(provider) == -1) {
      providers.add(provider);
      return provider;
    }
    return null;
  }

  public static Object getAdapter(Object source, String property) {
    return INSTANCE.getAdaptor0(source, property);
  }

  public static List<PropertyDescriptor> getAdapterPropertyDescriptors(
      Class<?> type) {
    return INSTANCE.getAdapterPropertyDescriptors0(type);
  }

  // -----------------------------------------------------------------------

  private final Map<Object, List<VendedAdapter>> vendedAdapters;

  private BeanAdapterFactory() {
    vendedAdapters = new HashMap<Object, List<VendedAdapter>>();
  }

  private Object getAdaptor0(Object source, String property) {
    if (source == null || property == null) {
      throw new IllegalArgumentException();
    }

    property = property.intern();

    BeanAdapterProvider provider = getProvider(source, property);

    if (provider != null) {
      List<VendedAdapter> adapters = vendedAdapters.get(source);
      if (adapters != null) {
        for (int i = adapters.size() - 1; i >= 0; i--) {
          VendedAdapter vendedAdapter = adapters.get(i);
          Object adapter = vendedAdapter.getAdapter();

          if (adapter == null) {
            vendedAdapters.remove(i);
          } else if ((vendedAdapter.getProvider() == provider)
              && (vendedAdapter.getProperty() == property)) {
            return adapter;
          }
        }
      } else {
        adapters = new ArrayList<VendedAdapter>(1);
        vendedAdapters.put(source, adapters);
      }

      Object adapter = provider.createAdapter(source, property);
      adapters.add(new VendedAdapter(property, provider, adapter));

      return adapter;
    }
    return null;
  }

  private List<PropertyDescriptor> getAdapterPropertyDescriptors0(Class<?> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type must be non-null");
    }

    ArrayList<PropertyDescriptor> des = new ArrayList<PropertyDescriptor>();

    for (BeanAdapterProvider provider : providers) {
      Class<?> pdType = provider.getAdapterClass(type);

      if (pdType != null) {
        BeanInfo info = getBeanInfo(pdType);

        if (info != null) {
          PropertyDescriptor[] pds = info.getPropertyDescriptors();

          if (pds != null) {
            for (PropertyDescriptor pd : pds) {
              if (provider.providesAdapter(type, pd.getName())) {
                des.add(pd);
              }
            }
          }
        }
      }
    }

    return des;
  }

  private BeanInfo getBeanInfo(Class<?> type) {
    try {
      return Introspector.getBeanInfo(type);
    } catch (IntrospectionException ie) {
      return null;
    }
  }

  private BeanAdapterProvider getProvider(Object source, String property) {
    Class<?> type = source.getClass();
    for (BeanAdapterProvider provider : providers) {
      if (provider.providesAdapter(type, property)) {
        return provider;
      }
    }
    return null;
  }

  private static final class VendedAdapter {
    private final BeanAdapterProvider provider;
    private final String property;
    private final/* WeakReference<Object> */Object adapter;

    public VendedAdapter(String property, BeanAdapterProvider provider,
        Object adapter) {
      this.property = property;
      this.adapter = adapter;
      this.provider = provider;
    }

    public Object getAdapter() {
      return adapter;
    }

    public String getProperty() {
      return property;
    }

    public BeanAdapterProvider getProvider() {
      return provider;
    }
  }
}
