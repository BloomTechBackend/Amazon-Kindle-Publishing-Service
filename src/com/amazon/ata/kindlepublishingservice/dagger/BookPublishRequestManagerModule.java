package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.managers.BookPublishRequestManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.LinkedList;

@Module

public class BookPublishRequestManagerModule {

    @Singleton
    @Provides
    public BookPublishRequestManager provideBookPublishRequestManager() {
        return new BookPublishRequestManager(new LinkedList<>());
    }
}
