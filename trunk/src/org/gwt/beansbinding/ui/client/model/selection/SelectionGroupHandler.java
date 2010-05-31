/**
 * 
 */
package org.gwt.beansbinding.ui.client.model.selection;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author ladislav.gazo
 *
 * @param <SS>
 * @param <SV>
 */
public abstract class SelectionGroupHandler<SS, SV> implements ValueChangeHandler<Boolean>,
		HasPropertyChangeSupport {
	public static final String FORM_VALUE = "value";
	private final PropertyChangeSupport pcs;
	protected Set<CheckBox> selected = new HashSet<CheckBox>();
	protected Object value;
	protected CheckBox[] buttons;
	private boolean multivalued = false;

	public SelectionGroupHandler() {
		pcs = new PropertyChangeSupport(this);
	}
	
	public SelectionGroupHandler(boolean multivalued) {
		this();
		this.multivalued = multivalued;
	}

	public void setButtons(CheckBox[] buttons) {
		this.buttons = buttons;
	}
	
	public abstract Object getValueFromSelected();
	protected abstract Object getObjectFromFormValue(String formValue);
	protected boolean compare(Object value, Object formValue) {
		return value.equals(formValue);
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		if(buttons == null) {
			return;
		}
		Object formValue;
		selected.clear();
		for(CheckBox button : buttons) {
			formValue = getObjectFromFormValue(button.getFormValue());
			if(compare(value, formValue)) {
				selected.add(button);
				button.setValue(true);
				if(!multivalued) {
					this.value = value;
					return;
				}
			}
		}
		
		if(multivalued) {
			//TODO:
		}
		this.value = value;
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (multivalued || (!multivalued && event.getValue() == true)) {
			Object oldValue = null;
			if(selected != null) {
				oldValue = value;
			}
			if(!multivalued) {
				selected.clear();
				selected.add((CheckBox) event.getSource());
			} else {
				if(event.getValue() == true) {
					selected.add((CheckBox) event.getSource());
				} else {
					selected.remove((CheckBox) event.getSource());
				}
			}
			value = getValueFromSelected();
			pcs.firePropertyChange(FORM_VALUE, oldValue, value);
		}
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}