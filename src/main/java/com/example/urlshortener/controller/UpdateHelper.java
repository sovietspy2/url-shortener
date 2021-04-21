package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlRecord;
import com.example.urlshortener.repository.UrlRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateHelper {

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Async
    public void incrementClick(String key) {
        Optional<UrlRecord> urlRecord = urlRecordRepository.findById(key);

        if (urlRecord.isPresent()) {
            urlRecord.get().setClicked(urlRecord.get().getClicked()+1);
            urlRecordRepository.save(urlRecord.get());
        }
    }

}
