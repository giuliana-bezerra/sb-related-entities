package br.com.giulianabezerra.sbrelatedentities;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Configuration
public class WriterConfig {
  @Bean
  ItemWriter<Venda> writer(DataSource dataSource) {
    return chunk -> {
      List<? extends Venda> vendas = chunk.getItems();
      SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(vendas.toArray());
      KeyHolder keyHolder = new GeneratedKeyHolder();
      NamedParameterJdbcTemplate jdbcVendas = new NamedParameterJdbcTemplate(dataSource);

      for (int i = 0; i < batch.length; i++) {
        jdbcVendas.update("INSERT INTO vendas (description) VALUES (:description)", batch[0], keyHolder,
            new String[] { "id" });
        Long idVenda = keyHolder.getKey().longValue();

        Venda venda = chunk.getItems().get(i);
        List<ItemVenda> itensVenda = venda.itensVendas().stream()
            .map(itemVenda -> new ItemVenda(itemVenda.id(), itemVenda.description(), idVenda))
            .toList();

        SimpleJdbcInsert jdbcItensVendas = new SimpleJdbcInsert(dataSource)
            .withTableName("itens_vendas");
        jdbcItensVendas.executeBatch(SqlParameterSourceUtils.createBatch(itensVenda));
      }
    };
  }
}
