package com.jack.musicguess.myui;


import java.util.ArrayList;

import com.jack.musicguess.modle.MyGridAdapter;
import com.jack.musicguess.modle.WordButton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView
{
	public final int COUNT=24;
	public MyGridAdapter mMyGridAdapter ;
	//自定义View的构造方法
	public MyGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mMyGridAdapter = new MyGridAdapter();
		setAdapter(mMyGridAdapter);
	}
	public void updateData(ArrayList<WordButton> list){
		mMyGridAdapter.mArrayList=list;
		//重新设置Adapter
		setAdapter(mMyGridAdapter);
	}


}
