package polit.v1;




import java.util.Calendar;

import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.home);
        //getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.backgorund);
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.titlebar);
        }
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       new Copyfiles(this.getApplicationContext());
       RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
       new Ad(rel,this);
       //sync using broadcast receiver 
      try {
		     Intent intent = new Intent(getApplicationContext(), Syncreceiver.class); 
			 intent.putExtra("syncmessage","database");
			 // use a static variable for the request code the same code should be used for canceling the broadcast. 
			 PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			 // Get the AlarmManager service
			 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
			 Calendar cal = Calendar.getInstance();
			 am.set(AlarmManager.RTC,cal.getTimeInMillis(), sender);
			 }
	 catch(Exception e) {Log.e("error",e.getMessage());} 
	
   /*  Thread t = new Thread(new Runnable() {

           public void run() {
        	   //Launch and/or Bind to service here
        	   startService(new Intent(getApplicationContext(), Dbsync.class));
           }
       });
       t.start();*/
      
    }
    
    public void hometopic(View v) {
    	Intent i = new Intent(this.getApplicationContext(),Topics.class);
	    startActivity(i);  
      	}
    
    public void homecandidate(View v) {
      	Intent i = new Intent(this.getApplicationContext(),Candidates.class);
	    startActivity(i);  
      	}
    
	public void onClickInfo(View v) {
		Intent i = new Intent(getApplicationContext(), Webview.class);
	    i.putExtra("viewtype","local");
        startActivity(i);  
				}
	
	public void shareapp(View v){
    	Intent shareIntent = 
	    	 new Intent(android.content.Intent.ACTION_SEND);
	    	//set the type
	    	shareIntent.setType("text/plain");
        	//add a subject
	    	String shareMessage = "Check out Who Said What When App: http://bit.ly/whosaidwhatwhen";
	      	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
	    	//build the body of the message to be shared
	       	//add the message
	    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
	    	Log.e("button selected",shareMessage);
	    	//start the chooser for sharing
	    	startActivity(Intent.createChooser(shareIntent, "Application Share"));
    	
 	}

}