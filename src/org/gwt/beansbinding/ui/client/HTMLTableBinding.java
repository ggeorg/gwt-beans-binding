package org.gwt.beansbinding.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BindingListener;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;
import org.gwt.beansbinding.ui.client.model.table.TableModel;
import org.gwt.beansbinding.ui.client.model.table.TableModelEvent;
import org.gwt.beansbinding.ui.client.model.table.TableModelListener;

import com.google.gwt.user.client.ui.HTMLTable;

public final class HTMLTableBinding<E, SS, TS> extends
    AutoBinding<SS, List<E>, TS, List> {

  private Property<TS, ? extends HTMLTable> tableP;
  private ElementsProperty<TS> elementsP;
  private Handler handler = new Handler();
  private HTMLTable table;
  private BindingTableModel model;
  private boolean editable = true;
  private List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();

  protected HTMLTableBinding(UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, TS targetObject,
      Property<TS, ? extends HTMLTable> targetJTableProperty, String name) {
    super(strategy == UpdateStrategy.READ_WRITE ? UpdateStrategy.READ
        : strategy, sourceObject, sourceListProperty, targetObject,
        new ElementsProperty<TS>(), name);

    if (targetJTableProperty == null) {
      throw new IllegalArgumentException("target JTable property can't be null");
    }

    tableP = targetJTableProperty;
    elementsP = (ElementsProperty<TS>) getTargetProperty();
  }

  protected void bindImpl() {
    elementsP.setAccessible(isTableAccessible());
    tableP.addPropertyStateListener(getTargetObject(), handler);
    elementsP.addPropertyStateListener(null, handler);
    super.bindImpl();
  }

  protected void unbindImpl() {
    elementsP.removePropertyStateListener(null, handler);
    tableP.removePropertyStateListener(getTargetObject(), handler);
    elementsP.setAccessible(false);
    cleanupForLast();
    super.unbindImpl();
  }

  private boolean isTableAccessible() {
    return tableP.isReadable(getTargetObject())
        && tableP.getValue(getTargetObject()) != null;
  }

  private boolean isTableAccessible(Object value) {
    return value != null && value != PropertyStateEvent.UNREADABLE;
  }

  private void cleanupForLast() {
    if (table == null) {
      return;
    }

    // XXX table.setModel(new DefaultTableModel());
    table = null;
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

    if (columnProperty == null) {
      throw new IllegalArgumentException("can't have null column property");
    }

    if (name == null && HTMLTableBinding.this.getName() != null) {
      name = HTMLTableBinding.this.getName() + ".COLUMN_BINDING";
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

    if (columnProperty == null) {
      throw new IllegalArgumentException("can't have null column property");
    }

    if (name == null && HTMLTableBinding.this.getName() != null) {
      name = HTMLTableBinding.this.getName() + ".COLUMN_BINDING";
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
      cb.adjustColumn(cb.getColumn() + (up ? 1 : -1));
    }
  }

  private final class ColumnProperty extends Property {
    private ColumnBinding binding;

    public Class<? extends Object> getWriteType(Object source) {
      return binding.columnClass == null ? Object.class : binding.columnClass;
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

  public final class ColumnBinding extends AbstractColumnBinding {
    private Class<?> columnClass;
    private boolean editable = true;
    private boolean editableSet;
    private String columnName;
    private Object editingObject;

    private ColumnBinding(int column, Property<E, ?> columnProperty, String name) {
      super(column, columnProperty, new ColumnProperty(), name);
      ((ColumnProperty) getTargetProperty()).binding = this;
    }

    private void setEditingObject(Object editingObject) {
      this.editingObject = editingObject;
    }

    private void adjustColumn(int newCol) {
      setColumn(newCol);
    }

    public ColumnBinding setColumnName(String name) {
      HTMLTableBinding.this.throwIfBound();
      this.columnName = name;
      return this;
    }

    public ColumnBinding setColumnClass(Class<?> columnClass) {
      HTMLTableBinding.this.throwIfBound();
      this.columnClass = columnClass;
      return this;
    }

    public Class<?> getColumnClass() {
      return columnClass == null ? Object.class : columnClass;
    }

    public String getColumnName() {
      return columnName == null ? getSourceProperty().toString() : columnName;
    }

    public ColumnBinding setEditable(boolean editable) {
      this.editable = editable;
      return this;
    }

    public boolean isEditable() {
      return editable;
    }

    private void bindUnmanaged0() {
      bindUnmanaged();
    }

    private void unbindUnmanaged0() {
      unbindUnmanaged();
    }

    private SyncFailure saveUnmanaged0() {
      return saveUnmanaged();
    }

    private void setSourceObjectUnmanaged0(Object source) {
      setSourceObjectUnmanaged(source);
    }
  }

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

        if (table == null) {
          table = tableP.getValue(getTargetObject());
          model = new BindingTableModel();
          // XXX table.setModel(model);
        }

        model.setElements((List) pse.getNewValue(), true);
      }
    }
  }

  private final class BindingTableModel extends ListBindingManager implements
      TableModel {
    private final List<TableModelListener> listeners;

    public BindingTableModel() {
      listeners = new ArrayList<TableModelListener>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#getColBindings()
     */
    @Override
    protected AbstractColumnBinding[] getColBindings() {
      AbstractColumnBinding[] bindings = new AbstractColumnBinding[getColumnBindings().size()];
      bindings = getColumnBindings().toArray(bindings);
      return bindings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.beansbinding.ui.client.model.table.TableModel#getRowCount()
     */
    public int getRowCount() {
      return size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.beansbinding.ui.client.model.table.TableModel#getValueAt(int,
     *      int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
      return valueAt(rowIndex, columnIndex);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
      ColumnBinding cb = HTMLTableBinding.this.getColumnBinding(columnIndex);
      BindingListener[] cbListeners = cb.getBindingListeners();
      BindingListener[] tbListeners = getBindingListeners();

      cb.setSourceObjectUnmanaged0(this.getElement(rowIndex));
      cb.setEditingObject(value);
      cb.bindUnmanaged0();

      for (BindingListener listener : tbListeners) {
        listener.bindingBecameBound(cb);
      }

      PropertyStateEvent pse = new PropertyStateEvent(cb.getTargetProperty(),
          cb.getTargetObject(), true, getValueAt(rowIndex, columnIndex), value,
          false, cb.getSourceProperty().isWriteable(cb.getSourceObject()));

      for (BindingListener listener : cbListeners) {
        listener.targetChanged(cb, pse);
      }

      for (BindingListener listener : tbListeners) {
        listener.targetChanged(cb, pse);
      }

      SyncFailure failure = cb.saveUnmanaged0();

      if (failure == null) {
        for (BindingListener listener : cbListeners) {
          listener.synced(cb);
        }

        for (BindingListener listener : tbListeners) {
          listener.synced(cb);
        }
      } else {
        for (BindingListener listener : cbListeners) {
          listener.syncFailed(cb, failure);
        }

        for (BindingListener listener : tbListeners) {
          listener.syncFailed(cb, failure);
        }
      }

      cb.unbindUnmanaged0();

      for (BindingListener listener : tbListeners) {
        listener.bindingBecameUnbound(cb);
      }

      cb.setEditingObject(null);
      cb.setSourceObjectUnmanaged0(null);
    }

    public Class<?> getColumnClass(int columnIndex) {
      Class<?> klass = HTMLTableBinding.this.getColumnBinding(columnIndex).getColumnClass();
      return klass == null ? Object.class : klass;
    }

    protected void allChanged() {
      fireTableModelEvent(new TableModelEvent(this, 0, Integer.MAX_VALUE));
    }

    protected void valueChanged(int row, int column) {
      fireTableModelEvent(new TableModelEvent(this, row, row, column));
    }

    protected void added(int row, int length) {
      assert length > 0; // enforced by ListBindingManager

      fireTableModelEvent(new TableModelEvent(this, row, row + length - 1,
          TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.INSERT));
    }

    protected void removed(int row, int length) {
      assert length > 0; // enforced by ListBindingManager

      fireTableModelEvent(new TableModelEvent(this, row, row + length - 1,
          TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.DELETE));
    }

    protected void changed(int row) {
      fireTableModelEvent(new TableModelEvent(this, row, row,
          TableModelEvent.ALL_COLUMNS));
    }

    public String getColumnName(int columnIndex) {
      ColumnBinding binding = HTMLTableBinding.this.getColumnBinding(columnIndex);
      return binding.getColumnName() == null
          ? binding.getSourceProperty().toString() : binding.getColumnName();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
      if (!HTMLTableBinding.this.isEditable()) {
        return false;
      }

      ColumnBinding binding = HTMLTableBinding.this.getColumnBinding(columnIndex);
      if (!binding.isEditable()) {
        return false;
      }

      return binding.getSourceProperty().isWriteable(getElement(rowIndex));
    }

    public void addTableModelListener(TableModelListener l) {
      listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
      listeners.remove(l);
    }

    private void fireTableModelEvent(TableModelEvent e) {
      for (TableModelListener listener : listeners) {
        listener.tableChanged(e);
      }
    }

    public int getColumnCount() {
      return columnCount();
    }
  }
}
