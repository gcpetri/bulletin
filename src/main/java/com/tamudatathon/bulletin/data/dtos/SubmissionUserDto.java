package com.tamudatathon.bulletin.data.dtos;

import java.util.List;

public class SubmissionUserDto {
    
    private List<String> discordInfo;

    public List<String> getDiscordInfo() {
        return this.discordInfo;
    }

    public void setDiscordInfo(List<String> discordInfo) {
        this.discordInfo = discordInfo;
    }
}
