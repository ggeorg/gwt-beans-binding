# Introduction #

At the end of this page, you'll have an application that contains a Grid filled with data from a java.util.List. Whenever you select a row in the Grid, the TextBox(es) below the Grid are filled with the data from the currently selected row, and of course you can edit the selected row data:

![http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding1.png](http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding1.png)

# Construct the Model #

First, we create the Java Bean that extends AbstractBean:

```
  public class User extends AbstractBean {
    private int id;
    private String firstName;
    private String lastName;

    public User() {
      // Nothing to do here!
    }

    public User(int id, String firstName, String lastName) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      int oldValue = this.id;
      this.id = id;
      firePropertyChange("id", oldValue, this.id);
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      String oldValue = this.firstName;
      this.firstName = firstName;
      firePropertyChange("firstName", oldValue, this.firstName);
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      String oldValue = this.lastName;
      this.lastName = lastName;
      firePropertyChange("lastName", oldValue, this.lastName);
    }
  }
```

Our model looks like:

```
    final List<User> users = ObservableCollections.observableList(new ArrayList<User>());
    users.add(new User(1, "George", "Georgopoulos"));
    users.add(new User(2, "Maria", "Matsouka"));
    users.add(new User(3, "Evi", "Matsouka"));
    users.add(new User(4, "Davidoula", "Georgopoulou"));
```

_Note that, ObservableCollections provides factory methods for creating observable lists and maps. An Observable collection notifies listeners of changes to the contained elements._

# Bind the Model to the View #

Here is the code to create the UI and the bindings to the model:

```
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.util.AbstractBean;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.beansbinding.ui.client.GWTBindings;
import org.gwt.beansbinding.ui.client.GridBinding;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;

public class Test implements EntryPoint {

  static {
    GWTBeansBinding.init();
  }

  public class User extends AbstractBean {
    private int id;
    private String firstName;
    private String lastName;

    public User() {
      // Nothing to do here!
    }

    public User(int id, String firstName, String lastName) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      int oldValue = this.id;
      this.id = id;
      firePropertyChange("id", oldValue, this.id);
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      String oldValue = this.firstName;
      this.firstName = firstName;
      firePropertyChange("firstName", oldValue, this.firstName);
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      String oldValue = this.lastName;
      this.lastName = lastName;
      firePropertyChange("lastName", oldValue, this.lastName);
    }
  }

  public void onModuleLoad() {
    final List<User> users = ObservableCollections.observableList(new ArrayList<User>());
    users.add(new User(1, "George", "Georgopoulos"));
    users.add(new User(2, "Maria", "Matsouka"));
    users.add(new User(3, "Evi", "Matsouka"));
    users.add(new User(4, "Davidoula", "Georgopoulou"));

    final Grid grid = new Grid();
    grid.setCellSpacing(0);
    grid.setBorderWidth(1);

    GridBinding<User, List<User>, Grid> gridBinding = GWTBindings.createGridBinding(
        UpdateStrategy.READ, users, grid);

    // define the property to be used to derive list elements
    BeanProperty<User, Integer> idP = BeanProperty.create("id");
    BeanProperty<User, String> firstNameP = BeanProperty.create("firstName");
    BeanProperty<User, String> lastNameP = BeanProperty.create("lastName");

    // add the detail binding
    gridBinding.addColumnBinding(idP);
    gridBinding.addColumnBinding(firstNameP);
    gridBinding.addColumnBinding(lastNameP);

    gridBinding.bind();

    FlowPanel panel = new FlowPanel();
    panel.add(grid);

    RootPanel.get().add(panel, 10, 10);
  }
}
```

It looks like this:

![http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding2.png](http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding2.png)

GridBinding binds a java.util.List of objects to act as the rows of a GWT Grid widget. Each object in the source List represents one row in the Grid. Mappings from properties of the source objects to columns are created by adding ColumnBinding(s) to a GridBinding instance.

# Bind to the selection of a Grid #

In addition to binding the elements of a Grid, it is possible to bind to the selection of a Grid. When binding to the selection of a Grid backed by a GridBinding, the selection is always in terms of elements from the source List.

Before we bind to the selection of a Grid, lets add a simple selection model to the Grid similar to the [Mail](http://gwt.google.com/samples/Mail/Mail.html) demo:

```
  private int selectedRow = -1;

  private void addSimpleSelectionModel(final Grid grid) {
    grid.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Cell cell = grid.getCellForEvent(event);
        if (cell != null) {
          if (selectedRow != -1) {
            DOM.setStyleAttribute(
                grid.getRowFormatter().getElement(selectedRow),
                "backgroundColor", "");
          }
          selectedRow = cell.getRowIndex();
          if (selectedRow != -1) {
            DOM.setStyleAttribute(
                grid.getRowFormatter().getElement(selectedRow),
                "backgroundColor", "#adcce7");
          }
        }
      }
    });
  }
```

![http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding3.png](http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding3.png)

To bind to the selection of a grid we have to use a HTMLTableAdapterProvider, and, because we want to edit the selected row with a TextBox we have to use a HasTextAdapterProvider. This is because any property of a class that conforms to the Java Beans specification is an excellent candidate for use by BeanProperty and other Property implementations that resolve properties in a similar manner. But adapters have to be registered for a handful of properties that don't correctly conform to the specification (in this case, don't fire property change notification). Those adapters may provide synthetic properties (e.g.: selectedItem), so that they can be used.

Add the following lines after GWTBeansBinding.init():

```
    // Register all Adapters used in code
    BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
    BeanAdapterFactory.addProvider(new HTMLTableAdapterProvider());
```

Next, we create the two TextBox widgets, one for first name and one for last name and add them in the UI:

```
    TextBox firstNameTB = new TextBox();
    TextBox lastNameTB = new TextBox();

    ...

    FlowPanel panel = new FlowPanel();
    panel.add(grid);
    panel.add(new Label("First Name:"));
    panel.add(firstNameTB);
    panel.add(new Label("Last Name:"));
    panel.add(lastNameTB);
```

and bind them to the Grid's selection:

```
    // bind to the selection of grid
    
    TextBox firstNameTB = new TextBox();

    Binding<Grid, User, TextBox, String> firstNameBinding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, grid,
        BeanProperty.<Grid, User> create("selectedItem.firstName"),
        firstNameTB, BeanProperty.<TextBox, String> create("text"));

    TextBox lastNameTB = new TextBox();

    Binding<Grid, User, TextBox, String> lastNameBinding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, grid,
        BeanProperty.<Grid, User> create("selectedItem.lastName"), lastNameTB,
        BeanProperty.<TextBox, String> create("text"));

    // create a group all of bindings
    BindingGroup bindingGroup = new BindingGroup();
    bindingGroup.addBinding(gridBinding);
    bindingGroup.addBinding(firstNameBinding);
    bindingGroup.addBinding(lastNameBinding);
    bindingGroup.bind();
```

At this point our application is ready. The Grid is filled with data from a java.util.List and we can select and edit the individual rows:

![http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding4.png](http://gwt-beans-binding.googlecode.com/svn/wiki/grid-binding4.png)

The final file looks like:

```
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.core.client.ext.BeanAdapterFactory;
import org.gwt.beansbinding.core.client.util.AbstractBean;
import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.beansbinding.ui.client.GWTBindings;
import org.gwt.beansbinding.ui.client.GridBinding;
import org.gwt.beansbinding.ui.client.adapters.HTMLTableAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;
import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Test implements EntryPoint {

  static {
    GWTBeansBinding.init();
    
    // Register all Adapters used in code
    BeanAdapterFactory.addProvider(new HasTextAdapterProvider());
    BeanAdapterFactory.addProvider(new HTMLTableAdapterProvider());
  }

  public class User extends AbstractBean {
    private int id;
    private String firstName;
    private String lastName;

    public User() {
      // Nothing to do here!
    }

    public User(int id, String firstName, String lastName) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      int oldValue = this.id;
      this.id = id;
      firePropertyChange("id", oldValue, this.id);
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      String oldValue = this.firstName;
      this.firstName = firstName;
      firePropertyChange("firstName", oldValue, this.firstName);
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      String oldValue = this.lastName;
      this.lastName = lastName;
      firePropertyChange("lastName", oldValue, this.lastName);
    }
  }

  public void onModuleLoad() {
    final List<User> users = ObservableCollections.observableList(new ArrayList<User>());
    users.add(new User(1, "George", "Georgopoulos"));
    users.add(new User(2, "Maria", "Matsouka"));
    users.add(new User(3, "Evi", "Matsouka"));
    users.add(new User(4, "Davidoula", "Georgopoulou"));

    final Grid grid = new Grid();
    grid.setCellSpacing(0);
    grid.setBorderWidth(1);

    addSimpleSelectionModel(grid);

    GridBinding<User, List<User>, Grid> gridBinding = GWTBindings.createGridBinding(
        UpdateStrategy.READ, users, grid);

    // define the property to be used to derive list elements
    BeanProperty<User, Integer> idP = BeanProperty.create("id");
    BeanProperty<User, String> firstNameP = BeanProperty.create("firstName");
    BeanProperty<User, String> lastNameP = BeanProperty.create("lastName");

    // add the detail binding
    gridBinding.addColumnBinding(idP);
    gridBinding.addColumnBinding(firstNameP);
    gridBinding.addColumnBinding(lastNameP);
    
    // bind to the selection of grid
    
    TextBox firstNameTB = new TextBox();

    Binding<Grid, User, TextBox, String> firstNameBinding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, grid,
        BeanProperty.<Grid, User> create("selectedItem.firstName"),
        firstNameTB, BeanProperty.<TextBox, String> create("text"));

    TextBox lastNameTB = new TextBox();

    Binding<Grid, User, TextBox, String> lastNameBinding = Bindings.createAutoBinding(
        UpdateStrategy.READ_WRITE, grid,
        BeanProperty.<Grid, User> create("selectedItem.lastName"), lastNameTB,
        BeanProperty.<TextBox, String> create("text"));

    // create a group all of bindings
    BindingGroup bindingGroup = new BindingGroup();
    bindingGroup.addBinding(gridBinding);
    bindingGroup.addBinding(firstNameBinding);
    bindingGroup.addBinding(lastNameBinding);
    bindingGroup.bind();

    FlowPanel panel = new FlowPanel();
    panel.add(grid);
    panel.add(new Label("First Name:"));
    panel.add(firstNameTB);
    panel.add(new Label("Last Name:"));
    panel.add(lastNameTB);

    RootPanel.get().add(panel, 10, 10);
  }

  private int selectedRow = -1;

  private void addSimpleSelectionModel(final Grid grid) {
    grid.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Cell cell = grid.getCellForEvent(event);
        if (cell != null) {
          if (selectedRow != -1) {
            DOM.setStyleAttribute(
                grid.getRowFormatter().getElement(selectedRow),
                "backgroundColor", "");
          }
          selectedRow = cell.getRowIndex();
          if (selectedRow != -1) {
            DOM.setStyleAttribute(
                grid.getRowFormatter().getElement(selectedRow),
                "backgroundColor", "#adcce7");
          }
        }
      }
    });
  }

}
```

&lt;wiki:gadget url="http://mosaic.arkasoft.com/gwt-mosaic-wiki.xml?v=3" height="95" width="728" border="0"/&gt;