package com.jack.musicguess.util;

import android.util.Log;

public class MyLog
{
	//�Ŵ���Ϣ
	public static boolean Debug=true;
	public static void d(String tag,String message){
		if(Debug){
			Log.d(tag, message);
		}
	}
	//Info��Ϣ
	public static boolean Info=true;
	public static void i(String tag,String message){
		if(Info){
			Log.i(tag, message);
		}
	}
	//Error��Ϣ
		public static boolean Error=true;
		public static void e(String tag,String message){
			if(Error){
				Log.e(tag, message);
			}
		}
		//Warn��Ϣ
				public static boolean Warn=true;
				public static void w(String tag,String message){
					if(Warn){
						Log.w(tag, message);
					}
				}
	
	
}
