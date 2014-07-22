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

public class TYJC extends Activity implements OnSeekBarChangeListener{

	private SeekBar seek_gaoya,seek_diya,seek_wendu;
	private SharedPreferences sp;
	private Editor editor;
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
		seek_gaoya.setProgress(sp.getInt("gaoyaProgress", 0));
		seek_diya.setProgress(sp.getInt("diyaProgress", 0));
		seek_wendu.setProgress(sp.getInt("wenduProgress", 0));
	}
	
	private void initView(){
		seek_gaoya = (SeekBar) findViewById(R.id.seek_gaoya);
		seek_diya = (SeekBar) findViewById(R.id.seek_diya);
		seek_wendu = (SeekBar) findViewById(R.id.seek_wendu);
		seek_gaoya.setOnSeekBarChangeListener(this);
		seek_diya.setOnSeekBarChangeListener(this);
		seek_wendu.setOnSeekBarChangeListener(this);
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
		}else if(seekBar == seek_diya){
			editor.putFloat("diya", 1.5f+progress*0.1f);
			editor.putInt("diyaProgress", progress);
		}else if(seekBar == seek_wendu){
			editor.putFloat("wendu", 50+progress*5);
			editor.putInt("wenduProgress", progress);
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
