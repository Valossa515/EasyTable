package br.com.EasyTable.Shared.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String code;
    private String text;

    public Message(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
