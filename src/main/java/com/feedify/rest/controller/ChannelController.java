package com.feedify.rest.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.feedify.commands.CategoryCommand;
import com.feedify.commands.ChannelCommand;
import com.feedify.commands.CurrentPageCommand;
import com.feedify.commands.PostCategoryCommand;
import com.feedify.commands.PostChannelCommand;
import com.feedify.config.security.SecurityUser;
import com.feedify.database.entity.Channel;
import com.feedify.rest.service.ChannelService;
import com.feedify.rest.service.FaviconExtractorService;
import com.rometools.rome.feed.synd.SyndFeed;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChannelController {

	private ChannelService channelService;
	private FaviconExtractorService faviconExtractorService;

	public ChannelController(ChannelService channelService, FaviconExtractorService faviconExtractorService) {
		this.channelService = channelService;
		this.faviconExtractorService = faviconExtractorService;
	}

	/**
	 * @param mav
	 * @param url
	 * @return ModelAndView
	 */
	@GetMapping(value = { "/search-channel" })
	public ModelAndView searchForChannel(ModelAndView mav, @RequestParam("url") String url,
			@AuthenticationPrincipal SecurityUser securityUser) {

		Optional<SyndFeed> parsedRssFeedFromURL = channelService.parseRssFeedFromURL(url);
		boolean channelExists = channelService.existsChannelURL(securityUser.getUser(), url);

		if (!parsedRssFeedFromURL.isPresent()) {
			mav.addObject("channelInfo", "URL was not found");

		} else if (channelExists) {
			mav.addObject("channelInfo", "Channel already exists");

		} else {
			SyndFeed feed = parsedRssFeedFromURL.get();
			mav.addObject("channelInfo", feed.getTitle());

			try {
				List<URL> links = faviconExtractorService.findFaviconLinks(feed.getLink());
				if (!links.isEmpty())
					mav.addObject("faviconLink", links.get(0).toString());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		ChannelCommand channelCommand = new ChannelCommand();
		channelCommand.setChannelUrl(url);

		mav.addObject("channelCommand", channelCommand);
		mav.setViewName("layouts/subscribe-modal :: subscribe-modal");
		return mav;
	}

	/**
     * @param mav
     * @param channelCommand
     * @return ModelAndView
     */
    @PostMapping(value = { "/save-channel" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ModelAndView saveChannel(ModelAndView mav, @RequestBody PostChannelCommand postChannelCommand,
            @AuthenticationPrincipal SecurityUser securityUser) {
        String url = postChannelCommand.getChannelUrl();
        String categoryNameOfChannel = postChannelCommand.getCategory();
        CurrentPageCommand currentPageCommand = postChannelCommand.getCurrentPageCommand();
        try {
            Optional<Channel> channel = channelService.subscribeToChannel(securityUser.getUser(), url, categoryNameOfChannel);

        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    	mav = redirectToFeedsPage(mav, currentPageCommand);

		return mav;
    }

	@GetMapping(value = "/find-categories")
	@ResponseBody
	public List<String> findCategories(@AuthenticationPrincipal SecurityUser securityUser) {
		List<String> allCategories = new ArrayList<String>();
		try {

			allCategories = channelService.findAllChannelsCategoriesByUser(securityUser.getUser());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allCategories;

	}

	@PostMapping(value = { "/edit-category" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView editCategory(ModelAndView mav, @RequestBody PostCategoryCommand postCategoryCommand,
			@AuthenticationPrincipal SecurityUser securityUser) {
		CategoryCommand categoryCommand = postCategoryCommand.getCategoryCommand();
		String oldName = categoryCommand.getName();
		String newName = postCategoryCommand.getNewCategoryName();
		CurrentPageCommand currentPageCommand = postCategoryCommand.getCurrentPageCommand();
		try {

			channelService.changeCategoryName(securityUser.getUser(), oldName, newName);
		} catch (Exception e) {

		}

		mav = redirectToFeedsPage(mav, currentPageCommand);

		return mav;
	}

	private ModelAndView redirectToFeedsPage(ModelAndView mav, CurrentPageCommand currentPageCommand) {
		if (currentPageCommand.getCategoryName() != null)
			mav.setViewName("redirect:/feeds-page?currentFeedsUrl=" + currentPageCommand.getCurrentFeedsUrl()
					+ "&category=" + currentPageCommand.getCategoryName() + "&view="
					+ currentPageCommand.getSelectedView() + "&period=" + currentPageCommand.getSelectedPeriod()
					+ "&orderBy=" + currentPageCommand.getSelectedOrder() + "&pageNumber=0");
		else if (currentPageCommand.getChannelTitle() != null)
			mav.setViewName("redirect:/feeds-page?currentFeedsUrl=" + currentPageCommand.getCurrentFeedsUrl()
					+ "&channelTitle=" + currentPageCommand.getChannelTitle() + "&view="
					+ currentPageCommand.getSelectedView() + "&period=" + currentPageCommand.getSelectedPeriod()
					+ "&orderBy=" + currentPageCommand.getSelectedOrder() + "&pageNumber=0");
		else
			mav.setViewName("redirect:/feeds-page?currentFeedsUrl=" + currentPageCommand.getCurrentFeedsUrl() + "&view="
					+ currentPageCommand.getSelectedView() + "&period=" + currentPageCommand.getSelectedPeriod()
					+ "&orderBy=" + currentPageCommand.getSelectedOrder() + "&pageNumber=0");

		return mav;
	}

	@PostMapping(value = { "/delete-category" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView deleteCategory(ModelAndView mav, @RequestBody PostCategoryCommand postCategoryCommand,
			@AuthenticationPrincipal SecurityUser securityUser) {
		CategoryCommand categoryCommand = postCategoryCommand.getCategoryCommand();
		String categoryName = categoryCommand.getName();
		CurrentPageCommand currentPageCommand = postCategoryCommand.getCurrentPageCommand();
		try {

			channelService.deleteCategory(securityUser.getUser(), categoryName);
		} catch (Exception e) {
			e.printStackTrace();

		}

		mav = redirectToFeedsPage(mav, currentPageCommand);

		return mav;
	}
}
