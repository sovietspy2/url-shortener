package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlRecord;
import com.example.urlshortener.DynamodbRepository.UrlRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;

@Slf4j
@Component
public class UpdateHelper {

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Autowired
    private JedisPool jedisPool;

    @Async
    public void incrementClick(String key) {
        Optional<UrlRecord> urlRecord = urlRecordRepository.findById(key);

        if (urlRecord.isPresent()) {
            urlRecord.get().setClicked(urlRecord.get().getClicked() + 1);
            urlRecordRepository.save(urlRecord.get());
            log.debug("click count updated");
        }
    }

    @Async
    public void updateCache(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {

            jedis.set(key, value);
            log.debug("cache updated!");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
