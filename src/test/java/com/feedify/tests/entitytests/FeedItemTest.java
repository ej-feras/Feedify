package com.feedify.tests.entitytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.feedify.database.entity.FeedItem;
import com.feedify.database.repository.FeedItemRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class FeedItemTest {

    @Autowired
    private FeedItemRepository feedItemRepo;
    private static final String DESC = "A short story";
    private LocalDateTime ldt= LocalDateTime.now();

    @BeforeTransaction
    public void init(){
        FeedItem feedItem = new FeedItem();
        feedItem.setId(1l);
        feedItem.setDescription(DESC);
        feedItem.setLink("https://google-news");
        feedItem.setTitle("One day in my life");
        feedItem.setContent("Here is some content");
        feedItem.setPublishDate(ldt);
        feedItemRepo.save(feedItem);

        FeedItem feedItem_ = new FeedItem();
        feedItem_.setId(2l);
        feedItem_.setDescription(DESC);
        feedItem_.setLink("https://spiegel-news");
        feedItem_.setTitle("A second story");
        feedItem_.setContent("Here is some content for feed2");
        feedItem_.setPublishDate(ldt);
        feedItemRepo.save(feedItem_);

    }

    @Test
    public void findFeedItemTest(){
        FeedItem feedItem = feedItemRepo.findById(2l).get();
        assertEquals(feedItem.getId(), 2);
    }

    @Test
    public void updateFeedItemTest(){
        FeedItem feedItem = feedItemRepo.findById(1l).get();
        feedItem.setTitle("A second chance");
        assertEquals(feedItem.getTitle(), "A second chance");
    }

    @Test
    public void findAllFeedItemTest(){
        List<FeedItem> feedItems = feedItemRepo.findAll();
        assertEquals(feedItems.size(), 2);
    }
}
