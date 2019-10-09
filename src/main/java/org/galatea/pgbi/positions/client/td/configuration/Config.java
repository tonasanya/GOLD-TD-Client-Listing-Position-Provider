package org.galatea.pgbi.positions.client.td.configuration;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  /**
   * Position source timezone
   *
   * @param timeZoneCode
   * @return
   */
  @Bean
  public TimeZone timeZone(@Value("${input.timezone.code}") String timeZoneCode) {

    if (!ZoneId.getAvailableZoneIds().contains(timeZoneCode)) {
      throw new IllegalArgumentException(
          "Time zonce code " + timeZoneCode + " is not a valid timezone");
    }

    return TimeZone.getTimeZone(timeZoneCode);
  }
}
