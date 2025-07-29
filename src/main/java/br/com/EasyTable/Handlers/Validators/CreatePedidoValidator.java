package br.com.EasyTable.Handlers.Validators;

import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Handlers.Validators.Annotations.ValidCreatePedido;
import jakarta.validation.ConstraintValidator;

public class CreatePedidoValidator implements ConstraintValidator<ValidCreatePedido, CreatePedidoRequest> {
    @Override
    public boolean isValid(CreatePedidoRequest request, jakarta.validation.ConstraintValidatorContext context) {
        if (request == null) return false;

        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (request.mesaId() == null || request.mesaId().isBlank()) {
            context.buildConstraintViolationWithTemplate("O campo 'mesaId' é obrigatório.")
                    .addPropertyNode("mesaId")
                    .addConstraintViolation();
            isValid = false;
        }

        if (request.itens() == null || request.itens().isEmpty()) {
            context.buildConstraintViolationWithTemplate("O pedido deve conter ao menos um item.")
                    .addPropertyNode("itens")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
