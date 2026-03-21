package com.jitendra.userservice.model;

import lombok.Data;


public interface UserSummary {
    Long getId();
    String getName();
    String getEmail();
}