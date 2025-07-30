package br.com.EasyTable.Borders.Dtos.Responses;

import java.math.BigDecimal;

public record FechamentoResponse(
        String comandaId,
        BigDecimal total,
        String mensagem) { }
