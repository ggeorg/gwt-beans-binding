/**
 * 
 */
package org.gwt.beansbinding.ui.client.model.selection;


/**
 * @author ladislav.gazo
 *
 * @param <SS>
 * @param <SV>
 */
public class BooleanValuedRadioButtonGroupHandler<SS, SV> extends SelectionGroupHandler<SS, SV> {
	@Override
	public Object getValueFromSelected() {
		return Boolean.valueOf(selected.iterator().next().getFormValue());
	}

	@Override
	protected Object getObjectFromFormValue(String formValue) {
		return Boolean.valueOf(formValue);
	}
}