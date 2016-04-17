package com.simple.service;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleSpamTextServiceTest {

    private SimpleSpamTextService spamText;

    @Before
    public void clear() {
        spamText = new SimpleSpamTextService();

        spamText.repository = asList(
                "spam1",
                "spam2");

        spamText.replacement = "xxx";
    }

    @Test
    public void aTextWithNoSpamRemainsUnchanged() {
        assertThat(spamText.process("Valid text"), is("Valid text"));
    }

    @Test
    public void spamIsReplacedOnText() {
        assertThat(spamText.process("text with spam spam1"), is("text with spam xxx"));
    }

    @Test
    public void allSpamIsReplacedOnTextWhenRepeated() {
        assertThat(spamText.process("text with spam1 spam spam1 spam1"), is("text with xxx spam xxx xxx"));
    }

    @Test
    public void allDifferentSpamIsReplacedOnText() {
        assertThat(spamText.process("text with spam spam1 spam2"), is("text with spam xxx xxx"));
    }

}
