package com.freshman.webapi.util.md5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.ParseException;


public class MD5Util {
	
	public static String md5(String txt) {
        try{
             MessageDigest md = MessageDigest.getInstance("MD5");
             md.update(txt.getBytes("UTF-8"));    //问题主要出在这里，Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
             StringBuffer buf=new StringBuffer();            
             for(byte b:md.digest()){
                  buf.append(String.format("%02x", b&0xff));        
             }
            return  buf.toString().toUpperCase();
          }catch( Exception e ){
              e.printStackTrace(); 

              return null;
           } 
   }
	
	/** 
     * 解码 
     * @param str 
     * @return string 
     */  
    public static byte[] decode(String str){  
    byte[] bt = null;  
    try {  
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
        bt = decoder.decodeBuffer( str );  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
  
        return bt;  
    }
	    
		public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
			//订单同步
	    	//String key = MD5Util.md5("data={\"fun\":\"f_OrderSync\",\"user\":{\"user\":\"91\"},\"data\":{\"OrderCode\":\"1602290001\",\"ProductType\":1,\"ProductID\":47,\"TimeID\":86,\"Adult\":1,\"Child\":0,\"PeopleSum\":0,\"DealMoney\":11,\"OrderMoney\":11,\"Remark\":\"备注\",\"Contact\":\"张三\",\"ContactPhone\":\"13000000000\",\"Guests\":[{\"Name\":\"张三\",\"Sex\":1,\"IsAdult\":false,\"IDCode\":\"500231199901011548\",\"Type\":1,\"Number\":\"123\",\"Phone\":\"13000000000\"}],\"Prices\":[{\"Name\":\"张三\",\"Amount\":1,\"Money\":11,\"MoneySum\":11}]}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1452219022&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
	    	//String key = MD5Util.md5("data={\"fun\":\"f_OrderSync\",\"user\":{\"user\":\"91\"},\"data\":{\"OrderCode\":\"1603030008\",\"ProductType\":1,\"ProductID\":47,\"TimeID\":86,\"Adult\":1,\"Child\":0,\"PeopleSum\":0,\"DealMoney\":11.0,\"OrderMoney\":11.0,\"Remark\":\"null\",\"Contact\":\"张三\",\"ContactPhone\":\"13000000000\",\"Guests\":[{\"Name\":\"马云\",\"Sex\":0,\"IsAdult\":true,\"IDCode\":\"500231199901011548\",\"Type\":0,\"Number\":\"500231199901011548\",\"Phone\":\"13333333333\"}],\"Prices\":[{\"Name\":\"成人\",\"Amount\":1,\"Money\":11.0,\"MoneySum\":11.0}]}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1452219022&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
	    	//索引同步
			//String key = MD5Util.md5("data={\"fun\":\"f_ProductIndex\",\"user\":{\"user\":\"963\"},\"data\":{\"ptype\":\"3\"}}&signKey=sPlBMjespRRRQpVzBpn1&timeSpan=1458615497&usersKey=FA6374F4-A3C7-436E-9369-BA89F3EEA60D&key=sPlBMjespRRRQpVzBpn1");
			//信息同步
			String key = MD5Util.md5("888888");
			//旅客同步
			//String key = MD5Util.md5("data={\"fun\":\"f_GuestSync\",\"user\":{\"user\":\"91\"},\"data\":{\"resourceId\":532,\"Guests\":[{\"Name\":\"马云\",\"Sex\":0,\"IsAdult\":true,\"IDCode\":\"500231199901011548\",\"Type\":0,\"Number\":\"500231199901011548\",\"Phone\":\"13333333333\"},{\"Name\":\"雷布斯\",\"Sex\":0,\"IsAdult\":true,\"IDCode\":\"500231199901011548\",\"Type\":0,\"Number\":\"500231199901011548\",\"Phone\":\"13333333333\"}]}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1457058238587&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
			//支付同步
			//String key = MD5Util.md5("data={\"fun\":\"f_Pay\",\"user\":{\"user\":\"139\"},\"data\":{\"type\":1,\"resourceId\":543,\"money\":11,\"remark\":\"\"}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1457425396510&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
			//退款申请
			//String key = MD5Util.md5("data={\"fun\":\"f_Pay\",\"user\":{\"user\":\"91\"},\"data\":{\"type\":2,\"resourceId\":543,\"money\":11,\"remark\":\"呵呵\"}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1457425396510&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
			//取消退款
			//String key = MD5Util.md5("data={\"fun\":\"f_CancelRefound\",\"user\":{\"user\":\"91\"},\"data\":{\"resourceId\":567,\"refoundId\":119,\"remark\":\"呵呵\"}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1457506600&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
			//取消订单
			//String key = MD5Util.md5("data={\"fun\":\"f_Cancel\",\"user\":{\"user\":\"91\"},\"data\":{\"resourceId\":577,\"remark\":\"呵呵\"}}&signKey=Ii2Wu7ypEwrOE3F7L3QQ&timeSpan=1457506600&usersKey=FB6195DE-A1CC-4292-A528-9E30B47A38ED&key=Ii2Wu7ypEwrOE3F7L3QQ");
			//String date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(1452528000 * 1000l));
			//String str=java.net.URLDecoder.decode("%7B%22user%22%3A%7B%22user%22%3A0%2C%22pass%22%3A%22%22%7D%2C%22data%22%3A%7B%22resourceId%22%3A2722%2C%22status%22%3Atrue%2C%22payDeadLine%22%3A%222016-03-16+16%3A00%22%2C%22remark%22%3A%22%22%7D%7D","utf-8");   
			/*String tpTme = "2014-11-11T14:00:00+08:00";
			SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss+08:00");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time = df.parse(tpTme);*/
			//String i=PropUtils.getProperty("DOCKING_PRODUCT_URL");
			System.out.println(key);
		}

}
