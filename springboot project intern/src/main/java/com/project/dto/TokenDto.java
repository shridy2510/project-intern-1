package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String access_token;
    private String refresh_token;

}
