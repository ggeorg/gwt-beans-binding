package org.gwt.beansbinding.core.client.util;

import com.google.gwt.core.client.GWT;

public final class GWTBeansBinding extends Object {
  
  public GWTBeansBinding() {
    // Nothing to do here!
  }
  
  public static void init() {
    try {
      GWT.create(GWTBeansBinding.class);
    } catch (Throwable t) {
      // GWT.log(t.getMessage(), t);
    }
  }

}
