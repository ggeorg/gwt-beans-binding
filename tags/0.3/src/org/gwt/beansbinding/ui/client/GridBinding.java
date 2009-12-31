package org.gwt.beansbinding.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.core.client.util.Parameters;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;

import com.google.gwt.user.client.ui.Grid;

@SuppressWarnings("unchecked")
public final class GridBinding<E, SS, TS> extends
    AutoBinding<SS, List<E>, TS, List> {

  private Property<TS, ? extends Grid> tableP;
  private ElementsProperty<TS> elementsP;
  private Handler handler = new Handler();
  private Grid grid;
  private BindingTableModel model;
  private boolean editable = true;
  private List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();

  protected GridBinding(UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, TS targetObject,
      Property<TS, ? extends Grid> targetGridProperty, String name) {
    super((strategy == UpdateStrategy.READ_WRITE) ? UpdateStrategy.READ
        : strategy, sourceObject, sourceListProperty, targetObject,
        new ElementsProperty<TS>(), name);

    Parameters.checkNotNull(targetGridProperty, "targetGridProperty");

    tableP = targetGridProperty;
    elementsP = (ElementsProperty<TS>) getTargetProperty();
  }

  @Override
  protected void bindImpl() {
    elementsP.setAccessible(isTableAccessible());
    tableP.addPropertyStateListener(getTargetObject(), handler);
    elementsP.addPropertyStateListener(null, handler);
    super.bindImpl();
  }

  @Override
  protected void unbindImpl() {
    elementsP.removePropertyStateListener(null, handler);
    tableP.removePropertyStateListener(getTargetObject(), handler);
    elementsP.setAccessible(false);
    cleanupForLast();
    super.unbindImpl();
  }

  private boolean isTableAccessible() {
    return tableP.isReadable(getTargetObject())
        && (tableP.getValue(getTargetObject()) != null);
  }

  private boolean isTableAccessible(Object value) {
    return (value != null) && (value != PropertyStateEvent.UNREADABLE);
  }

  private void cleanupForLast() {
    if (grid == null) {
      return;
    }

    // XXX table.setModel(new DefaultTableModel());
    setModel(null);

    grid = null;
    model.setElements(null, true);
    model = null;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public boolean isEditable() {
    return editable;
  }

  public ColumnBinding addColumnBinding(Property<E, ?> columnProperty) {
    return addColumnBinding(columnProperty, null);
  }

  public ColumnBinding addColumnBinding(Property<E, ?> columnProperty,
      String name) {
    throwIfBound();

    Parameters.checkNotNull(columnProperty, "columnProperty");

    if ((name == null) && (GridBinding.this.getName() != null)) {
      name = GridBinding.this.getName() + ".COLUMN_BINDING";
    }

    ColumnBinding binding = new ColumnBinding(columnBindings.size(),
        columnProperty, name);
    columnBindings.add(binding);

    return binding;
  }

  public ColumnBinding addColumnBinding(int index, Property<E, ?> columnProperty) {
    return addColumnBinding(index, columnProperty, null);
  }

  public ColumnBinding addColumnBinding(int index,
      Property<E, ?> columnProperty, String name) {
    throwIfBound();

    Parameters.checkNotNull(columnProperty, "columnProperty");

    if ((name == null) && (GridBinding.this.getName() != null)) {
      name = GridBinding.this.getName() + ".COLUMN_BINDING";
    }

    ColumnBinding binding = new ColumnBinding(index, columnProperty, name);
    columnBindings.add(index, binding);
    adjustIndices(index + 1, true);

    return binding;
  }

  public boolean removeColumnBinding(ColumnBinding binding) {
    throwIfBound();

    boolean retVal = columnBindings.remove(binding);

    if (retVal) {
      adjustIndices(binding.getColumn(), false);
    }

    return retVal;
  }

  public ColumnBinding removeColumnBinding(int index) {
    throwIfBound();

    ColumnBinding retVal = columnBindings.remove(index);

    if (retVal != null) {
      adjustIndices(index, false);
    }

    return retVal;
  }

  public ColumnBinding getColumnBinding(int index) {
    return columnBindings.get(index);
  }

  public List<ColumnBinding> getColumnBindings() {
    // XXX return Collections.unmodifiableList(columnBindings);
    return columnBindings;
  }

  private void adjustIndices(int start, boolean up) {
    int size = columnBindings.size();

    for (int i = start; i < size; i++) {
      ColumnBinding cb = columnBindings.get(i);
      cb.adjustColumn(cb.getColumn() + (up ? 1 : (-1)));
    }
  }

  // ------------------------------------------------------------------------
  private final class ColumnProperty extends Property {

    private ColumnBinding binding;

    public Class<? extends Object> getWriteType(Object source) {
      return (binding.columnClass == null) ? Object.class : binding.columnClass;
    }

    public Object getValue(Object source) {
      if (binding.isBound()) {
        return binding.editingObject;
      }

      throw new UnsupportedOperationException();
    }

    public void setValue(Object source, Object value) {
      throw new UnsupportedOperationException();
    }

    public boolean isReadable(Object source) {
      return binding.isBound();
    }

    public boolean isWriteable(Object source) {
      return true;
    }

    public void addPropertyStateListener(Object source,
        PropertyStateListener listener) {
    }

    public void removePropertyStateListener(Object source,
        PropertyStateListener listener) {
    }

    public PropertyStateListener[] getPropertyStateListeners(Object source) {
      return new PropertyStateListener[0];
    }
  }

  // -----------------------------------------------------------------------
  public final class ColumnBinding extends AbstractColumnBinding {

    private Class<?> columnClass;
    private boolean editable = true;
    private String columnName;
    private Object editingObject;

    private ColumnBinding(int column, Property<E, ?> columnProperty, String name) {
      super(column, columnProperty, new ColumnProperty(), name);
      ((ColumnProperty) getTargetProperty()).binding = this;
    }

    private void adjustColumn(int newCol) {
      setColumn(newCol);
    }

    public ColumnBinding setColumnName(String name) {
      GridBinding.this.throwIfBound();
      this.columnName = name;

      return this;
    }

    public ColumnBinding setColumnClass(Class<?> columnClass) {
      GridBinding.this.throwIfBound();
      this.columnClass = columnClass;

      return this;
    }

    public Class<?> getColumnClass() {
      return (columnClass == null) ? Object.class : columnClass;
    }

    public String getColumnName() {
      return (columnName == null) ? getSourceProperty().toString() : columnName;
    }

    public ColumnBinding setEditable(boolean editable) {
      this.editable = editable;

      return this;
    }

    public boolean isEditable() {
      return editable;
    }
  }

  // -----------------------------------------------------------------------
  private class Handler implements PropertyStateListener {
    public void propertyStateChanged(PropertyStateEvent pse) {
      if (!pse.getValueChanged()) {
        return;
      }

      if (pse.getSourceProperty() == tableP) {
        cleanupForLast();

        boolean wasAccessible = isTableAccessible(pse.getOldValue());
        boolean isAccessible = isTableAccessible(pse.getNewValue());

        if (wasAccessible != isAccessible) {
          elementsP.setAccessible(isAccessible);
        } else if (elementsP.isAccessible()) {
          elementsP.setValueAndIgnore(null, null);
        }
      } else {
        if (((ElementsProperty.ElementsPropertyStateEvent) pse).shouldIgnore()) {
          return;
        }

        if (grid == null) {
          grid = tableP.getValue(getTargetObject());
          model = new BindingTableModel();

          // XXX table.setModel(model);
          setModel(model);
        }

        model.setElements((List) pse.getNewValue(), true);
      }
    }
  }
  
  private void setModel(ListBindingManager model) {
    grid.getElement().setPropertyObject("model", model);
  }

  // -----------------------------------------------------------------------
  private final class BindingTableModel extends ListBindingManager {
    
    public BindingTableModel() {
      grid.getElement().setPropertyObject("model", this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#getColBindings()
     */
    @Override
    protected AbstractColumnBinding[] getColBindings() {
      AbstractColumnBinding[] bindings = new AbstractColumnBinding[getColumnBindings().size()];
      bindings = getColumnBindings().toArray(bindings);
      return bindings;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#allChanged()
     */
    protected void allChanged() {
      grid.resize(size(), columnCount());
      grid.clear(true);
      contentsChanged(0, size());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#valueChanged(int,
     *      int)
     */
    protected void valueChanged(int row, int column) {
      grid.setHTML(row, column, ""+ valueAt(row, column));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#added(int,
     *      int)
     */
    protected void added(int row, int length) {
      assert length > 0; // enforced by ListBindingManager

      if (row > grid.getRowCount()) {
        grid.resizeRows(length);
      } else {
        for (int i = 0; i < length; i++) {
          grid.insertRow(row);
        }
      }

      contentsChanged(row, row + length);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#removed(int,
     *      int)
     */
    protected void removed(int row, int length) {
      assert length > 0; // enforced by ListBindingManager

      for (int i = row + length - 1, n = row; i >= n; --i) {
        grid.removeRow(row);
      }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#changed(int)
     */
    protected void changed(int row) {
      contentsChanged(row, row);
    }

    private void contentsChanged(int row0, int row1) {
      for (int row = row0; row < row1; row++) {
        for (int column = 0, n = columnCount(); column < n; column++) {
          valueChanged(row, column);
        }
      }
    }

  }
}
