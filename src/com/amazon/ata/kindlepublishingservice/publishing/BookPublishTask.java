package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.managers.BookPublishRequestManager;

import javax.inject.Inject;

public class BookPublishTask implements Runnable {

    private final BookPublishRequestManager requestManager;
    private final PublishingStatusDao publishingStatusDao;
    private final CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager requestManager, PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.requestManager = requestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {
        BookPublishRequest publishRequest = requestManager.getBookPublishRequestToProcess();
        CatalogItemVersion newBook = null;
//        try {
        if (publishRequest == null) {
            return;
        } else {
            publishingStatusDao.setPublishingStatus(publishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    publishRequest.getBookId());
            KindleFormattedBook formattedBook = KindleFormatConverter.format(publishRequest);
            try {
                newBook = catalogDao.createOrUpdateBook(formattedBook);
                publishingStatusDao.setPublishingStatus(publishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL,
                        newBook.getBookId());
//                } catch (BookNotFoundException e) {
//                    publishingStatusDao.setPublishingStatus(publishRequest.getPublishingRecordId(),
//                            PublishingRecordStatus.FAILED,
//                            publishRequest.getBookId(),
//                            e.getMessage());
//                }
            } catch (BookNotFoundException e) {
                publishingStatusDao.setPublishingStatus(publishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        publishRequest.getBookId(),
                        e.getMessage());
            }
            // any exception caught while processing:
//        } catch (Exception e) {
//            publishingStatusDao.setPublishingStatus(publishRequest.getPublishingRecordId(),
//                    PublishingRecordStatus.FAILED,
//                    publishRequest.getBookId(),
//                    e.getMessage());
//        }
        }
    }

}
