package com.feedify.commands;

import lombok.Data;

@Data
public class PostChannelCommand {

    private String channelUrl;
    private String category;
    private CurrentPageCommand currentPageCommand;
}
