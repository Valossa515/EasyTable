package br.com.EasyTable.Shared.Handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandlerResponseWithResult<T> extends HandlerResponse {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HandlerResponseWithResult{" +
                "statusCode=" + getStatusCode() +
                ", messages=" + getMessages() +
                ", result=" + result +
                '}';
    }
}
