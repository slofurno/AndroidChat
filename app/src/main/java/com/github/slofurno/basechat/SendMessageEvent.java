package com.github.slofurno.basechat;

public class SendMessageEvent{

    private ChatMessage Message;

    public SendMessageEvent(ChatMessage message){
        Message=message;
    }

    public ChatMessage getMessage(){
        return Message;
    }
}
