package com.feedify.commands;

import lombok.Data;

@Data
public class ReadCommand {
    
    private Long feedId;
    private boolean read;
}
