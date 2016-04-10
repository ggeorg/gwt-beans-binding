# Introduction #

Because gwt-beans-binding supports several UI components let's try to pick those applying to selecting one or more values from a set of available. We will be referring to single selection when we use radio button because we select one value from the set. In opposite we will refer to multiple selection in case of checkbox, the reason is obvious.

# Our example #

One example is more then thousands of words so here it is, with several types of binding:

```
public class MightyPanel extends Composite {
	private CheckBox red;
	private CheckBox green;
	private CheckBox blue;
	private CheckBox white;

	private CheckBox java;
	private CheckBox dotnet;
	private CheckBox php;

	private RadioButton gwt;
	private RadioButton zk;

	private RadioButton frontend;
	private RadioButton middle;
	private RadioButton backend;

	public MightyPanel() {
		red = new CheckBox("Red");
		red.setFormValue("1");
		green = new CheckBox("Green");
		green.setFormValue("2");
		blue = new CheckBox("Blue");
		blue.setFormValue("4");
		white = new CheckBox("White");
		white.setFormValue("8");
		
		java = new CheckBox("Java");
		java.setFormValue("javaLang");
		dotnet = new CheckBox(".NET");
		dotnet.setFormValue("dotnetLang");
		php = new CheckBox("PHP");
		php.setFormValue("phpLang");

		gwt = new RadioButton("favoriteUIFrameworkGroup", "GWT");
		gwt.setFormValue(Boolean.TRUE.toString());
		zk = new RadioButton("favoriteUIFrameworkGroup", "ZK");
		zk.setFormValue(Boolean.FALSE.toString());
		
		frontend = new RadioButton("developerTypeGroup", "Frontend");
		frontend.setFormValue(Integer.valueOf(10).toString());
		middle = new RadioButton("developerTypeGroup", "Middle");
		middle.setFormValue(Integer.valueOf(20).toString());
		backend = new RadioButton("developerTypeGroup", "Backend");
		backend.setFormValue(Integer.valueOf(30).toString());
		
		bindMe();
	}

	private void bindMe() {
		// let's have a bean
		Questionaire bean = new Questionaire();

		SelectionBinding<Questionaire, Integer> coloursYouLikeBinding = SelectionBinding
				.createMultipleWithIntegerMask(UpdateStrategy.READ_WRITE, bean, BeanProperty
						.<Questionaire, Integer> create("coloursYouLike"), red, green, blue, white);
		coloursYouLikeBinding.bind();

		SelectionBinding<Questionaire, String> langYouUseBinding = SelectionBinding
				.createMultipleWithTokenizedString(UpdateStrategy.READ_WRITE, bean, BeanProperty
						.<Questionaire, String> create("langYouUse"), java, dotnet, php);
		langYouUseBinding.bind();

		SelectionBinding<Questionaire, Boolean> favoriteUIFrameworkBinding = SelectionBinding
				.createSingleWithBooleanValues(UpdateStrategy.READ_WRITE, bean, BeanProperty
						.<Questionaire, Boolean> create("favoriteUIFramework"), gwt, zk);
		favoriteUIFrameworkBinding.bind();
		
		SelectionBinding<Questionaire, Integer> developerTypeBinding = SelectionBinding
		.createSingleWithIntegerValues(UpdateStrategy.READ_WRITE, bean, BeanProperty
				.<Questionaire, Integer> create("developerType"), frontend, middle, backend);
		developerTypeBinding.bind();
	}
}
```

And here is the bean (not complete, only for imagination):

```
public class Questionaire implements HasPropertyChangeSupport {
	private Integer coloursYouLike;
	private String langYouUse;
	private Boolean favoriteUIFramework;
	private Integer developerType;
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
}
```

# "Theoretical" explanation #

As you can see it from the example there is a class responsible for the binding with several methods - **SelectionBinding**.

The class has two kinds of methods:
  * for single selection - starting with **createSingleWith...**, used for RadioButton
  * for multiple selection - starting with **createMultipleWith...**, used for CheckBox

The method determines what type of field it will assign the value to (Integer, Boolean, String,...) and also what type of "serialization" of the value will be used (Integer bit mask, tokenized string, pure value, ...).

Important thing for binding successfully is to set the **form value** using the **setFormValue** in the UI component.

With single selection you are usually binding the value to a field without any transformation - only converting the string representation to one required by field type.

With multiple selection it is possible to bind it only to one field  currently (it might be the requirement to bind the selection to the collection type but it is not implemented yet). Therefore there is transformation required where the selected values are persisted in one field representation.
Currently there are:
  * integer mask - integer values (form values) are bits in an integer mask
  * tokenized string - each selected form value is concatenated with the space separator to a single string

In case these types are not sufficient for you the implementation is meant to be extended for other types.