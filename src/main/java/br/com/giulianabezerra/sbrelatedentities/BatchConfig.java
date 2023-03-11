package br.com.giulianabezerra.sbrelatedentities;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
  private JobRepository jobRepository;
  private PlatformTransactionManager transactionManager;

  public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
  }

  @Bean
  Job job(Step step) {
    return new JobBuilder("job", jobRepository)
        .start(step)
        .incrementer(new RunIdIncrementer())
        .build();
  }

  @Bean
  Step step(ItemReader<Venda> reader, ItemWriter<Venda> writer) {
    return new StepBuilder("step", jobRepository)
        .<Venda, Venda>chunk(10, transactionManager)
        .reader(reader)
        .writer(writer)
        .build();
  }

  @Bean
  ItemReader<Venda> reader() {
    List<Venda> vendas = GeradorDeVendas.gerar();
    ListItemReader<Venda> reader = new ListItemReader<>(vendas);
    return reader;
  }
}

record Venda(Long id, String description, List<ItemVenda> itensVendas) {

}

record ItemVenda(Long id, String description, Long idVenda) {

}