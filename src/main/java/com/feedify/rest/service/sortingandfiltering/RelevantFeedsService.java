package com.feedify.rest.service.sortingandfiltering;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feedify.database.entity.FeedItemUser;
import com.feedify.database.entity.User;
import com.feedify.database.repository.RecentlyReadFeedItemUserRepository;
import com.feedify.recommender.RecommenderUtil;
import com.feedify.recommender.TF_IDF;

@Service
public class RelevantFeedsService {

    @Autowired
    private RecentlyReadFeedItemUserRepository recentlyReadFeedItemUserRepository;

    public List<FeedItemUser> findFeedItemsUserOrderedByRelevance(User user, List<FeedItemUser> feedsToSort) {
        List<String> query = createSortingByRelevanceQuery(user);

        List<List<String>> docs = new ArrayList<List<String>>();
        Map<FeedItemUser, Double> tfIdfMap = new LinkedHashMap<FeedItemUser, Double>();

        // add main document (query)
        docs.add(query);

        // add documents with which the main query is to be compared.
        // These are all feed items except the 100 feed items that make up the
        // main query.
        feedsToSort.stream().forEach(e -> {
            if (e.getLastReadingDate() == null) {

                String language = e.getFeedItem().getLanguage();
                String title = e.getFeedItem().getTitle();
                String description = e.getFeedItem().getDescription();
                String tags = e.getFeedItem().getCategories().stream().map(c -> c.getName()).reduce("",
                        (a, b) -> a + " " + b + " ");

                // a document consists of the title, the description, and tags of
                // a feed item
                String doc = title + " " + description + " " + tags;
                List<String> cleanedDocument = RecommenderUtil.cleanDocument(doc, language);
                docs.add(cleanedDocument);
                // initialize the similarity of feedItemsUser to the main query with 0.
                tfIdfMap.put(e, 0.0);
            }
        });

        // similarity between main document (query) and other docs
        TF_IDF tfIdf = new TF_IDF(docs);
        for (int i = 1; i < tfIdf.getDocs().size(); i++) {
            FeedItemUser key = (FeedItemUser) tfIdfMap.keySet().toArray()[i - 1];
            tfIdfMap.put(key, tfIdf.getSimilarity(0, i));
        }

        // sort similarity in desc order
        Map<FeedItemUser, Double> reverseSortedMap = tfIdfMap.entrySet().stream()
                .sorted(Map.Entry.<FeedItemUser, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        reverseSortedMap.forEach((k, v) -> System.out.println(k.getId() + " -> " + v));

        List<FeedItemUser> feedItemsUser = reverseSortedMap.keySet().stream().collect(Collectors.toList());

        return feedItemsUser;
    }

    private List<String> createSortingByRelevanceQuery(User user) {
        // create a query of the 100 recently read feeds.
        List<String> query = new ArrayList<>();

        List<FeedItemUser> top100RecentlyReadFeeds = recentlyReadFeedItemUserRepository
                .findTop100ByUserAndLastReadingDateNotNullOrderByLastReadingDateDesc(user);

        if (top100RecentlyReadFeeds.isEmpty()) {
            query = user.getUserInterests();

        } else {
            List<List<String>> bagsOfWords = top100RecentlyReadFeeds.stream().map(e -> {
                String language = e.getFeedItem().getLanguage();
                String tags = e.getFeedItem().getCategories().stream().map(c -> c.getName()).reduce("",
                        (a, b) -> a + " " + b + " ");
                String userInterests = user.getUserInterests().stream().reduce("", (a, b) -> a + " " + b + " ");
                String currentQuery = e.getFeedItem().getTitle() + " " + e.getFeedItem().getDescription() + " " + " "
                        + tags + " " + userInterests;
                List<String> wordList = RecommenderUtil.cleanDocument(currentQuery, language);
                return wordList;
            }).collect(Collectors.toList());

            bagsOfWords.forEach(query::addAll);
        }

        return query;
    }

}
