package keker.footcoutiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_Activity extends Activity {

	private ListView LV_edit=null;
	private List<HashMap<String, Object>> boisson=null;
	private SimpleAdapter adapter=null;
	
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.edit_activity);
	        

			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
	        LV_edit=(ListView)findViewById(R.id.LV_edit);
	       
	        
	        DB_Helper db_helper=new DB_Helper(Edit_Activity.this);
	        boisson=new ArrayList<HashMap<String, Object>>(db_helper.get_allboisson());
	        
	        if (boisson != null) {
	         HashMap<String, Object> titre=new HashMap<String, Object>();
	    
	     		adapter = new SimpleAdapter(this,
	     					boisson, R.layout.boisson_edit,
	     					new String[] { "Name", "Qte_nom", "Qte_par_lot","Prix", "Prix_vente"}, new int[] { R.id.boisson_name,
	     							R.id.boisson_qtenom, R.id.boisson_qtelot, R.id.boisson_prix, R.id.boisson_prixvente}){
	     				@Override
	     				public boolean isEnabled(int position)
	     				{
	     				    return true;
	     				}
	         };
	 	        LV_edit.setAdapter(adapter);
	         }
	        
	        LV_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 			  @Override
	 			  public void onItemClick(AdapterView<?> parent, View vue,
	 			    int position, long id) {
	 				  final int pos=position;
	 				  final HashMap item = (HashMap) parent.getItemAtPosition(position);
	 				 View Editview = View.inflate(Edit_Activity.this, R.layout.edit_dialog, null);
	 	 				final TextView TV_Qtenom= (TextView)Editview.findViewById(R.id.TV_dialog_qtenom);
	 	 				TV_Qtenom.setHint(item.get("Qte_nom").toString());
	 	 				final TextView TV_Qtelot= (TextView)Editview.findViewById(R.id.TV_dialog_qtelot);
	 	 				TV_Qtelot.setHint(item.get("Qte_par_lot").toString());
	 	 				final TextView TV_Prixachat= (TextView)Editview.findViewById(R.id.TV_dialog_prixachat);
	 	 				TV_Prixachat.setHint(item.get("Prix").toString());
	 	 				final TextView TV_Prixvente= (TextView)Editview.findViewById(R.id.TV_dialog_prixvente);
	 	 				TV_Prixvente.setHint(item.get("Prix_vente").toString());
					
	 	 				
	 				  AlertDialog.Builder alert = new AlertDialog.Builder(Edit_Activity.this);
	 			    alert.setView(Editview);
	 		 	   		alert.setTitle(item.get("Name").toString());
	 		 	   			alert.setMessage("");
	 		 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 		 	   		public void onClick(DialogInterface dialog, int whichButton) {
	 		 	   		DB_Helper db_helper2=new DB_Helper(Edit_Activity.this);	
	 		 	   		HashMap<String, Object> output=new HashMap<String, Object>();
	 		 	   		output.put("Name", item.get("Name").toString());
	 		 	  	if(TV_Qtenom.getText().toString().trim().equals("")){
	 			 	   	output.put("Qte_nom", item.get("Qte_nom").toString());
	 			 	   	} else {
	 		 	   		output.put("Qte_nom", TV_Qtenom.getText().toString());
	 			 	   	}
	 		 	  	if(TV_Qtelot.getText().toString().trim().equals("")){
	 			 	   	output.put("Qte_par_lot", item.get("Qte_par_lot").toString());
	 			 	   	} else {
	 		 	   		output.put("Qte_par_lot", TV_Qtelot.getText().toString());
	 			 	   	}
	 		 	  	if(TV_Prixachat.getText().toString().trim().equals("")){
	 			 	   	output.put("Prix", item.get("Prix").toString());
	 			 	   	} else {
	 		 	   	output.put("Prix", TV_Prixachat.getText().toString());
	 			 	   	}
			 	  	if(TV_Prixvente.getText().toString().trim().equals("")){
	 			 	   	output.put("Prix_vente", item.get("Prix_vente").toString());
	 			 	   	} else {
	 		 	  output.put("Prix_vente", TV_Prixvente.getText().toString());
	 			 	   	}
	 		 	  output.put("Last_Qte", boisson.get(pos).get("Last_Qte").toString());
	 		 	boolean updateok=db_helper2.maj_boisson(output);
	 		 	if(updateok){
	 		 	   			boisson.set(pos, output);
	 		 	   		adapter.notifyDataSetChanged();
	 			   		CharSequence text = item.get("Name").toString()+" modifié avec succès";
	 		    		int duration = Toast.LENGTH_LONG;
	 		    		Toast toast = Toast.makeText(Edit_Activity.this, text, duration);
	 		    		toast.show();
	 		 	}	   	else {
 			   		CharSequence text = "erreur lors de l'enregistrement";
 		    		int duration = Toast.LENGTH_LONG;
 		    		Toast toast = Toast.makeText(Edit_Activity.this, text, duration);
 		    		toast.show();
 		    		}
	 		 	}
	 		 	   		
	 		 	   		});
	 		 	   		alert.show();
	 				  
	 			  
	 			  }
	 			  });
	        
	  
	       
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.edit_menu, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	       
			if (id == android.R.id.home) {
				// Respond to the action bar's Up/Home button
				// NavUtils.navigateUpFromSameTask(this);
				onBackPressed();
				return true;
			}
	        
	        
	        return super.onOptionsItemSelected(item);
	    }
}
