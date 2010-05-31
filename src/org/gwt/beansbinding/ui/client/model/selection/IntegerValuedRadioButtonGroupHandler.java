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
public class IntegerValuedRadioButtonGroupHandler<SS, SV> extends SelectionGroupHandler<SS, SV> {
	@Override
	public Object getValueFromSelected() {
		return Integer.valueOf(selected.iterator().next().getFormValue());
	}
	
	@Override
	protected Object getObjectFromFormValue(String formValue) {
		return Integer.valueOf(formValue);
	}
}