/*    */ package com.shzx.application.common.tool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ToolSunTime
/*    */ {
/*    */   private static final double π = 3.141592653589793D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getSunriseTime(Integer dayOfYear, Double longitude, Double latitude)
/*    */   {
/* 25 */     double result = (300.0D - longitude.doubleValue() - arccos(tan(0.409066391572982D * cos(6.283185307179586D * (dayOfYear.intValue() + 9) / 365.0D)) * tan(latitude.doubleValue() * 3.141592653589793D / 180.0D)) * 180.0D / 3.141592653589793D) / 15.0D;
/* 26 */     return format(result);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getSunsetTime(Integer dayOfYear, Double longitude, Double latitude)
/*    */   {
/* 39 */     double result = (300.0D - longitude.doubleValue() + arccos(tan(0.409066391572982D * cos(6.283185307179586D * (dayOfYear.intValue() + 9) / 365.0D)) * tan(latitude.doubleValue() * 3.141592653589793D / 180.0D)) * 180.0D / 3.141592653589793D) / 15.0D;
/* 40 */     return format(result);
/*    */   }
/*    */   
/*    */   public static double arccos(double value)
/*    */   {
/* 45 */     return Math.acos(value);
/*    */   }
/*    */   
/*    */   public static double cos(double value)
/*    */   {
/* 50 */     return Math.cos(value);
/*    */   }
/*    */   
/*    */   public static double tan(double value)
/*    */   {
/* 55 */     return Math.tan(value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String format(double time)
/*    */   {
/* 66 */     StringBuffer buffer = new StringBuffer();
/* 67 */     int hour = (int)Math.floor(time);
/* 68 */     int minutes = (int)((time - hour) * 60.0D);
/* 69 */     int seconds = (int)((time - hour) * 60.0D * 60.0D - minutes * 60);
/* 70 */     buffer.append(String.format("%02d", new Object[] { Integer.valueOf(hour) })).append(":");
/* 71 */     buffer.append(String.format("%02d", new Object[] { Integer.valueOf(minutes) })).append(":");
/* 72 */     buffer.append(String.format("%02d", new Object[] { Integer.valueOf(seconds) }));
/* 73 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              E:\iot-core-2.1.0.jar!\com\iot\core\commons\tool\ToolSunTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */