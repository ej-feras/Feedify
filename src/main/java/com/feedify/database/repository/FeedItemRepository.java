package com.feedify.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.feedify.database.entity.Channel;
import com.feedify.database.entity.FeedItem;
import com.feedify.database.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {

    Long countByCategories_name(String categoryName);

    List<FeedItem> findAllByChannel(Channel channel);

    Optional<FeedItem> findByLink(String link);

    Page<FeedItem> findByUsersIsAndPublishDate_YearAndPublishDate_MonthAndPublishDate_DayOfMonthOrderByPublishDateAsc(
            User user, int year, int month, int day, Pageable pageable);
}
