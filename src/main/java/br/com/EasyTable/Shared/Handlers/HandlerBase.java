package br.com.EasyTable.Shared.Handlers;

import br.com.EasyTable.Shared.Models.Message;
import br.com.EasyTable.Shared.Properties.MessageResources;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class HandlerBase<TIn, TOut> implements IHandler<TIn, TOut> {
    private final Logger logger;
    private final Validator validator;

    public HandlerBase(Logger logger, Validator validator) {
        this.logger = logger;
        this.validator = validator;
    }

    @Override
    public CompletableFuture<HandlerResponseWithResult<TOut>> execute(TIn request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (validator != null) {
                    Set<ConstraintViolation<TIn>> violations = validator.validate(request);
                    if (!violations.isEmpty()) {
                        throw new ConstraintViolationException(violations);
                    }
                }

                return doExecute(request).join();
            } catch (ConstraintViolationException e) {
                HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
                response.setStatusCode(400);
                response.setMessages(
                        e.getConstraintViolations().stream()
                                .map(v -> new Message(v.getPropertyPath().toString(), v.getMessage()))
                                .toList()
                );
                return response;
            } catch (Exception e) {
                logger.error("Unexpected error: {}", e.getMessage(), e);

                HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
                response.setStatusCode(500);
                response.setMessages(List.of(new Message("000", MessageResources.get("error.unexpected"))));
                return response;
            }
        });
    }

    protected abstract CompletableFuture<HandlerResponseWithResult<TOut>> doExecute(TIn request);

    protected HandlerResponseWithResult<TOut> notFound(String message) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(404);
        response.setMessages(List.of(new Message(MessageResources.get("error.not_found_error_code"), message)));
        return response;
    }

    protected HandlerResponseWithResult<TOut> success(TOut result) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(200);
        response.setResult(result);
        return response;
    }

    protected HandlerResponseWithResult<TOut> created(TOut result) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(201);
        response.setResult(result);
        return response;
    }

    protected HandlerResponseWithResult<TOut> badRequest(String code, String message) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(400);
        response.setMessages(List.of(new Message(code, message)));
        return response;
    }

    protected HandlerResponseWithResult<TOut> noContent() {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(204);
        response.setResult(null);
        return response;
    }

    protected HandlerResponseWithResult<TOut> unauthorized(String code, String message) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(401);
        response.setMessages(List.of(new Message(code, message)));
        return response;
    }

    protected HandlerResponseWithResult<TOut> forbidden (String code, String message) {
        HandlerResponseWithResult<TOut> response = new HandlerResponseWithResult<>();
        response.setStatusCode(403);
        response.setMessages(List.of(new Message(MessageResources.get("forbidden.error_code"), MessageResources.get("forbidden.error_message") )));
        return response;
    }
}
