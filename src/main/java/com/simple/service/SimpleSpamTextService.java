package com.simple.service;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class SimpleSpamTextService {

    List<String> repository = asList(
            "<sexist text>",
            "<racist text>");

    String replacement = "***";

    public String process(String text) {
        return repository.stream().reduce(text, (r, x) -> r.replaceAll(x, replacement));
    }
}
