package com.jack.musicguess.modle;

public class Song
{
	//����������
	private String mSongName;
	//�����ļ�������
	private String mSongFileName;
	//�������Ƶĳ���
	private int mSongNameLength;
	
	//�������Ե�setter��getter����
	
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
	//�õ����������ַ�����ķ���
	public char[] getNameCharacters(){
		return mSongName.toCharArray();
	}
	
}
