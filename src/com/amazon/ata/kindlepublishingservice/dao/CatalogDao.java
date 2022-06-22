package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId

    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);


        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    /**
     * Performs 'soft' delete on the book by marking it as inactive when deleting it
     * @param bookId HashKey/ Id of the book to delete
     * @return the deleted book
     */
    public CatalogItemVersion RemoveBookFromCatalog (String bookId) {
        CatalogItemVersion deletedBook = getLatestVersionOfBook(bookId);

        if (deletedBook == null || deletedBook.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        deletedBook.setInactive(true);
        dynamoDbMapper.save(deletedBook);

        return deletedBook;
    }

//    public void validateBookExists(String bookId) {
//        CatalogItemVersion book = getLatestVersionOfBook(bookId);
//
//        if (book == null) {
//            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
//        }
//    }
    public boolean validateBookExists(String bookId) {
        try {
            CatalogItemVersion book = getLatestVersionOfBook(bookId);
            //                throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
            return book != null;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + "\n Must insert a bookId, partition key.");
        }
        return true;
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book) {
        if(book.getBookId() == null) { //add new book if it does not exist
            CatalogItemVersion newlyCreatedBook = new CatalogItemVersion();
            newlyCreatedBook.setBookId(KindlePublishingUtils.generateBookId());
            newlyCreatedBook.setTitle(book.getTitle());
            newlyCreatedBook.setAuthor(book.getAuthor());
            newlyCreatedBook.setGenre(book.getGenre());
            newlyCreatedBook.setText(book.getText());
            newlyCreatedBook.setVersion(1);
            return addCatalogItemVersion(newlyCreatedBook);
        } else { //update existing book; if book doesn't exist, throw BookNotFoundException via nother method
//            CatalogItemVersion oldBook = getLatestVersionOfBook(book.getBookId()); //throws exception if book is not found
//            oldBook.setInactive(true);
            //            addCatalogItemVersion(oldBook);
            CatalogItemVersion newBook = getLatestVersionOfBook(book.getBookId()); //throws exception if book is not found
            RemoveBookFromCatalog(book.getBookId());
            newBook.setVersion(newBook.getVersion() + 1);
            addCatalogItemVersion(newBook);
            return newBook;
        }
    }
    public CatalogItemVersion addCatalogItemVersion(CatalogItemVersion bookToAdd) {
        dynamoDbMapper.save(bookToAdd);
        return bookToAdd;
    }
}
