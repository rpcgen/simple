package com.simple.service;

import com.simple.Application;
import com.simple.domain.SimpleComment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class SimpleCommentServiceStatusCodeIntegrationTest {

    @Autowired
    private SimpleCommentService service;

    @Mock
    HttpServletResponse response;

    @Value("${local.server.port}")
    int port;

    @Before
    public void clear() {
        service.clear();
    }

    @Test
    public void getCommentsResponseCodeMustBe200() {

        service.addComment("My Comment");

        when()
                .get("http://localhost:" + port + "/comment")
        .then()
                .statusCode(200);
    }

    @Test
    public void getCommentsResponseCodeMustBe204WhenResultIsEmpty() {

        when()
                .get("http://localhost:" + port + "/comment")
        .then()
                .statusCode(204);
    }

    @Test
    public void putCommentsResponseCodeMustBe201() {

        given()
                .contentType("text/plain")
                .body("My Comment")
        .when()
                .put("http://localhost:" + port + "/comment")
        .then()
                .statusCode(201);
    }

    @Test
    public void deleteCommentsResponseCodeMustBe200() {

        service.addComment("My Comment");

        long id = service.getComments(response).get(0).getId();

        when()
                .delete("http://localhost:" + port + "/comment/" + id)
        .then()
                .statusCode(200);
    }

    @Test
    public void deleteCommentsResponseCodeMustBe404WhenInvalidId() {

        when()
                .delete("http://localhost:" + port + "/comment/999")
        .then()
                .statusCode(404);
    }

    @Test
    public void upVoteCommentsResponseCodeMustBe200() {

        service.addComment("My Comment");

        long id = service.getComments(response).get(0).getId();

        when()
                .post("http://localhost:" + port + "/comment/" + id + "/up-vote")
        .then()
                .statusCode(200);
    }

    @Test
    public void upVoteCommentsResponseCodeMustBe400WhenInvalidId() {

        when()
                .post("http://localhost:" + port + "/comment/999/up-vote")
        .then()
                .statusCode(400);
    }

    @Test
    public void linkAResponsesToACommentResponseCodeMustBe200() {

        service.addComment("My Comment 1");
        service.addComment("My Comment 2");

        List<SimpleComment> comments = service.getComments(response);

        long id1 = comments.get(0).getId();
        long id2 = comments.get(1).getId();

        when()
                .post("http://localhost:" + port + "/comment/" + id1 + "/in-response-to/" + id2)
        .then()
                .statusCode(200);
    }

    @Test
    public void linkAResponsesToACommentResponseCodeMustBe404WhenInvalidResponseId() {

        service.addComment("My Comment");

        long id = service.getComments(response).get(0).getId();

        when()
                .post("http://localhost:" + port + "/comment/999/in-response-to/" + id)
                .then()
                .statusCode(400);
    }

    @Test
    public void linkAResponsesToACommentResponseCodeMustBe404WhenInvalidParentId() {

        service.addComment("My Comment");

        long id = service.getComments(response).get(0).getId();

        when()
                .post("http://localhost:" + port + "/comment/" + id + "/in-response-to/999")
                .then()
                .statusCode(400);
    }

    @Test
    public void getRankedCommentsResponseCodeMustBe200() {

        service.addComment("My Comment");

        when()
                .get("http://localhost:" + port + "/comment/ranked")
        .then()
                .statusCode(200);
    }

    @Test
    public void getRankedCommentsResponseCodeMustBe204WhenResultIsEmpty() {

        when()
                .get("http://localhost:" + port + "/comment/ranked")
        .then()
                .statusCode(204);
    }
}
