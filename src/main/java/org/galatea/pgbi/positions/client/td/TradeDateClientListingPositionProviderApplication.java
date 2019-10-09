package org.galatea.pgbi.positions.client.td;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TradeDateClientListingPositionProviderApplication {

  public static void main(String[] args) {
    log.info(
        "Starting Trade Date Client Listing Position Provider Application with the following args {}",
        args);
    SpringApplication.run(TradeDateClientListingPositionProviderApplication.class, args);
  }
}
