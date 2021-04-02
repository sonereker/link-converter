package com.sonereker.linkconverter.link;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO record for LinkConversion
 */
public record LinkDto(@JsonProperty("url") String url, @JsonProperty("deepLink") String deepLink) {
}
