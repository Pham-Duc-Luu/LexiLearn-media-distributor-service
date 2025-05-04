//package com.application.Strategy.CloudStorageStrategy;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class CloudStorageContext {
//    private final Map<String, CloudStorageStrategy> storageStrategies;
//
//    @Autowired
//    public CloudStorageContext(Map<String, CloudStorageStrategy> storageStrategies) {
//        this.storageStrategies = storageStrategies;
//    }
//
//    public CloudStorageStrategy getStrategy(String storageType) {
//        return storageStrategies.getOrDefault(storageType, storageStrategies.get("s3Storage")); // Default to S3
//    }
//}
