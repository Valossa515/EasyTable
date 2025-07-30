package br.com.EasyTable.Shared.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@Getter // toString() retorna só a descrição
@RequiredArgsConstructor // Gera o construtor com o campo final
public enum PedidoStatus {
    PENDENTE,
    EM_PREPARACAO,
    PRONTO,
    ENTREGUE,
    CANCELADO;
}