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

public class Topics extends Activity {
	ArrayList<Integer> topicid, count;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics);
        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
        new Ad(rel,this);
        setTitle("By Topic");
        Database db = new Database(this,"polit.db");
        ListView list = (ListView)findViewById(R.id.tlist);
        
        String query ="select topicid,count(*) from topicquote where deleted = 0 group by topicid";
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
           
        query = "select _id,topic from topic where _id in ("+topic.substring(1, topic.length()-1)+") and deleted = 0 order by topic";
    	cur = db.query(query);
    	startManagingCursor(cur);
	    cur.moveToFirst();
	    Log.e("cursor count",""+cur.getCount());
	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.topics, cur, new String[] {"topic","_id"}, 
	    		new int[] {R.id.ttopicname,R.id.ttopics});
	       adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
	    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
	    		  	if(columnIndex == cur.getColumnIndex("_id")) {
		    	        Button	 button = (Button)view;
		    	        @SuppressWarnings("unused")
						int person = cur.getInt(columnIndex);
		    	       	button.setText(cur.getString(cur.getColumnIndex("topic")));
		    	       	button.setTag(cur.getInt(columnIndex));
		       	     	return true;}
	    		  	if(columnIndex == cur.getColumnIndex("topic")) {
		    	        TextView tv = (TextView)view;
		       	       	tv.setText(cur.getString(columnIndex));
		       	       	tv.setTag(cur.getString(cur.getColumnIndex("topic")));
		    	    	return true;}
	    	   	return true;
	    	    }
	    	});
	    	    Log.e("test",""+adapter.getCount());
	    	 list.setAdapter(adapter);
	    	 list.setFocusable(false);
	    	 findViewById(R.id.ttopics).setVisibility(View.GONE);
	    	 findViewById(R.id.ttopicname).setVisibility(View.GONE);
	    	 
	}
	
	public void topicsclick(View v) {
    	//startActivity (new Intent(con, Display.class));
		Button button = (Button)v.findViewById(R.id.ttopics);
		int id = Integer.parseInt(button.getTag().toString());
		  Log.e("button selected",""+id);
		  Intent i = new Intent(getApplicationContext(),Quotesbyperson.class);
		 i.putExtra("id", id);
		 startActivity(i);
      	}
}
