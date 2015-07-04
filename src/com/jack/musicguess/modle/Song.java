package com.jack.musicguess.modle;

public class Song
{
	//歌曲的名称
	private String mSongName;
	//歌曲文件的名称
	private String mSongFileName;
	//歌曲名称的长度
	private int mSongNameLength;
	
	//三个属性的setter和getter方法
	
	public void setSongName(String name){
		this.mSongName=name;
		this.mSongNameLength=name.length();
	}
	public String getSongName(){
		return mSongName;
	}
	
	public void setSongFileName(String name){
		this.mSongFileName=name;
	}
	public String getSongFileName(){
		return mSongFileName;
	}
	
	public int getSongNameLength(){
		return mSongNameLength;
	}
	//得到歌曲名称字符数组的方法
	public char[] getNameCharacters(){
		return mSongName.toCharArray();
	}
	
}
