package com.example.urlshortener.controller;

import com.example.urlshortener.DynamodbRepository.UrlRecordRepository;
import com.example.urlshortener.service.KeyUtil;
import com.example.urlshortener.service.UpdateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
public class RedirectController {

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Autowired
    private UpdateHelper updateHelper;

    @Autowired
    private KeyUtil keyUtil;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @GetMapping(path = "/{shortUrl}")
    public void redirect(HttpServletResponse response, @PathVariable String shortUrl) throws Exception {

        String originalUrl = keyUtil.getCachedOriginalIfExists(shortUrl);
        response.sendRedirect(originalUrl);
        log.info(shortUrl + " -> " + originalUrl);
        updateHelper.incrementClick(shortUrl);
    }

}
