package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlRecord;
import com.example.urlshortener.repository.UrlRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
public class RedirectController {

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Autowired
    private UpdateHelper updateHelper;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @GetMapping(path = "/{shortUrl}")
    public void redirect(HttpServletResponse response, @PathVariable String shortUrl) throws Exception {


        Optional<UrlRecord> record = urlRecordRepository.findByShortUrl(shortUrl);

        if (record.isPresent()) {

            response.sendRedirect(record.get().getOriginalUrl());

            log.info(shortUrl+" -> "+record.get().getOriginalUrl());

            updateHelper.incrementClick(record.get().getId());

        }
    }

}
