package com.kun.tyjc;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android_serialport_api.SerialPort;

public class ReadDataService extends Service
{
	//private static final String TAG="InitDeviceService";
	
	
	Context mContext;
	
	
	
	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private ImageView top_left,top_right,down_left,down_right;
	private TextView tv_warn;
	private float gaoyaOKValue,diyaOKValue,wenduOKValue;
	private SharedPreferences sp;
	private Editor editor;
//	private byte[] buf;
	
	private ServiceThread mServiceThread;
	private static ArrayList<Byte> mbyReadBuffer = new ArrayList<Byte>();
	
	private static final int PACKAGE_POS_SYNC1 = 0;
	private static final int PACKAGE_POS_SYNC2 = 1;
	private static final int PACKAGE_POS_LEN = 2;
	private static final int PACKAGE_SYNC1 = 0x55;
	private static final int PACKAGE_SYNC2 = 0xAA;
	byte[] byData;
	int size;
	int n = 0;//确认四条消息全部收到
	boolean isOk = true;
	public static final String TOP_LEFT_QIYA = "top_left_qiya";
	public static final String TOP_LEFT_WENDU = "top_left_wendu";
	public static final String TOP_RIGHT_QIYA = "top_right_qiya";
	public static final String TOP_RIGHT_WENDU = "top_right_wendu";
	public static final String DOWN_LEFT_QIYA = "down_left_qiya";
	public static final String DOWN_LEFT_WENDU = "down_left_wendu";
	public static final String DOWN_RIGHT_QIYA = "down_right_qiya";
	public static final String DOWN_RIGHT_WENDU = "down_right_wendu";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		sp = getSharedPreferences("tyjc", Context.MODE_PRIVATE);
		editor = sp.edit();
		editor.putBoolean("show", true);
		editor.commit();
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
			
			mServiceThread = new ServiceThread();
			mServiceThread.start();
		} catch (SecurityException e) {
//			DisplayError(R.string.error_security);
		} catch (IOException e) {
//			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
//			DisplayError(R.string.error_configuration);
		}
	}
    
	public void onStart(Intent intent,int startId) 
	{
		super.onStart(intent,startId);
	}
	
	public void onDestroy(){
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}
	
	
	private class ServiceThread extends Thread
	{
		public void run()
		{
			while(!isInterrupted())
			{
				try
				{
					sleep(( mbyReadBuffer.size() != 0) ? 1 : 100);
					byData = readData();
					if(byData == null)
						continue;
//					onDataReceived(byData, byData.length);
					showError(byData);
//					Log.d("serial"," ServiceThread readData read data = " +getString(byData));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				////////////////////////////////////////////////////////////
				//
//				if(mSerialPort == null) return;
//				byte[] buffer = mSerialPort.jniRead(0, 256, 0);
//				size = buffer.length;
//				if (size > 0) {
//					onDataReceived(buffer, size);
//				}
				////////////////////////////////////////////////////////////
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
                    synchronized(mbyReadBuffer)
	 					{
	 						for(int i = 0; buffer != null && i < size; ++i)
	 							mbyReadBuffer.add(buffer[i]);
	 					}
//					if (size > 0) {
//						onDataReceived(buffer, size);
//					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	private static int getIntFromByte(byte val)
	{	
		return (int)((val >= 0) ? val : (val + 256));	
	}
	
	private static String getString(byte[] byData)
	{
		String str = new String();
		for(int i = 0; i < byData.length; ++i)
			str += ", 0x" + Integer.toHexString(getIntFromByte(byData[i]));
		return str;
	}
	
	
	private byte[] readData()
	{
		synchronized(mbyReadBuffer)
		{
			while(mbyReadBuffer.size() > PACKAGE_POS_SYNC2)
			{
				if( getIntFromByte(mbyReadBuffer.get(PACKAGE_POS_SYNC1)) == PACKAGE_SYNC1 
						&& getIntFromByte(mbyReadBuffer.get(PACKAGE_POS_SYNC2)) == PACKAGE_SYNC2 )
					break;
				mbyReadBuffer.remove(0);
			}
			if(mbyReadBuffer.size() > PACKAGE_POS_LEN)
			{				

				int len = getIntFromByte(mbyReadBuffer.get(PACKAGE_POS_LEN));

				int posChecksum = len - 1;
				if(mbyReadBuffer.size() > posChecksum)
				{
					byte checkSum = 0;
					/*fix me , compare checkSum and mbyReadBuffer.get(posChecksum)*/
					
					//if(mbyReadBuffer.get(posChecksum) == checkSum) //checkSum ok
					{
						//数据正确	生成数据包
						int lenPackage =  len;
						byData = new byte[lenPackage];
						for(int i = 0; i < lenPackage; ++i)
							byData[i] = mbyReadBuffer.remove(0);
//						Log.d("serial"," ServiceThread readData read data = " +getString(byData));
						return byData;
					}
//					else //checkSum error
//					{
//						Log.e("serial" ," readData EEEEEE " + mbyReadBuffer);
//						mbyReadBuffer.remove(0);
//						return null;
//					}
				}
			}
		}

		return null;
	}
	
	private void showError(byte[] data){
		if(data==null)
			return;
		//获取标准值
		gaoyaOKValue  = sp.getFloat("gaoya", 3.0f);
		diyaOKValue = sp.getFloat("diya", 1.8f);
		wenduOKValue = sp.getFloat("wendu", 75);
				
		String result = "";
		
		String fangxiang ="";//确定是哪一个轮胎的信息
		
		float qiyaValue;
		float wenduValue;
		
		int fangxiangNumber = -1;	
		qiyaValue =  (float)getIntFromByte(data[4])*3.44f/102f; //压力值
		wenduValue =getIntFromByte(data[5]) - 50; //温度值
		switch(data[3]){
		case 0x00://左前
			fangxiang = "左前轮：";
			fangxiangNumber = 0;
			editor.putFloat(TOP_LEFT_QIYA, qiyaValue);
			editor.putFloat(TOP_LEFT_WENDU, wenduValue);
			n++;
			break;
		case 0x01://右前
			fangxiang = "右前轮：";
			fangxiangNumber = 1;
			editor.putFloat(TOP_RIGHT_QIYA, qiyaValue);
			editor.putFloat(TOP_RIGHT_WENDU, wenduValue);
			n++;
			break;
		case 0x10://左后
			fangxiang = "左后轮";
			fangxiangNumber = 2; 
			editor.putFloat(DOWN_LEFT_QIYA, qiyaValue);
			editor.putFloat(DOWN_LEFT_WENDU, wenduValue);
			n++;
			break;
		case 0x11://右后
			fangxiang = "右后轮";
			fangxiangNumber = 3;
			editor.putFloat(DOWN_RIGHT_QIYA, qiyaValue);
			editor.putFloat(DOWN_RIGHT_WENDU, wenduValue);
			n++;
			break;
		}
		editor.commit();
		if(qiyaValue>gaoyaOKValue){//气压高
			result = fangxiang+"气压高";
			isOk = false;
		}else if(qiyaValue<diyaOKValue){
			result = fangxiang+"气压低";
			isOk = false;
		}else if(wenduValue>wenduOKValue){
			result = fangxiang+"温度高";
			isOk = false;
		}else{
			result = "";
		}
		if(fangxiangNumber==3&&!isOk&&n==4&&sp.getBoolean("show", false)){
			Intent intent = new Intent();
			intent.setClass(mContext, ShowError.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			isOk = true;
			n=0;
		}
//		Intent intent = new Intent();
//		System.out.println("xxxxxxxxxxxxxxxxxxxxaa  "+fangxiangNumber);
//		intent.putExtra("number", fangxiangNumber);
//		intent.putExtra("result", result);
//		intent.putExtra("qiya", qiyaValue);
//		intent.putExtra("wendu", wenduValue);
//		intent.putExtra("isOk", isOk);
//		if(!isOk){
//			intent.setClass(mContext, ShowError.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//		}
//			Intent intent1 = new Intent();
//			intent1.putExtra("number", fangxiangNumber);
//			intent1.putExtra("result", result);
//			intent1.putExtra("qiya", qiyaValue);
//			intent1.putExtra("wendu", wenduValue);
//			intent1.putExtra("isOk", isOk);
//			intent1.setAction("com.kun.tyjc.action.info");
//			sendBroadcast(intent1);
		
	}
	
//	//改变四个轮子的背景颜色图
//	private void setErrorImage(int fangxiangNumber, String result){
//		if(fangxiangNumber==0){
//			top_left.setImageResource(R.drawable.left_top_error);
//		}else{
//			top_left.setImageResource(R.drawable.left_top_ok);
//		}
//		if(fangxiangNumber==1){
//			top_right.setImageResource(R.drawable.right_top_error);
//		}else{
//			top_right.setImageResource(R.drawable.right_top_ok);
//		}
//		if(fangxiangNumber==2){
//			down_left.setImageResource(R.drawable.left_down_error);
//		}else{
//			down_left.setImageResource(R.drawable.left_down_ok);
//		}
//		if(fangxiangNumber==3){
//			down_right.setImageResource(R.drawable.right_down_error);
//		}else{
//			down_right.setImageResource(R.drawable.right_down_ok);
//		}
//		tv_warn.setText(result);
//	}


	
}
