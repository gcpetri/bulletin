package com.tamudatathon.bulletin.data.dtos;

public class UserDto {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String discordInfo;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiscordInfo() {
        return this.discordInfo;
    }

    public void setDiscordInfo(String discordInfo) {
        this.discordInfo = discordInfo;
    }
}
