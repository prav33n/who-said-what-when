package polit.v1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Persondisplay extends Baseclass {
	int id;
	Cursor cur;
	String name,affiliation;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persondisplay);
        RelativeLayout rel = (RelativeLayout)findViewById(R.id.ads);
        new Ad(rel,this);
        Bundle msg = getIntent().getExtras();
        id = msg.getInt("id");
        Database db = new Database(this,"polit.db");
        String query = "select _id,firstname,lastname,bio,politicalaffiliation,picturefilename,biosource,race from person where _id ="+id+" and deleted = 0";
        cur = db.query(query);
    	startManagingCursor(cur);
	    cur.moveToFirst();
	    name = cur.getString(cur.getColumnIndex("firstname"))+" "+cur.getString(cur.getColumnIndex("lastname"));
	    setTitle(name+"\n"+cur.getString(cur.getColumnIndex("race")));
	    if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Republican"))
    	setTitleColor(getResources().getColor(R.color.republican));
	    else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Democrat"))
	    setTitleColor(getResources().getColor(R.color.democrate));
	    else if(cur.getString(cur.getColumnIndex("politicalaffiliation")).equalsIgnoreCase("Independent"))
	    setTitleColor(getResources().getColor(R.color.independent));
	    Log.e("cursor count",""+cur.getCount()+"//"+id+"//"+msg.getInt("id"));
	    TextView politaffiliation = (TextView)findViewById(R.id.paffiliation);
	    affiliation = cur.getString(cur.getColumnIndex("politicalaffiliation"));
	    politaffiliation.setText(affiliation);
	    TextView bio = (TextView)findViewById(R.id.pbio);
	    bio.setText(cur.getString(cur.getColumnIndex("bio")));
	    TextView tv = (TextView)findViewById(R.id.pfullname);
	    tv.setText(name);
        Button button =  (Button)findViewById(R.id.pquotesby);
	    button.setTag(name);
	    TextView source = (TextView)findViewById(R.id.psource);
	    source.setText("Source: "+cur.getString(cur.getColumnIndex("biosource")));
	    ImageView picture = (ImageView)findViewById(R.id.ppicturefilename);
	    picture.setImageBitmap(setimage(picture,cur.getString(cur.getColumnIndex("picturefilename"))));
 }
	   public void quotesbyclick(View v) {
		   v.findViewById(R.id.pquotesby);
			 Log.e("button selected",""+id);
			  Intent i = new Intent(getApplicationContext(), Topicsbyperson.class);
			  i.putExtra("id", id);
			  i.putExtra("politician",name);
			  i.putExtra("affiliation",affiliation);
			  startActivity(i);
	    	//startActivity (new Intent(con, Display.class));
	      	}
	   
	   public void quotesclick(View v) {
		     v.findViewById(R.id.pquotes);
			 Log.e("button selected",""+id);
			  Intent i = new Intent(getApplicationContext(), Quotes.class);
			  i.putExtra("id", id);
			  i.putExtra("quotesby",name);
			  i.putExtra("affiliation",affiliation);
			  startActivity(i);
		   }
	    	//startActivity (new Intent(con, Display.class));§
}
