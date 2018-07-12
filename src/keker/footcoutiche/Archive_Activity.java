package keker.footcoutiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class Archive_Activity extends Activity {

	private ListView LV_archive = null;
	private Spinner SP_choix = null;
	private List<String> listchoix = null;
	private Button B_ok = null;
	private Button B_cacherarchive = null;
	private ArrayAdapter Spadapter = null;
	private List<HashMap<String, Object>> list = null;
	private List<HashMap<String, Object>> listStat = null;
	private SimpleAdapter LV_adapter = null;
	private SimpleAdapter LV_adapter2 = null;
	private String choix = null;
	private RelativeLayout Layout_archive = null;
	private ListView LV_boissonarchive = null;
	private TextView TV_type = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.archive);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		LV_archive = (ListView) findViewById(R.id.LV_datearchive);

		LV_boissonarchive = (ListView) findViewById(R.id.LV_boissonarchive);

		TV_type = (TextView) findViewById(R.id.TV_type);
		Layout_archive = (RelativeLayout) findViewById(R.id.Layout_arch);

		DB_Helper dbCon = new DB_Helper(this);
		SP_choix = (Spinner) findViewById(R.id.Sp_choix);
		listchoix = new ArrayList<String>();
		listchoix.add("inventaire");
		listchoix.add("course");
		listchoix.add("Les deux");

		Spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listchoix);
		Spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SP_choix.setAdapter(Spadapter);

		SP_choix.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				choix = Spadapter.getItem(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		B_cacherarchive = (Button) findViewById(R.id.B_cacherarchive);
		B_cacherarchive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listStat.clear();
				LV_adapter2.notifyDataSetChanged();
				Layout_archive.setVisibility(View.GONE);
				LV_archive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 260));

			}
		});

		B_ok = (Button) findViewById(R.id.B_choix);
		B_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listStat != null) {
					listStat.clear();
					LV_adapter2.notifyDataSetChanged();
				}
				Layout_archive.setVisibility(View.GONE);
				LV_archive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 260));

				DB_Helper dbCon = new DB_Helper(Archive_Activity.this);
				list = new ArrayList<HashMap<String, Object>>(dbCon
						.get_arch(choix));

				if (list != null) {
					LV_adapter = new SimpleAdapter(Archive_Activity.this, list,
							R.layout.inv_archive,
							new String[] { "Type", "Date" }, new int[] {
									R.id.ET_Type, R.id.ET_Date }) {
						@Override
						public boolean isEnabled(int position) {
							return true;
						}
					};

					LV_archive
							.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View vue, int position, long id) {
									DB_Helper dbCon = new DB_Helper(
											Archive_Activity.this);
									listStat = new ArrayList<HashMap<String, Object>>(
											dbCon.get_archive(list
													.get(position).get("Date")
													.toString()));
									TV_type.setText(list.get(position)
											.get("Type").toString()
											+ " du "
											+ list.get(position).get("Date")
													.toString());
									LV_adapter2 = new SimpleAdapter(
											Archive_Activity.this, listStat,
											R.layout.boisson_archive,
											new String[] { "Name", "Qte" },
											new int[] { R.id.TV_Name_archive,
													R.id.TV_Qte_archive, }) {
										@Override
										public boolean isEnabled(int position) {
											return true;
										}
									};

									LV_boissonarchive
											.setOnItemClickListener(new AdapterView.OnItemClickListener() {
												@Override
												public void onItemClick(
														AdapterView<?> parent,
														View vue, int position,
														long id) {

												}
											});
									LV_archive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
									LV_boissonarchive.setAdapter(LV_adapter2);
									Layout_archive.setVisibility(View.VISIBLE);
								}
							});
					LV_archive.setAdapter(LV_adapter);

				}
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
