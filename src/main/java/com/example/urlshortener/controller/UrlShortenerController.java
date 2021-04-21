package com.example.urlshortener.controller;


import com.example.urlshortener.model.UrlRecord;
import com.example.urlshortener.repository.UrlRecordRepository;
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

    @GetMapping(path = "/list")
    public List<UrlRecord> listRecords() {
        log.info("Im in controller");
        Pageable pageable = PageRequest.of(0, 100);
        return urlRecordRepository.findAll(pageable).getContent();
    }

    @PostMapping(path = "/create")
    public String createNewShortUrl(HttpServletRequest request, @RequestParam String originalUrl) {
        String key = getUniqueKey();
        UrlRecord urlRecord = new UrlRecord(originalUrl, key, LocalDateTime.now());
        urlRecordRepository.save(urlRecord);
        String newShortLink = "localhost:8080/"+key;
        return newShortLink;
    }

    private String generateKey() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;

    }

    private String getUniqueKey() {
        String key = generateKey();

        while(urlRecordRepository.findByShortUrl(key).isPresent()) {
            key = generateKey();
        }
        return key;

    }

}
