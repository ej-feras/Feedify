package com.feedify.rest.service.sortingandfiltering;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.feedify.database.entity.FeedItemUser;

@Service
public class CustomPagination {

	public double getLastPageNumber(Integer pageSize, List<?> list) {
		double lastPageNumber = Math.ceil(list.size() / (double) pageSize);

		return lastPageNumber;
	}
	


	public List<FeedItemUser> getNextPageOfFeedItems(Integer pageNumber, Integer pageSize,
			List<FeedItemUser> feedItemsUsers) {

		double lastPage =  getLastPageNumber(pageSize, feedItemsUsers);
		int nextPage = (pageNumber * pageSize) + pageSize;

		// feeds number is less than the page size
		if (pageNumber < lastPage && feedItemsUsers.size() < pageSize)
			return feedItemsUsers;
		// next feeds page index is greater than feeditems last index
		else if (pageNumber < lastPage && nextPage > feedItemsUsers.size())
			feedItemsUsers = feedItemsUsers.subList(pageNumber * pageSize, feedItemsUsers.size());
		else if (pageNumber < lastPage)
			feedItemsUsers = feedItemsUsers.subList(pageNumber * pageSize, (pageNumber * pageSize) + pageSize);

		else
			feedItemsUsers = Collections.emptyList();

		return feedItemsUsers;
	}

	public List<Long> getNextPageOfFeedItemsIds(Integer pageNumber, Integer pageSize, List<Long> feedItemsIds) {
		double lastPage = getLastPageNumber(pageSize, feedItemsIds);

		// ids number is less than the page size
		if (pageNumber < lastPage && feedItemsIds.size() < pageSize)
			return feedItemsIds;
		else if (pageNumber < lastPage)
			feedItemsIds = feedItemsIds.subList(0, pageNumber * pageSize);

		return feedItemsIds;
	}
}
