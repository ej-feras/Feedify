package com.feedify.commands;

import lombok.Data;

@Data
public class CategoryCommand {

    private String name;
    private Long numberOfUnreadFeeds;
}
