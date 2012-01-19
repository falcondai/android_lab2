package edu.uchicago.android.lab2dai;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewLogActivity extends Activity {
	public static final String tag = "ViewLogActivity";
	
	private Resources res;
	private TextView loc_log;
	private InputStream in;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set up UI
		setContentView(R.layout.loc_log);
		loc_log = (TextView)findViewById(R.id.loc_log);
		
		res = getResources();
		
		((Button)findViewById(R.id.log_clear_btn)).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						File log = new File(getFilesDir(), res.getString(R.string.loc_hist_log));

						if (!log.delete()) {
							Log.e(tag, "Log is not deleted.");
							Toast.makeText(ViewLogActivity.this, "Failed to clear location log.", Toast.LENGTH_SHORT).show();
						}
						else {
							try {
								log.createNewFile();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							Log.d(tag, "Log cleared.");
							loc_log.setText("(EMPTY)");
							Toast.makeText(ViewLogActivity.this, "Log cleared.", Toast.LENGTH_SHORT).show();
						}
					}
				}
			);
		
		((Button)findViewById(R.id.export_log_btn)).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
							//cannot access external storage
							Toast.makeText(ViewLogActivity.this, res.getString(R.string.media_not_available), Toast.LENGTH_SHORT).show();
							return ;
						}
						else {
							//external storage access available
							try {
								//copy log from app file dir to external dir
								InputStream fin = new FileInputStream(getFilesDir()+"/"+res.getString(R.string.loc_hist_log));
								File dest = new File(Environment.getExternalStorageDirectory()+"/location_log/");
								dest.mkdirs();
								OutputStream fout = new FileOutputStream(Environment.getExternalStorageDirectory()+"/location_log/"+res.getString(R.string.loc_hist_log));
								byte[] buf = new byte[1024];
								int len;

								while ((len = fin.read(buf)) > 0) {
									fout.write(buf, 0, len);
								}
								fin.close();
								fout.close();
								
								Toast.makeText(ViewLogActivity.this, "Log is exported to "+dest.getPath(), Toast.LENGTH_SHORT).show();
							}

							catch (Exception e) {
								Log.e(tag, e.toString());
							}
						}
					}
				});
	}
	
	protected void onResume() {
		super.onResume();
		
		//display the log
		try {
			in = new BufferedInputStream(openFileInput(res.getString(R.string.loc_hist_log)));
			loc_log.setText("Location Log:\n");
			int len;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) > 0) {
				loc_log.append(new String(buf), 0, len);
			}
		}
		catch (Exception e) {
			Log.e(tag, e.toString());
			loc_log.setText("(ERROR)");
		}
	}
	
	protected void onPause() {
		super.onPause();
		if (in != null) {
			try {
				in.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

