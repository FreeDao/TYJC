package com.kun.tyjc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;





import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android_serialport_api.SerialPort;

public class PipeiActivity extends Activity implements OnClickListener{
	private Button btn_left_top, btn_right_top,
				btn_left_buttom, btn_right_buttom;
	private TextView tv_left_top, tv_right_top,
				tv_left_buttom, tv_right_buttom;
	private OutputStream mOutputStream;
	private SerialPort mSerialPort;
	private Application mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pipei);		
		btn_left_buttom = (Button) findViewById(R.id.btn_left_buttom);
		btn_left_top = (Button) findViewById(R.id.btn_left_top);
		btn_right_buttom = (Button) findViewById(R.id.btn_right_buttom);
		btn_right_top = (Button) findViewById(R.id.btn_right_top);
		btn_left_buttom.setOnClickListener(this);
		btn_left_top.setOnClickListener(this);
		btn_right_buttom.setOnClickListener(this);
		btn_right_top.setOnClickListener(this);
		tv_left_buttom = (TextView) findViewById(R.id.tv_left_buttom);
		tv_left_top = (TextView) findViewById(R.id.tv_left_top);
		tv_right_buttom = (TextView) findViewById(R.id.tv_right_buttom);
		tv_right_top = (TextView) findViewById(R.id.tv_right_top);
		
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
		} catch (SecurityException e) {
//			DisplayError(R.string.error_security);
		} catch (IOException e) {
//			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
//			DisplayError(R.string.error_configuration);
		}
		registerReceiver(receiver, new IntentFilter("com.kun.tyjc.pipei"));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_left_top:
			try {
				byte[] temp = Command.getByteFromInt(new int[]{0x55, 0xAA, 0x06, 0x01, 0x00, 0xF8}, 6);
				mOutputStream.write(temp);
				tv_left_top.setText("正在匹配");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btn_right_top:
			try {
				byte[] temp = Command.getByteFromInt(new int[]{0x55, 0xAA, 0x06, 0x01, 0x01, 0xF8}, 6);
				mOutputStream.write(temp);
				tv_right_top.setText("正在匹配");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btn_left_buttom:
			try {
				byte[] temp = Command.getByteFromInt(new int[]{0x55, 0xAA, 0x06, 0x01, 0x10, 0xF8}, 6);
				mOutputStream.write(temp);
				tv_left_buttom.setText("正在匹配");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btn_right_buttom:
			try {
				byte[] temp = Command.getByteFromInt(new int[]{0x55, 0xAA, 0x06, 0x01, 0x11, 0xF8}, 6);
				mOutputStream.write(temp);
				tv_right_buttom.setText("正在匹配");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int data = intent.getIntExtra("index", -1);
			if(data==0x00){
				tv_left_top.setText("匹配成功！");
			}else if(data==0x01){
				tv_right_top.setText("匹配成功！");
			}else if(data==0x10){
				tv_left_buttom.setText("匹配成功！");
			}else if(data==0x11){
				tv_right_buttom.setText("匹配成功！");
			}
		}
		
	};

}
