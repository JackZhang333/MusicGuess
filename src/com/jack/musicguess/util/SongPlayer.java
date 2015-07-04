package com.jack.musicguess.util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class SongPlayer
{
	public final static int CANCLE=0;
	public final static int ENTER=1;
	public final static int COIN=2;
	
	//定义一个音效名称的数组
	public static String[] tonesName=new String[]{
		"cancel.mp3",
		"enter.mp3",
		"coin.mp3"
	};
	//定义一个MediaPlayer的数组
	public static MediaPlayer[] tonesPlayer=new MediaPlayer[tonesName.length];
	//定义一个方法播放音效
	public static void playTone(Context context,int flag){
		if(tonesPlayer[flag]==null){
			tonesPlayer[flag]=new MediaPlayer();
			AssetManager assetManager=context.getAssets();
			try
			{
				AssetFileDescriptor fileDescriptor=assetManager.openFd(tonesName[flag]);
				tonesPlayer[flag].setDataSource(fileDescriptor.getFileDescriptor(),
						                        fileDescriptor.getStartOffset(), 
						                        fileDescriptor.getLength());
				tonesPlayer[flag].prepare();
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tonesPlayer[flag].start();
	}
	public static MediaPlayer mediaPlayer;
	//播放声音的方法
	public static void playSong(Context context,String fileName){
		if(mediaPlayer==null){
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.reset();
		AssetManager assetManager=context.getAssets();
		try
		{
			AssetFileDescriptor fileDescriptor=assetManager.openFd(fileName);
			FileDescriptor fileD=fileDescriptor.getFileDescriptor();
			mediaPlayer.setDataSource(fileD, fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void stopSong(){
		if(mediaPlayer==null){
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.stop();
	}
	
}
