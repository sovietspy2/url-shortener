package com.example.urlshortener.DynamodbRepository;

import com.example.urlshortener.model.UrlRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRecordRepository extends PagingAndSortingRepository<UrlRecord, String> {

    @EnableScan
    @EnableScanCount
    Page<UrlRecord> findAll(Pageable pageable);

    @EnableScan
    @EnableScanCount
    Optional<UrlRecord> findByShortUrl(String shortUrl);
}
