package com.sonereker.linkconverter.link;

import org.springframework.data.repository.CrudRepository;

/**
 * JpaRepository for the LinkConversion entity
 */
interface LinkConversionRepository extends CrudRepository<LinkConversion, Long> {
}
