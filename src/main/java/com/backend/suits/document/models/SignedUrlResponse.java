package com.backend.suits.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignedUrlResponse {
    @JsonProperty("signedURL")
    private String signedUrl;

}
