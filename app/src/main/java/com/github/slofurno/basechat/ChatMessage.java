package com.github.slofurno.basechat;

/**
 * Created by slofurno on 5/1/2015.
 */
public class ChatMessage {
    public String name;
    public String message;

    public ChatMessage(String sender, String message){
        this.message =message;
        this.name =sender;
    }

    public String toString(){
        return name + " : " + message;
    }

}
