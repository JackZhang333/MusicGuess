package com.jack.musicguess.ui;
import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.musicguess.AllPass;
import com.jack.musicguess.R;
import com.jack.musicguess.data.Const;
import com.jack.musicguess.modle.IshowDialogListener;
import com.jack.musicguess.modle.IwordButtonListener;
import com.jack.musicguess.modle.Song;
import com.jack.musicguess.modle.WordButton;
import com.jack.musicguess.myui.MyGridView;
import com.jack.musicguess.util.GetRandomCharUtil;
import com.jack.musicguess.util.MyLog;
import com.jack.musicguess.util.SongPlayer;
import com.jack.musicguess.util.Util;

public class MainActivity extends Activity implements IwordButtonListener
{
	//出现对话框的三种情况
	public static final int DELETWORD=1;
	public static final int TIPANSWER=2;
	public static final int LACKCOIN=3;
	/**
	 * 声明passView中的控件
	 */
	//当前关卡的文本
	public TextView mCurrentSongIndexPassView;
	//当前正在播放歌曲的索引
	public TextView mCurrentSongIndex;
	//当前关卡的歌曲名称
	public TextView mPassViewSongName;
	//浮动按钮三个
	public ImageButton deleteBtn;
	public ImageButton answerTipBtn;
	public ImageButton shareBtn;
	//过关界面
	public View passView;
	//设置系统输出的log的标记
	public static final String TAG="MusicGuess.Jack";
	//标记歌曲答案的三种状态
	public static final int ANSWER_RIGHT=0;
	public static final int ANSWER_WRONG=1;
	public static final int ANSWER_LACK=2;
	//三个动画：盘片旋转动画，指针进入动画，指针复原动画
	private Animation mplayerRotate;
	@Override
	protected void onPause()
	{
		super.onPause();
		//存入关卡和金币的信息
		Util.writeData(MainActivity.this, mCurrentIndex-1, mCurrentCoin);
		mPlay.clearAnimation();
		SongPlayer.stopSong();
	}

	private Animation mpinIn;
	private Animation mpinOut;
	//三个动画对应的Interpolator
	private Interpolator mplayerIntor;
	private Interpolator mpinInIntor;
	private Interpolator mpinOutIntor;
	//为播放按钮添加监听事件，为指针和盘绑定动画
	private ImageButton mbtnStart;
	private ImageView mPlay;
	private ImageView mPin;
	//是否在播放中
	private boolean isRunning=false;
	//GridView
	private MyGridView myGridView;
	private ArrayList<WordButton> arrayList;
	//已选择文字框
	private LinearLayout layout;
	private ArrayList<WordButton> selectedList;
	//声明播放的歌曲
	private Song mCurrentSong;
	private int mCurrentIndex=0;
	// 金币数量的初始化
	private int mCurrentCoin = Const.TOTAL_COIN;
	// 显示金币数TextView
	private TextView mViewCerrentCoin;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//读取关卡和金币信息
		int[] data=Util.loadData(MainActivity.this);
		mCurrentIndex=data[Const.DATA_ROUND_INDEX];
		mCurrentCoin=data[Const.DATA_COIN_NUM];
		//盘片动画的初始化
		mplayerRotate=AnimationUtils.loadAnimation(this,R.anim.rotate_player);
		mplayerIntor=new LinearInterpolator();
		mplayerRotate.setInterpolator(mplayerIntor);
		mplayerRotate.setAnimationListener(new AnimationListener()
		{
			
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				mPin.startAnimation(mpinOut);
			}
		});
		//指针进入动画的初始化
		mpinIn=AnimationUtils.loadAnimation(this, R.anim.rotate_pin_in);
		mpinInIntor=new LinearInterpolator();
		mpinIn.setInterpolator(mpinInIntor);
		mpinIn.setFillAfter(true);
		mpinIn.setAnimationListener(new AnimationListener()
		{
			
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				mPlay.startAnimation(mplayerRotate);
			}
		});
		//指针恢复的动画初始化
	    mpinOut=AnimationUtils.loadAnimation(this, R.anim.rotate_pin_out);
	    mpinOutIntor=new LinearInterpolator();
		mpinOut.setInterpolator(mpinOutIntor);
		mpinOut.setFillAfter(true);
		mpinOut.setAnimationListener(new AnimationListener()
		{
			
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				isRunning=false;
				mbtnStart.setVisibility(View.VISIBLE);
				
			}
		});
		//实例化播放按钮,绑定监听器
		mbtnStart=(ImageButton) findViewById(R.id.btn_play_start);
		mbtnStart.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				handleStart();
			}
		});
		//为播放盘和指针绑定控件，添加动画
		mPlay=(ImageView) findViewById(R.id.imageView1);
		mPin=(ImageView) findViewById(R.id.imageView2);
		//初始化网格视图
		myGridView=(MyGridView) findViewById(R.id.gridView);
		arrayList=new ArrayList<WordButton>();
		//应用注册方法传递接口
		myGridView.mMyGridAdapter.RegisterWorButtonListener(this);
		//初始化被选择文字框
		selectedList=new ArrayList<WordButton>();
		layout=(LinearLayout) findViewById(R.id.wordSelectedContainer);
		//设置金币数量的显示
		mViewCerrentCoin=(TextView) findViewById(R.id.txt_bar_coins);
		mViewCerrentCoin.setText(mCurrentCoin+"");
		//初始化文字选择框中的数据
		initCurrentStageData();
		handleDeleteWord();
		handleAnswerTip();
		
		
		
	}

	private void handleStart()
	{   if(mPin !=null){

		if(!isRunning ){
			isRunning=true;
			mPin.startAnimation(mpinIn);
			mbtnStart.setVisibility(View.INVISIBLE);
			SongPlayer.playSong(MainActivity.this, mCurrentSong.getSongFileName());
		}
	}

	}
	//返回歌曲信息的方法
	public Song getCurrentSong(int index){
		Song song =new Song();
		song.setSongFileName(Const.SONG_INFO[index][Const.SONG_FILE_NAME]);
		song.setSongName(Const.SONG_INFO[index][Const.SONG_NAME]);
		return song;
		
	}
	public void initCurrentStageData(){
		//获得歌曲信息
		mCurrentSong=getCurrentSong(mCurrentIndex++);
		char[] chars=mCurrentSong.getNameCharacters();
		//设置播放界面歌曲的索引
		mCurrentSongIndex=(TextView) findViewById(R.id.game_round);
		mCurrentSongIndex.setText(mCurrentIndex+"");
		//歌曲信息填入选择框
		//把容器里的数据填充到主视图中
		selectedList=initSelectedWord();
		//清除已有的选择框文字
		layout.removeAllViews();
		for(int i=0;i<selectedList.size();i++){
			LayoutParams params=new LayoutParams(140, 140);
			layout.addView(selectedList.get(i).viewButton, params);
		}
		//获得数据
		arrayList=initAllData();
		//更新数据 MyGridView
		myGridView.updateData(arrayList);
		//一开始就播放音乐
		handleStart();
	}
	//生成填充文字选择区的文字方法
	public String[] generateWords(){
		Random random=new Random();
		String[] cs = new String[myGridView.COUNT];
		//获取歌曲的名字
		for (int i=0;i<mCurrentSong.getSongNameLength();i++){
			cs[i]=mCurrentSong.getNameCharacters()[i]+"";
			
		}
		//填充随机的文字
		for(int i=mCurrentSong.getSongNameLength();i<myGridView.COUNT;i++){
			cs[i]=GetRandomCharUtil.getRandomChar()+"";
			
		}
		for (int i = myGridView.COUNT - 1; i >= 0; i--) {
			int index = random.nextInt(i + 1);
			
			String buf = cs[index];
			cs[index] = cs[i];
			cs[i] = buf;
		}
		
		return cs;
		
	}
	
	//更新数据的方法
	public ArrayList<WordButton> initAllData(){
		ArrayList<WordButton> list=new ArrayList<WordButton>();
		String[] words = generateWords();
		for(int i=0;i<myGridView.COUNT;i++){
			WordButton button=new WordButton();
			button.buttonWord=words[i];
			list.add(button);
		}
		return list;
	}
	//选择文字框中的内容填充
	public ArrayList<WordButton> initSelectedWord(){
		ArrayList<WordButton> list=new ArrayList<WordButton>();
		for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			View v=Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
			final WordButton wordBtn=new WordButton();
			wordBtn.viewButton=(Button) v.findViewById(R.id.itemBtn);
			wordBtn.viewButton.setBackgroundResource(R.drawable.game_wordblank);
			wordBtn.viewButton.setTextColor(Color.WHITE);
			wordBtn.isVisibale=false;
			wordBtn.viewButton.setText("");
			//选择文字框按钮的点击事件
			wordBtn.viewButton.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					clearAnswer(wordBtn);
					
				}
			});
			
			list.add(wordBtn);
			
		}
		return list;
	}

	@Override
	public void wordButtonOnclick(WordButton wordButton)
	{
		setSlectedButton(wordButton);
//		Toast.makeText(this, wordButon.buttonIndex+"", Toast.LENGTH_SHORT).show();
		int answerStatus=getAnswerStatus();
		switch (answerStatus)
		{
		case ANSWER_LACK:
			//答案不完整执行的方法
			for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			 selectedList.get(i).viewButton.setTextColor(Color.WHITE);
			}
			break;
		case ANSWER_RIGHT:
			handlePassEvent();
			break;
		case ANSWER_WRONG:
			//答案错误执行的方法,闪烁文字
			sparkWords();
			break;
		}
	}
	//答案错误时，文字闪烁
	private void sparkWords(){
		TimerTask task=new TimerTask()
		{
			boolean mChange=true;
			int times=0;
			@Override
			public void run()
			{
				runOnUiThread(new Runnable(){

					@Override
					public void run()
					{
						if(++times>5){
							return;
						}
						for(int i=0;i<mCurrentSong.getSongNameLength();i++){
							if(mChange){
							selectedList.get(i).viewButton.setTextColor(Color.RED);
							}
							else selectedList.get(i).viewButton.setTextColor(Color.WHITE);
						}
						mChange=!mChange;
						
					}
					
				});
				
			}
		};
		Timer timer=new Timer();
		timer.schedule(task, 1, 200);
	}
	//设置已选文字在已选框中的可见性
	private void setSlectedButton(WordButton wordButton){
		if(wordButton==null) return;
		for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			if( selectedList.get(i).viewButton.getText()==""){
				selectedList.get(i).viewButton.setText(wordButton.buttonWord);
				selectedList.get(i).buttonWord=wordButton.buttonWord;
				selectedList.get(i).buttonIndex=wordButton.buttonIndex;
				//设置按钮可见
				setVisible(wordButton, View.INVISIBLE);
				break;
				
			}
		}
	}
	//设置按钮的可见性
	private void setVisible(WordButton wordButton,int visibility){
		wordButton.viewButton.setVisibility(visibility);
		wordButton.isVisibale = (visibility==View.VISIBLE) ? true: false;
		MyLog.d(TAG, wordButton.buttonIndex+"按钮被设置为不可见");
	}
	//清除已选文字的方法
	private void clearAnswer(WordButton wordButton){
		wordButton.viewButton.setText("");
		wordButton.buttonWord="";
		wordButton.isVisibale=false;
		setVisible(arrayList.get(wordButton.buttonIndex), View.VISIBLE);
		//设置log信息
		MyLog.d(TAG, "已选择框文字"+wordButton.buttonIndex);
	}
	//判断答案状态的方法
	private int getAnswerStatus(){
		for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			if(selectedList.get(i).viewButton.getText()==""){
				return ANSWER_LACK;
			}
		}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			sb.append(selectedList.get(i).buttonWord);
		}
		return (sb.toString().equals(mCurrentSong.getSongName()))?
				ANSWER_RIGHT:ANSWER_WRONG;
	}
	/**
	 * 从选择区删除一个不是答案的文字
	 */
	/**
	 * 删除文字
	 */
	private void deleteOneWord() {
		// 减少金币
		if (!handleCoins(-getDeleteValue())) {
			handleShowDialog(LACKCOIN);
			return;
		}
		MyLog.d(TAG, "金币可以被减少");
		// 将这个索引对应的WordButton设置为不可见
		setVisible(findNotAnswerWord(), View.INVISIBLE);
	}
	
	/**
	 * 找到一个不是答案的文件，并且当前是可见的
	 *
	 * @return
	 */
	private WordButton findNotAnswerWord() {
		Random random = new Random();
		WordButton buf = null;
		
		while(true) {
			int index = random.nextInt(myGridView.COUNT);
			
			buf = arrayList.get(index);
			MyLog.d(TAG, "取出一个随机文字");
			if (buf.isVisibale && !isTheAnswerWord(buf)) {
				MyLog.d(TAG, "找到了一个不是答案且可见的文字");
				return buf;
			}
		}
	}
	
	/**
	 * 判断某个文字是否为答案
	 *
	 * @param word
	 * @return
	 */
	private boolean isTheAnswerWord(WordButton word) {
		boolean result = false;
		
		for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
			if (word.viewButton.getText().equals
					("" + mCurrentSong.getNameCharacters()[i])) {
				MyLog.d(TAG, "已经判断是否为答案");
				result = true;
				
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 从资源文件获取删除和提示的金额
	 */
	public int getDeleteValue(){
		return this.getResources().getInteger(R.integer.pay_delete);
	}
	public int getAnswerTipValue(){
		return this.getResources().getInteger(R.integer.pay_answer_tip);
	}
	/**
	 * 判断答案是否可以删除的方法
	 */
	private boolean handleCoins(int value){
		if(mCurrentCoin+value>=0){
			mCurrentCoin+=value;
		mViewCerrentCoin.setText(mCurrentCoin+"");
		MyLog.d(TAG, "金币已经被减少");
			return true;
		}
		return false;
	}
	/**
	 * 提示答案的方法
	 */
	private void tipOneWord(){
		wordButtonOnclick(findOneAnswer(findPosition()));
		//找到选择文字框中没有显示文字的第一个位置
		//根据要提示的位置找到一个答案的文字
	}
	private int findPosition(){
		for(int i=0;i<selectedList.size();i++){
			if(selectedList.get(i).viewButton.getText()==""){
				return i;
			}
		}
		return -1;
	}
	private WordButton findOneAnswer(int position){
		if(position==-1){
			return null;
		}
		if (!handleCoins(-getAnswerTipValue())) {
			// 金币不够，显示提示对话框
			handleShowDialog(LACKCOIN);
			return null;
		}
		WordButton wb=null;
		String str=mCurrentSong.getNameCharacters()[position]+"";
		for(int i=0;i<arrayList.size();i++){
			if(arrayList.get(i).buttonWord.equals(str)){
				wb= arrayList.get(i);
			}
		}
		return wb;
	}
	
	/**
	 * 处理浮动按钮点击的方法：删除答案；提示答案
	 */
	
	private void handleDeleteWord(){
		deleteBtn=(ImageButton) findViewById(R.id.btn_delete_word);
		deleteBtn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				MyLog.d(TAG, "按钮被点击");
//				deleteOneWord();
				handleShowDialog(DELETWORD);
			}
		});
	}
	//提示答案事件处理
	private void handleAnswerTip(){
		answerTipBtn=(ImageButton) findViewById(R.id.btn_answer_tip);
		answerTipBtn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
//				tipOneWord();
				handleShowDialog(TIPANSWER);
				int answerStatus=getAnswerStatus();
				switch (answerStatus)
				{
				case ANSWER_LACK:
					//答案不完整执行的方法
					for(int i=0;i<mCurrentSong.getSongNameLength();i++){
					 selectedList.get(i).viewButton.setTextColor(Color.WHITE);
					}
					break;
				case ANSWER_RIGHT:
					handlePassEvent();
					break;
				case ANSWER_WRONG:
					//答案错误执行的方法,闪烁文字
					sparkWords();
					break;
				}
			}
		});
	}
	private void handlePassEvent(){
		//把关卡和金币信息存起来
		Util.writeData(MainActivity.this, mCurrentIndex-1, mCurrentCoin);
		passView=(LinearLayout)this.findViewById(R.id.passView);
		passView.setVisibility(View.VISIBLE);
		//没等歌播完，就把答案填对了，要先停止盘片的旋转动画
		mPlay.clearAnimation();
		SongPlayer.stopSong();
		//播放金币掉落音效
		SongPlayer.playTone(MainActivity.this, SongPlayer.COIN);
		//设置过关界面中当前管卡的数值
		mCurrentSongIndexPassView=(TextView) findViewById(R.id.pass_game_round);
		if(mCurrentSongIndexPassView!=null){
			
			mCurrentSongIndexPassView.setText(mCurrentIndex+"");
		}
		//设置过关界面歌曲的名称
		mPassViewSongName=(TextView) findViewById(R.id.pass_game_name);
		if(mPassViewSongName!=null){
			mPassViewSongName.setText(mCurrentSong.getSongName());
		}
		//为下一关按钮设定监听事件
		ImageButton btnNextRound=(ImageButton) findViewById(R.id.btnNext);
		btnNextRound.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(isIfTheLast()){
				//显示通关界面
				Intent i=new Intent(MainActivity.this,AllPass.class);
				startActivity(i);
				//关闭当前activity
				finish();
				}
				else{
					//开始下一关
					//隐藏过关界面
					passView.setVisibility(View.GONE);
					//加载初始化数据
					initCurrentStageData();
				}
			}
		});
	}
	public boolean isIfTheLast(){
		return mCurrentIndex==Const.SONG_INFO.length;
	}
	//删除一个错误答案的提示框
	public IshowDialogListener deletListener=new IshowDialogListener()
	{
		
		@Override
		public void onClick()
		{
			deleteOneWord();
			
		}
	};
	//提示一个正确答案的提示框
		public IshowDialogListener tipAnswerListener=new IshowDialogListener()
		{
			
			@Override
			public void onClick()
			{
				tipOneWord();
			}
		};
		//金币不足的提示框
		public IshowDialogListener lackCoinsListener=new IshowDialogListener()
		{

			@Override
			public void onClick()
			{
				// TODO Auto-generated method stub

			}
		};
	private void handleShowDialog(int flag){
		switch(flag){
		case DELETWORD:
			Util.showDialog(MainActivity.this,
					"你确定要花费"+getDeleteValue()+"个金币来删除一个错误答案吗？", 
					deletListener);
			break;
		case TIPANSWER:
			Util.showDialog(MainActivity.this,
					"你确定要花费"+getAnswerTipValue()+"个金币来提示一个答案吗？", 
					tipAnswerListener);
			break;
		case LACKCOIN:
			Util.showDialog(MainActivity.this,
					"金币不足请充值！", 
					lackCoinsListener);
			break;
		}
	}
}
