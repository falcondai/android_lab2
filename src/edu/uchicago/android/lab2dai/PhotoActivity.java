package edu.uchicago.android.lab2dai;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PhotoActivity extends ListActivity {
	public static final String tag = "PhotoActivity";
	
	private Resources res;
	private File photo_dir;
	private ListView lv;
	
	//helper function
	private String generateImageName() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return "lab2_"+timestamp+".jpg";
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set up UI
		setContentView(R.layout.photo);
		lv = getListView();
		
		res = getResources();
		photo_dir = new File(Environment.getExternalStorageDirectory()+res.getString(R.string.photo_dir));
		
		//assuming the device can handle this intent
		// TODO explicitly check this
		
		((Button)findViewById(R.id.capture_btn)).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
							//the external storage is not available
							Toast.makeText(PhotoActivity.this, res.getString(R.string.media_not_available), Toast.LENGTH_SHORT).show();
							return ;
						}
						//the external storage is ready
						Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						photo_dir.mkdirs();
						try {
							i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photo_dir, generateImageName())));
							startActivityForResult(i, 0);
						}
						catch (Exception e) {
							Log.e(tag, e.toString());
						}
					}
				});
		
//		lv.setOnItemClickListener(
//				new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View v,
//							int pos, long id) {
//						Log.d(tag, "Clicked parent: "+parent.toString()+" View: "+v.toString()+" pos: "+String.valueOf(pos)+" id: "+String.valueOf(id));
//					}
//				});
		
		//click item to delete
		lv.setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int pos, long id) {
						Log.d(tag, "Clicked parent: "+parent.toString()+" View: "+v.toString()+" pos: "+String.valueOf(pos)+" id: "+String.valueOf(id));
						File fn = new File(photo_dir, (String)lv.getItemAtPosition(pos));
						if (!fn.delete()) {
							Toast.makeText(PhotoActivity.this, "Failed to delete "+fn.getName(), Toast.LENGTH_SHORT).show();
							return ;
						}
						
//						((ArrayAdapter<String>) lv.getAdapter()).remove((String) lv.getItemAtPosition(pos));
						PhotoActivity.this.setListAdapter(new ArrayAdapter<String>(PhotoActivity.this, R.layout.photo_item, photo_dir.list(null)));
						Toast.makeText(PhotoActivity.this, "Deleted "+fn.getName(), Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			//the external storage is not available
			Toast.makeText(PhotoActivity.this, res.getString(R.string.media_not_available), Toast.LENGTH_SHORT).show();
			return ;
		}
		
		//the external storage is available
		if (!photo_dir.exists()) {
			//the photo dir does not exist
			Toast.makeText(PhotoActivity.this, photo_dir.toString()+" does not exist.", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		//photo dir exists
		Log.d(tag, String.valueOf(photo_dir.list(null).length));
		
		//populate the lv
//		setListAdapter(new ArrayAdapter<String>(this, R.layout.photo_roll, R.id.filename, photo_dir.list(null)));
		setListAdapter(new ArrayAdapter<String>(this, R.layout.photo_item, photo_dir.list(null)));
		
//		Log.d(tag, lv.toString());
//		Log.d(tag, String.valueOf(lv.getCount()));
//		Log.d(tag, String.valueOf(lv.getChildCount()));
		
		for (int i=0; i<lv.getChildCount(); i++) {
			View child = lv.getChildAt(i);
			Log.d(tag, String.valueOf(child.getId()));
			//Toast.makeText(PhotoActivity.this, String.valueOf(child.getId()), Toast.LENGTH_SHORT).show();
			
//			((Button)(lv.getChildAt(i).findViewById(R.id.delete_btn))).setOnClickListener(
//					new OnClickListener() {
//						public void onClick(View v) {
//							Toast.makeText(PhotoActivity.this, String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
//						}
//					});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			Toast.makeText(this, "Photo saved at "+photo_dir.getPath(), Toast.LENGTH_SHORT).show();
		}
	}
}
