package com.feedify.database.entity;

import lombok.Data;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.feedify.database.entity.compositeIds.FeedItemUserId;

@Data
@Entity
@Table(name = "feed_item_x_user")
public class FeedItemUser {

	@EmbeddedId
	private FeedItemUserId id = new FeedItemUserId();
	private boolean liked;
	private boolean read;
	private boolean readLater;
	@Column(columnDefinition = "integer default 0")
	private Integer clicks;
	private LocalDateTime lastReadingDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("feedItemId")
	private FeedItem feedItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;
}
