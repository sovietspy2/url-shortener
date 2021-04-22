package com.example.urlshortener.service;

import com.example.urlshortener.DynamodbRepository.UrlRecordRepository;
import com.example.urlshortener.model.UrlRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Component
public class KeyUtil {

    @Autowired
    private JedisPool jedisPool;

    @Value("${maxCachedKeysCount}")
    private Long maxCachedKeysCount;

    @Autowired
    private UrlRecordRepository urlRecordRepository;

    @Autowired
    private UpdateHelper updateHelper;

    private static String KEYS = "keys";

    @Scheduled(fixedRate = 5000)
    private void generateKeyToRedis() {

        try (Jedis jedis = jedisPool.getResource()) {

            Long count = jedis.llen(KEYS);

            if (count < maxCachedKeysCount) {
                String key = getUniqueKey();
                jedis.rpush(KEYS, key);
                log.debug("generated and added key to cache: "+key);

            } else {
                log.debug("cache is full!");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
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

    public String getKey() {

        String key = null;

        try (Jedis jedis = jedisPool.getResource()) {

            key = jedis.rpop(KEYS);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return key;
    }

    public String getCachedOriginalIfExists(String key) {
        String original = null;

        try (Jedis jedis = jedisPool.getResource()) {

           original =  jedis.get(key);

           if (original==null) {
               Optional<UrlRecord> record = urlRecordRepository.findByShortUrl(key);

               if (record.isPresent()) {
                    original = record.get().getOriginalUrl();
                    updateHelper.updateCache(key, original);
               }
           }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return original;

    }

}
