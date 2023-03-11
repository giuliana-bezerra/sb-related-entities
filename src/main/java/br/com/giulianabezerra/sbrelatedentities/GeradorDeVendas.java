package br.com.giulianabezerra.sbrelatedentities;

import java.util.ArrayList;
import java.util.List;

public class GeradorDeVendas {
  static List<Venda> gerar() {
    List<Venda> vendas = new ArrayList<>();

    List<ItemVenda> itensVenda1 = new ArrayList<>() {
      {
        add(new ItemVenda(null, "Computador", null));
        add(new ItemVenda(null, "Cadeira Gamer", null));
        add(new ItemVenda(null, "Teclado Mecânico", null));
      }
    };
    Venda venda1 = new Venda(null, "Itens de Informática", itensVenda1);
    vendas.add(venda1);

    List<ItemVenda> itensVenda2 = new ArrayList<>() {
      {
        add(new ItemVenda(null, "Geladeira", null));
        add(new ItemVenda(null, "Máquina de Lavar", null));
      }
    };
    Venda venda2 = new Venda(null, "Eletrodomésticos", itensVenda2);
    vendas.add(venda2);

    return vendas;
  }
}