/**
 * 
 */
package org.gwt.beansbinding.ui.client.model.selection;

import java.util.Iterator;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author ladislav.gazo
 * 
 * @param <SS>
 * @param <SV>
 */
public class TokenizedStringCheckBoxGroupHandler<SS, SV> extends SelectionGroupHandler<SS, SV> {
	private final String separator;
	
	public TokenizedStringCheckBoxGroupHandler() {
		super(true);
		separator = " ";
	}
	
	public TokenizedStringCheckBoxGroupHandler(String separator) {
		super(true);
		this.separator = separator;
	}

	@Override
	public Object getValueFromSelected() {
		StringBuilder builder = new StringBuilder();
		Iterator<CheckBox> it = selected.iterator();
		while(it.hasNext()) {
			builder.append(it.next().getFormValue());
			if(it.hasNext()) {
				builder.append(separator);
			}
		}
		return builder.toString();
	}

	@Override
	protected Object getObjectFromFormValue(String formValue) {
		return formValue;
	}
	
	@Override
	protected boolean compare(Object value, Object formValue) {
		return ((String) value).contains((String) formValue);
	}
}