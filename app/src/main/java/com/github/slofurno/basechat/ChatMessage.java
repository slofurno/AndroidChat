package com.github.slofurno.basechat;

/**
 * Created by slofurno on 5/1/2015.
 */
public class ChatMessage {
    private String name;
    private String message;

    private ChatMessage(){}

    public ChatMessage(String name, String message){
        this.message =message;
        this.name =name;
    }

    public String getName(){
        return name;
    }

    public String getMessage(){
        return message;
    }

    public String toString(){
        return name + " : " + message;
    }

}
