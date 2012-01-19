package edu.uchicago.android.lab2dai;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {
	public static final String tag = "LocationActivity";
	
	private LocationManager lm;
	private LocationListener lln;
	private LocationListener llg;
	private TextView tvn;
	private TextView tvg;
	private TextView tvl;
	private Resources res;
	private OutputStream out;
	
	//helper function
	protected String displayLocation(Location location) {
		List<String> sl = new ArrayList<String>();
		sl.add("Provider: "+location.getProvider());
		sl.add("Timestamp: "+String.valueOf(location.getTime()));
		sl.add("Latitude: "+String.valueOf(location.getLatitude()));
		sl.add("Longitude: "+String.valueOf(location.getLongitude()));
		if (location.hasAltitude()) {
			sl.add("Altitude: "+String.valueOf(location.getAltitude()));
		}
		if (location.hasAccuracy()) {
			sl.add("Accuracy: "+String.valueOf(location.getAccuracy()));
		}
		if (location.hasSpeed()) {
			sl.add("Speed: "+String.valueOf(location.getSpeed()));
		}
		if (location.hasBearing()) {
			sl.add("Bearing: "+String.valueOf(location.getBearing()));
		}
		
		String o = new String();
		Iterator<String> iterator = sl.iterator();
		while (iterator.hasNext()) {
			o += iterator.next() + '\n';
		}
		
		return o;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set up UI
		setContentView(R.layout.location);
		tvn = (TextView)findViewById(R.id.network);
		tvg = (TextView)findViewById(R.id.gps);
		tvl = (TextView)findViewById(R.id.last_loc);
		
		((Button)this.findViewById(R.id.view_log_btn2)).setOnClickListener(
        		new OnClickListener() {
        			public void onClick(View v) {
        				Intent i = new Intent(LocationActivity.this, ViewLogActivity.class);
        				startActivity(i);
        			}
        		});
		
		Log.d(tag, "UI loaded.");
		
		res = getResources();
		
		//obtain LocationManager
		lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Log.d(tag, "Location Manager initialized.");
			
		//log is saved at the default directory
		try {
			//append to the existing log (or create it)
			out = new BufferedOutputStream(openFileOutput(res.getString(R.string.loc_hist_log), MODE_APPEND));
		}
		catch (Exception e) {
			Log.e(tag, e.toString());
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
		
		//Location listeners
		//network listener
		lln = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Log.d(tag, "Location Changed: "+location.toString());
				
				tvn.setText(displayLocation(location));
				try {
					out.write(displayLocation(location).getBytes());
					out.flush();
				}
				catch (Exception e) {
					Log.e(tag, e.toString());
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.d(tag, "Provider Disabled: "+provider);
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.d(tag, "Provider Enabled: "+provider);
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		};
		
		//GPS listener
		llg = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Log.d(tag, "Location Changed: "+location.toString());
				
				tvg.setText(displayLocation(location));
				try {
					out.write(displayLocation(location).getBytes());
					out.flush();
				}
				catch (Exception e) {
					Log.e(tag, e.toString());
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.d(tag, "Provider Disabled: "+provider);
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.d(tag, "Provider Enabled: "+provider);
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		};
		
	}
	
	//pulling location data at specified intervals
	protected void onResume() {
		super.onResume();
		//query the last known location		
		Location last_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (last_loc != null) {
			tvl.setText("Last Known Location:\n" + displayLocation(last_loc));
		}
		else {
			tvl.setText("Last known location does not exist.\n");
		}
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, res.getInteger(R.integer.loc_pull_int), 0, lln);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, res.getInteger(R.integer.loc_pull_int), 0, llg);
	}
	
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(lln);
		lm.removeUpdates(llg);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		try {
			out.close();
		}
		catch (Exception e) {
			Log.e(tag, e.toString());
		}
	}
}
