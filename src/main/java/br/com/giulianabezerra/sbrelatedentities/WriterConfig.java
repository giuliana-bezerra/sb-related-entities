package br.com.giulianabezerra.sbrelatedentities;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class WriterConfig {

  @Bean
  ItemWriter<Venda> writer(DataSource dataSource) {
    return chunk -> {
      SimpleJdbcInsert jdbcVendas = new SimpleJdbcInsert(dataSource)
          .withTableName("vendas")
          .usingGeneratedKeyColumns("id");

      chunk.getItems().forEach(venda -> {
        BeanPropertySqlParameterSource vendaParams = new BeanPropertySqlParameterSource(venda);
        Long idVenda = jdbcVendas.executeAndReturnKey(vendaParams).longValue();

        List<ItemVenda> itensVenda = venda.itensVendas().stream()
            .map(itemVenda -> new ItemVenda(itemVenda.id(), itemVenda.description(), idVenda))
            .toList();

        SimpleJdbcInsert jdbcItensVendas = new SimpleJdbcInsert(dataSource)
            .withTableName("itens_vendas");
        jdbcItensVendas.executeBatch(SqlParameterSourceUtils.createBatch(itensVenda));
      });
    };
  }
}
