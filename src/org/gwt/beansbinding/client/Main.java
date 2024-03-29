package org.gwt.beansbinding.client;

import org.gwt.beansbinding.core.client.AbstractBindingListener;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.Validator;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.Binding.SyncFailure;
import org.gwt.beansbinding.core.client.Binding.SyncFailureType;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.core.client.util.AbstractBean;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Main implements EntryPoint {

  static {
    GWTBeansBinding.init();

    // Register all Adapters used in code
    BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
  }

  public class Counter extends AbstractBean {
    int value;

    public Counter() {
      value = 0;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      int oldValue = this.value;
      this.value = value;
      firePropertyChange("value", oldValue, this.value);
    }

    public void increase() {
      setValue(value + 1);
    }

    public void decrease() {
      setValue(value - 1);
    }
  }

  public void onModuleLoad() {
    FlowPanel panel = new FlowPanel();

    TextBox textBox = new TextBox();
    Button btn1 = new Button("Add 1");
    Button btn2 = new Button("Subtract 1");

    panel.add(textBox);
    panel.add(btn1);
    panel.add(btn2);

    // the model

    final Counter counter = new Counter();

    // the binding

    Binding<Counter, Integer, TextBox, String> binding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, counter,
        BeanProperty.<Counter, Integer> create("value"), textBox,
        BeanProperty.<TextBox, String> create("text"));

    // the validator

    binding.setValidator(new Validator<Integer>() {
      public Result validate(Integer value) {
        if (value < 0) {
          return new Result(null, "Please enter a positive value or 0.");
        }
        return null;
      }
    });

    // add a BindingListener

    binding.addBindingListener(new AbstractBindingListener() {
      @Override
      public void syncFailed(Binding binding, SyncFailure failure) {
        SyncFailureType type = failure.getType();
        if (type == SyncFailureType.VALIDATION_FAILED) {
          TextBox textBox = (TextBox) binding.getTargetObject();
          textBox.getElement().getStyle().setBackgroundColor("red");
          textBox.setFocus(true);

          Window.alert(failure.getValidationResult().getDescription());
        }
      }

      @Override
      public void synced(Binding binding) {
        TextBox textBox = (TextBox) binding.getTargetObject();
        textBox.getElement().getStyle().setBackgroundColor("white");
      }
    });

    binding.bind();

    // add the controller

    btn1.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent arg0) {
        counter.increase();
      }
    });

    btn2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent arg0) {
        counter.decrease();
      }
    });

    RootPanel.get().add(panel, 10, 10);
  }
}
