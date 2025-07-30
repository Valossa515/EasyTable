package br.com.EasyTable.Borders.Dtos.Requests;

import br.com.EasyTable.Shared.Enums.PedidoStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

public record CreatePedidoRequest(
        String mesaId,
        List<String> itensIds,  // Apenas os IDs dos itens
        @JsonIgnore
        Date dataHora,
        @JsonIgnore
        PedidoStatus status,
        String comandaId,
        @JsonIgnore
        String qrCodeAcompanhamento
) { }
