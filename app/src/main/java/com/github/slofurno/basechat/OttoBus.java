package com.github.slofurno.basechat;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by slofurno on 5/1/2015.
 */
public enum OttoBus {
    INSTANCE;
    public static OttoBus getInstance() {
        return INSTANCE;
    }
    private Bus BUS = new Bus(ThreadEnforcer.ANY);

    public Bus getBus(){
        return BUS;
    }


}
