package com.sonereker.linkconverter.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

/**
 * LinkConversion Entity
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "deep_link", nullable = false)
    private String deepLink;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * Builds a LinkConversion instance with properties given in LinkDto
     *
     * @param linkDto {@link com.sonereker.linkconverter.link.LinkDto}
     * @return LinkConversion instance
     */
    public static LinkConversion fromDto(LinkDto linkDto) {
        return LinkConversion.builder().deepLink(linkDto.deepLink()).url(linkDto.url()).build();
    }

    @PrePersist
    private void prePersist() {
        this.created = LocalDateTime.now();
    }
}
