
## Explaining Commit Messages
MT1: 
- Create class and sequence diagrams (inside resources file).
- Implement soft-delete feature.  We don’t want to lose previous versions of the book that we have sold to customers. Instead, we’ll mark the current version as inactive so that it can never be returned by the GetBook operation, essentially deleted.

MT2:
-  Build BookPublishRequestManager, which includes methods for adding and retrieving BookPublishRequests. It manages a collection of book publish requests, ensuring that requests are processed in the same order that they are submitted.
- Update SubmitBookForPublishingActivity according to the design document, which allows books to be submitted for publishing and returns a response with a publishingRecordId.

MT3: 
- Create the GetPublishingStatusActivity class and implement the GetPublishingStatus operation. This allows a client to check in on the state of their publishing request.

MT4:
- Write a new class BookPublishTask that implements a Runnable and processes a publish request from the BookPublishRequestManager. Using concurrency, this functionality processes the requests to get books published.
- Update the Dagger code and relevant dependencies.
- Update BookPublishRequestManagerto be thread-safe so that it behaves as expected even if multiple threads are accessing it.

## The Problem: Amazon Kindle Publishing

The Amazon Kindle store provides millions of ebooks to our customers. The process of publishing an
ebook to the kindle catalog is currently an extremely manual process, which causes a long wait time
to add a book to the catalog.

As a member of the Amazon Kindle team, you will be launching a new service that allows our
publishing department to convert books into a digital format.

The overview, architecture, and implementation are covered in the [design document here](DESIGN_DOCUMENT.md). Almost all major pieces of software at Amazon first go through an intensive design review to answer the question "Are we building the right thing for our customer?".


## Project Preparedness Tasks

Up to this point, the services we have developed in projects have had synchronous APIs. This
means that a client makes a request to the service, all of the work required to fulfill this request
is done, and then a response is returned to the client. In an asynchronous API, a client makes a
request, the service returns a response immediately, and the service completes the work after the
client disconnects. This is helpful when the work that needs to be done will take a long amount of
time. A client will only wait so long for a response, so it is helpful to quickly return a
successful response acknowledging the work is under way. The service will then continue to work on
the request as it continues to receive other, new requests. The service is working on these requests
concurrently. 

