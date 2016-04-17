package com.simple.service;

import com.simple.domain.SimpleComment;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCommentServiceTest {

    private static Matcher<List<SimpleComment>> containsCommentWithText(String text) {

        return new TypeSafeMatcher<List<SimpleComment>>() {

            @Override
            public boolean matchesSafely(List<SimpleComment> candidates) {

                for (SimpleComment comment : candidates)
                    if (comment.getText().equals(text))
                        return true;

                return false;
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("not contains the item with description: " + description);
            }
        };
    }

    private SimpleCommentService service;

    @Mock
    HttpServletResponse response;

    @Mock
    SimpleSpamTextService spamText;

    @Mock
    SimpleRankService rank;

    @Mock
    SimpleCommentRepository repository;

    @Before
    public void clear() {
        service = new SimpleCommentService();
        service.setSpamtextService(spamText);
        service.setSimpleCommentRepository(repository);
        service.setSimpleRankService(rank);
    }

    @Test
    public void itIsPossibleToGetAllComments() {

        Iterable<SimpleComment> result = asList(
                new SimpleComment(1l, "Text 1", now()),
                new SimpleComment(2l, "Text 2", now()),
                new SimpleComment(3l, "Text 3", now()));

        when(repository.findAll()).thenReturn(result);

        assertThat(service.getComments(response).size(), is(3));
        assertThat(service.getComments(response), containsCommentWithText("Text 1"));
        assertThat(service.getComments(response), containsCommentWithText("Text 2"));
        assertThat(service.getComments(response), containsCommentWithText("Text 3"));
    }

    @Test
    public void itIsPossibleToAddComments() {

        service.addComment("My Comment");

        verify(repository).save(any(SimpleComment.class));
    }

    @Test
    public void newCommentsAreProcessedToPreventSpamText() {

        service.addComment("My Comment");

        verify(spamText).process(eq("My Comment"));
    }

    @Test
    public void itIsPossibleToUpVoteComments() {

        SimpleComment result = new SimpleComment(1L, "Text 1", now());
        result.setUpVotes(10L);

        when(repository.findOne(1L)).thenReturn(result);

        service.upVote(1L, response);

        assertThat(result.getUpVotes(), is(11L));
    }

    @Test
    public void itIsPossibleToMarkACommentAsAResponse() {

        SimpleComment result = new SimpleComment(1L, "Text 1", now());
        SimpleComment parent = new SimpleComment(2L, "Text 2", now());

        when(repository.findOne(1L)).thenReturn(result);
        when(repository.findOne(2L)).thenReturn(parent);

        service.responseTo(1L, 2L, response);

        assertThat(result.getInResponseTo(), is(parent));
    }

    @Test
    public void itIsPossibleToGetAllCommentsSortedByRank() throws ScriptException {

        SimpleComment comment1 = new SimpleComment(1L, "Text 1", now());
        SimpleComment comment2 = new SimpleComment(2L, "Text 2", now());

        Map<Long, Long> rankMap = new HashMap<>();
        rankMap.put(1L, 1L);
        rankMap.put(2L, 4L);

        when(repository.findOne(1L)).thenReturn(comment1);
        when(repository.findOne(2L)).thenReturn(comment2);

        when(rank.getRank()).thenReturn(rankMap);

        List<SimpleComment> result = service.getRankedComments(response);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getId(), is(2L));
        assertThat(result.get(1).getId(), is(1L));
    }

    @Test
    public void itIsPossibleToRemoveComments() {

        service.remComment(1L);

        verify(repository).delete(eq(1L));
    }
}
