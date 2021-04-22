package com.example.urlshortener.controller;


import com.example.urlshortener.model.UrlRecord;
import com.example.urlshortener.DynamodbRepository.UrlRecordRepository;
import com.example.urlshortener.service.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
public class UrlShortenerController {

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Autowired
    private KeyUtil keyUtil;

    @GetMapping(path = "/list")
    public List<UrlRecord> listRecords() {
        log.info("Im in controller");
        Pageable pageable = PageRequest.of(0, 100);
        return urlRecordRepository.findAll(pageable).getContent();
    }

    @PostMapping(path = "/create")
    public String createNewShortUrl(HttpServletRequest request, @RequestParam String originalUrl) {
        String key = keyUtil.getKey();
        UrlRecord urlRecord = new UrlRecord(originalUrl, key, LocalDateTime.now());
        urlRecordRepository.save(urlRecord);
        String newShortLink = "localhost:8080/"+key;
        return newShortLink;
    }


}
