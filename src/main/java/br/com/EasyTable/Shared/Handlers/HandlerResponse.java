package br.com.EasyTable.Shared.Handlers;

import br.com.EasyTable.Shared.Models.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class HandlerResponse {
    private int statusCode;
    private List<Message> messages = Collections.emptyList();

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode <= 299;
    }

    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public void setMessages(List<Message> messages) { this.messages = messages; }
}
