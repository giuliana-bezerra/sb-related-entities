CREATE TABLE IF NOT EXISTS vendas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  description  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS itens_vendas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  description  VARCHAR(255) NOT NULL,
  id_venda BIGINT NOT NULL,
  CONSTRAINT fk_vendas FOREIGN KEY (id_venda) REFERENCES vendas(id)
);  