package polit.v1;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Candidates extends Baseclass{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.candidates);
	        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
	        new Ad(rel,this);
	        setTitle("Politicians");
	        Database db = new Database(this,"polit.db");
	        ListView list = (ListView)findViewById(R.id.clist);
	        String query = "select _id,firstname,lastname,shortbio,politicalaffiliation,picturefilename,race from person where deleted = 0 order by lastname";
	    	Cursor cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
		    Log.e("cursor count",""+cur.getCount());
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.candidates, cur, new String[] {"firstname","shortbio","politicalaffiliation","picturefilename","race"}, 
		    		new int[] {R.id.cname,R.id.cshortbio,R.id.caffiliation,R.id.cpicturefilename,R.id.crace});
		  	adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
		    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
		    		    	if(columnIndex == cur.getColumnIndex("firstname")) {
			    	    	TextView tv = (TextView)view;
			    	    	if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
			    	    	    		tv.setTextColor(getResources().getColor(R.color.republican));
			    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
			    	    		tv.setTextColor(getResources().getColor(R.color.democrate));
			    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
	    	    	    		tv.setTextColor(getResources().getColor(R.color.independent));
		    	    	   	if(cur.getString(columnIndex)!= null)
			    	    	tv.setText(cur.getString(columnIndex)+" "+cur.getString(cur.getColumnIndex("lastname")));
			    	    	tv.setTag(cur.getString(cur.getColumnIndex("_id")));
			    	    	return true;}
		    		    	else if(columnIndex == cur.getColumnIndex("shortbio")) {
			    	    	TextView tv = (TextView)view;
			    	    	if(cur.getString(columnIndex)!= null)
			    	    	tv.setText(cur.getString(columnIndex));
			    	    	return true;}
		    		    	else if(columnIndex == cur.getColumnIndex("race")) {
				    	    	TextView tv = (TextView)view;
				    	    	if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
		    	    	    		tv.setTextColor(getResources().getColor(R.color.republican));
				    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
				    	    		tv.setTextColor(getResources().getColor(R.color.democrate));
				    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
				    	    		tv.setTextColor(getResources().getColor(R.color.independent));
				    	    	if(cur.getString(columnIndex)!= null)
				    	    	tv.setText(cur.getString(columnIndex));
				    	    	else 
				    	    	tv.setVisibility(View.GONE);
				    	    	return true;}
		    		    	else if(columnIndex == cur.getColumnIndex("politicalaffiliation")) {
			    	    	TextView tv = (TextView)view;
			    	    	if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
	    	    	    		tv.setTextColor(getResources().getColor(R.color.republican));
			    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
			    	    		tv.setTextColor(getResources().getColor(R.color.democrate));
			    	    	else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
			    	    		tv.setTextColor(getResources().getColor(R.color.independent));
			    	    	if(cur.getString(columnIndex)!= null){
    			    	    	tv.setText(cur.getString(columnIndex));
    			    	    	}
			    	    	return true;}
		    		    	else if(columnIndex == cur.getColumnIndex("picturefilename")) {
			    	    	ImageView img = (ImageView)view;
			    	    	if(cur.getString(columnIndex)!= null){
			    	    		img.setImageBitmap(setimage(img,cur.getString(columnIndex)));
    			    	    	}
			    	    	return true;}
		    	          	return true;
		    	    }
		    	});
		  	 list.setAdapter(adapter);
	    	 findViewById(R.id.ccandidates).setVisibility(View.GONE);
	    	 
	    	 list.setOnItemClickListener(new OnItemClickListener() 
	    	    {
	    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	    				long arg3) {
	    		   // TODO Auto-generated method stub
	    			TextView tv = (TextView)arg1.findViewById(R.id.cname);
	    			int id = Integer.parseInt(tv.getTag().toString());
	    			 Intent i = new Intent(getApplicationContext(), Persondisplay.class);
	    		     i.putExtra("id", id);
	    		     startActivity(i);
	    			}
	    	  });	    	 
	 }
}
