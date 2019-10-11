package org.galatea.pgbi.positions.client.td.configuration;

import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Config {

  /**
   * Position source timezone
   */
  @Bean
  public TimeZone timeZone(@Value("${input.timezone.code}") String timeZoneCode) {

    return TimeZone.getTimeZone(timeZoneCode);
  }
}
