package com.left.gank.data.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "URL_COLLECT".
 */
public class UrlCollect {


    private Long id;
    /**
     * Not-null value.
     */
    private String url;
    private String comment;
    private java.util.Date date;
    private String g_type;
    private String g_author;

    public UrlCollect() {
    }

    public UrlCollect(Long id) {
        this.id = id;
    }

    public UrlCollect(Long id, String url, String comment, java.util.Date date, String g_type, String g_author) {
        this.id = id;
        this.url = url;
        this.comment = comment;
        this.date = date;
        this.g_type = g_type;
        this.g_author = g_author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getG_type() {
        return g_type;
    }

    public void setG_type(String g_type) {
        this.g_type = g_type;
    }

    public String getG_author() {
        return g_author;
    }

    public void setG_author(String g_author) {
        this.g_author = g_author;
    }

}