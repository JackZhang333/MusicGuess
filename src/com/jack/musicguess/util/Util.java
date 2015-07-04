package com.jack.musicguess.util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jack.musicguess.R;
import com.jack.musicguess.data.Const;
import com.jack.musicguess.modle.IshowDialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Util
{
	//接口的实例
	public static IshowDialogListener listener;
    public static View getView(Context context,int layOutId){
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(layOutId, null);
    	return layout;
    }
    //定义一个通用的showDialog方法
    public static void showDialog(final Context context,String content,final IshowDialogListener listener){
    	final AlertDialog alertDialog;
    	//获得自定义的dialogView
    	View dialogView = getView(context,R.layout.dialog_view);
    	//获得并设置提示内容
    	TextView contentText=(TextView) dialogView.findViewById(R.id.dialog_tip_text);
    	contentText.setText(content);
    	//获得提示框中的俩个按钮
    	ImageButton btnCancel=(ImageButton) dialogView.findViewById(R.id.btn_cancel);
    	ImageButton btnOk=(ImageButton) dialogView.findViewById(R.id.btn_ok);
    	//为两个按钮绑定点击事件监听器
    	AlertDialog.Builder builder= new AlertDialog.Builder(context, R.style.Theme_Transparent);
    	alertDialog=builder.create();
    	alertDialog.setView(dialogView);
    	
    	btnCancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//点击取消对应的事件
				if(alertDialog!=null){
					
					alertDialog.cancel();
					SongPlayer.playTone(context, SongPlayer.CANCLE);
				}
				
			}
		});
    	btnOk.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//点击确定对应的事件
				alertDialog.cancel();
				listener.onClick();
				SongPlayer.playTone(context, SongPlayer.ENTER);
			}
		});
    	alertDialog.show();
    	
    }
//    //定义存文件数据的方法
//    public static void writeData(Context context,int roundIndex,int coinNum){
//    	FileOutputStream fopStream= null;
//    	try
//		{
//    		fopStream = context.openFileOutput(Const.FILE_NAME, Context.MODE_PRIVATE);
//			DataOutputStream dos=new DataOutputStream(fopStream);
//			dos.writeInt(roundIndex);
//			dos.writeInt(coinNum);
//		} catch (FileNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			if(fopStream!=null){
//				
//				try
//				{
//					fopStream.close();
//				} catch (IOException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//    }
//    //定义取文件的方法
//    public static int[] loadData(Context context){
//    	FileInputStream fis=null;
//    	int[] a= {0,Const.TOTAL_COIN};
//    	try
//		{
//			fis=context.openFileInput(Const.FILE_NAME);
//			DataInputStream dis= new DataInputStream(fis);
//			a[Const.DATA_ROUND_INDEX]=dis.readInt();
//			a[Const.DATA_COIN_NUM]=dis.readInt();
//			
//		} catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}finally{
//			if(fis!=null){
//				try
//				{
//					fis.close();
//				} catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//    	return a;
//    }
    //利用SharedPreference来存取数据
    public static void writeData(Context context,int songIndex,int coinsNum){
    	//获取SharedPreferences
    	SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
    	//获取对应的Editor
    	Editor editor=sharedPreferences.edit();
    	editor.putInt("songIndex", songIndex);
    	editor.putInt("coinsNum",coinsNum);
    	editor.commit();
    	
    }
    //利用SharedPreference来取数据
    public static int[] loadData(Context context){
    	//定义一个二维数组接受取出的数值
    	int[] a= new int[2];
    	//获取系统中的SharedPreferences
    	SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
    	a[0]=sharedPreferences.getInt("songIndex", 0);
    	a[1]=sharedPreferences.getInt("coinsNum", Const.TOTAL_COIN);
    	//返回数组
    	return a;
    	
    }
    
}
