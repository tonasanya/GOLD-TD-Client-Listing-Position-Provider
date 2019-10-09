package org.galatea.pgbi.positions.client.td.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Position Key domain object for Trade Date Client Listing/Account Level positions
 */
@Value
@Builder
public class PositionKey {

  @NonNull
  private final ZonedDateTime knowledgeDateTime;
  @NonNull
  private final ZonedDateTime effectiveDateTime;
  //Source client identifier
  @NonNull
  private final String clientId;
  //Source account identifier
  @NonNull
  private final String accountId;
  //Counterparty account identifier
  @NonNull
  private final String counterpartyId;
  //unenriched listing level RIC for example or a source id
  @NonNull
  private final String securityId;
  //Can be just a local date due to the assumption that the position versioning is handled from one
  //location only per unique id (most likely per account)
  @NonNull
  private final LocalDate versionDate;
  @NonNull
  private final Long versionNumber;

}
