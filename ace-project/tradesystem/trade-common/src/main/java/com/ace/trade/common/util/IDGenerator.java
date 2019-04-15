package com.ace.trade.common.util;

import java.util.UUID;

public class IDGenerator {
    public static String generatorID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
