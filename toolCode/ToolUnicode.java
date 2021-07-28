package com.shzx.application.common.tool;

public class ToolUnicode {
    public ToolUnicode() {
    }

    public static String unicodetoString(String unicode) {
        if (unicode != null && !"".equals(unicode)) {
            StringBuffer sb = new StringBuffer();
            int pos = 0;

            int i;
            while((i = unicode.indexOf("\\u", pos)) != -1) {
                sb.append(unicode.substring(pos, i));
                if (i + 5 < unicode.length()) {
                    pos = i + 6;
                    sb.append((char)Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    public static String stringtoUnicode(String string) {
        if (string != null && !"".equals(string)) {
            StringBuffer unicode = new StringBuffer();

            for(int i = 0; i < string.length(); ++i) {
                char c = string.charAt(i);
                unicode.append("\\u" + Integer.toHexString(c));
            }

            return unicode.toString();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String s = stringtoUnicode("中文");
        System.out.println("编码：" + s);
        System.out.println(s);
        String s1 = unicodetoString(s);
        System.out.println("解码：" + s1);
    }
}