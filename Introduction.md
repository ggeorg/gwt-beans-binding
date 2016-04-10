# Introduction #

GWT Beans Binding lets you bind, or link, properties so that when one property changes, all properties bounded to it will automatically change as well.

Let's start with a simple program to demonstrate binding. It looks like this:

![http://gwt-beans-binding.googlecode.com/svn/wiki/binding1.png](http://gwt-beans-binding.googlecode.com/svn/wiki/binding1.png)

Java code:
```
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {

  public void onModuleLoad() {
    FlowPanel panel = new FlowPanel();

    Label label = new Label("0");
    Button btn1 = new Button("Add 1");
    Button btn2 = new Button("Subtract 1");

    panel.add(label);
    panel.add(btn1);
    panel.add(btn2);

    RootPanel.get().add(panel, 10, 10);
  }
}
```

The object of this program is to activate the buttons to add or subtract 1 from the number shown in the label.

# Model/View/Controller #

One approach to solving this problem in a procedure-oriented language might use this pseudo-code:

  * "Listen" for an action on the Button
  * Extract the text content from the Label
  * Convert to an integer
  * Add (or substract) one
  * Convert the value to a string and put it back into the label

The problem with this approach is that it treats the number as integer part of the label. In MVC terms there is an abstract counter somewhere (our model), the label is a way to view its value, and the buttons control the counter, so that any change to the counter's value is reflected in the view.

GWT Beans Binding is perfectly suited to the Model/View/Controller architecture, because it lets you bind a property of the model to the view. As soon as you change the value of an attribute in the model, the view changes automagically; you don't need to write any code to make it happen.

# Construct the Model #

Here is the additional code to create a Counter class:

```
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
```

_Note, that in GWT Beans Binding to use a bean as a target, just use it as is. To use it as a source you'll need to add support for PropertyChangeListener(s). This is easy to do using the PropertyChangeSupport class or by extending [AbstractBean](http://code.google.com/p/gwt-beans-binding/source/browse/trunk/src/org/gwt/beansbinding/core/client/util/AbstractBean.java)._

# Bind the Model to the View #

The change to the code looks like this:

```
    ...
    panel.add(btn1);
    panel.add(btn2);

    // the model

    final Counter counter = new Counter();

    // the binding

    Binding<Counter, Integer, Label, String> binding = Bindings.createAutoBinding(
        UpdateStrategy.READ, counter,
        BeanProperty.<Counter, Integer> create("value"), label,
        BeanProperty.<Label, String> create("text"));

    binding.bind();
```

# And the Controller #

Finally, add a ClickHandler to each Button so that it can control the model:

```
    ...
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
```

The entire file looks like this:

```
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.core.client.util.AbstractBean;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

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

    Label label = new Label("0");
    Button btn1 = new Button("Add 1");
    Button btn2 = new Button("Subtract 1");

    panel.add(label);
    panel.add(btn1);
    panel.add(btn2);

    // the model

    final Counter counter = new Counter();

    // the binding

    Binding<Counter, Integer, Label, String> binding = Bindings.createAutoBinding(
        UpdateStrategy.READ, counter,
        BeanProperty.<Counter, Integer> create("value"), label,
        BeanProperty.<Label, String> create("text"));

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
```

&lt;wiki:gadget url="http://mosaic.arkasoft.com/gwt-mosaic-wiki.xml?v=3" height="95" width="728" border="0"/&gt;