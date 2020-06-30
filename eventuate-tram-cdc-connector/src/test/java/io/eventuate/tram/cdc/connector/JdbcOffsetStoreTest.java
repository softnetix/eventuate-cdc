package io.eventuate.tram.cdc.connector;

import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.local.BinlogFileOffset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JdbcOffsetStoreTest.Config.class)
public class JdbcOffsetStoreTest {
  @Configuration
  @EnableAutoConfiguration
  public static class Config {
    @Bean
    public JdbcOffsetStore jdbcOffsetStore(JdbcTemplate jdbcTemplate, EventuateSchema eventuateSchema) {
      return new JdbcOffsetStore(UUID.randomUUID().toString(), jdbcTemplate, eventuateSchema);
    }

    @Bean
    public EventuateSchema eventuateSchema(@Value("${eventuate.database.schema:#{null}}") String eventuateDatabaseSchema) {
      return new EventuateSchema(eventuateDatabaseSchema);
    }
  }

  @Autowired
  private JdbcOffsetStore jdbcOffsetStore;

  @Test
  public void testOffsetSaving() {
    Assert.assertEquals(Optional.empty(), jdbcOffsetStore.getLastBinlogFileOffset());

    BinlogFileOffset offset = new BinlogFileOffset("test_file", 1000);

    jdbcOffsetStore.save(offset);

    Assert.assertEquals(Optional.of(offset), jdbcOffsetStore.getLastBinlogFileOffset());
  }
}
