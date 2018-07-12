package keker.footcoutiche;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Inventaire_Activity extends Activity {

	private ListView LV_inv=null;
	private Button B_saveinv=null;
	private List<HashMap<String, Object>> boisson=null;
	private List<HashMap<String, Object>> boissonbinder=null;
	private SimpleAdapter adapter=null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaire);
        
        
       ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		LV_inv = (ListView)findViewById(R.id.LV_inventaire);
        B_saveinv=(Button)findViewById(R.id.Saveinv);
       B_saveinv.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DB_Helper dbhelper=new DB_Helper(Inventaire_Activity.this);
				boolean save_result=dbhelper.save_inv(boissonbinder);
				if(save_result){
					CharSequence text = "inventaire enregistré avec succès";
		    		int duration = Toast.LENGTH_LONG;
		    		Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
		    		toast.show();
				} else {
					CharSequence text = "erreur lors de l'enregistrement de l'inventaire";
		    		int duration = Toast.LENGTH_LONG;
		    		Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
		    		toast.show();
				}
			}
		});	
        
        
       DB_Helper db_helper=new DB_Helper(Inventaire_Activity.this);
       boisson=new ArrayList<HashMap<String, Object>>(db_helper.get_boisson());
       
       if (boisson != null) {
       boissonbinder=new ArrayList<HashMap<String, Object>>();
         for(int i=0;i<=boisson.size()-1;i++){
    	   final HashMap<String, Object> map=new HashMap<String, Object>();
    	   map.put("Name",boisson.get(i).get("Name").toString());
    	   map.put("Last_Qte", boisson.get(i).get("Last_Qte").toString());
    	   map.put("Last_Prix", boisson.get(i).get("Prix").toString());
    	   boissonbinder.add(map);
       }
         final HashMap<String, Object> titre=new HashMap<String, Object>();
         titre.put("Name", "boisson");
         titre.put("Last_Qte", "Quantité en stock");         
   
    		adapter = new SimpleAdapter(this,
    					boissonbinder, R.layout.boisson_inv,
    					new String[] { "Name", "Last_Qte"}, new int[] { R.id.boisson_name,
    							R.id.boisson_qte}){
    				@Override
    				public boolean isEnabled(int position)
    				{
    				    return true;
    				}
        };
	        LV_inv.setAdapter(adapter);
        }
       
       LV_inv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View vue,
			    int position, long id) {
				  final int pos=position;
				  final HashMap item = (HashMap) parent.getItemAtPosition(position);
				  AlertDialog.Builder alert = new AlertDialog.Builder(Inventaire_Activity.this);
				    final EditText input = new EditText(Inventaire_Activity.this);
				    input.setHint("Qté de "+item.get("Name").toString());
				    input.setInputType(InputType.TYPE_CLASS_NUMBER);
				    alert.setView(input);
		 	   		alert.setTitle("");
		 	   			alert.setMessage("");
		 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		 	   		public void onClick(DialogInterface dialog, int whichButton) {
		 	   		HashMap<String, Object> output=new HashMap<String, Object>();
		 	   		output.put("Name", item.get("Name").toString());
		 	   		output.put("Last_Qte", input.getText().toString());
		 	   		output.put("Last_Prix", boissonbinder.get(pos).get("Last_Prix"));
		 	   			boissonbinder.set(pos, output);
		 	   		adapter.notifyDataSetChanged();
		 	   		     	   		}
		 	   		});
		 	   		alert.show();
				  
			  }  
			  });
       
    }
    
    
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.inventaire_menu, menu);
	        
	        return true;
	        

	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.add) {
	        	
	        	View Editview = View.inflate(Inventaire_Activity.this,
						R.layout.add_dialog, null);
	        	final TextView TV_Name= (TextView)Editview.findViewById(R.id.TV_adddialog_name);
	        	final TextView TV_Qtenom= (TextView)Editview.findViewById(R.id.TV_adddialog_qtenom);;
					final TextView TV_Qtelot= (TextView)Editview.findViewById(R.id.TV_adddialog_qtelot);
					final TextView TV_Prixachat= (TextView)Editview.findViewById(R.id.TV_adddialog_prixachat);
					final TextView TV_Prixvente= (TextView)Editview.findViewById(R.id.TV_adddialog_prixvente);
					AlertDialog.Builder alert = new AlertDialog.Builder(Inventaire_Activity.this);
			    
			    alert.setView(Editview);
	 	   		alert.setTitle("Nouvelle boisson");
	 	   		alert.setMessage("");
	 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 	   		public void onClick(DialogInterface dialog, int whichButton) {
	 	   		
	 	   		if(TV_Name.getText().toString().trim().equals("")||TV_Qtenom.getText().toString().trim().equals("")||TV_Qtelot.getText().toString().trim().equals("")||TV_Prixachat.getText().toString().trim().equals("")||TV_Prixvente.getText().toString().trim().equals("")){
	 	    	   	CharSequence text = "Erreur: certains champs sont vides";
	 	   		int duration = Toast.LENGTH_LONG;
	 	   		Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
	 	   		toast.show();
	 	   		}else{
	 	   		DB_Helper db_helper3=new DB_Helper(Inventaire_Activity.this);
	 	   		String result=db_helper3.ajouter_boisson(TV_Name.getText().toString(), Float.valueOf(TV_Prixachat.getText().toString()), Float.valueOf(TV_Prixvente.getText().toString()), Integer.parseInt(TV_Qtenom.getText().toString()), Integer.parseInt(TV_Qtelot.getText().toString())); 	   		
	 	 	   	CharSequence text = result;
	 			int duration = Toast.LENGTH_LONG;
	 			Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
	 			toast.show();
	 			HashMap<String, Object> output=new HashMap<String, Object>();
	 	   		output.put("Name", TV_Name.getText().toString());
	 	   		output.put("Last_Qte", "0");
	 	   		output.put("Last_Prix", TV_Prixachat.getText().toString());
	 	   		boissonbinder.add(output);
	 	   	adapter.notifyDataSetChanged();
	 	   		}
	 	   		
	 	   		}
	 	   		});
	 	   		alert.show();
	        
	            return true;
	        }
	        
	     /*   if (id == R.id.delete) {
	        	
	        	AlertDialog.Builder alert = new AlertDialog.Builder(Inventaire_Activity.this);
	        	 final EditText input = new EditText(Inventaire_Activity.this);
			    input.setHint("Nom exact de la boisson");
			     alert.setView(input);
	 	   		alert.setTitle("Supprimer une boisson");
	 	   		alert.setMessage("");
	 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 	   		public void onClick(DialogInterface dialog, int whichButton) {
	 	   		DB_Helper db_helper4=new DB_Helper(Inventaire_Activity.this);
	 	   	HashMap<String, Object> output=new HashMap<String, Object>();
	 	   		output=db_helper4.supp_boisson(input.getText().toString());
	 	   		if(output.containsKey("Result")){
	 	   		CharSequence text = "cette boisson n'existe pas";
        		int duration = Toast.LENGTH_LONG;
        		Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
        		toast.show();
	 	   		} else {
 	   		boissonbinder.remove(output);
 	   	adapter.notifyDataSetChanged();
	   		CharSequence text = input.getText().toString()+" supprimée";
    		int duration = Toast.LENGTH_LONG;
    		Toast toast = Toast.makeText(Inventaire_Activity.this, text, duration);
    		toast.show();
	 	   		}
	 	   		}
	 	   		});
	 	   		alert.show();
	 	   		
	            return true;
	        } */
	        
	        
	       if(id==android.R.id.home) {
	            // Respond to the action bar's Up/Home button
	                //NavUtils.navigateUpFromSameTask(this);
	                onBackPressed();
	                return true;
	            }

	        
	        return super.onOptionsItemSelected(item);
	    }
	    
	    
	  
	
}
