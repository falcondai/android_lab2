package edu.uchicago.android.lab2dai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoRollView extends LinearLayout {
	
	private ImageView thumb;
	private TextView filename;
	private Button delete_btn;
	
	public PhotoRollView(Context context) {
		super(context);
		
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		li.inflate(R.layout.photo_roll, this);
		
		thumb = (ImageView)findViewById(R.id.thumbnail);
		filename = (TextView)findViewById(R.id.filename);
		delete_btn = (Button)findViewById(R.id.delete_btn);
		
		delete_btn.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(v.getContext(), String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
					}
				});
		
		// TODO how to implement adapter with inflated views? 
	}
	

}
