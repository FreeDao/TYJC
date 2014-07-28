package com.kun.tyjc;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowError extends Activity {

	private ImageView top_left,top_right,down_left,down_right;
	private TextView tv_left_top_qiya,tv_left_top_wendu,tv_right_top_qiya,tv_right_top_wendu,
						tv_left_down_qiya,tv_left_down_wendu,tv_right_down_qiya,tv_right_down_wendu;
	boolean isShow = false;
	DecimalFormat df = new DecimalFormat("#0.00");
	private SharedPreferences sp;
	private Editor editor;
	private String bar = "Bar";
	private String C = "℃";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, ReadDataService.class);
		startService(intent);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("tyjc", Context.MODE_PRIVATE);
		editor = sp.edit();
		initView();
//		registerReceiver(receiver, new IntentFilter("com.kun.tyjc.action.info"));
	}
	
	@Override
	public void onResume(){
		super.onResume();
		error();
	}
	
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	public void onStop(){
		super.onStop();
		editor.putBoolean("show", false);
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		editor.putBoolean("show", false);
		editor.commit();
		Intent intent = new Intent();
		intent.setClass(this, TYJC.class);
		startActivity(intent);
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initView(){
		top_left = (ImageView) findViewById(R.id.iv_left_top);
		top_right = (ImageView) findViewById(R.id.iv_right_top);
		down_left = (ImageView) findViewById(R.id.iv_left_down);
		down_right = (ImageView) findViewById(R.id.iv_right_down);
//		tv_warn = (TextView) findViewById(R.id.tv_warn);
		tv_left_top_qiya = (TextView) findViewById(R.id.tv_left_top_qiya);
		tv_left_top_wendu = (TextView) findViewById(R.id.tv_left_top_wendu);
		
		tv_right_top_qiya = (TextView) findViewById(R.id.tv_right_top_qiya);
		tv_right_top_wendu = (TextView) findViewById(R.id.tv_right_top_wendu);
		
		tv_left_down_qiya = (TextView) findViewById(R.id.tv_left_down_qiya);
		tv_left_down_wendu = (TextView) findViewById(R.id.tv_left_down_wendu);
		
		tv_right_down_qiya  = (TextView) findViewById(R.id.tv_right_down_qiya);
		tv_right_down_wendu  = (TextView) findViewById(R.id.tv_right_down_wendu);
	}
	
	private void error(){
		 float top_left_qiya = sp.getFloat(ReadDataService.TOP_LEFT_QIYA, 0);
		 int top_left_wendu = sp.getInt(ReadDataService.TOP_LEFT_WENDU, 0);
		 float top_right_qiya = sp.getFloat(ReadDataService.TOP_RIGHT_QIYA, 0);
		 int top_right_wendu = sp.getInt(ReadDataService.TOP_RIGHT_WENDU, 0);
		 float down_left_qiya = sp.getFloat(ReadDataService.DOWN_LEFT_QIYA, 0);
		 int down_left_wendu = sp.getInt(ReadDataService.DOWN_LEFT_WENDU, 0);
		 float down_right_qiya = sp.getFloat(ReadDataService.DOWN_RIGHT_QIYA, 0);
		 int down_right_wendu = sp.getInt(ReadDataService.DOWN_RIGHT_WENDU, 0);
		 //获取标准值
		 float gaoyaOKValue  = sp.getFloat("gaoya", ReadDataService.GAOYA);
		 float diyaOKValue = sp.getFloat("diya", ReadDataService.DIYA);
		 int wenduOKValue = sp.getInt("wendu", ReadDataService.WENDU);
		 
		 top_left.setImageResource(R.drawable.left_top_ok);
		 top_right.setImageResource(R.drawable.right_top_ok);
		 down_left.setImageResource(R.drawable.left_down_ok);
		 down_right.setImageResource(R.drawable.right_down_ok);
		 
		 String errorString = "";
		 if(top_left_qiya>gaoyaOKValue){
			 errorString = "左前轮胎压高";
			 top_left.setImageResource(R.drawable.left_top_error);
		 }else if(top_left_qiya<diyaOKValue){
			 errorString = "左前轮胎压低";
			 top_left.setImageResource(R.drawable.left_top_error);
		 }else if(top_left_wendu>wenduOKValue){
			 errorString = "左前轮温度高";
			 top_left.setImageResource(R.drawable.left_top_error);
		 }else if(top_right_qiya>gaoyaOKValue){
			 errorString = "右前轮胎压高";
			 top_right.setImageResource(R.drawable.right_top_error);
		 }else if(top_right_qiya<diyaOKValue){
			 errorString = "右前轮胎压低";
			 top_right.setImageResource(R.drawable.right_top_error);
		 }else if(top_right_wendu>wenduOKValue){
			 errorString = "右前轮温度高";
			 top_right.setImageResource(R.drawable.right_top_error);
		 }else if(down_left_qiya>gaoyaOKValue){
			 errorString = "左后轮胎压高";
			 down_left.setImageResource(R.drawable.left_down_error);
		 }else if(down_left_qiya<diyaOKValue){
			 errorString = "左后轮胎压低";
			 down_left.setImageResource(R.drawable.left_down_error);
		 }else if(down_left_wendu>wenduOKValue){
			 errorString = "左后轮温度高";
			 down_left.setImageResource(R.drawable.left_down_error);
		 }else if(down_right_qiya>gaoyaOKValue){
			 errorString = "右后轮胎压高";
			 down_right.setImageResource(R.drawable.right_down_error);
		 }else if(down_right_qiya<diyaOKValue){
			 errorString = "右后轮胎压低";
			 down_right.setImageResource(R.drawable.right_down_error);
		 }else if(down_right_wendu>wenduOKValue){
			 errorString = "右后轮温度高";
			 down_right.setImageResource(R.drawable.right_down_error);
		 }
		 tv_left_top_qiya.setText(df.format(top_left_qiya));
		 tv_left_top_wendu.setText(String.valueOf(top_left_wendu));
		 tv_right_top_qiya.setText(df.format(top_right_qiya));
		 tv_right_top_wendu.setText(String.valueOf(top_right_wendu));
		 tv_left_down_qiya.setText(df.format(down_left_qiya));
		 tv_left_down_wendu.setText(String.valueOf(down_left_wendu));
		 tv_right_down_qiya.setText(df.format(down_right_qiya));
		 tv_right_down_wendu.setText(String.valueOf(down_right_wendu));
//		 tv_warn.setText(errorString);
		 
	}
//	
//	//改变四个轮子的背景颜色图
//	private void setErrorImage(int fangxiangNumber, String result,boolean isOk,double qiya, double wendu){
//		if(fangxiangNumber==0){
//			if(isOk){
//				top_left.setImageResource(R.drawable.left_top_ok);
//			}else{
//				top_left.setImageResource(R.drawable.left_top_error);
//			}
//			tv_left_top_qiya.setText(df.format(qiya));
//			tv_left_top_wendu.setText(String.valueOf(wendu));
////			System.out.println("xxxxxxxxxxxxxxxx "+qiya+" "+wendu);
//		}
//		if(fangxiangNumber==1){
//			if(isOk){
//				top_right.setImageResource(R.drawable.right_top_ok);
//			}else{
//				top_right.setImageResource(R.drawable.right_top_error);
//			}
//			tv_right_top_qiya.setText(df.format(qiya));
//			tv_right_top_wendu.setText(String.valueOf(wendu));
//		}
//		if(fangxiangNumber==2){
//			if(isOk){
//				down_left.setImageResource(R.drawable.left_down_ok);
//			}else{
//				down_left.setImageResource(R.drawable.left_down_error);
//			}
//			tv_left_down_qiya.setText(df.format(qiya));
//			tv_left_down_wendu.setText(String.valueOf(wendu));
//		}
//		if(fangxiangNumber==3){
//			if(isOk){
//				down_right.setImageResource(R.drawable.right_down_ok);
//			}else{
//				down_right.setImageResource(R.drawable.right_down_error);
//			}
//			tv_right_down_qiya.setText(df.format(qiya));
//			tv_right_down_wendu.setText(String.valueOf(wendu));
//		}
//		tv_warn.setText(result);
//	}
//	
//	private BroadcastReceiver receiver = new BroadcastReceiver(){
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			int fangxiangNumber = intent.getIntExtra("number", -1);
//			String result = intent.getStringExtra("result");
//			double qiya = intent.getDoubleExtra("qiya", 0);
//			double wendu = intent.getDoubleExtra("wendu", 0);
//			boolean isOk = intent.getBooleanExtra("isOk", false);
//			setErrorImage(fangxiangNumber, result, isOk,qiya,wendu);
//		}
//		
//	};
	
	
	
}
