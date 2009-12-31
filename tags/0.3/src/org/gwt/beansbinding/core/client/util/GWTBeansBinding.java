package org.gwt.beansbinding.core.client.util;

import java.util.Date;

import org.gwt.beansbinding.ui.client.adapters.HTMLTableAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasHTMLAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasTextAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.HasValueAdapterProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResetButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public final class GWTBeansBinding extends Object {

  public GWTBeansBinding() {
    // Nothing to do here!
  }

  public static void init() {

    // Register all known HasText implementors

    HasTextAdapterProvider.register(Anchor.class);
    HasTextAdapterProvider.register(Button.class);
    HasTextAdapterProvider.register(ButtonBase.class);
    HasTextAdapterProvider.register(CheckBox.class);
    HasTextAdapterProvider.register(CustomButton.class);
    HasTextAdapterProvider.register(CustomButton.Face.class);
    HasTextAdapterProvider.register(DialogBox.class);
    HasTextAdapterProvider.register(HTML.class);
    HasTextAdapterProvider.register(Hyperlink.class);
    HasTextAdapterProvider.register(InlineHTML.class);
    HasTextAdapterProvider.register(InlineHyperlink.class);
    HasTextAdapterProvider.register(InlineLabel.class);
    HasTextAdapterProvider.register(Label.class);
    HasTextAdapterProvider.register(MenuItem.class);
    HasTextAdapterProvider.register(PasswordTextBox.class);
    HasTextAdapterProvider.register(PushButton.class);
    HasTextAdapterProvider.register(RadioButton.class);
    HasTextAdapterProvider.register(ResetButton.class);
    HasTextAdapterProvider.register(RichTextArea.class);
    HasTextAdapterProvider.register(SubmitButton.class);
    HasTextAdapterProvider.register(SuggestBox.class);
    HasTextAdapterProvider.register(TextArea.class);
    HasTextAdapterProvider.register(TextBox.class);
    HasTextAdapterProvider.register(TextBoxBase.class);
    HasTextAdapterProvider.register(ToggleButton.class);
    HasTextAdapterProvider.register(TreeItem.class);

    // Register all known HasHTML implementors

    HasHTMLAdapterProvider.register(Anchor.class);
    HasHTMLAdapterProvider.register(Button.class);
    HasHTMLAdapterProvider.register(ButtonBase.class);
    HasHTMLAdapterProvider.register(CheckBox.class);
    HasHTMLAdapterProvider.register(CustomButton.class);
    HasHTMLAdapterProvider.register(CustomButton.Face.class);
    HasHTMLAdapterProvider.register(DialogBox.class);
    HasHTMLAdapterProvider.register(HTML.class);
    HasHTMLAdapterProvider.register(Hyperlink.class);
    HasHTMLAdapterProvider.register(InlineHTML.class);
    HasHTMLAdapterProvider.register(InlineHyperlink.class);
    HasHTMLAdapterProvider.register(MenuItem.class);
    HasHTMLAdapterProvider.register(PushButton.class);
    HasHTMLAdapterProvider.register(RadioButton.class);
    HasHTMLAdapterProvider.register(ResetButton.class);
    HasHTMLAdapterProvider.register(RichTextArea.class);
    HasHTMLAdapterProvider.register(SubmitButton.class);
    HasHTMLAdapterProvider.register(ToggleButton.class);
    HasHTMLAdapterProvider.register(TreeItem.class);

    // Register all known HasValue implementors
    HasValueAdapterProvider.<Boolean> register(CheckBox.class);
    HasValueAdapterProvider.<Date> register(DateBox.class);
    HasValueAdapterProvider.<Date> register(DatePicker.class);
    HasValueAdapterProvider.<String> register(PasswordTextBox.class);
    HasValueAdapterProvider.<Boolean> register(RadioButton.class);
    HasValueAdapterProvider.<String> register(SuggestBox.class);
    HasValueAdapterProvider.<String> register(TextArea.class);
    HasValueAdapterProvider.<String> register(TextBox.class);
    HasValueAdapterProvider.<String> register(TextBoxBase.class);

    // Register all known HTMLTable implementors
    HTMLTableAdapterProvider.register(FlexTable.class);
    HTMLTableAdapterProvider.register(Grid.class);

    // Initialize property descriptors

    if (!GWT.isScript()) {
      return;
    }

    try {
      GWT.create(GWTBeansBinding.class);
    } catch (Throwable t) {
      GWT.log(t.getMessage(), t);
    }
  }

}
