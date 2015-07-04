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
	//���ֶԻ�����������
	public static final int DELETWORD=1;
	public static final int TIPANSWER=2;
	public static final int LACKCOIN=3;
	/**
	 * ����passView�еĿؼ�
	 */
	//��ǰ�ؿ����ı�
	public TextView mCurrentSongIndexPassView;
	//��ǰ���ڲ��Ÿ���������
	public TextView mCurrentSongIndex;
	//��ǰ�ؿ��ĸ�������
	public TextView mPassViewSongName;
	//������ť����
	public ImageButton deleteBtn;
	public ImageButton answerTipBtn;
	public ImageButton shareBtn;
	//���ؽ���
	public View passView;
	//����ϵͳ�����log�ı��
	public static final String TAG="MusicGuess.Jack";
	//��Ǹ����𰸵�����״̬
	public static final int ANSWER_RIGHT=0;
	public static final int ANSWER_WRONG=1;
	public static final int ANSWER_LACK=2;
	//������������Ƭ��ת������ָ����붯����ָ�븴ԭ����
	private Animation mplayerRotate;
	@Override
	protected void onPause()
	{
		super.onPause();
		//����ؿ��ͽ�ҵ���Ϣ
		Util.writeData(MainActivity.this, mCurrentIndex-1, mCurrentCoin);
		mPlay.clearAnimation();
		SongPlayer.stopSong();
	}

	private Animation mpinIn;
	private Animation mpinOut;
	//����������Ӧ��Interpolator
	private Interpolator mplayerIntor;
	private Interpolator mpinInIntor;
	private Interpolator mpinOutIntor;
	//Ϊ���Ű�ť��Ӽ����¼���Ϊָ����̰󶨶���
	private ImageButton mbtnStart;
	private ImageView mPlay;
	private ImageView mPin;
	//�Ƿ��ڲ�����
	private boolean isRunning=false;
	//GridView
	private MyGridView myGridView;
	private ArrayList<WordButton> arrayList;
	//��ѡ�����ֿ�
	private LinearLayout layout;
	private ArrayList<WordButton> selectedList;
	//�������ŵĸ���
	private Song mCurrentSong;
	private int mCurrentIndex=0;
	// ��������ĳ�ʼ��
	private int mCurrentCoin = Const.TOTAL_COIN;
	// ��ʾ�����TextView
	private TextView mViewCerrentCoin;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ȡ�ؿ��ͽ����Ϣ
		int[] data=Util.loadData(MainActivity.this);
		mCurrentIndex=data[Const.DATA_ROUND_INDEX];
		mCurrentCoin=data[Const.DATA_COIN_NUM];
		//��Ƭ�����ĳ�ʼ��
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
		//ָ����붯���ĳ�ʼ��
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
		//ָ��ָ��Ķ�����ʼ��
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
		//ʵ�������Ű�ť,�󶨼�����
		mbtnStart=(ImageButton) findViewById(R.id.btn_play_start);
		mbtnStart.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				handleStart();
			}
		});
		//Ϊ�����̺�ָ��󶨿ؼ�����Ӷ���
		mPlay=(ImageView) findViewById(R.id.imageView1);
		mPin=(ImageView) findViewById(R.id.imageView2);
		//��ʼ��������ͼ
		myGridView=(MyGridView) findViewById(R.id.gridView);
		arrayList=new ArrayList<WordButton>();
		//Ӧ��ע�᷽�����ݽӿ�
		myGridView.mMyGridAdapter.RegisterWorButtonListener(this);
		//��ʼ����ѡ�����ֿ�
		selectedList=new ArrayList<WordButton>();
		layout=(LinearLayout) findViewById(R.id.wordSelectedContainer);
		//���ý����������ʾ
		mViewCerrentCoin=(TextView) findViewById(R.id.txt_bar_coins);
		mViewCerrentCoin.setText(mCurrentCoin+"");
		//��ʼ������ѡ����е�����
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
	//���ظ�����Ϣ�ķ���
	public Song getCurrentSong(int index){
		Song song =new Song();
		song.setSongFileName(Const.SONG_INFO[index][Const.SONG_FILE_NAME]);
		song.setSongName(Const.SONG_INFO[index][Const.SONG_NAME]);
		return song;
		
	}
	public void initCurrentStageData(){
		//��ø�����Ϣ
		mCurrentSong=getCurrentSong(mCurrentIndex++);
		char[] chars=mCurrentSong.getNameCharacters();
		//���ò��Ž������������
		mCurrentSongIndex=(TextView) findViewById(R.id.game_round);
		mCurrentSongIndex.setText(mCurrentIndex+"");
		//������Ϣ����ѡ���
		//���������������䵽����ͼ��
		selectedList=initSelectedWord();
		//������е�ѡ�������
		layout.removeAllViews();
		for(int i=0;i<selectedList.size();i++){
			LayoutParams params=new LayoutParams(140, 140);
			layout.addView(selectedList.get(i).viewButton, params);
		}
		//�������
		arrayList=initAllData();
		//�������� MyGridView
		myGridView.updateData(arrayList);
		//һ��ʼ�Ͳ�������
		handleStart();
	}
	//�����������ѡ���������ַ���
	public String[] generateWords(){
		Random random=new Random();
		String[] cs = new String[myGridView.COUNT];
		//��ȡ����������
		for (int i=0;i<mCurrentSong.getSongNameLength();i++){
			cs[i]=mCurrentSong.getNameCharacters()[i]+"";
			
		}
		//������������
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
	
	//�������ݵķ���
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
	//ѡ�����ֿ��е��������
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
			//ѡ�����ֿ�ť�ĵ���¼�
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
			//�𰸲�����ִ�еķ���
			for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			 selectedList.get(i).viewButton.setTextColor(Color.WHITE);
			}
			break;
		case ANSWER_RIGHT:
			handlePassEvent();
			break;
		case ANSWER_WRONG:
			//�𰸴���ִ�еķ���,��˸����
			sparkWords();
			break;
		}
	}
	//�𰸴���ʱ��������˸
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
	//������ѡ��������ѡ���еĿɼ���
	private void setSlectedButton(WordButton wordButton){
		if(wordButton==null) return;
		for(int i=0;i<mCurrentSong.getSongNameLength();i++){
			if( selectedList.get(i).viewButton.getText()==""){
				selectedList.get(i).viewButton.setText(wordButton.buttonWord);
				selectedList.get(i).buttonWord=wordButton.buttonWord;
				selectedList.get(i).buttonIndex=wordButton.buttonIndex;
				//���ð�ť�ɼ�
				setVisible(wordButton, View.INVISIBLE);
				break;
				
			}
		}
	}
	//���ð�ť�Ŀɼ���
	private void setVisible(WordButton wordButton,int visibility){
		wordButton.viewButton.setVisibility(visibility);
		wordButton.isVisibale = (visibility==View.VISIBLE) ? true: false;
		MyLog.d(TAG, wordButton.buttonIndex+"��ť������Ϊ���ɼ�");
	}
	//�����ѡ���ֵķ���
	private void clearAnswer(WordButton wordButton){
		wordButton.viewButton.setText("");
		wordButton.buttonWord="";
		wordButton.isVisibale=false;
		setVisible(arrayList.get(wordButton.buttonIndex), View.VISIBLE);
		//����log��Ϣ
		MyLog.d(TAG, "��ѡ�������"+wordButton.buttonIndex);
	}
	//�жϴ�״̬�ķ���
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
	 * ��ѡ����ɾ��һ�����Ǵ𰸵�����
	 */
	/**
	 * ɾ������
	 */
	private void deleteOneWord() {
		// ���ٽ��
		if (!handleCoins(-getDeleteValue())) {
			handleShowDialog(LACKCOIN);
			return;
		}
		MyLog.d(TAG, "��ҿ��Ա�����");
		// �����������Ӧ��WordButton����Ϊ���ɼ�
		setVisible(findNotAnswerWord(), View.INVISIBLE);
	}
	
	/**
	 * �ҵ�һ�����Ǵ𰸵��ļ������ҵ�ǰ�ǿɼ���
	 *
	 * @return
	 */
	private WordButton findNotAnswerWord() {
		Random random = new Random();
		WordButton buf = null;
		
		while(true) {
			int index = random.nextInt(myGridView.COUNT);
			
			buf = arrayList.get(index);
			MyLog.d(TAG, "ȡ��һ���������");
			if (buf.isVisibale && !isTheAnswerWord(buf)) {
				MyLog.d(TAG, "�ҵ���һ�����Ǵ��ҿɼ�������");
				return buf;
			}
		}
	}
	
	/**
	 * �ж�ĳ�������Ƿ�Ϊ��
	 *
	 * @param word
	 * @return
	 */
	private boolean isTheAnswerWord(WordButton word) {
		boolean result = false;
		
		for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
			if (word.viewButton.getText().equals
					("" + mCurrentSong.getNameCharacters()[i])) {
				MyLog.d(TAG, "�Ѿ��ж��Ƿ�Ϊ��");
				result = true;
				
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * ����Դ�ļ���ȡɾ������ʾ�Ľ��
	 */
	public int getDeleteValue(){
		return this.getResources().getInteger(R.integer.pay_delete);
	}
	public int getAnswerTipValue(){
		return this.getResources().getInteger(R.integer.pay_answer_tip);
	}
	/**
	 * �жϴ��Ƿ����ɾ���ķ���
	 */
	private boolean handleCoins(int value){
		if(mCurrentCoin+value>=0){
			mCurrentCoin+=value;
		mViewCerrentCoin.setText(mCurrentCoin+"");
		MyLog.d(TAG, "����Ѿ�������");
			return true;
		}
		return false;
	}
	/**
	 * ��ʾ�𰸵ķ���
	 */
	private void tipOneWord(){
		wordButtonOnclick(findOneAnswer(findPosition()));
		//�ҵ�ѡ�����ֿ���û����ʾ���ֵĵ�һ��λ��
		//����Ҫ��ʾ��λ���ҵ�һ���𰸵�����
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
			// ��Ҳ�������ʾ��ʾ�Ի���
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
	 * ��������ť����ķ�����ɾ���𰸣���ʾ��
	 */
	
	private void handleDeleteWord(){
		deleteBtn=(ImageButton) findViewById(R.id.btn_delete_word);
		deleteBtn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				MyLog.d(TAG, "��ť�����");
//				deleteOneWord();
				handleShowDialog(DELETWORD);
			}
		});
	}
	//��ʾ���¼�����
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
					//�𰸲�����ִ�еķ���
					for(int i=0;i<mCurrentSong.getSongNameLength();i++){
					 selectedList.get(i).viewButton.setTextColor(Color.WHITE);
					}
					break;
				case ANSWER_RIGHT:
					handlePassEvent();
					break;
				case ANSWER_WRONG:
					//�𰸴���ִ�еķ���,��˸����
					sparkWords();
					break;
				}
			}
		});
	}
	private void handlePassEvent(){
		//�ѹؿ��ͽ����Ϣ������
		Util.writeData(MainActivity.this, mCurrentIndex-1, mCurrentCoin);
		passView=(LinearLayout)this.findViewById(R.id.passView);
		passView.setVisibility(View.VISIBLE);
		//û�ȸ貥�꣬�ͰѴ�����ˣ�Ҫ��ֹͣ��Ƭ����ת����
		mPlay.clearAnimation();
		SongPlayer.stopSong();
		//���Ž�ҵ�����Ч
		SongPlayer.playTone(MainActivity.this, SongPlayer.COIN);
		//���ù��ؽ����е�ǰ�ܿ�����ֵ
		mCurrentSongIndexPassView=(TextView) findViewById(R.id.pass_game_round);
		if(mCurrentSongIndexPassView!=null){
			
			mCurrentSongIndexPassView.setText(mCurrentIndex+"");
		}
		//���ù��ؽ������������
		mPassViewSongName=(TextView) findViewById(R.id.pass_game_name);
		if(mPassViewSongName!=null){
			mPassViewSongName.setText(mCurrentSong.getSongName());
		}
		//Ϊ��һ�ذ�ť�趨�����¼�
		ImageButton btnNextRound=(ImageButton) findViewById(R.id.btnNext);
		btnNextRound.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(isIfTheLast()){
				//��ʾͨ�ؽ���
				Intent i=new Intent(MainActivity.this,AllPass.class);
				startActivity(i);
				//�رյ�ǰactivity
				finish();
				}
				else{
					//��ʼ��һ��
					//���ع��ؽ���
					passView.setVisibility(View.GONE);
					//���س�ʼ������
					initCurrentStageData();
				}
			}
		});
	}
	public boolean isIfTheLast(){
		return mCurrentIndex==Const.SONG_INFO.length;
	}
	//ɾ��һ������𰸵���ʾ��
	public IshowDialogListener deletListener=new IshowDialogListener()
	{
		
		@Override
		public void onClick()
		{
			deleteOneWord();
			
		}
	};
	//��ʾһ����ȷ�𰸵���ʾ��
		public IshowDialogListener tipAnswerListener=new IshowDialogListener()
		{
			
			@Override
			public void onClick()
			{
				tipOneWord();
			}
		};
		//��Ҳ������ʾ��
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
					"��ȷ��Ҫ����"+getDeleteValue()+"�������ɾ��һ���������", 
					deletListener);
			break;
		case TIPANSWER:
			Util.showDialog(MainActivity.this,
					"��ȷ��Ҫ����"+getAnswerTipValue()+"���������ʾһ������", 
					tipAnswerListener);
			break;
		case LACKCOIN:
			Util.showDialog(MainActivity.this,
					"��Ҳ������ֵ��", 
					lackCoinsListener);
			break;
		}
	}
}
