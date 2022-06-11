package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.PublishingStatusRecordConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetPublishingStatusActivity {

    PublishingStatusDao publishingStatusDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusActivity() {
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest request) {
        List<PublishingStatusItem> statusResponse =
                publishingStatusDao.getPublishingRecordStatus(request.getPublishingRecordId());

        // Convert each Item in the list to Record:
        List<PublishingStatusRecord> statusRecordList = new ArrayList<>();

        for(PublishingStatusItem statusItem : statusResponse) {
            statusRecordList.add(PublishingStatusRecordConverter.toPublishingStatusRecord(statusItem));
        }

        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(statusRecordList)
                .build();
    }
}
