package com.simple.service;

import com.simple.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class SimpleCommentServicePaginationIntegrationTest {

    @Autowired
    private SimpleCommentService service;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void clear() {
        service.clear();
    }

    @Test
    public void paginationIsEnabled() {

        for (int i = 0; i < 50; i++)
            service.addComment("My Comment " + i);

        List response = when()
                .get("http://localhost:" + port + "/comment-pageable")
                .as(List.class);

        assertThat(response.size(), is(20));
    }
}
