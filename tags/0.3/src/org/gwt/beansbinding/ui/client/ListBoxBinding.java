/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */

package org.gwt.beansbinding.ui.client;

import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.ObjectProperty;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.core.client.util.Parameters;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Binds a {@code List} of objects to act as the elements of a {@code ListBox}.
 * Each object in the source {@code List} provides one element in the {@code
 * ListBox}. By setting a
 * {@link org.jdesktop.swingbinding.ListBoxBinding.DetailBinding DetailBinding}
 * you can specify the property to use to derive each list element from its
 * corresponding object in the source {@code List}. The default {@code
 * DetailBinding} uses the objects directly. Instances of {@code ListBoxBinding}
 * are obtained by calling one of the {@code createListBoxBinding} methods in
 * the {@code SwingBindings} class.
 * <p>
 * Here is an example of creating a binding from a {@code List} of {@code
 * Person} objects to a {@code ListBox}:
 * <p>
 * 
 * <pre><code>
 *    // create the person list
 *    List<Person> people = createPersonList();
 *
 *    // create the binding from List to ListBox
 *    ListBoxBinding lb = SwingBindings.createListBoxBinding(READ, people, jList);
 *
 *    // define the property to be used to derive list elements
 *    ELProperty fullNameP = ELProperty.create("${firstName} ${lastName}");
 *
 *    // add the detail binding
 *    lb.setDetailBinding(fullNameP);
 *
 *    // realize the binding
 *    lb.bind();
 * </code></pre>
 * 
 * <p>
 * The {@code ListBox} target of a {@code ListBoxBinding} acts as a live view of
 * the objects in the source {@code List}, regardless of the update strategy
 * (the meaning of the update strategy is <a href="#CLARIFICATION">clarified
 * later</a> in this document). {@code ListBoxBinding} listens to the property
 * specified for any {@code DetailBinding}, for all objects in the {@code List},
 * and updates the values displayed in the {@code ListBox} in response to
 * change. If the {@code List} is an instance of {@code ObservableList}, then
 * changes to the {@code List} contents (such as adding, removing or replacing
 * an object) are also reflected in the {@code ListBox}. <b>Important:</b>
 * Changing the contents of a non-observable {@code List} while it is
 * participating in a {@code ListBoxBinding} is unsupported, resulting in
 * undefined behavior and possible exceptions.
 * <p>
 * <a name="CLARIFICATION">{@code ListBoxBinding} requires</a> extra
 * clarification on the operation of the {@code refresh} and {@code save}
 * methods and the meaning of the update strategy. The target property of a
 * {@code ListBoxBinding} is not the target {@code ListBox} property provided in
 * the constructor, but rather a private synthetic property representing the
 * {@code List} of objects to show in the target {@code ListBox}. This synthetic
 * property is readable/writeable only when the {@code ListBoxBinding} is bound
 * and the target {@code ListBox} property is readable with a {@code non-null}
 * value.
 * <p>
 * It is this private synthetic property on which the {@code refresh} and
 * {@code save} methods operate; meaning that these methods simply cause syncing
 * between the value of the source {@code List} property and the value of the
 * synthetic target property (representing the {@code List} to be shown in the
 * target {@code ListBox}). These methods do not, therefore, have anything to do
 * with refreshing <i>values</i> in the {@code ListBox}. Likewise, the update
 * strategy, which simply controls when {@code refresh} and {@code save} are
 * automatically called, also has nothing to do with refreshing <i>values</i> in
 * the {@code ListBox}.
 * <p>
 * <b>Note:</b> At the current time, the {@code READ_WRITE} update strategy is
 * not useful for {@code ListBoxBinding}. To prevent unwanted confusion, {@code
 * READ_WRITE} is translated to {@code READ} by {@code ListBoxBinding's}
 * constructor.
 * <p>
 * {@code ListBoxBinding} works by installing a custom model on the target
 * {@code ListBox}, as appropriate, to represent the source {@code List}. The
 * model is installed on a target {@code ListBox} with the first succesful call
 * to {@code refresh} with that {@code ListBox} as the target. Subsequent calls
 * to {@code refresh} update the elements in this already-installed model. The
 * model is uninstalled from a target {@code ListBox} when either the {@code
 * ListBoxBinding} is unbound or when the target {@code ListBox} property
 * changes to no longer represent that {@code ListBox}. Note: When the model is
 * uninstalled from a {@code ListBox}, it is replaced with a {@code
 * DefaultListModel}, in order to leave the {@code ListBox} functional.
 * <p>
 * Some of the above is easier to understand with an example. Let's consider a
 * {@code ListBoxBinding} ({@code binding}), with update strategy {@code READ},
 * between a property representing a {@code List} ({@code listP}) and a property
 * representing a {@code ListBox} ({@code jListP}). {@code listP} and {@code
 * jListP} both start off readable, referring to a {@code non-null} {@code List}
 * and {@code non-null} {@code ListBox} respectively. Let's look at what happens
 * for each of a sequence of events:
 * <p>
 * <table border=1>
 * <tr>
 * <th>Sequence</th>
 * <th>Event</th>
 * <th>Result</th>
 * </tr>
 * <tr valign="baseline">
 * <td align="center">1</td>
 * <td>explicit call to {@code binding.bind()}</td>
 * <td>- synthetic target property becomes readable/writeable <br>
 * - {@code refresh()} is called <br>
 * - model is installed on target {@code ListBox}, representing list of objects</td>
 * </tr>
 * <tr valign="baseline">
 * <td align="center">2</td>
 * <td>{@code listP} changes to a new {@code List}</td>
 * <td>- {@code refresh()} is called <br>
 * - model is updated with new list of objects</td>
 * </tr>
 * <tr valign="baseline">
 * <td align="center"><a name="STEP3" href="#NOTICE">3</a></td>
 * <td>{@code jListP} changes to a new {@code ListBox}</td>
 * <td>- model is uninstalled from old {@code ListBox}</td>
 * </tr>
 * <tr valign="baseline">
 * <td align="center">4</td>
 * <td>explicit call to {@code binding.refresh()}</td>
 * <td>- model is installed on target {@code ListBox}, representing list of
 * objects</td>
 * </tr>
 * <tr valign="baseline">
 * <td align="center">5</td>
 * <td>{@code listP} changes to a new {@code List}</td>
 * <td>- {@code refresh()} is called <br>
 * - model is updated with new list of objects</td>
 * </tr>
 * <tr valign="baseline">
 * <td align="center">6</td>
 * <td>explicit call to {@code binding.unbind()}</td>
 * <td>- model is uninstalled from target {@code ListBox}</td>
 * </tr>
 * </table>
 * <p>
 * <a name="NOTICE">Notice</a> that in <a href="#STEP3">step 3</a>, when the
 * value of the {@code ListBox} property changed, the new {@code ListBox} did
 * not automatically get the model with the elements applied to it. A change to
 * the target value should not cause an {@code AutoBinding} to sync the target
 * from the source. Step 4 forces a sync by explicitly calling {@code refresh}.
 * Alternatively, it could be caused by any other action that results in a
 * {@code refresh} (for example, the source property changing value, or an
 * explicit call to {@code unbind} followed by {@code bind}).
 * <p>
 * {@code DetailBindings} are managed by the {@code ListBox}. They are not to be
 * explicitly bound, unbound, added to a {@code BindingGroup}, or accessed in a
 * way that is not allowed for a managed binding.
 * <p>
 * In addition to binding the elements of a {@code ListBox}, it is possible to
 * bind to the selection of a {@code ListBox}. When binding to the selection of
 * a {@code ListBox} backed by a {@code ListBoxBinding}, the selection is always
 * in terms of elements from the source {@code List}, regardless of any {@code
 * DetailBinding} specified. See the list of <a
 * href="package-summary.html#SWING_PROPS"> interesting swing properties</a> in
 * the package summary for more details.
 * 
 * @param <E> the type of elements in the source {@code List}
 * @param <SS> the type of source object (on which the source property resolves
 *          to {@code List})
 * @param <TS> the type of target object (on which the target property resolves
 *          to {@code ListBox})
 * 
 * @author Shannon Hickey
 */
@SuppressWarnings("unchecked")
public final class ListBoxBinding<E, SS, TS> extends
    AutoBinding<SS, List<E>, TS, List> {

  private Property<TS, ? extends ListBox> listP;
  private ElementsProperty<TS> elementsP;
  private Handler handler = new Handler();
  private ListBox list;
  private BindingListModel model;
  private DetailBinding itemTextBinding, itemValueBinding;
  private final Property DETAIL_PROPERTY = new Property() {
    public Class<Object> getWriteType(Object source) {
      return Object.class;
    }

    public Object getValue(Object source) {
      throw new UnsupportedOperationException();
    }

    public void setValue(Object source, Object value) {
      throw new UnsupportedOperationException();
    }

    public boolean isReadable(Object source) {
      throw new UnsupportedOperationException();
    }

    public boolean isWriteable(Object source) {
      return true;
    }

    public void addPropertyStateListener(Object source,
        PropertyStateListener listener) {
      throw new UnsupportedOperationException();
    }

    public void removePropertyStateListener(Object source,
        PropertyStateListener listener) {
      throw new UnsupportedOperationException();
    }

    public PropertyStateListener[] getPropertyStateListeners(Object source) {
      throw new UnsupportedOperationException();
    }
  };

  /**
   * Constructs an instance of {@code ListBoxBinding}.
   * 
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          the {@code List} of elements
   * @param targetObject the target object
   * @param targetListBoxProperty a property on the target object that resolves
   *          to a {@code ListBox}
   * @param name a name for the {@code ListBoxBinding}
   * @throws IllegalArgumentException if the source property or target property
   *           is {@code null}
   */
  protected ListBoxBinding(UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, TS targetObject,
      Property<TS, ? extends ListBox> targetListBoxProperty, String name) {
    super(strategy == UpdateStrategy.READ_WRITE ? UpdateStrategy.READ
        : strategy, sourceObject, sourceListProperty, targetObject,
        new ElementsProperty<TS>(), name);

    Parameters.checkNotNull(targetListBoxProperty, "targetListBoxProperty");

    listP = targetListBoxProperty;
    elementsP = (ElementsProperty<TS>) getTargetProperty();
    setItemTextBinding(null);
  }

  @Override
  protected void bindImpl() {
    elementsP.setAccessible(isListBoxAccessible());
    listP.addPropertyStateListener(getTargetObject(), handler);
    elementsP.addPropertyStateListener(null, handler);
    super.bindImpl();
  }

  @Override
  protected void unbindImpl() {
    elementsP.removePropertyStateListener(null, handler);
    listP.removePropertyStateListener(getTargetObject(), handler);
    elementsP.setAccessible(false);
    cleanupForLast();
    super.unbindImpl();
  }

  private boolean isListBoxAccessible() {
    return listP.isReadable(getTargetObject())
        && (listP.getValue(getTargetObject()) != null);
  }

  private boolean isListAccessible(Object value) {
    return value != null && (value != PropertyStateEvent.UNREADABLE);
  }

  private void cleanupForLast() {
    if (list == null) {
      return;
    }

    resetListSelection();

    // XXX list.setModel(new DefaultListModel());
    setModel(null);

    list = null;
    model.setElements(null, true);
    model = null;
  }

  /**
   * Creates a {@code DetailBinding} and sets it as the {@code DetailBinding}
   * for this {@code ListBoxBinding}. A {@code DetailBinding} specifies the
   * property of the objects in the source {@code List} to be used as the
   * elements of the {@code ListBox}. If the {@code detailProperty} parameter is
   * {@code null}, the {@code DetailBinding} specifies that the objects
   * themselves be used.
   * 
   * @param detailProperty the property with which to derive each list value
   *          from its corresponding object in the source {@code List}
   * @return the {@code DetailBinding}
   */
  public DetailBinding setItemTextBinding(Property<E, ?> detailProperty) {
    return setItemTextBinding(detailProperty, null);
  }

  /**
   * Creates a named {@code DetailBinding} and sets it as the {@code
   * DetailBinding} for this {@code ListBoxBinding}. A {@code DetailBinding}
   * specifies the property of the objects in the source {@code List} to be used
   * as the elements of the {@code ListBox}. If the {@code detailProperty}
   * parameter is {@code null}, the {@code DetailBinding} specifies that the
   * objects themselves be used.
   * 
   * @param detailProperty the property with which to derive each list value
   *          from its corresponding object in the source {@code List}
   * @return the {@code DetailBinding}
   */
  public DetailBinding setItemTextBinding(Property<E, ?> detailProperty,
      String name) {
    throwIfBound();

    if ((name == null) && (ListBoxBinding.this.getName() != null)) {
      name = ListBoxBinding.this.getName() + ".DETAIL_BINDING";
    }

    itemTextBinding = (detailProperty == null) ? new DetailBinding(
        ObjectProperty.<E> create(), name) : new DetailBinding(detailProperty,
        name);

    return itemTextBinding;
  }

  /**
   * Returns the {@code DetailBinding} for this {@code ListBoxBinding}. A
   * {@code DetailBinding} specifies the property of the source {@code List}
   * elements to be used as the elements of the {@code ListBox}.
   * 
   * @return the {@code DetailBinding}
   * @see #setItemTextBinding(Property, String)
   */
  public DetailBinding getItemTextBinding() {
    return itemTextBinding;
  }

  public DetailBinding setItemValueBinding(Property<E, ?> detailProperty) {
    return setItemValueBinding(detailProperty, null);
  }

  public DetailBinding setItemValueBinding(Property<E, ?> detailProperty,
      String name) {
    throwIfBound();

    if ((name == null) && (ListBoxBinding.this.getName() != null)) {
      name = ListBoxBinding.this.getName() + ".DETAIL_BINDING";
    }

    itemValueBinding = (detailProperty == null) ? new DetailBinding(
        ObjectProperty.<E> create(), name) : new DetailBinding(detailProperty,
        name);

    return itemValueBinding;
  }

  public DetailBinding getItemValueBinding() {
    return itemValueBinding;
  }

  private void resetListSelection() {
    // ListSelectionModel selectionModel = list.getSelectionModel();
    // selectionModel.setValueIsAdjusting(true);
    // selectionModel.clearSelection();
    // selectionModel.setAnchorSelectionIndex(-1);
    // selectionModel.setLeadSelectionIndex(-1);
    // selectionModel.setValueIsAdjusting(false);
    for (int i = 0, n = list.getItemCount(); i < n; i++) {
      list.setItemSelected(i, false);
    }
  }

  /**
   * {@code DetailBinding} represents a binding between a property of the
   * elements in the {@code ListBoxBinding's} source {@code List}, and the
   * values shown in the {@code ListBox}. Values in the {@code ListBox} are
   * aquired by fetching the value of the {@code DetailBinding's} source
   * property for the associated object in the source {@code List}.
   * <p>
   * A {@code Converter} may be specified on a {@code DetailBinding}. Specifying
   * a {@code Validator} is also possible, but doesn't make sense since {@code
   * ListBox} values aren't editable.
   * <p>
   * {@code DetailBindings} are managed by their {@code ListBoxBinding}. They
   * are not to be explicitly bound, unbound, added to a {@code BindingGroup},
   * or accessed in a way that is not allowed for a managed binding.
   * 
   * @see org.jdesktop.swingbinding.ListBoxBinding#setItemTextBinding(Property,
   *      String)
   */
  public final class DetailBinding extends AbstractColumnBinding {
    private DetailBinding(Property<E, ?> detailProperty, String name) {
      super(0, detailProperty, DETAIL_PROPERTY, name);
    }
  }

  private class Handler implements PropertyStateListener {
    public void propertyStateChanged(PropertyStateEvent pse) {
      if (!pse.getValueChanged()) {
        return;
      }

      if (pse.getSourceProperty() == listP) {
        cleanupForLast();

        boolean wasAccessible = isListAccessible(pse.getOldValue());
        boolean isAccessible = isListAccessible(pse.getNewValue());

        if (wasAccessible != isAccessible) {
          elementsP.setAccessible(isAccessible);
        } else if (elementsP.isAccessible()) {
          elementsP.setValueAndIgnore(null, null);
        }
      } else {
        if (((ElementsProperty.ElementsPropertyStateEvent) pse).shouldIgnore()) {
          return;
        }

        if (list == null) {
          list = listP.getValue(getTargetObject());
          resetListSelection();
          model = new BindingListModel();

          // XXX list.setModel(model);
          setModel(model);

        } else {
          resetListSelection();
        }

        model.setElements((List) pse.getNewValue(), true);
      }
    }
  }

  private void setModel(BindingListModel model) {
    list.getElement().setPropertyObject("model", model);
  }

  private final class BindingListModel extends ListBindingManager {

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#getColBindings()
     */
    protected AbstractColumnBinding[] getColBindings() {
      if (itemValueBinding != null) {
        return new AbstractColumnBinding[] {
            getItemTextBinding(), getItemValueBinding()};
      } else {
        return new AbstractColumnBinding[] {getItemTextBinding()};
      }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#allChanged()
     */
    protected void allChanged() {
      list.clear();
      added(0, size());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#valueChanged(int,
     *      int)
     */
    protected void valueChanged(int row, int column) {
      ListItem listItem = getElementAt(row);
      list.setItemText(row, listItem.item.toString());
      list.setValue(row, listItem.value.toString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#added(int,
     *      int)
     */
    protected void added(int row, int length) {
      assert length > 0; // enforced by ListBindingManager

      for (int i = row, n = row + length; i < n; ++i) {
        ListItem listItem = getElementAt(i);
        if (i >= list.getItemCount()) {
          list.addItem(listItem.item.toString(), listItem.value.toString());
        } else {
          list.insertItem(listItem.item.toString(), listItem.value.toString(),
              row);
        }
      }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#removed(int,
     *      int)
     */
    protected void removed(int index, int length) {
      assert length > 0; // enforced by ListBindingManager

      for (int i = index + length - 1, n = index; i >= n; --i) {
        list.removeItem(i);
      }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#changed(int)
     */
    protected void changed(int row) {
      valueChanged(row, 0);
    }

    private ListItem getElementAt(int index) {
      if (columnCount() >= 2) {
        return new ListItem(valueAt(index, 0), valueAt(index, 1));
      } else {
        return new ListItem(valueAt(index, 0), valueAt(index, 0));
      }
    }
  }

  class ListItem {
    private Object item;
    private Object value;

    public ListItem(Object item, Object value) {
      this.item = item;
      this.value = value;
    }
  }
}
