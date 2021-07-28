package com.shzx.application.common.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolLog {
    public static Logger log = LoggerFactory.getLogger(ToolLog.class);

    public ToolLog() {
    }

    public static void warn(Object obj) {
        try {
            String e = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            e = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            if (obj instanceof Exception) {
                Exception e1 = (Exception)obj;
                StringWriter sw = new StringWriter();
                e1.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.warn("代码位置：" + e + "; " + obj + "; 异常信息：" + str);
            } else {
                log.warn("代码位置：" + e + "; " + obj + "; ");
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void info(Object obj) {
        try {
            String e = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            e = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            if (obj instanceof Exception) {
                Exception e1 = (Exception)obj;
                StringWriter sw = new StringWriter();
                e1.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.info("代码位置：" + e + "; " + obj + "; 异常信息：" + str);
            } else {
                log.info("代码位置：" + e + "; " + obj + ";");
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void error(Object obj) {
        try {
            String e = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            e = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            if (obj instanceof Exception) {
                Exception e1 = (Exception)obj;
                StringWriter sw = new StringWriter();
                e1.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.error("代码位置：" + e + "; " + obj + "; 异常信息：" + str);
            } else {
                log.error("代码位置：" + e + ";  异常信息：" + obj);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void error(String errorMsg, Exception exception) {
        try {
            String e = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            e = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();
            log.error("代码位置：" + e + "; " + errorMsg + "; 异常信息：" + str);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void dbWarn(Object obj) {
        try {
            String e = "";
            String location = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            location = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            if (obj instanceof Exception) {
                Exception e1 = (Exception)obj;
                e = location + e1.getMessage();
                log.warn(e.substring(0, e.length() > 512 ? 512 : e.length()));
            } else {
                e = location + obj.toString();
                log.warn(e.substring(0, e.length() > 512 ? 512 : e.length()));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static String getCodeLocation() {
        try {
            String e = "";
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            e = stacks[2].getClassName() + "." + stacks[2].getMethodName() + "(" + stacks[2].getLineNumber() + ")";
            return e;
        } catch (Exception var2) {
            error(var2);
            return "";
        }
    }
}
