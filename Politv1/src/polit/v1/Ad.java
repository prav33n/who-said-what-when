package polit.v1;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.*; 
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

public class Ad extends Activity{
	private AdView adView;
	String banid ="a1500c1959f276b";
	public Ad(RelativeLayout ads, Activity act){
		 // Create an ad
        adView = new AdView(act, AdSize.BANNER,banid);
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        Log.e("ads",""+ads.getId());
        ads.addView(adView);
        AdRequest adRequest = new AdRequest();
        adView.loadAd(adRequest);
	}

}
