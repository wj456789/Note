package com.shzx.application.common.tool;

import java.util.UUID;

public class ToolUUID {
    public ToolUUID() {
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getPkString() {
        return ToolTime.getNowStringByAllTime() + ToolRandom.getStringByLen(6).toUpperCase();
    }
}
