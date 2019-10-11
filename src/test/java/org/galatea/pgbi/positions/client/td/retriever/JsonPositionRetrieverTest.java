package org.galatea.pgbi.positions.client.td.retriever;

import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;

import com.google.common.collect.Lists;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.TimeZone;
import lombok.SneakyThrows;
import org.galatea.pgbi.core.domain.Pair;
import org.galatea.pgbi.core.test.APowerMockSpringTest;
import org.galatea.pgbi.core.translator.Translator;
import org.galatea.pgbi.positions.client.td.configuration.Config;
import org.galatea.pgbi.positions.client.td.domain.PositionKey;
import org.galatea.pgbi.positions.client.td.domain.PositionValue;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test class for the JSON Position Retriever
 */
@ContextConfiguration(classes = {JsonPositionRetriever.class, Config.class})
@ActiveProfiles(profiles = "test")
@PrepareForTest({LocalDateTime.class, ZoneId.class, JsonPositionRetriever.class})
public class JsonPositionRetrieverTest extends APowerMockSpringTest {

  @Autowired
  private JsonPositionRetriever jsonPositionRetriever;
  @MockBean
  private Translator<String, Pair<PositionKey, PositionValue>> jsonToPositionTranslator;
  @Autowired
  private TimeZone timeZone;

  @Before
  @SneakyThrows
  public void setUp() {
    PowerMockito.mockStatic(LocalDateTime.class);
    ZoneId zoneId = TimeZone.getTimeZone("GMT").toZoneId();
    ZonedDateTime mockTime = ZonedDateTime.of(2019, 10, 11, 9, 0, 0, 0, zoneId);
    when(LocalDateTime.now()).thenReturn(mockTime.toLocalDateTime());

    PowerMockito.mockStatic(ZoneId.class);
    when(ZoneId.systemDefault()).thenReturn(TimeZone.getTimeZone("GMT").toZoneId());
  }

  @Test(expected = IllegalStateException.class)
  @SneakyThrows
  @DirtiesContext
  public void JsonPositionRetriever_DirectoryDoesntExist_ThrowsIllegalStateException() {
    //given
    String testDirectory = "N/A";
    jsonPositionRetriever.setDirectoryName(testDirectory);

    //when
    jsonPositionRetriever.getPositions();

    //then
    //Throw IllegalStateException
  }

  @Test(expected = IllegalStateException.class)
  @SneakyThrows
  @DirtiesContext
  public void JsonPositionRetriever_DirectoryContainsNoFiles_ThrowsIllegalStateException() {
    //given
    String testDirectory = "src\\test\\resources\\Empty";
    jsonPositionRetriever.setDirectoryName(testDirectory);

    //when
    jsonPositionRetriever.getPositions();

    //then
    //Throw IllegalStateException
  }

  @Test
  @SneakyThrows
  public void JsonPositionRetriever_NormalConditions_LoadPositions() {
    //given
    ZonedDateTime dateTime1 = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1570527347L), TimeZone.getTimeZone("UTC").toZoneId());
    ZonedDateTime dateTime2 = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(1570700147), TimeZone.getTimeZone("UTC").toZoneId());
    String counterparty = "FIRM";
    String timeZone = "UTC";

    String account1 = "ICP5952";
    String ric1 = "MYOS.5";
    Long qty1 = 8799L;

    String account2 = "ICP6736";
    String ric2 = "RGLD.18";
    Long qty2 = 7902L;

    String account3 = "ECP3475";
    String ric3 = "FYT.9";
    Long qty3 = 8951L;

    String ric4 = "FYT.11";

    PositionKey key1 =
        PositionKey.builder().accountId(account1).counterpartyId(counterparty)
            .effectiveDateTime(dateTime1).knowledgeDateTime(dateTime1).securityId(ric1).versionDate(
            LocalDate.now()).versionNumber(1L)
            .build();
    PositionValue value1 = PositionValue.builder().businessDate(dateTime1.toLocalDate())
        .effectiveDate(dateTime1.toLocalDate()).quantity(qty1).timezone(timeZone).build();
    Pair<PositionKey, PositionValue> position1 = new Pair<>(key1, value1);

    PositionKey key2 = PositionKey.builder().accountId(account2).counterpartyId(counterparty)
        .effectiveDateTime(dateTime2).knowledgeDateTime(dateTime1).securityId(ric2).versionDate(
            LocalDate.now()).versionNumber(1L)
        .build();
    PositionValue value2 = PositionValue.builder().businessDate(dateTime1.toLocalDate())
        .effectiveDate(dateTime2.toLocalDate()).quantity(qty2).timezone(timeZone).build();
    Pair<PositionKey, PositionValue> position2 = new Pair<>(key2, value2);

    PositionKey key3 = PositionKey.builder().accountId(account3).counterpartyId(counterparty)
        .effectiveDateTime(dateTime2).knowledgeDateTime(dateTime1).securityId(ric3).versionDate(
            LocalDate.now()).versionNumber(1L)
        .build();
    PositionValue value3 = PositionValue.builder().businessDate(dateTime1.toLocalDate())
        .effectiveDate(dateTime2.toLocalDate()).quantity(qty3).timezone(timeZone).build();
    Pair<PositionKey, PositionValue> position3 = new Pair<>(key3, value3);

    PositionKey key4 = PositionKey.builder().accountId(account3).counterpartyId(counterparty)
        .effectiveDateTime(dateTime2).knowledgeDateTime(dateTime1).securityId(ric4).versionDate(
            LocalDate.now()).versionNumber(1L)
        .build();
    PositionValue value4 = PositionValue.builder().businessDate(dateTime1.toLocalDate())
        .effectiveDate(dateTime2.toLocalDate()).quantity(qty3).timezone(timeZone).build();
    Pair<PositionKey, PositionValue> position4 = new Pair<>(key4, value4);

    Collection<Pair<PositionKey, PositionValue>> expectedPositions =
        Lists.newArrayList(position1, position2, position3, position4);

    String jsonPosition1 =
        "{\"ric\":\"MYOS.5\",\"position_type\":\"TD\",\"knowledge_date\":1570527347,\"effective_date\":1570527347,\"account\":\"ICP5952\",\"direction\":\"Credit\",\"qty\":8799,\"purpose\":\"Outright\",\"time_stamp\":1570527347}";

    String jsonPosition2 =
        "{\"ric\":\"RGLD.18\",\"position_type\":\"TD\",\"knowledge_date\":1570527347,\"effective_date\":1570700147,\"account\":\"ICP6736\",\"direction\":\"Debit\",\"qty\":7902,\"purpose\":\"Outright\",\"time_stamp\":1570527347}";

    String jsonPosition3 =
        "{\"ric\":\"FYT.9\",\"position_type\":\"TD\",\"knowledge_date\":1570527347,\"effective_date\":1570700147,\"account\":\"ECP3475\",\"direction\":\"Credit\",\"qty\":8951,\"purpose\":\"Outright\",\"time_stamp\":1570527347}";

    String jsonPosition4 =
        "{\"ric\":\"FYT.11\",\"position_type\":\"TD\",\"knowledge_date\":1570527347,\"effective_date\":1570700147,\"account\":\"ECP3475\",\"direction\":\"Credit\",\"qty\":8951,\"purpose\":\"Outright\",\"time_stamp\":1570527347}";

    when(jsonToPositionTranslator.translate(jsonPosition1)).thenReturn(position1);
    when(jsonToPositionTranslator.translate(jsonPosition2)).thenReturn(position2);
    when(jsonToPositionTranslator.translate(jsonPosition3)).thenReturn(position3);
    when(jsonToPositionTranslator.translate(jsonPosition4)).thenReturn(position4);

    //when
    Collection<Pair<PositionKey, PositionValue>> positions = jsonPositionRetriever.getPositions();

    //then
    assertReflectionEquals(expectedPositions, positions, LENIENT_ORDER);
  }
}
