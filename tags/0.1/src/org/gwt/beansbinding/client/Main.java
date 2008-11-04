package org.gwt.beansbinding.client;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.ui.client.adapters.TextBoxAdapterProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {
  
  static {
    try {
      GWT.create(BeanAdapter.class);
    } catch (Throwable t) {
      // GWT.log(t.getMessage(), t);
    }
  }

  public Main() {
    BeanAdapterFactory.addProvider(new TextBoxAdapterProvider());
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final FlowPanel flowPanel = new FlowPanel();

    final TextBox textBox1 = new TextBox();
    textBox1.setText("TextBox1");

    final TextBox textBox2 = new TextBox();
    textBox2.setText("TextBox2");

    BindingGroup bindingGroup = new BindingGroup();
    Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
        textBox1, BeanProperty.create("text"), textBox2,
        BeanProperty.create("text"));
    bindingGroup.addBinding(binding);
    bindingGroup.bind();

    flowPanel.add(textBox1);
    flowPanel.add(textBox2);

    RootPanel.get().add(flowPanel);
  }
}
