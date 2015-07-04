package com.jack.musicguess;

import com.jack.musicguess.data.Const;
import com.jack.musicguess.ui.MainActivity;
import com.jack.musicguess.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class AllPass extends Activity
{
	ImageButton btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_pass);
		FrameLayout allPassBarCoin=(FrameLayout) findViewById(R.id.layout_bar_coin);
		allPassBarCoin.setVisibility(View.GONE);
		btnBack=(ImageButton) findViewById(R.id.btn_bar_back);
		if(btnBack!=null){
			btnBack.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Intent intent=new Intent(AllPass.this,MainActivity.class);
					Util.writeData(AllPass.this, 0, Const.TOTAL_COIN);
					startActivity(intent);
					finish();
				}
			});
		}
	}
}
