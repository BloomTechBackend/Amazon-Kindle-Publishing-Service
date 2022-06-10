package com.amazon.ata.kindlepublishingservice.managers;

import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {

    Queue<BookPublishRequest> bookPublishRequestQueue;

    @Inject
    public BookPublishRequestManager(Queue<BookPublishRequest> bookPublishRequestQueue) {
        this.bookPublishRequestQueue = new LinkedList<>();
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        this.bookPublishRequestQueue.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() { return this.bookPublishRequestQueue.poll();}
}
