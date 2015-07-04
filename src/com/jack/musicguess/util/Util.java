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
	//�ӿڵ�ʵ��
	public static IshowDialogListener listener;
    public static View getView(Context context,int layOutId){
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(layOutId, null);
    	return layout;
    }
    //����һ��ͨ�õ�showDialog����
    public static void showDialog(final Context context,String content,final IshowDialogListener listener){
    	final AlertDialog alertDialog;
    	//����Զ����dialogView
    	View dialogView = getView(context,R.layout.dialog_view);
    	//��ò�������ʾ����
    	TextView contentText=(TextView) dialogView.findViewById(R.id.dialog_tip_text);
    	contentText.setText(content);
    	//�����ʾ���е�������ť
    	ImageButton btnCancel=(ImageButton) dialogView.findViewById(R.id.btn_cancel);
    	ImageButton btnOk=(ImageButton) dialogView.findViewById(R.id.btn_ok);
    	//Ϊ������ť�󶨵���¼�������
    	AlertDialog.Builder builder= new AlertDialog.Builder(context, R.style.Theme_Transparent);
    	alertDialog=builder.create();
    	alertDialog.setView(dialogView);
    	
    	btnCancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//���ȡ����Ӧ���¼�
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
				//���ȷ����Ӧ���¼�
				alertDialog.cancel();
				listener.onClick();
				SongPlayer.playTone(context, SongPlayer.ENTER);
			}
		});
    	alertDialog.show();
    	
    }
//    //������ļ����ݵķ���
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
//    //����ȡ�ļ��ķ���
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
    //����SharedPreference����ȡ����
    public static void writeData(Context context,int songIndex,int coinsNum){
    	//��ȡSharedPreferences
    	SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
    	//��ȡ��Ӧ��Editor
    	Editor editor=sharedPreferences.edit();
    	editor.putInt("songIndex", songIndex);
    	editor.putInt("coinsNum",coinsNum);
    	editor.commit();
    	
    }
    //����SharedPreference��ȡ����
    public static int[] loadData(Context context){
    	//����һ����ά�������ȡ������ֵ
    	int[] a= new int[2];
    	//��ȡϵͳ�е�SharedPreferences
    	SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
    	a[0]=sharedPreferences.getInt("songIndex", 0);
    	a[1]=sharedPreferences.getInt("coinsNum", Const.TOTAL_COIN);
    	//��������
    	return a;
    	
    }
    
}
