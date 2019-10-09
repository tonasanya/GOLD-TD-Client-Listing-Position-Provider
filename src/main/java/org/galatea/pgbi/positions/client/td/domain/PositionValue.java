package org.galatea.pgbi.positions.client.td.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Position domain object for Trade Date Client Listing/Account Level positions
 */
@Value
@Builder
public class PositionValue {

  private final LocalDate businessDate;
  private final LocalDate effectiveDate;
  private final String timezone;
  @NonNull
  private final Long quantity;
}
