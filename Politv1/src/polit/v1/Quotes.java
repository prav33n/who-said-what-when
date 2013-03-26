package polit.v1;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class Quotes extends Activity {
	int topicid;
	String query;
	final Activity activity = this;
	Cursor cur;
	String topic,candidatename;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes);
        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
        new Ad(rel,this);
        Bundle msg = getIntent().getExtras();
        topicid = msg.getInt("id");
        Database db = new Database(this,"polit.db");
        ListView list = (ListView)findViewById(R.id.qlist);
        //    View headerView = View.inflate(this, R.layout.listheader, null);
     //   list.addHeaderView(headerView);
  	  if(msg.getString("quotesby") != null){
  		  candidatename = msg.getString("quotesby");
  		activity.setTitle("Quotes from "+candidatename);
  	   if(msg.getString("affiliation").equalsIgnoreCase("Republican"))
	    	setTitleColor(getResources().getColor(R.color.republican));
		    else if(msg.getString("affiliation").equalsIgnoreCase("Democrat"))
		    setTitleColor(getResources().getColor(R.color.democrate));
		    else if(msg.getString("affiliation").equalsIgnoreCase("Independent"))
		    setTitleColor(getResources().getColor(R.color.independent));
  		query = "select * from quote where personid = "+topicid+" and deleted = 0 order by pubdate desc";
  		
  	  }
  	  else if(msg.getString("topic") != null){
  		topic = msg.getString("topic");
        activity.setTitle(topic);
        query = "select * from quote where _id in (select quoteid from topicquote where topicid = "+topicid+") and deleted = 0 order by pubdate desc";
    	  }
  	  else if(msg.getInt("personid") !=0){
  		query = "select topic from topic where _id = "+topicid;
  		Cursor cur = db.query(query);
     	startManagingCursor(cur);
     	cur.moveToFirst();
     	Log.e("data logged",""+cur.getColumnCount()+cur.getColumnIndex("topic")+"//"+query);
     	topic = cur.getString(0);
     	cur.close();
     	query = "select firstname,lastname,politicalaffiliation from person where _id = "+msg.getInt("personid")+" and deleted = 0";
        cur = db.query(query);
     	startManagingCursor(cur);
     	cur.moveToFirst();
     	candidatename = cur.getString(cur.getColumnIndex("firstname"))+" "+cur.getString(cur.getColumnIndex("lastname"));
     	 if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
         	setTitleColor(getResources().getColor(R.color.republican));
     	    else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
     	    setTitleColor(getResources().getColor(R.color.democrate));
     	    else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
     	    setTitleColor(getResources().getColor(R.color.independent));
     	cur.close();
  		setTitle(candidatename+" on "+topic);
  		query = "select * from quote where _id in (select quoteid from topicquote where topicid = "+topicid+") and personid ="+msg.getInt("personid")+" and deleted = 0 order by pubdate desc";
  		}
  	Log.e("query string","//"+query);  
  	cur = db.query(query);
  	startManagingCursor(cur);
  	
	cur.moveToFirst();
	Log.e("cursor count",""+cur.getCount()+"//"+cur.getColumnCount());
	    
  	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.quotes, cur, new String[] {"pubdate","quote","pubsource","bitly","_id","pubtitle"}, 
  	    		new int[] {R.id.qpubdate,R.id.qquote,R.id.qsource,R.id.qshare,R.id.qview,R.id.qarticletitle});
  	    
  	    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
  	    	   public boolean setViewValue(View view, Cursor cur, int columnIndex) {
  	    		if(columnIndex == cur.getColumnIndex("pubdate"))
  	    		{
  	    			TextView tv = (TextView)view;
  	    			try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cur.getString(columnIndex));
						SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");  
					    String dt = df.format(date.getTime());
						tv.setText(dt);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						tv.setText(cur.getString(columnIndex));
					}
  	    			
  	    		}
  	    		else if(columnIndex == cur.getColumnIndex("quote"))
  	    		{
  	    			TextView tv = (TextView)view;
  	    			tv.setText(cur.getString(columnIndex));
  	    		}
  	    		else if(columnIndex == cur.getColumnIndex("pubsource"))
  	    		{
  	    			TextView tv = (TextView)view;
  	    			tv.setText("Source: "+cur.getString(columnIndex));
  	    		}
  	    		else if(columnIndex == cur.getColumnIndex("pubtitle"))
  	    		{
  	    			TextView tv = (TextView)view;
  	    			tv.setText(cur.getString(columnIndex));
  	    		}
  	    		else if(columnIndex == cur.getColumnIndex("bitly"))
  	    		{
  	    			Button button = (Button)view;
  	    			button.setTag(cur.getString(columnIndex));
  	    		}
  	    		else if(columnIndex == cur.getColumnIndex("_id"))
  	    		{
  	    			Button button = (Button)view;
  	    			button.setTag(cur.getString(cur.getColumnIndex("bitly")));
  	    		}
  	    	 	return true;
  	    	    }
  	    	});
  	    
     	  list.setAdapter(adapter);
		  list.setFocusable(false);	
     	  findViewById(R.id.quotesdisplay).setVisibility(View.GONE);
  	  
  /*	 list.setOnItemClickListener(new OnItemClickListener() 
	    {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		   // TODO Auto-generated method stub
			ImageButton button = (ImageButton)arg1.findViewById(R.id.qview);
	     	String url = button.getTag().toString();
			  Log.e("url selected",""+url);
		   Intent i = new Intent(getApplicationContext(), Webview.class);
		   i.putExtra("url",url);
		   startActivity(i);
	        Log.e("item selected",""+arg2+"//"+arg3);
		}
	  });*/    	
}
	  public void sharelink(View v) {
	    	//startActivity (new Intent(con, Display.class));
	    	Button button = (Button)v.findViewById(R.id.qshare);
	    	String url = button.getTag().toString();	    	
	    	//create the send intent
	    	Intent shareIntent = 
	    	 new Intent(android.content.Intent.ACTION_SEND);
	    	//set the type
	    	shareIntent.setType("text/plain");
         	//add a subject
	    	 //      shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "<Politician> said this about <topic>");
	    	      //build the body of the message to be shared
//	    	      String shareMessage = url;
	    	//      String shareMessage = "Check out <articleUrl> about <Politican> on <Topic>. Got it from <bitly>";
	    	
	    	String subject = "";
	    	String shareMessage = "";
	    	if(topic == null){
	    	subject = "Quote by "+candidatename;
	    	shareMessage = "Check out "+url+" about "+candidatename+". Got it from http://bit.ly/whosaidwhatwhen";
	    	}
	    	else{
	    		subject =	candidatename+" said this about "+topic;
	    		shareMessage = "Check out "+url+" about "+candidatename+" on "+topic+". Got it from http://bit.ly/whosaidwhatwhen";
	    		}    	
	    	
   	    	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
	    	//build the body of the message to be shared
	    	
	      	//add the message
	    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
	    	 shareMessage);
	    	Log.e("button selected",shareMessage+"//"+subject);
	    	//start the chooser for sharing
	    	startActivity(Intent.createChooser(shareIntent, 
	    	 "Quotes Share"));
/*	    	
	    	 Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
	    	    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
	    	    share.setType("text/plain");
	    	    share.putExtra(Intent.EXTRA_TITLE,"Topic & person name");
	    	    share.putExtra(Intent.EXTRA_SUBJECT,"quote");
	    	    share.putExtra(Intent.EXTRA_TEXT,url);
	    	    startActivity(Intent.createChooser(share, "Share Quotes")); */
	     	}
	    
	    public void viewmore(View v) {
	    	//startActivity (new Intent(con, Display.class));
	    	Button button = (Button)v.findViewById(R.id.qview);
			String url = button.getTag().toString();
			  Log.e("button selected",""+url);
		   Intent i = new Intent(getApplicationContext(), Webview.class);
		   i.putExtra("url",url);
		   i.putExtra("viewtype","internet");
		   startActivity(i);
	      	}
	    
	    public void groupclick(View v) {
	    	//startActivity (new Intent(con, Display.class));
		   Intent i = new Intent(getApplicationContext(), Topicsbyperson.class);
		   i.putExtra("id",topicid);
		   startActivity(i);
	      	}
}