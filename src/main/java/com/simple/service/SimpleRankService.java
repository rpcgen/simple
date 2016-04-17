package com.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class SimpleRankService {

    private SimpleCommentRepository repository;

    @Autowired
    public void setSimpleCommentRepository(SimpleCommentRepository repository) {
        this.repository = repository;
    }

    private ScriptEngine engine  = new ScriptEngineManager().getEngineByName("groovy");

    public Map<Long, Long> getRank() throws ScriptException {

        Bindings bindings = engine.createBindings();
        bindings.put("comments", repository.findAll());

        return (Map<Long, Long>) engine.eval(getScriptReader(), bindings);
    }

    private InputStreamReader getScriptReader() {
        return new InputStreamReader(getClass().getResourceAsStream("/rank.groovy"));
    }
}
