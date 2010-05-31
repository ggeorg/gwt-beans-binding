/**
 * 
 */
package org.gwt.beansbinding.ui.client.model.selection;

import com.google.gwt.user.client.ui.CheckBox;


/**
 * @author ladislav.gazo
 *
 * @param <SS>
 * @param <SV>
 */
public class IntegerMaskCheckBoxGroupHandler<SS, SV> extends SelectionGroupHandler<SS, SV> {
	
	public IntegerMaskCheckBoxGroupHandler() {
		super(true);
	}

	@Override
	public Object getValueFromSelected() {
		Integer result = 0;
		for(CheckBox box : selected) {
			result += Integer.valueOf(box.getFormValue());
		}
		return result;
	}
	
	@Override
	protected Object getObjectFromFormValue(String formValue) {
		return Integer.valueOf(formValue);
	}
	
	@Override
	protected boolean compare(Object value, Object formValue) {
		// 110
		// 001
		// 000
		int a = (((Integer)value).intValue() & ((Integer)formValue).intValue());
		return a != 0;
	}
}