package com.feedify.database.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.feedify.database.entity.Channel;
import com.feedify.database.entity.FeedItemUser;
import com.feedify.database.entity.User;
import com.feedify.database.entity.compositeIds.FeedItemUserId;

public interface RecentlyReadFeedItemUserRepository extends JpaRepository<FeedItemUser, FeedItemUserId> {

        List<FeedItemUser> findTop100ByUserAndLastReadingDateNotNullOrderByLastReadingDateDesc(User user);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullOrderByLastReadingDateDesc(User user, Pageable pageable);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullAndFeedItem_publishLocalDateGreaterThanEqualOrderByLastReadingDateDesc(
                        User user, LocalDate startDate, Pageable pageable);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullOrderByLastReadingDateAsc(User user, Pageable pageable);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullAndFeedItem_publishLocalDateGreaterThanEqualOrderByLastReadingDateAsc(
                        User user, LocalDate startDate, Pageable pageable);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullOrderByFeedItem_Channel_TitleAscLastReadingDateDesc(
                        User user, Pageable pageable);

        Page<FeedItemUser> findByUserAndLastReadingDateNotNullAndFeedItem_publishLocalDateGreaterThanEqualOrderByFeedItem_Channel_TitleAscLastReadingDateDesc(
                        User user, LocalDate startDate, Pageable pageable);

        Collection<? extends FeedItemUser> findByUserAndLastReadingDateNotNullAndFeedItem_Channel(User user,
                        Channel channel);

        Collection<? extends FeedItemUser> findByUserAndLastReadingDateNotNullAndFeedItem_ChannelAndFeedItem_publishLocalDateGreaterThanEqual(
                        User user, Channel channel, LocalDate startDate);

        Optional<FeedItemUser> findByIdAndLastReadingDateNotNull(FeedItemUserId feedItemUserId);

}
