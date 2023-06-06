package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

public class BootstrapFeedbackPopoverTest {

    public static class BootstrapFeedbackPopoverPage extends WebPage {
        @Override
        protected void onInitialize() {
            super.onInitialize();

            TextField<String> textField = new TextField<>("input", new Model<String>());

            Form<Void> form = new Form<Void>("form"){
                @Override
                protected void onSubmit() {
                    super.onSubmit();
                    textField.error("this is an error from textfield with tabs (\t), new lines (\r\n), double quotes (\"hey\")");
                    error("this is an error from form");
                }
            };
            BootstrapFeedbackPopover feedback = new BootstrapFeedbackPopover("feedback");

            add(form);
            form.add(feedback);
            feedback.add(textField);
        }
    }

    @Test
    public void test(){

        WicketTester tester = new WicketTester();
        tester.startPage(BootstrapFeedbackPopoverPage.class);

        tester.assertComponent("form:feedback", BootstrapFeedbackPopover.class);

        tester.submitForm("form");

        String response = tester.getLastResponseAsString();

        MatcherAssert.assertThat(response, StringContains.containsString("this is an error from textfield with tabs (\\t), new lines (\\r\\n), double quotes (\\\"hey\\\")"));
    }

}
