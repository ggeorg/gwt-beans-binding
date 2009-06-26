package org.gwt.beansbinding.ui.client;

import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.ObjectProperty;
import org.gwt.beansbinding.core.client.Property;

import com.google.gwt.user.client.ui.ListBox;

/**
 * A factory class for creating instances of the custom GWT-Mosaic
 * {@code Binding} implementations provided by this package.
 */
public class GWTBindings {

  private GWTBindings() {
    // Nothing to do here!
  }

  /**
   * Creates a {@code ListBoxBinding} from direct references to a {@code List}
   * and {@code ListBox}.
   * 
   * @param <E>
   * @param strategy the update strategy
   * @param sourceList the source {@code List}
   * @param targetListBox the target {@code ListBox}
   * @return the {@code ListBoxBinding}
   */
  public static <E> ListBoxBinding<E, List<E>, ListBox> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, List<E> sourceList,
      ListBox targetListBox) {
    return new ListBoxBinding<E, List<E>, ListBox>(strategy, sourceList,
        ObjectProperty.<List<E>> create(), targetListBox,
        ObjectProperty.<ListBox> create(), null);
  }

  /**
   * Creates a named {@code ListBoxBinding} from direct references to a
   * {@code List} and {@code ListBox}.
   * 
   * @param <E>
   * @param strategy the update strategy
   * @param sourceList the source {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   */
  public static <E> ListBoxBinding<E, List<E>, ListBox> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, List<E> sourceList,
      ListBox targetListBox, String name) {
    return new ListBoxBinding<E, List<E>, ListBox>(strategy, sourceList,
        ObjectProperty.<List<E>> create(), targetListBox,
        ObjectProperty.<ListBox> create(), name);
  }

  /**
   * Creates {@code ListBoxBinding} from an object and property that resolves to
   * {@code List} and a direct reference to a {@code ListBox}.
   * 
   * @param <E>
   * @param <SS>
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          a {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   * @throws IllegalArgumentException if {@code sourceListProperty} is
   *           {@code null}
   */
  public static <E, SS> ListBoxBinding<E, SS, ListBox> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, ListBox targetListBox) {
    return new ListBoxBinding<E, SS, ListBox>(strategy, sourceObject,
        sourceListProperty, targetListBox, ObjectProperty.<ListBox> create(),
        null);
  }

  /**
   * Creates a named {@code ListBoxBinding} from an object and property that
   * resolves to {@code List} and a direct reference to a {@code ListBox}.
   * 
   * @param <E>
   * @param <SS>
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          a {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   * @throws IllegalArgumentException if {@code sourceListProperty} is
   *           {@code null}
   */
  public static <E, SS> ListBoxBinding<E, SS, ListBox> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, ListBox targetListBox,
      String name) {
    return new ListBoxBinding<E, SS, ListBox>(strategy, sourceObject,
        sourceListProperty, targetListBox, ObjectProperty.<ListBox> create(),
        name);
  }
}
