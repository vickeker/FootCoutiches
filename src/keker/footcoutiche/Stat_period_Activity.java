package keker.footcoutiche;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Stat_period_Activity extends Activity {

	private ListView LV_Stat_period = null;
	private Spinner SP_inv1 = null;
	private Spinner SP_inv2 = null;
	private Button B_act = null;
	private ArrayAdapter<String> Sp1adapter = null;
	private ArrayAdapter<String> Sp2adapter = null;
	private SimpleAdapter LV_adapter = null;
	private List<String> list_inv1 = null;
	private List<String> list_inv2 = null;
	private List<HashMap<String, Object>> Stat = null;
	private String date_inv1 = null;
	private String date_inv2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stat_period);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		LV_Stat_period = (ListView) findViewById(R.id.LV_Stat_period);

		B_act = (Button) findViewById(R.id.B_act);
		B_act.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB_Helper db_helper = new DB_Helper(Stat_period_Activity.this);

				try {
					Stat = new ArrayList<HashMap<String, Object>>(db_helper
							.get_Stat(date_inv1, date_inv2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Stat != null) {

					LV_adapter = new SimpleAdapter(Stat_period_Activity.this,
							Stat, R.layout.boisson_stat, new String[] { "Name",
									"SommeVente", "Vente", "SommeAchat",
									"CoutAchat" }, new int[] {
									R.id.boisson_name_s,
									R.id.boisson_qte_vendu,
									R.id.boisson_prixtotal,
									R.id.boisson_qte_achat,
									R.id.boisson_couttotal }) {
						@Override
						public boolean isEnabled(int position) {
							return true;
						}
					};
					LV_Stat_period.setAdapter(LV_adapter);
				}

				LV_Stat_period
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View vue, int position, long id) {

							}
						});
			}
		});

		DB_Helper dbCon = new DB_Helper(this);
		SP_inv1 = (Spinner) findViewById(R.id.Sp_inv1);
		list_inv1 = new ArrayList<String>(dbCon.get_invs());
		Sp1adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list_inv1);
		Sp1adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SP_inv1.setAdapter(Sp1adapter);

		SP_inv1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				date_inv1 = Sp1adapter.getItem(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		SP_inv2 = (Spinner) findViewById(R.id.Sp_inv2);
		list_inv2 = new ArrayList<String>(dbCon.get_invs());
		Sp2adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list_inv2);
		Sp2adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SP_inv2.setAdapter(Sp2adapter);

		SP_inv2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				date_inv2 = Sp2adapter.getItem(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stat_period_menu, menu);

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
