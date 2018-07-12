package keker.footcoutiche;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Graph_boisson_Activity extends Activity  {

	 private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	  /** The main renderer that includes all the renderers customizing a chart. */
	  private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	  /** The most recently added series. */
	  private XYSeries mCurrentSeries;
	  /** The most recently created renderer, customizing the current series. */
	  private XYSeriesRenderer mCurrentRenderer;
	  /** Button for creating a new series of data. */
	  private Button B_ok;
	  /** Button for adding entered data to the current series. */
	  private Button mAdd;
	  /** Edit text field for entering the X value of the data to be added. */
	  private Spinner Sp_boisson;
	  /** Edit text field for entering the Y value of the data to be added. */
	  private Spinner Sp_info;
	  /** The chart view that displays the data. */
	  private GraphicalView mChartView;	
	  private List<String> listboisson=null;
	  private List<String> listinfo=null;
	  private ArrayAdapter Sp1adapter=null;
	  private ArrayAdapter Sp2adapter=null;
	  private List<HashMap<String, Object>> getBoisson=null;
	  private String Boisson=null;
	  private String Info=null;
	
	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.graph_boisson);
	    
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    DB_Helper dbCon = new DB_Helper(this);
	    // the top part of the UI components for adding new data points
	   B_ok = (Button) findViewById(R.id.B_ok);
	    Sp_boisson=(Spinner)findViewById(R.id.Sp_boisson);
	    getBoisson=new ArrayList<HashMap<String, Object>>(dbCon.get_boisson());
	    listboisson = new ArrayList<String>();
	    for(int i=0;i<=getBoisson.size()-1;i++){
	    listboisson.add(i, getBoisson.get(i).get("Name").toString());
	    }

		Sp1adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listboisson);
		Sp1adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Sp_boisson.setAdapter(Sp1adapter);

		Sp_boisson.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				Boisson=Sp1adapter.getItem(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		
		
	    Sp_info=(Spinner)findViewById(R.id.Sp_info);
	    
	    listinfo = new ArrayList<String>();
	    listinfo.add("Prix");
	    listinfo.add("Achat");
	    listinfo.add("Stock");
	    listinfo.add("Vente");
		Sp2adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listinfo);
		Sp2adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Sp_info.setAdapter(Sp2adapter);

		Sp_info.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				Info=Sp2adapter.getItem(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	    

	    // set some properties on the main renderer
	    mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setAxisTitleTextSize(16);
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(15);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setPointSize(5);

	   
	    B_ok.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View v) {
	        String seriesTitle = "Series " + Info +" "+Boisson;
	        // create a new series of data
	        XYSeries series = new XYSeries(seriesTitle);
	        mDataset.addSeries(series);
	        mCurrentSeries = series;
	        // create a new renderer for the new series
	        XYSeriesRenderer renderer = new XYSeriesRenderer();
	        mRenderer.addSeriesRenderer(renderer);
	        // set some renderer properties
	        renderer.setPointStyle(PointStyle.CIRCLE);
	       
	        renderer.setFillPoints(true);
	        renderer.setDisplayChartValues(true);
	        renderer.setDisplayChartValuesDistance(10);
	        mCurrentRenderer = renderer;
	        final long enjour=86400000L;
	        if(Info.equals("Prix")){
	        	 renderer.setColor(Color.BLUE);
	        	final long Date_min_l;
	        	DB_Helper dbCon1 = new DB_Helper(Graph_boisson_Activity.this);	
	        	List<HashMap<String, Object>> listprix= new ArrayList<HashMap<String, Object>>(dbCon1.getStatprix(Boisson));
	        	if (listprix.size()!=0){
	        	SimpleDateFormat dateFormat = new SimpleDateFormat(
	    				"yyyy-MM-dd hh:mm:ss");
	    		Date Date_min=new Date();
	    		
	    		try {
					Date_min=dateFormat.parse(listprix.get(0).get("Date").toString());
					Log.i("date min", String.valueOf(Date_min));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		Date_min_l=Date_min.getTime()/enjour;
	    		Date Date_max=new Date();
	    		try {
					Date_max=dateFormat.parse(listprix.get(listprix.size()-1).get("Date").toString());
					Log.i("date max", String.valueOf(Date_max));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		long temps;
	    		temps=Date_max.getTime()-Date_min.getTime();
	    		temps=temps/enjour;
	    		ArrayList<Float> Listprix_2=new ArrayList<Float>();
	    		
	    		for(int i=0;i<=listprix.size()-1;i++){
	    		Listprix_2.add(Float.parseFloat(listprix.get(i).get("Prix").toString()));	
	    		Date date=new Date();
	    		try {
					date=dateFormat.parse(listprix.get(i).get("Date").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		long date_long=(date.getTime()/enjour)-Date_min_l;
	    		mCurrentSeries.add(date_long, Double.parseDouble(listprix.get(i).get("Prix").toString()));
	    		}
	    		float prixmax=Collections.max(Listprix_2);
	        	mRenderer.setXAxisMax(temps);
	        	mRenderer.setXAxisMin(0);
	        	mRenderer.setYAxisMax(prixmax);
	        	mRenderer.setYAxisMin(0);
	        }
	        }
	        
	        if(Info.equals("Vente")){
	        	renderer.setColor(Color.GREEN);
	        	DB_Helper dbCon1 = new DB_Helper(Graph_boisson_Activity.this);	
	        	List<HashMap<String, Object>> listvente= new ArrayList<HashMap<String, Object>>(dbCon1.getStatvente(Boisson));
	        	if (listvente.size()!=0){
	        	SimpleDateFormat dateFormat = new SimpleDateFormat(
	    				"yyyy-MM-dd hh:mm:ss");
	    		Date Date_min=new Date();
	    		final Date Date_max=new Date();
	    		long Date_min_l=0L;	    		
	    		long datel=0L; 		

	    		ArrayList<Float> Listvente_2=new ArrayList<Float>();
	    		int vente=0;
	    		int demarrer=-1;
	    		final List<HashMap<String, Object>> listvente_2=new ArrayList<HashMap<String, Object>>();
	    		for(int i=0;i<=listvente.size()-1;i++){
	    		if (listvente.get(i).get("Type").toString().equals("inventaire") && demarrer==-1){
	    			
		    		try {
						Date_min=dateFormat.parse(listvente.get(0).get("Date").toString());
						Log.i("date min", String.valueOf(Date_min));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		Date_min_l=Date_min.getTime()/enjour;
		    		vente=Integer.parseInt(listvente.get(i).get("Qte").toString());
		    		demarrer=i;
	    		}
	    		if (demarrer!=-1 && i!=demarrer){
	    			if(listvente.get(i).get("Type").toString().equals("course")){
	    				vente=vente+Integer.parseInt(listvente.get(i).get("Qte").toString());	    				
	    			} else {
	    				vente=vente-Integer.parseInt(listvente.get(i).get("Qte").toString());	
	    			HashMap<String, Object> map=new HashMap<String, Object>();
	    			Date date=new Date();
		    		try {
						date=dateFormat.parse(listvente.get(i).get("Date").toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		datel=(date.getTime()/enjour)-Date_min_l;
	    			map.put("Date", datel);
	    			map.put("vente", vente);
	    			listvente_2.add(map);
	    			
	    			vente=Integer.parseInt(listvente.get(i).get("Qte").toString());	
	    			}
	    		}
	    		}
	    		
	    		long temps;
	    		temps=datel+Date_min_l-(Date_min.getTime()/enjour);
	    		List<Double> listvented=new ArrayList<Double>();
	    		for(int i=0;i<=listvente_2.size()-1;i++){
	    		mCurrentSeries.add(Double.parseDouble(listvente_2.get(i).get("Date").toString()), Double.parseDouble(listvente_2.get(i).get("vente").toString()));
	    		listvented.add( Double.parseDouble(listvente_2.get(i).get("vente").toString()));
	    		}
	    		Double ventemax=Collections.max(listvented);
	    		Double ventemin=Collections.min(listvented);
	        	mRenderer.setXAxisMax(temps);
	        	mRenderer.setXAxisMin(0);
	        	mRenderer.setYAxisMax(ventemax);
	        	mRenderer.setYAxisMin(ventemin);
	        	}
	        } 
	        
	        if(Info.equals("Stock")){
	        	renderer.setColor(Color.YELLOW);
	        	DB_Helper dbCon1 = new DB_Helper(Graph_boisson_Activity.this);	
	        	List<HashMap<String, Object>> liststock= new ArrayList<HashMap<String, Object>>(dbCon1.getStatstock(Boisson));
	        	if (liststock.size()!=0){
	        	SimpleDateFormat dateFormat = new SimpleDateFormat(
	    				"yyyy-MM-dd hh:mm:ss");
	   			List<Double> liststockd=new ArrayList<Double>();
	   			Date Date_min=new Date();
	   			Date Date_max=new Date();
	   			long Date_min_l=0L;
	   			long Date_max_l=0L;
	    		long datel=0L; 
	    		try {
	    		Date_min=dateFormat.parse(liststock.get(0).get("Date").toString());
	    		Date_max=dateFormat.parse(liststock.get(liststock.size()-1).get("Date").toString());
				Log.i("date min", String.valueOf(Date_min));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Date_min_l=Date_min.getTime()/enjour;
    		Date_max_l=Date_max.getTime()/enjour;
    		
	    		for(int i=0;i<=liststock.size()-1;i++){
	    			double stock=Double.parseDouble(liststock.get(i).get("Qte").toString());	 
	    			Date date=new Date();
		    		try {
						date=dateFormat.parse(liststock.get(i).get("Date").toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		datel=(date.getTime()/enjour)-Date_min_l;
		    		
		    		
	    		mCurrentSeries.add(datel, stock);
	    		liststockd.add(stock);
	    		}
	    		Double stockmax=Collections.max(liststockd);
	    		Double stockmin=Collections.min(liststockd);
	        	mRenderer.setXAxisMax(Date_max_l);
	        	mRenderer.setXAxisMin(0);
	        	mRenderer.setYAxisMax(stockmax);
	        	mRenderer.setYAxisMin(stockmin);
	        	
	        	}
	        }
	        
	        
	        if(Info.equals("Achat")){
	        	renderer.setColor(Color.RED);
	        	DB_Helper dbCon1 = new DB_Helper(Graph_boisson_Activity.this);	
	        	List<HashMap<String, Object>> listachat= new ArrayList<HashMap<String, Object>>(dbCon1.getStatachat(Boisson));
	   			if (listachat.size()!=0){
	        	SimpleDateFormat dateFormat = new SimpleDateFormat(
	    				"yyyy-MM-dd hh:mm:ss");
	   			List<Double> listachatd=new ArrayList<Double>();
	   			Date Date_min=new Date();
	   			Date Date_max=new Date();
	   			long Date_min_l=0L;
	   			long Date_max_l=0L;
	    		long datel=0L; 
	    		try {
	    		Date_min=dateFormat.parse(listachat.get(0).get("Date").toString());
	    		Date_max=dateFormat.parse(listachat.get(listachat.size()-1).get("Date").toString());
				Log.i("date min", String.valueOf(Date_min));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Date_min_l=Date_min.getTime()/enjour;
    		Date_max_l=Date_max.getTime()/enjour;
    		
	    		for(int i=0;i<=listachat.size()-1;i++){
	    			double achat=Double.parseDouble(listachat.get(i).get("Qte").toString());	 
	    			Date date=new Date();
		    		try {
						date=dateFormat.parse(listachat.get(i).get("Date").toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		datel=(date.getTime()/enjour)-Date_min_l;
		    		
		    		
	    		mCurrentSeries.add(datel, achat);
	    		listachatd.add(achat);
	    		}
	    		Double achatmax=Collections.max(listachatd);
	    		Double achatmin=Collections.min(listachatd);
	        	mRenderer.setXAxisMax(Date_max_l);
	        	mRenderer.setXAxisMin(0);
	        	mRenderer.setYAxisMax(achatmax);
	        	mRenderer.setYAxisMin(achatmin);
	   			}
	        	
	        }
	        
	        mChartView.repaint();
	      }
	    });	
	    
	 }
	 
	    @Override
	    protected void onResume() {
	      super.onResume();
	      if (mChartView == null) {
	        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	        mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
	        // enable the chart click events
	        mRenderer.setClickEnabled(true);
	        mRenderer.setSelectableBuffer(10);
	        mChartView.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	            // handle the click event on the chart
	            SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	            if (seriesSelection == null) {
	              Toast.makeText(Graph_boisson_Activity.this, "No chart element", Toast.LENGTH_SHORT).show();
	            } else {
	              // display information of the clicked point
	              Toast.makeText(
	                  Graph_boisson_Activity.this,
	                  "Chart element in series index " + seriesSelection.getSeriesIndex()
	                      + " data point index " + seriesSelection.getPointIndex() + " was clicked"
	                      + " closest point value X=" + seriesSelection.getXValue() + ", Y="
	                      + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
	            }
	          }
	        });
	        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
	            LayoutParams.FILL_PARENT));
	        boolean enabled = mDataset.getSeriesCount() > 0;
	      } else {
	        mChartView.repaint();
	      }
	    }

	    /**
	     * Enable or disable the add data to series widgets
	     * 
	     * @param enabled the enabled state
	     */
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

