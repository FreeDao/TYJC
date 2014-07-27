/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.kun.tyjc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TYJC extends Activity implements OnSeekBarChangeListener{

	private SeekBar seek_gaoya,seek_diya,seek_wendu;
	private SharedPreferences sp;
	private Editor editor;
	private TextView tv_gaoya, tv_diya, tv_gaowen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("tyjc", Context.MODE_PRIVATE);
		editor = sp.edit();
		setContentView(R.layout.settings);
		initView();
	}
	
	public void onResume(){
		super.onResume();
		seek_gaoya.setProgress(sp.getInt("gaoyaProgress", 5));
		seek_diya.setProgress(sp.getInt("diyaProgress", 3));
		seek_wendu.setProgress(sp.getInt("wenduProgress", 5));
		
		//获取设置值
		float gaoyaOKValue  = sp.getFloat("gaoya", ReadDataService.GAOYA);
		float diyaOKValue = sp.getFloat("diya", ReadDataService.DIYA);
		int wenduOKValue = sp.getInt("wendu", ReadDataService.WENDU);
		tv_gaoya.setText(String.valueOf(gaoyaOKValue));
		tv_diya.setText(String.valueOf(diyaOKValue));
		tv_gaowen.setText(String.valueOf(wenduOKValue));
	}
	
	private void initView(){
		seek_gaoya = (SeekBar) findViewById(R.id.seek_gaoya);
		seek_diya = (SeekBar) findViewById(R.id.seek_diya);
		seek_wendu = (SeekBar) findViewById(R.id.seek_wendu);
		seek_gaoya.setOnSeekBarChangeListener(this);
		seek_diya.setOnSeekBarChangeListener(this);
		seek_wendu.setOnSeekBarChangeListener(this);
		tv_diya = (TextView) findViewById(R.id.diyaValue);
		tv_gaoya = (TextView) findViewById(R.id.gaoyaValue);
		tv_gaowen = (TextView) findViewById(R.id.gaowenValue);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(seekBar == seek_gaoya){
			editor.putFloat("gaoya", 2.5f+progress*0.1f);
			editor.putInt("gaoyaProgress", progress);
			tv_gaoya.setText(String.valueOf(2.5f+progress*0.1f));
		}else if(seekBar == seek_diya){
			editor.putFloat("diya", 1.5f+progress*0.1f);
			editor.putInt("diyaProgress", progress);
			tv_diya.setText(String.valueOf(1.5f+progress*0.1f));
		}else if(seekBar == seek_wendu){
			editor.putInt("wendu", 50+progress*5);
			editor.putInt("wenduProgress", progress);
			tv_gaowen.setText(String.valueOf(50+progress*5));
		}
		editor.commit();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
