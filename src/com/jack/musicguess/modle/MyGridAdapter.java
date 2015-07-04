package com.jack.musicguess.modle;

import java.util.ArrayList;

import com.jack.musicguess.R;
import com.jack.musicguess.util.Util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MyGridAdapter extends BaseAdapter
{
	public ArrayList<WordButton> mArrayList=new ArrayList<WordButton>();
    //声明播放动画
	public Animation animation;
	//声明接口
	public IwordButtonListener mIwordButtonListener;
	
	public MyGridAdapter()
	{
		
	}

	@Override
	public int getCount()
	{
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
		final WordButton holder;
		animation=AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale_word_button);
		animation.setStartOffset(position*100);
		if(v==null){
			v=Util.getView(parent.getContext(), R.layout.self_ui_gridview_item);
			holder=mArrayList.get(position);
			holder.buttonIndex=position;
			holder.viewButton=(Button)v.findViewById(R.id.itemBtn);
			holder.viewButton.startAnimation(animation);
			holder.viewButton.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					mIwordButtonListener.wordButtonOnclick(holder);
					
					
				}
			});
			v.setTag(holder);
			
			
		}
		else holder=(WordButton) v.getTag();
		holder.viewButton.setText(holder.buttonWord);
		return v;
	}
	//注册接口的方法
	public void RegisterWorButtonListener(IwordButtonListener listener){
		mIwordButtonListener=listener;
	}

}
