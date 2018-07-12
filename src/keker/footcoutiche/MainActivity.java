package keker.footcoutiche;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

private Button B_inv=null;
private Button B_course=null;
private Button B_statperiod=null;
private Button B_statgraph=null;
private Button B_archive=null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        B_inv=(Button)findViewById(R.id.Inventaire);
        B_inv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
	    				Inventaire_Activity.class);
				startActivity(intent);
			}
		});
        
        B_course=(Button)findViewById(R.id.Course);
        B_course.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
	    				Course_Activity.class);
				startActivity(intent);
			}
		});
        
        B_statperiod=(Button)findViewById(R.id.Statistique);
        B_statperiod.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
	    				Stat_period_Activity.class);
				startActivity(intent);
			}
		});
        
        B_statgraph=(Button)findViewById(R.id.Statistique_graph);
        B_statgraph.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
	    				Graph_boisson_Activity.class);
				startActivity(intent);
			}
		});
       
        B_archive=(Button)findViewById(R.id.Archive);
        B_archive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
	    				Archive_Activity.class);
				startActivity(intent);
			}
		});
       
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        
        if (id == R.id.add) {
        	
        	
        	View Editview = View.inflate(MainActivity.this,
					R.layout.add_dialog, null);
        	final TextView TV_Name= (TextView)Editview.findViewById(R.id.TV_adddialog_name);
        	final TextView TV_Qtenom= (TextView)Editview.findViewById(R.id.TV_adddialog_qtenom);;
				final TextView TV_Qtelot= (TextView)Editview.findViewById(R.id.TV_adddialog_qtelot);
				final TextView TV_Prixachat= (TextView)Editview.findViewById(R.id.TV_adddialog_prixachat);
				final TextView TV_Prixvente= (TextView)Editview.findViewById(R.id.TV_adddialog_prixvente);
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		    
		    alert.setView(Editview);
 	   		alert.setTitle("Nouvelle boisson");
 	   		alert.setMessage("");
 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 	   		public void onClick(DialogInterface dialog, int whichButton) {
 	   		
 	   		if(TV_Name.getText().toString().trim().equals("")||TV_Qtenom.getText().toString().trim().equals("")||TV_Qtelot.getText().toString().trim().equals("")||TV_Prixachat.getText().toString().trim().equals("")||TV_Prixvente.getText().toString().trim().equals("")){
 	    	   	CharSequence text = "Erreur: certains champs sont vides";
 	   		int duration = Toast.LENGTH_LONG;
 	   		Toast toast = Toast.makeText(MainActivity.this, text, duration);
 	   		toast.show();
 	   		}else{
 	   		DB_Helper db_helper3=new DB_Helper(MainActivity.this);
 	   		String result=db_helper3.ajouter_boisson(TV_Name.getText().toString(), Float.valueOf(TV_Prixachat.getText().toString()), Float.valueOf(TV_Prixvente.getText().toString()), Integer.parseInt(TV_Qtenom.getText().toString()), Integer.parseInt(TV_Qtelot.getText().toString())); 	   		
 	 	   	CharSequence text = result;
 			int duration = Toast.LENGTH_LONG;
 			Toast toast = Toast.makeText(MainActivity.this, text, duration);
 			toast.show();
 	   		}
 	   		
 	   		}
 	   		});
 	   		alert.show();

 	   		
        
            return true;
        }
        
        
        if (id == R.id.edit) {
        	Intent intent = new Intent(MainActivity.this,
    				Edit_Activity.class);
			startActivity(intent);
        	
        	  return true;
        }
        
        
        return super.onOptionsItemSelected(item);
    }
}
