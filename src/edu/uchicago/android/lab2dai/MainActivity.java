package edu.uchicago.android.lab2dai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)this.findViewById(R.id.loc_btn)).setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				Intent i = new Intent(MainActivity.this, LocationActivity.class);
        				startActivity(i);
        			}
        		});
        
        ((Button)this.findViewById(R.id.view_log_btn)).setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				Intent i = new Intent(MainActivity.this, ViewLogActivity.class);
        				startActivity(i);
        			}
        		});
        
        ((Button)this.findViewById(R.id.http_btn)).setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				Intent i = new Intent(MainActivity.this, HttpActivity.class);
        				startActivity(i);
        			}
        		});
        
        ((Button)this.findViewById(R.id.photo_btn)).setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        				startActivity(i);
        			}
        		});
    }
}