package com.feedify.database.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.feedify.database.entity.Channel;
import com.feedify.database.entity.FeedItemUser;
import com.feedify.database.entity.User;
import com.feedify.database.entity.compositeIds.FeedItemUserId;

@Repository
public interface ReadLaterFeedItemUserRepository extends JpaRepository<FeedItemUser, FeedItemUserId> {

        Page<FeedItemUser> findByUserAndReadLaterOrderByFeedItem_PublishDateDesc(User user, Boolean readLater,
                        Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterOrderByFeedItem_PublishDateAsc(User user, Boolean readLater,
                        Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterAndFeedItem_publishLocalDateGreaterThanEqualOrderByFeedItem_PublishDateDesc(
                        User user, Boolean readLater, LocalDate publishLocalDate, Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterAndFeedItem_publishLocalDateGreaterThanEqualOrderByFeedItem_PublishDateAsc(
                        User user, Boolean readLater, LocalDate publishLocalDate, Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterOrderByFeedItem_Channel_TitleAscFeedItem_PublishDateDesc(User user,
                        Boolean readLater, Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterAndFeedItem_publishLocalDateGreaterThanEqualOrderByFeedItem_Channel_TitleAscFeedItem_PublishDateDesc(
                        User user, Boolean readLater, LocalDate publishLocalDate, Pageable pageable);

        List<FeedItemUser> findByUserAndReadLaterAndFeedItem_Channel(User user, Boolean readLater, Channel channel);

        List<FeedItemUser> findByUserAndReadLaterAndFeedItem_ChannelAndFeedItem_publishLocalDateGreaterThanEqual(
                        User user, Boolean readLater, Channel channel, LocalDate publishLocalDate);

        Optional<FeedItemUser> findByIdAndReadLater(FeedItemUserId feedItemUserId, boolean readLater);

        Page<FeedItemUser> findByUserAndReadLaterOrderByReadAscFeedItem_PublishDateDesc(User user, boolean b,
                        Pageable pageable);

        Page<FeedItemUser> findByUserAndReadLaterAndFeedItem_publishLocalDateGreaterThanEqualOrderByReadAscFeedItem_PublishDateDesc(
                        User user, boolean b, LocalDate startDate, Pageable pageable);

        List<FeedItemUser> findByUserAndReadLaterOrderByFeedItem_PublishDateDesc(User user, boolean b);

        List<FeedItemUser> findByUserAndReadLaterAndFeedItem_publishLocalDateGreaterThanEqualOrderByFeedItem_PublishDateDesc(
                        User user, boolean b, LocalDate startDate);
}
