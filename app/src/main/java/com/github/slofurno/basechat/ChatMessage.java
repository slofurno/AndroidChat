package com.github.slofurno.basechat;

/**
 * Created by slofurno on 5/1/2015.
 */
public class ChatMessage {
    public String Sender;
    public String Message;

    public ChatMessage(String sender, String message){
        this.Message=message;
        this.Sender=sender;
    }

}
