package com.thecodinginterface.kinesis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class OrdersConsumer implements RequestHandler<KinesisEvent, Void> {
    private final ObjectMapper objMapper = new ObjectMapper();

    @Override
    public Void handleRequest(KinesisEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Kinesis Java Lambda Consumer Invoked: records = " + event.getRecords().size());

        for (KinesisEventRecord rec : event.getRecords()) {
            try {
                var order = objMapper.readValue(rec.getKinesis().getData().array(), Order.class);
                logger.log("Processed order: " + order);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log("Error de-serializing Order");
                logger.log("Error: " + e.getMessage());
            }
        }

        return null;
    }
}
