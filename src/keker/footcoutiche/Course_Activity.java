package keker.footcoutiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Course_Activity extends Activity {

	private ListView LV_course = null;
	private Button B_savecourse = null;
	private List<HashMap<String, Object>> boisson = null;
	private List<HashMap<String, Object>> boissonbinder = null;
	private SimpleAdapter adapter = null;
	private int qte_avantcourse = 0;
	private HashMap<String, Object> ListQteAv = null;
	private int qtenom;
	private int qteinv;
	private int qteparlot;
	private float qtefloatrec;
	private int qteintrec;
	private int qtetotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		LV_course = (ListView) findViewById(R.id.LV_course);
		B_savecourse = (Button) findViewById(R.id.Savecourse);
		B_savecourse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DB_Helper dbhelper = new DB_Helper(Course_Activity.this);
				boolean save_result = dbhelper.save_course(boissonbinder);
				if (save_result) {
					CharSequence text = "course enregistrée avec succès";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(Course_Activity.this, text,
							duration);
					toast.show();
				} else {
					CharSequence text = "erreur lors de l'enregistrement des courses";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(Course_Activity.this, text,
							duration);
					toast.show();
				}
			}
		});

		DB_Helper db_helper = new DB_Helper(Course_Activity.this);
		boisson = new ArrayList<HashMap<String, Object>>(db_helper.get_course());

		if (boisson != null) {
			boissonbinder = new ArrayList<HashMap<String, Object>>();
			ListQteAv = new HashMap<String, Object>();
			for (int i = 0; i <= boisson.size() - 1; i++) {
				final HashMap<String, Object> map = new HashMap<String, Object>();
				qtenom = Integer.parseInt(boisson.get(i).get("Qte_nom")
						.toString());
				qteinv = Integer.parseInt(boisson.get(i).get("Last_Qte")
						.toString());
				qteparlot = Integer.parseInt(boisson.get(i).get("Qte_par_lot")
						.toString());
				qtefloatrec = (qtenom - qteinv) / qteparlot;
				qteintrec = Integer
						.parseInt(String.format("%.0f", qtefloatrec));
				qtetotal = qteparlot * qteintrec;

				map.put("Prix", boisson.get(i).get("Prix").toString());
				map.put("Name", boisson.get(i).get("Name").toString());
				map.put("Qte_lot", String.valueOf(qteintrec));
				map.put("Qte_par_lot", String.valueOf(qteparlot));
				map.put("Qtetotal", String.valueOf(qtetotal));
				map.put("Qte_recommande", qteintrec + " x " + qteparlot + "("
						+ (qtenom - qteinv) + ")");
				map.put("New_Qte", Integer.parseInt(boisson.get(i).get("Last_Qte")
						.toString()));
				map.put("Qte_achete", "0");

				ListQteAv.put(
						boisson.get(i).get("Name").toString(),
						Integer.parseInt(boisson.get(i).get("Last_Qte")
								.toString()));
				boissonbinder.add(map);
			}
			
			
			adapter = new SimpleAdapter(this, boissonbinder,
					R.layout.boisson_course, new String[] { "Name",
							"Qte_recommande", "Qte_achete" }, new int[] {
							R.id.boisson_name_c, R.id.boisson_qte_c,
							R.id.boisson_qte_a }) {
				@Override
				public boolean isEnabled(int position) {
					return true;
				}
			};
			LV_course.setAdapter(adapter);
			//LinearLayout lignetitre=(LinearLayout)LV_course.getItemAtPosition(0);
			//lignetitre.setMinimumHeight(60);
		}

		LV_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View vue,
					int position, long id) {
				final int pos = position;
				
			
				final HashMap item = (HashMap) parent
						.getItemAtPosition(position);
				View Editview = View.inflate(Course_Activity.this,
						R.layout.course_dialog, null);
				final EditText ET_Qtelot = (EditText) Editview
						.findViewById(R.id.ET_qtelot);
				final EditText ET_Qteparlot = (EditText) Editview
						.findViewById(R.id.ET_qteparlot);
				final TextView TV_Qtetotal = (TextView) Editview
						.findViewById(R.id.TV_qtetotal);
				final EditText ET_Prix = (EditText) Editview
						.findViewById(R.id.ET_Prix);
				ET_Qtelot.setHint(boissonbinder.get(pos).get("Qte_lot").toString());
				ET_Qteparlot.setHint(boissonbinder.get(pos).get("Qte_par_lot").toString());
				TV_Qtetotal.setText(boissonbinder.get(pos).get("Qtetotal").toString());
				ET_Prix.setHint(boissonbinder.get(pos).get("Prix").toString());
				ET_Qtelot.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
						if(ET_Qteparlot.getText().toString().trim().equals("") && ET_Qtelot.getText().toString().trim().equals("")){
							TV_Qtetotal.setText(String.valueOf(Integer.parseInt(boissonbinder.get(pos).get("Qte_lot").toString())
									* Integer.parseInt(boissonbinder.get(pos).get("Qte_par_lot").toString())));
						} else {
						
						
						if(ET_Qteparlot.getText().toString().trim().equals("")){
							TV_Qtetotal.setText(String.valueOf(Integer.parseInt(ET_Qtelot
									.getText().toString())
									* Integer.parseInt(boissonbinder.get(pos).get("Qte_par_lot").toString())));
						} else {

						if(ET_Qtelot.getText().toString().trim().equals("")){
							TV_Qtetotal.setText(String.valueOf(0
									* Integer.parseInt(ET_Qteparlot.getText()
											.toString())));
						} else {
						TV_Qtetotal.setText(String.valueOf(Integer.parseInt(ET_Qtelot
								.getText().toString())
								* Integer.parseInt(ET_Qteparlot.getText()
										.toString())));
						}
						}
						}
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}
				});
				ET_Qteparlot.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
						if(ET_Qteparlot.getText().toString().trim().equals("") && ET_Qtelot.getText().toString().trim().equals("")){
							TV_Qtetotal.setText(String.valueOf(Integer.parseInt(boissonbinder.get(pos).get("Qte_lot").toString())
									* Integer.parseInt(boissonbinder.get(pos).get("Qte_par_lot").toString())));
						} else {
						
						if(ET_Qtelot
								.getText().toString().trim().equals("")){
						TV_Qtetotal.setText(String.valueOf(Integer.parseInt(boissonbinder.get(pos).get("Qte_lot").toString())
								* Integer.parseInt(ET_Qteparlot.getText()
										.toString())));
						} else {

						if(ET_Qteparlot
								.getText().toString().trim().equals("")){
						TV_Qtetotal.setText(String.valueOf(Integer.parseInt(ET_Qtelot
								.getText().toString())
								* 0));
						} else {
							TV_Qtetotal.setText(String.valueOf(Integer.parseInt(ET_Qtelot.getText().toString())
									* Integer.parseInt(ET_Qteparlot.getText()
											.toString())));
						}
						}
						}
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}
				});

				AlertDialog.Builder alert = new AlertDialog.Builder(
						Course_Activity.this);
				alert.setView(Editview);
				alert.setTitle("Achat de " + item.get("Name").toString());
				alert.setMessage("");
				alert.setNegativeButton("0",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								HashMap<String, Object> output = new HashMap<String, Object>();
								output.put("Name", item.get("Name").toString());
								output.put("Qte_lot", boissonbinder.get(pos).get("Qte_lot").toString());
								output.put("Qte_par_lot", boissonbinder.get(pos).get("Qte_par_lot").toString());
								output.put("Qtetotal", boissonbinder.get(pos).get("Qtetotal").toString());
								output.put("Qte_recommande",
										item.get("Qte_recommande").toString());
								output.put("Qte_achete", "0");
								output.put("New_Qte", Integer
										.parseInt(ListQteAv.get(
												item.get("Name").toString())
												.toString()));
								if (ET_Prix.getText().toString().trim()
										.equals("")) {
									output.put("Prix", boissonbinder.get(pos)
											.get("Prix").toString());
								} else {
									output.put("Prix", Float.parseFloat(ET_Prix
											.getText().toString()));
								}
								boissonbinder.set(pos, output);
								adapter.notifyDataSetChanged();
							}
						});
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								HashMap<String, Object> output = new HashMap<String, Object>();
								output.put("Name", item.get("Name").toString());
								output.put("Qte_lot", boissonbinder.get(pos).get("Qte_lot").toString());
								output.put("Qte_par_lot", boissonbinder.get(pos).get("Qte_par_lot").toString());
								output.put("Qtetotal", boissonbinder.get(pos).get("Qtetotal").toString());
								output.put("Qte_recommande",
										item.get("Qte_recommande").toString());
								output.put("Qte_achete", TV_Qtetotal.getText()
										.toString());
								output.put(
										"New_Qte",
										Integer.parseInt(ListQteAv.get(
												item.get("Name").toString())
												.toString())
												+ Integer.parseInt(TV_Qtetotal
														.getText().toString()));
								if (ET_Prix.getText().toString().trim()
										.equals("")) {
									output.put("Prix", boissonbinder.get(pos)
											.get("Prix").toString());
								} else {
									if (ET_Qteparlot.getText().toString().trim()
											.equals("")) {
										output.put("Prix", boissonbinder.get(pos)
												.get("Prix").toString());
									} else {
									float prixbase=(Float.parseFloat(ET_Prix
											.getText().toString())/Float.parseFloat(ET_Qteparlot.getText().toString()))*Integer.parseInt(boissonbinder.get(pos).get("Qte_par_lot").toString());
									output.put("Prix", prixbase);
									}
								}
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
		getMenuInflater().inflate(R.menu.course_menu, menu);

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
