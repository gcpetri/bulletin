package com.tamudatathon.bulletin.middleware;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    private String authId;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isAdmin;

    public String getAuthId() {
        return this.authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getEmailId() {
        return this.email;
    }

    public void setEmailId(String email) {
        this.email = email;
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

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String toString() {
        return this.authId;
    }
}
