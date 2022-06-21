package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.managers.BookPublishRequestManager;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishTask;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublisher;

import com.amazon.ata.kindlepublishingservice.publishing.NoOpTask;
import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;
import javax.xml.crypto.Data;

@Module
public class PublishingModule {

    @Provides
    @Singleton
//    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService) {
//        return new BookPublisher(scheduledExecutorService, new NoOpTask());
//    }
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService, BookPublishTask bookPublishTask) {
        return new BookPublisher(scheduledExecutorService, bookPublishTask);
    }


    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }

    @Provides
    @Singleton
    public BookPublishTask provideBookPublishTask (BookPublishRequestManager requestManager,
                                                   PublishingStatusDao publishingStatusDao,
                                                   CatalogDao catalogDao) {
        return new BookPublishTask(requestManager, publishingStatusDao, catalogDao);
    }
}
