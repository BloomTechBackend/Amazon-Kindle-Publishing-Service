package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.List;


public class PublishingStatusRecordConverter {

    private PublishingStatusRecordConverter() {};

    public static PublishingStatusRecord toPublishingStatusRecord(PublishingStatusItem publishingStatusItem) {
        return PublishingStatusRecord.builder()
                .withStatus(publishingStatusItem.getStatus().name())
                .withStatusMessage(publishingStatusItem.getStatusMessage())
                .withBookId(publishingStatusItem.getBookId())
                .build();
    }
}
