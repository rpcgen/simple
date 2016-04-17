package com.simple.service;

import com.simple.domain.SimpleComment;
import com.simple.restutils.Status204IfEmptyCollection;
import com.simple.restutils.Status400IfInvalidComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class SimpleCommentService {

    private static final Comparator<Map.Entry<Long, Long>> RANK_COMPARATOR = (x, y) ->
        x.getValue().equals(y.getValue()) ? x.getKey().compareTo(y.getKey()) : y.getValue().compareTo(x.getValue());

    private SimpleSpamTextService spamText;

    private SimpleCommentRepository repository;

    private SimpleRankService rank;

    @Autowired
    public void setSpamtextService(SimpleSpamTextService spamText) {
        this.spamText = spamText;
    }

    @Autowired
    public void setSimpleCommentRepository(SimpleCommentRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setSimpleRankService(SimpleRankService rank) {
        this.rank = rank;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void status404(HttpServletResponse response) {
        response.setStatus(SC_NOT_FOUND);
    }

    @ExceptionHandler(ScriptException.class)
    public void status500(HttpServletResponse response) {
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    private List<SimpleComment> toUnmodifiableList(Iterable<SimpleComment> comments) {

        List<SimpleComment> result = new ArrayList<>();

        for (SimpleComment comment : comments) {
            result.add(comment);
        }

        return unmodifiableList(result);
    }

    @Status204IfEmptyCollection
    @RequestMapping(path = "/comment", method = GET)
    public List<SimpleComment> getComments(HttpServletResponse response) {
        return toUnmodifiableList(repository.findAll());
    }

    @Status204IfEmptyCollection
    @RequestMapping(path = "/comment-pageable", method = GET)
    public List<SimpleComment> getCommentsPageable(HttpServletResponse response, Pageable pageable) {
        return toUnmodifiableList(repository.findAll(pageable));
    }

    @Status204IfEmptyCollection
    @RequestMapping(path = "/comment/ranked", method = GET)
    public List<SimpleComment> getRankedComments(HttpServletResponse response) throws ScriptException {
        return rank.getRank().entrySet().stream()
                .sorted(RANK_COMPARATOR)
                .map(Map.Entry::getKey)
                .map($ -> repository.findOne($))
                .collect(toList());
    }

    @ResponseStatus(CREATED)
    @RequestMapping(path = "/comment", method = PUT)
    public void addComment(@RequestBody String description) {
        repository.save(new SimpleComment(spamText.process(description)));
    }

    @Status400IfInvalidComment
    @RequestMapping(path = "/comment/{id}/up-vote", method = POST)
    public void upVote(@PathVariable("id") Long id, HttpServletResponse response) {

        SimpleComment comment = repository.findOne(id);

        if (comment != null)
            comment.setUpVotes(comment.getUpVotes() + 1);
    }

    @Status400IfInvalidComment
    @RequestMapping(path = "/comment/{id}/in-response-to/{parent}", method = POST)
    public void responseTo(@PathVariable("id") Long id, @PathVariable("parent") Long parent, HttpServletResponse response) {

        SimpleComment comment = repository.findOne(id);
        SimpleComment commentParent = repository.findOne(parent);

        if (comment != null && commentParent != null)
            comment.setInResponseTo(commentParent);
    }

    @RequestMapping(path = "/comment/{id}", method = DELETE)
    public void remComment(@PathVariable("id") Long id) {
        repository.delete(id);
    }

    void clear() {
        repository.deleteAll();
    }
}
