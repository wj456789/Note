package com.shzx.application.common.tool;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolMath {
    private static Logger logger = LoggerFactory.getLogger(ToolMath.class);

    public ToolMath() {
    }

    public static double parseDouble(String value, int dot) {
        double dvalue = Double.parseDouble(value);
        dvalue = (double)Math.round(dvalue * (double)dot) / (double)dot;
        return dvalue;
    }

    public static double parseDouble(String value) {
        return parseDouble(value, 100);
    }

    public static long parseDoubleToInt(Double value) {
        return (long)Math.floor(value);
    }

    public static float parseFloat(String value, int dot) {
        float fvalue = Float.parseFloat(value);
        fvalue = (float)Math.round(fvalue * (float)dot) / (float)dot;
        return fvalue;
    }

    public static float parseFloat(String value) {
        return parseFloat(value, 100);
    }

    public static String sum(String augend, String addend) {
        double dAugend = parseDouble(augend);
        double dAddend = parseDouble(addend);
        double sum = dAugend + dAddend;
        return parseDouble(Double.toString(sum)) + "";
    }

    public static String mul(String faciend, String multiplier, int dot) {
        double dFaciend = Double.parseDouble(faciend);
        double dMultiplier = Double.parseDouble(multiplier);
        double dAmass = dFaciend * dMultiplier;
        dAmass = (double)Math.round(dAmass * (double)dot) / (double)dot;
        return dAmass + "";
    }

    public static String mul(String faciend, String multiplier) {
        return mul(faciend, multiplier, 100);
    }

    public static String rate(String dividend, String divisor, int dot) {
        double dDividend = Double.parseDouble(dividend);
        double dDivisor = Double.parseDouble(divisor);
        double dQuotient = dDividend / dDivisor;
        dQuotient = (double)Math.round(dQuotient * (double)dot) / (double)dot;
        return dQuotient + "";
    }

    public static String rate(String dividend, String divisor) {
        return rate(dividend, divisor, 100);
    }

    public static String percent(String a, String b) {
        double dA = 0.0D;
        double dB = 0.0D;
        double dRate = 0.0D;
        String rate = "";

        try {
            if (b == null || Objects.equals(b, "0") || b.trim().length() <= 0) {
                return "-";
            }
        } catch (Exception var13) {
        }

        try {
            dA = Double.parseDouble(a);
        } catch (Exception var12) {
            dA = 0.0D;
        }

        try {
            dB = Double.parseDouble(b);
        } catch (Exception var11) {
            dB = 0.0D;
        }

        try {
            dRate = dA / dB;
            rate = Math.round(dRate * 10000.0D) / 100L + "%";
        } catch (Exception var10) {
            rate = "0%";
        }

        if (rate.equals("0%")) {
            rate = "-";
        }

        return rate;
    }

    public static String getResult(long value, int length) {
        StringBuffer sb = new StringBuffer();
        String _value = value + "";
        int i = 0;

        for(int j = length - _value.length(); i < j; ++i) {
            sb.append("0");
        }

        sb.append(value);
        return sb.toString();
    }

    public static String getResult(double value, int length) {
        StringBuffer sb = new StringBuffer();
        String _value = value + "";
        int i = 0;

        for(int j = length - _value.length(); i < j; ++i) {
            sb.append("0");
        }

        sb.append(value);
        return sb.toString();
    }

    public static int math16To10(String value) {
        try {
            Integer x = Integer.parseInt(value, 16);
            return x;
        } catch (NumberFormatException var2) {
            logger.error("16进制转10进制失败value={}", value, var2);
            return 0;
        }
    }
}