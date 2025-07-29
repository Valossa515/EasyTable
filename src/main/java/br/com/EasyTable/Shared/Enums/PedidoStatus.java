package br.com.EasyTable.Shared.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString(of = "descricao") // toString() retorna só a descrição
@RequiredArgsConstructor // Gera o construtor com o campo final
public enum PedidoStatus {
    PENDENTE("Pendente"),
    EM_PREPARACAO("Em Preparação"),
    PRONTO("Pronto"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    public static PedidoStatus fromDescricao(String descricao) {
        return Arrays.stream(PedidoStatus.values())
                .filter(status -> status.getDescricao().equalsIgnoreCase(descricao))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + descricao));
    }
}
