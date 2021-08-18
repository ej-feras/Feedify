package com.feedify.commands;

import lombok.Data;

@Data
public class ReadLaterCommand {

    private Long feedId;
    private boolean readLater;
}
