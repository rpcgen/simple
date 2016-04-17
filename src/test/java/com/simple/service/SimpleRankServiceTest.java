package com.simple.service;

import com.simple.domain.SimpleComment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.script.ScriptException;
import java.util.Map;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleRankServiceTest {

    @Mock
    SimpleCommentRepository repository;

    private SimpleRankService rank;

    @Before
    public void clear() {
        rank = new SimpleRankService();
        rank.setSimpleCommentRepository(repository);
    }

    @Test
    public void allCommentsMustHaveRank() throws ScriptException, NoSuchMethodException {

        Iterable<SimpleComment> result = asList(
                new SimpleComment(1L, "Text 1", now()),
                new SimpleComment(2L, "Text 2", now()),
                new SimpleComment(3L, "Text 3", now()));

        when(repository.findAll())
                .thenReturn(result);

        Map<Long, Long> rankResult = rank.getRank();

        assertThat(rankResult.size(), is(3));
    }

    @Test
    public void defaultRankMustBe0() throws ScriptException, NoSuchMethodException {

        when(repository.findAll()).thenReturn(asList(new SimpleComment(1L, "Text 1", now())));

        Map<Long, Long> rankResult = rank.getRank();

        assertThat(rankResult.get(1L), is(0L));
    }

    @Test
    public void eachUpVoteIncreasesTheRankBy1() throws ScriptException, NoSuchMethodException {

        SimpleComment c1 = new SimpleComment(1L, "Text 1", now());
        c1.setUpVotes(1L);

        SimpleComment c2 = new SimpleComment(2L, "Text 1", now());
        c2.setUpVotes(3L);

        when(repository.findAll()).thenReturn(asList(c1, c2));

        Map<Long, Long> rankResult = rank.getRank();

        assertThat(rankResult.get(1L), is(1L));
        assertThat(rankResult.get(2L), is(3L));
    }

    @Test
    public void eachResponseIncreasesTheRankBy5() throws ScriptException, NoSuchMethodException {

        SimpleComment c1 = new SimpleComment(1L, "Text 1", now());

        SimpleComment c2 = new SimpleComment(2L, "Text 1", now());
        c1.setInResponseTo(c1);

        when(repository.findAll()).thenReturn(asList(c1, c2));

        Map<Long, Long> rankResult = rank.getRank();

        assertThat(rankResult.get(1L), is(5L));
    }
}
