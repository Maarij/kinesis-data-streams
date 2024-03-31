package com.thecodinginterface.kinesis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PutRecordsApp {
    static final Logger logger = LogManager.getLogger(PutRecordsApp.class);
    static final ObjectMapper objMapper = new ObjectMapper();

    public static void main(String[] args) {
        logger.info("Starting PutRecord Producer");
        String streamName = args[0];

        // Instantiate the client
        var client = KinesisClient.builder().build();

        // Add shutdown hook to cleanly close the client when program terminates
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down program");
            client.close();
        }, "producer-shutdown"));

        Map<PutRecordsRequestEntry, Order> requestEntries = new LinkedHashMap<>();
        while(true) {
            // Generate fake order item data
            var order = OrderGenerator.makeOrder();
            logger.info(String.format("Generated %s", order));

            try {
                // Construct PutRecord request entries
                var entry = PutRecordsRequestEntry.builder()
                                    .partitionKey(order.getOrderID())
                                    .data(SdkBytes.fromByteArray(objMapper.writeValueAsBytes(order)))
                                    .build();
                requestEntries.put(entry, order);
            } catch(JsonProcessingException e) {
                logger.error(String.format("Failed to serialize %s", order), e);
            }

            // if there are enough request entries batched up then send them off to Kinesis
            if (requestEntries.size() > 20) {
                List<PutRecordsRequestEntry> entries = new ArrayList<>(requestEntries.keySet());
                // construct batched PutRecords request
                var putRequest = PutRecordsRequest.builder()
                        .streamName(streamName)
                        .records(entries)
                        .build();

                PutRecordsResponse response = null;
                try {
                    // execute batched PutRecords request
                    response = client.putRecords(putRequest);
                } catch(KinesisException e) {
                    logger.error("Failed executing PutRecords request", e);
                    continue;
                }

                // process the responses
                int i = 0;

                for (PutRecordsResultEntry resultEntry : response.records()) {
                    PutRecordsRequestEntry requestEntry = entries.get(i++);
                    Order o = requestEntries.get(requestEntry);
                    if (resultEntry.errorCode() != null) {
                        logger.error(String.format("Failed to produce %s", o));
                    } else {
                        logger.info(String.format("Produced %s sequence %s to Shard %s", o, resultEntry.sequenceNumber(), resultEntry.shardId()));
                    }
                }
                requestEntries.clear();

                // introduce artificial delay for demonstration purpose / visual tracking of logging
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
