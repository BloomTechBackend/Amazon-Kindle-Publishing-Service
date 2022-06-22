package com.amazon.ata.kindlepublishingservice;

import com.amazon.ata.kindlepublishingservice.dagger.ATAKindlePublishingServiceManager;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.dagger.DaggerApplicationComponent;
import com.amazon.ata.kindlepublishingservice.managers.BookPublishRequestManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static final ApplicationComponent component = DaggerApplicationComponent.create();
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);


        ATAKindlePublishingServiceManager publishingManager = component.provideATAKindlePublishingServiceManager();

        //MT4, milestone 2.
        BookPublishRequestManager publishRequestManager = component.provideBookPublishRequestManager();

        Thread bookPublishTaskThread = new Thread(component.provideBookPublishTask());

        try {
            publishingManager.start();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
