package org.gwt.beansbinding.core.client;

import java.util.EventListener;

/**
 * <code>BindingListener</code>s are registered on {@link Binding}s or
 * {@link BindingGroup}s to listen for changes to the state of {@link Binding}s.
 */
public interface BindingListener extends EventListener {

  void bindingBecameBound(Binding binding);
  
  void bindingBecameUnbound(Binding binding);
  
  void sourceChanged(Binding binding, PropertyStateEvent event);
  
  void synced(Binding binding);
  
  void syncFailed(Binding binding, Binding.SyncFailure failure);
  
  void targetChanged(Binding binding, PropertyStateEvent event);
}
