package com.sonereker.linkconverter.link;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for the LinkConversion entity
 */
interface LinkConversionRepository extends JpaRepository<LinkConversion, Long> {
}
