package polit.v1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Topicsbyperson extends Activity {
	int id;
	String topic,query;
	ArrayList<Integer> topicid, count;
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.topicsbyperson);
	        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
	        new Ad(rel,this);
	        Bundle msg = getIntent().getExtras();
	        id = msg.getInt("id");
	        setTitle("Quotes by Topic from "+msg.getString("politician"));
	        Database db = new Database(this,"polit.db");
	        ListView list = (ListView)findViewById(R.id.tplist);
	        if(msg.getString("affiliation").equalsIgnoreCase("Republican"))
	    	setTitleColor(getResources().getColor(R.color.republican));
		    else if(msg.getString("affiliation").equalsIgnoreCase("Democrat"))
		    setTitleColor(getResources().getColor(R.color.democrate));
		    else if(msg.getString("affiliation").equalsIgnoreCase("Independent"))
		    setTitleColor(getResources().getColor(R.color.independent));
	        
	        query ="select topicid,count(*) from topicquote where quoteid in (select _id from quote where personid ="+id+") and deleted = 0 group by topicid";
	        Cursor cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
		    Log.e("cursor Count",""+cur.getColumnCount()); //load sound filenames to the spinner
	        topicid=new ArrayList<Integer>();
	        count=new ArrayList<Integer>();
	        while (!cur.isAfterLast()) {
				topicid.add(cur.getInt(0));
				count.add(cur.getInt(1));
				cur.moveToNext();
				}
			cur.close();
					
			String topic = topicid.toString();
		    topic.substring(1, topic.length()-1);
			query = "select _id,topic from topic where _id in ("+topic.substring(1, topic.length()-1)+") and deleted = 0 order by topic";
			Log.e("logged data",topic+"//"+count.toString()+"//"+query);
			cur = db.query(query);
	    	startManagingCursor(cur);
		    cur.moveToFirst();
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.topicsbyperson, cur, new String[] {"topic","_id"}, 
		    		new int[] {R.id.tptopicname,R.id.tptopicquotes});
		    
		    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
		    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
		    		  	if(columnIndex == cur.getColumnIndex("_id")) {
			    	        Button	 button = (Button)view;
			    	        int person = cur.getInt(columnIndex);
			    	        button.setText(cur.getString(cur.getColumnIndex("topic")));
			    	       /* if(count.get(topicid.indexOf(person)) <=1)
			       	       	button.setText(""+count.get(topicid.indexOf(person))+" Quote");
			    	        else 
			    	        button.setText(""+count.get(topicid.indexOf(person))+" Quotes");*/
			       	     	button.setTag(person); 
			       	     	return true;}
		    		  	if(columnIndex == cur.getColumnIndex("topic")) {
			    	        TextView tv = (TextView)view;
			       	       	tv.setText(cur.getString(columnIndex));
			    	    	return true;}
		    	   	return true;
		    	    }
		    	}); 
		    
		    list.setAdapter(adapter);
		    list.setFocusable(false);		    
	    	findViewById(R.id.tptopicperson).setVisibility(View.GONE);
}
		
	    public void quotesclick(View v) {
	    	  v.findViewById(R.id.tptopicquotes);
			  Intent i = new Intent(getApplicationContext(),Quotes.class);
			  i.putExtra("personid",id);
			  i.putExtra("id",Integer.parseInt(v.getTag().toString()));
			  Log.e("logged values",""+Integer.parseInt(v.getTag().toString())+"//"+id);
			  startActivity(i);
	      	}
}		


