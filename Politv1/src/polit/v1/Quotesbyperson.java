package polit.v1;

import java.util.ArrayList;

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

public class Quotesbyperson extends Baseclass{

	int id;
	String topic;
	ArrayList<Integer> personid, count;
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.quotesbyperson);
	        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
	        new Ad(rel,this);
	        Bundle msg = getIntent().getExtras();
	        id = msg.getInt("id");
	        Database db = new Database(this,"polit.db");
	        String query ="select topic from topic where _id="+id;
	        Cursor cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
	        setTitle(cur.getString(0));
	        cur.close();
	        ListView list = (ListView)findViewById(R.id.tplist);
	        query ="select personid,count(*) from quote where _id in (select quoteid from topicquote where topicid ="+id+") and deleted = 0 group by personid";
	        cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
		    Log.e("cursor Count",""+cur.getColumnCount()); //load sound filenames to the spinner
	        personid=new ArrayList<Integer>();
	        count=new ArrayList<Integer>();
	        while (!cur.isAfterLast()) {
				personid.add(cur.getInt(0));
				count.add(cur.getInt(1));
				cur.moveToNext();
				}
			cur.close();
					
			String person = personid.toString();
			person.replace("[", "(");
			person.replace("]", ")");
			person.substring(1, person.length()-1);
			Log.e("logged quotes data",person+"//"+count.toString()+"//"+person.substring(1, person.length()-1));
			query = "select _id, firstname,lastname,picturefilename,politicalaffiliation,race from person where _id in ("+person.substring(1, person.length()-1)+") and deleted = 0 order by lastname";
			cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.quotesbyperson, cur, new String[] {"picturefilename","firstname","politicalaffiliation","race"}, 
		    		new int[] {R.id.tppicturefilename,R.id.tpname,R.id.tpaffiliation,R.id.tprace});
		    
		    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
		    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
		    		   int color = 0;
		    		   if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
			       	    	color =getResources().getColor(R.color.republican);
			       	    else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
			       	    	color = getResources().getColor(R.color.democrate);
			       		else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
			       			color = (getResources().getColor(R.color.independent));
		    		   
		    		  	if(columnIndex == cur.getColumnIndex("politicalaffiliation")) {
			    	        TextView	tv  = (TextView)view;
			    	        int person = cur.getInt(cur.getColumnIndex("_id"));
			    	        /*if(count.get(personid.indexOf(person)) <=1)
			       	       	tv.setText(""+count.get(personid.indexOf(person))+" Quote");
			    	        else*/ 
			    	        tv.setTextColor(color);
			    	        tv.setText(cur.getString(columnIndex));
			       	     	tv.setTag(person);
			       	     	return true;}
		    		  	if(columnIndex == cur.getColumnIndex("race")) {
			    	        TextView	tv  = (TextView)view;
			    	        tv.setTextColor(color);
			    	        tv.setText(cur.getString(columnIndex));
			       	     	return true;}
		    		  	else if(columnIndex == cur.getColumnIndex("firstname")) {
			    	        TextView tv = (TextView)view;
			    	        tv.setTextColor(color);
			       	       	tv.setText(cur.getString(columnIndex)+" "+cur.getString(cur.getColumnIndex("lastname")));
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
	    	findViewById(R.id.tpperson).setVisibility(View.GONE);
	    	
	   	 list.setOnItemClickListener(new OnItemClickListener() 
 	    {
 		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 				long arg3) {
 		   // TODO Auto-generated method stub
 			TextView tv = (TextView)arg1.findViewById(R.id.tpaffiliation);
 			Intent i = new Intent(getApplicationContext(),Quotes.class);
			  i.putExtra("personid", Integer.parseInt(tv.getTag().toString()));
			  i.putExtra("id",id);
			  Log.e("logged values",""+Integer.parseInt(tv.getTag().toString())+"//"+id);
			  startActivity(i);
 			}
 	  });	
}
		
	    public void quotesclick(View v) {
	    	  v.findViewById(R.id.tpaffiliation);
			  Intent i = new Intent(getApplicationContext(),Quotes.class);
			  i.putExtra("personid", Integer.parseInt(v.getTag().toString()));
			  i.putExtra("id",id);
			  Log.e("logged values",""+Integer.parseInt(v.getTag().toString())+"//"+id);
			  startActivity(i);
	      	}


}
