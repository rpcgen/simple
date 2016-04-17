package com.simple.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class SimpleComment {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @Id
    private Long id;
    private String text;
    private LocalDate date;
    private Long upVotes;

    @ManyToOne
    private SimpleComment inResponseTo;

    protected SimpleComment() {
    }

    public SimpleComment(String description) {
        this(ID_GENERATOR.getAndIncrement(), description, LocalDate.now());
    }

    public SimpleComment(Long id, String text, LocalDate date) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.upVotes = 0L;
        this.inResponseTo = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public SimpleComment getInResponseTo() {
        return inResponseTo;
    }

    public void setInResponseTo(SimpleComment inResponseTo) {
        this.inResponseTo = inResponseTo;
    }
}
