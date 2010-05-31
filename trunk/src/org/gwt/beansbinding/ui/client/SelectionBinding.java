/**
 * 
 */
package org.gwt.beansbinding.ui.client;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.ui.client.model.selection.BooleanValuedRadioButtonGroupHandler;
import org.gwt.beansbinding.ui.client.model.selection.IntegerMaskCheckBoxGroupHandler;
import org.gwt.beansbinding.ui.client.model.selection.IntegerValuedRadioButtonGroupHandler;
import org.gwt.beansbinding.ui.client.model.selection.SelectionGroupHandler;
import org.gwt.beansbinding.ui.client.model.selection.StringValuedRadioButtonGroupHandler;
import org.gwt.beansbinding.ui.client.model.selection.TokenizedStringCheckBoxGroupHandler;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * @author ladislav.gazo
 */
public class SelectionBinding<SS, SV> extends AutoBinding<SS, SV, SelectionGroupHandler<SS, SV>, String> {

	public static <SS, SV> SelectionBinding<SS, SV> createSingleWithIntegerValues(UpdateStrategy strategy,
			SS sourceObject, Property<SS, SV> sourceProperty, RadioButton... buttons) {
		return create(new IntegerValuedRadioButtonGroupHandler<SS, SV>(), strategy, sourceObject,
				sourceProperty, buttons);
	}

	public static <SS, SV> SelectionBinding<SS, SV> createSingleWithBooleanValues(UpdateStrategy strategy,
			SS sourceObject, Property<SS, SV> sourceProperty, RadioButton... buttons) {
		return create(new BooleanValuedRadioButtonGroupHandler<SS, SV>(), strategy, sourceObject,
				sourceProperty, buttons);
	}

	public static <SS, SV> SelectionBinding<SS, SV> createSingleWithStringValues(UpdateStrategy strategy,
			SS sourceObject, Property<SS, SV> sourceProperty, RadioButton... buttons) {
		return create(new StringValuedRadioButtonGroupHandler<SS, SV>(), strategy, sourceObject,
				sourceProperty, buttons);
	}

	public static <SS, SV> SelectionBinding<SS, SV> createMultipleWithIntegerMask(
			UpdateStrategy strategy, SS sourceObject,
			Property<SS, SV> sourceProperty, CheckBox... checkboxes) {
		return create(new IntegerMaskCheckBoxGroupHandler<SS, SV>(), strategy, sourceObject,
				sourceProperty, checkboxes);
	}

	public static <SS, SV> SelectionBinding<SS, SV> createMultipleWithTokenizedString(
			UpdateStrategy strategy, SS sourceObject,
			Property<SS, SV> sourceProperty, CheckBox... checkboxes) {
		return create(new TokenizedStringCheckBoxGroupHandler<SS, SV>(), strategy, sourceObject,
				sourceProperty, checkboxes);
	}
	
	private static <SS, SV> SelectionBinding<SS, SV> create(SelectionGroupHandler<SS, SV> handler,
			UpdateStrategy strategy, SS sourceObject, Property<SS, SV> sourceProperty, CheckBox... buttons) {
		SelectionGroupHandler<SS, SV> group = handler;
		handler.setButtons(buttons);
		for (CheckBox button : buttons) {
			button.addValueChangeHandler(group);
		}

		BeanProperty<SelectionGroupHandler<SS, SV>, String> groupProperty = BeanProperty
				.create(SelectionGroupHandler.FORM_VALUE);

		SelectionBinding<SS, SV> binding = new SelectionBinding<SS, SV>(strategy, sourceObject,
				sourceProperty, group, groupProperty, null);
		return binding;
	}

	private SelectionBinding(UpdateStrategy strategy, SS sourceObject, Property<SS, SV> sourceProperty,
			SelectionGroupHandler<SS, SV> targetObject,
			Property<SelectionGroupHandler<SS, SV>, String> targetProperty, String name) {
		super(strategy, sourceObject, sourceProperty, targetObject, targetProperty, name);
	}

}
