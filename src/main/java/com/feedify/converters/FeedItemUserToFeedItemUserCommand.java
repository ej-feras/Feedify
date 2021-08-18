package com.feedify.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.feedify.commands.FeedItemCommand;
import com.feedify.commands.FeedItemUserCommand;
import com.feedify.database.entity.FeedItem;
import com.feedify.database.entity.FeedItemUser;
import com.feedify.database.entity.User;

import lombok.Data;

@Component
@Data
public class FeedItemUserToFeedItemUserCommand implements Converter<FeedItemUser, FeedItemUserCommand> {
    private User user;
    private String channelCategory;

    @Nullable
    @Override
    public FeedItemUserCommand convert(FeedItemUser source) {
        if (source == null) {
            return null;
        }

        final FeedItemUserCommand feedItemUserCommand = new FeedItemUserCommand();
        FeedItemToFeedItemCommand feedItemToFeedItemCommand = new FeedItemToFeedItemCommand();
        FeedItem feedItem = source.getFeedItem();
        FeedItemCommand feedItemCommand = feedItemToFeedItemCommand.convert(feedItem);
        feedItemCommand.setChannelCategory(channelCategory);
        feedItemUserCommand.setUserId(user.getId());
        feedItemUserCommand.setFeedItemId(feedItem.getId());
        feedItemUserCommand.setFeedItemCommand(feedItemCommand);
        feedItemUserCommand.setLiked(source.isLiked());
        feedItemUserCommand.setReadLater(source.isReadLater());
        feedItemUserCommand.setRead(source.isRead());

        return feedItemUserCommand;
    }

}