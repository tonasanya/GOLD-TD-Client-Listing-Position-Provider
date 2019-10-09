package org.galatea.pgbi.positions.client.td.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * Position Key domain object for Trade Date Client Listing/Account Level positions
 */
@Value
@Builder
public class PositionKey {

  private final ZonedDateTime knowledgeDateTime;
  private final ZonedDateTime effectiveDateTime;
  //Source client identifier
  private final String clientId;
  //Source account identifier
  private final String accountId;
  //Counterparty account identifier
  private final String counterpartyId;
  //unenriched listing level RIC for example or a source id
  private final String securityId;
  //Can be just a local date due to the assumption that the position versioning is handled from one
  //location only per unique id (most likely per account)
  private final LocalDate versionDate;
  private final Long versionNumber;

}
