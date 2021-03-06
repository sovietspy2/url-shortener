package com.example.urlshortener.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.LocalDateTime;


@DynamoDBTable(tableName = "record")
public class UrlRecord {
    private String id;
    private String originalUrl;
    private String shortUrl;
    //private LocalDateTime createDate;
    private Integer clicked = 0;

    public UrlRecord() {

    }

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getOriginalUrl() {
        return originalUrl;
    }

    @DynamoDBAttribute
    public String getShortUrl() {
        return shortUrl;
    }

//    @DynamoDBAttribute
//    public LocalDateTime getCreateDate() {
//        return createDate;
//    }

    @DynamoDBAttribute
    public Integer getClicked() {
        return clicked;
    }

    public UrlRecord(String originalUrl, String shortUrl, LocalDateTime createDate) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        //this.createDate = createDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setClicked(Integer clicked) {
        this.clicked = clicked;
    }
}
