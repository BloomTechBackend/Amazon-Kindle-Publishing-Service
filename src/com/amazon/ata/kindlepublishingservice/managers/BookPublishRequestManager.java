package com.amazon.ata.kindlepublishingservice.managers;

import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class BookPublishRequestManager {

    Queue<BookPublishRequest> bookPublishRequestQueue;

    @Inject
    public BookPublishRequestManager(ConcurrentLinkedQueue<BookPublishRequest> bookPublishRequestQueue) {
        this.bookPublishRequestQueue = bookPublishRequestQueue;
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        this.bookPublishRequestQueue.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        return this.bookPublishRequestQueue.poll();
    }
}
