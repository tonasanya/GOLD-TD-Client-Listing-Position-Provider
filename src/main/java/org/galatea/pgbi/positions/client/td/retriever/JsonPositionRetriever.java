package org.galatea.pgbi.positions.client.td.retriever;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.galatea.pgbi.core.domain.Pair;
import org.galatea.pgbi.core.exception.TranslationException;
import org.galatea.pgbi.core.translator.Translator;
import org.galatea.pgbi.positions.client.td.domain.PositionKey;
import org.galatea.pgbi.positions.client.td.domain.PositionValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Retriever responsible for pulling the json files with today's front office positions and
 * returning a collection of positions
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JsonPositionRetriever implements Retriever {

  private final Translator<String, Pair<PositionKey, PositionValue>> jsonToPositionTranslator;
  @Setter
  @Value("${input.file.directory}")
  private String directoryName;
  private final TimeZone sourcePositionTimezone;

  @Override
  public Collection<Pair<PositionKey, PositionValue>> getPositions() throws Exception {

    log.debug("Fetching date for position request...");
    LocalDate requestDate = getSourcePositionDate(sourcePositionTimezone);
    log.info("Request date = {}", requestDate);

    log.debug("File directory location = {}", directoryName);
    File directory = new File(directoryName);
    File[] inputFiles = directory.listFiles();
    Collection<Pair<PositionKey, PositionValue>> positions = Sets.newHashSet();

    if (inputFiles != null && inputFiles.length != 0) {

      //Use for loop and not stream so that we can throw exception back up
      for (File inputFile : inputFiles) {
        Collection<Pair<PositionKey, PositionValue>> positionsFromFile =
            processInputFile(inputFile);
        log.debug("positions retrieved from the file {} are as follows: ", inputFile.getName(),
            positionsFromFile);
        log.info("{} positions retrieved from the file {}", positionsFromFile.size(),
            inputFile.getName());
        positions.addAll(positionsFromFile);
      }

    } else {
      log.error("inputFiles = {}", inputFiles);
      throw new IllegalStateException(
          "The directory " + directoryName + " is empty or null. No positions retrieved for date "
              + requestDate + ".");
    }

    log.info("{} positions retrieved in total for request date {}", positions.size(), requestDate);
    return positions;
  }

  //Get the date in the source timezone
  private LocalDate getSourcePositionDate(TimeZone timeZone) {

    log.debug("Timezone = {}", timeZone);
    ZonedDateTime localDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
    ZonedDateTime currentDateTime = localDateTime.withZoneSameInstant(timeZone.toZoneId());
    log.info("current date time at source timezone is {}", currentDateTime);

    return LocalDate
        .of(currentDateTime.getYear(), currentDateTime.getMonth(), currentDateTime.getDayOfMonth());
  }

  private Collection<Pair<PositionKey, PositionValue>> processInputFile(File inputFile)
      throws Exception {

    Collection<Pair<PositionKey, PositionValue>> positions = Lists.newArrayList();
    try (FileReader fileReader = new FileReader(inputFile)) {
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          try {
            positions.add(jsonToPositionTranslator.translate(line));
          } catch (TranslationException e) {
            log.warn("Failed to translate the line: " + line + " | Discarding position.");
          }
        }
      }
    }

    return positions;
  }
}
