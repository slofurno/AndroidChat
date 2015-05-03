package com.github.slofurno.basechat;

import java.util.List;

/**
 * Created by slofurno on 5/1/2015.
 */
public class ReceiveMessageEvent {

    private List<ChatMessage> Messages;

    public ReceiveMessageEvent(List<ChatMessage> messages){
        this.Messages=messages;
    }

    public List<ChatMessage>getMessages(){
        return Messages;
    }

}

