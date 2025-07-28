package br.com.EasyTable.Shared.Handlers;


import java.util.concurrent.CompletableFuture;

public interface IHandler<TIn, TOut> {
    CompletableFuture<HandlerResponseWithResult<TOut>> execute(TIn request);
}
