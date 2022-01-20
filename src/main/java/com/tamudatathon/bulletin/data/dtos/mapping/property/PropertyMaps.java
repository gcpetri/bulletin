package com.tamudatathon.bulletin.data.dtos.mapping.property;

import com.tamudatathon.bulletin.data.dtos.UserDto;
import com.tamudatathon.bulletin.data.entity.User;

import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyMaps {

    PropertyMap<User, UserDto> userToDtoMap;
    PropertyMap<UserDto, User> userFromDtoMap;

    @Autowired
    PropertyMaps() {
        this.userToDtoMap = new PropertyMap<User, UserDto>() {
            protected void configure() {
                map().setId(source.getUserId());
            }
        };
        this.userFromDtoMap = new PropertyMap<UserDto, User>() {
            protected void configure() {
                map().setUserId(source.getId());
            }
        };
    }

    public PropertyMap<User, UserDto> getUserToDtoMap() {
        return this.userToDtoMap;
    }

    public PropertyMap<UserDto, User> getUserFromDtoMap() {
        return this.userFromDtoMap;
    }
}
