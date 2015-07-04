package com.jack.musicguess.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class GetRandomCharUtil
{
	public static char getRandomChar(){
		 int highPos;
		 int lowPos;
		 Random random=new Random();
		 
		 highPos=(176+Math.abs(random.nextInt(39)));
		 lowPos=(161+Math.abs(random.nextInt(93)));
		 
		 byte[] b=new byte[2];
		 b[0]=(Integer.valueOf(highPos)).byteValue();
		 b[1]=(Integer.valueOf(lowPos)).byteValue();
         String str = null;
		try
		{
			str = new String(b,"GBK");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		 return str.charAt(0);
		
		
	}
}
