package keker.footcoutiche;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

public class DB_Helper extends SQLiteOpenHelper {

	public int id;
	public String Boisson = null;
	public String Col_Name = null;
	public String Col_Prix = null;
	public Integer Qte = null;
	private final Context myContext;
	private static String DB_NAME = "dbboisson";
	private SQLiteDatabase db;
	private static final int DATABASE_VERSION = 1;
	private String DB_PATH = null;

	public DB_Helper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		DB_PATH = myContext.getDatabasePath(DB_NAME).getPath();
		try {
			createDataBase();
		} catch (IOException e) {

			throw new Error("Error copying database");

		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL(CREATE_TABLE_SERIES);
		// Toast.makeText(myContext, "La base est cr&#65533;&#65533;",
		// Toast.LENGTH_SHORT).show();

	}

	/**
	 * Ex&#65533;cut&#65533; chaque fois que le num&#65533;ro de version de DB
	 * change.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		// onCreate(db);
	}

	public String ajouter_boisson(String boisson, float prix, float prix_vente, int qtenom, int qteparlot) {
		String result;
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		Col_Name = boisson.replaceAll("[^\\w]", "");
		Col_Prix = boisson.replaceAll("[^\\w]", "") + "_prix";
		Cursor cursorResults = db.query("Boisson_Qte", null, null, null,
				null, null, null, null);
		int col_exist = -2;
		if (null != cursorResults && cursorResults.getCount()>0) {
			if (cursorResults.moveToFirst()) {
				do {
					col_exist = cursorResults.getColumnIndex("Col_Name");
				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		if (col_exist == -2) {
			Log.i("test_col_exist", "erreur cursor");
			result="Erreur curseur";
		} else {
			if (col_exist == -1) {
				try {
					db.beginTransaction();
					// Création de la nouvelle table, avec toute les
					// modifications
					db.execSQL("ALTER TABLE Boisson_Qte ADD COLUMN '"
							+ Col_Name + "' INTEGER DEFAULT '0'");
					db.execSQL("ALTER TABLE Boisson_Qte ADD COLUMN '"
							+ Col_Prix + "' REAL DEFAULT '0'");
					db.setTransactionSuccessful();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					db.endTransaction();
				}
			ContentValues values = new ContentValues();
			values.put("Name", boisson);
			values.put("Prix", prix);
			values.put("Prix_vente", prix_vente);
			values.put("Qte_nom", qtenom);
			values.put("Qte_par_lot", qteparlot);
			values.put("Last_Qte", 0);
			long t = db.insert("Boisson", null, values);
			Log.i("row new boisson", String.valueOf(t));
			result=boisson+" enregistré avec succès";
		}
			else {
				result="Erreur: "+boisson+" déjà existant";
		}
			
		}
		db.close();
		return result;
	}

	public boolean maj_boisson(HashMap<String, Object> boisson) {
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		Col_Name = boisson.get("Name").toString().replaceAll("[^\\w]", "");
		Col_Prix = boisson.get("Name").toString().replaceAll("[^\\w]", "") + "_prix";

			ContentValues values = new ContentValues();
			values.put("Prix", Float.parseFloat(boisson.get("Prix").toString()));
			values.put("Prix_vente", Float.parseFloat(boisson.get("Prix_vente").toString()));
			values.put("Qte_nom", Integer.parseInt(boisson.get("Qte_nom").toString()));
			values.put("Qte_par_lot", Integer.parseInt(boisson.get("Qte_par_lot").toString()));
			int t=db.update("Boisson", values, "Name='"+boisson.get("Name").toString()+"'", null);

		db.close();
		if(t>=1){
			return true;
		} else { return false; } 
	}
	
	
	
	
	public HashMap<String, Object> supp_boisson(String boisson) {
		HashMap<String, Object> output = new HashMap<String, Object>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		String[] colonnesARecup1 = new String[] { "Name" };
		Boolean boisson_exist = false;
		Cursor cursorResults1 = db.query("Boisson", colonnesARecup1, null,
				null, null, null, null, null);
		if (null != cursorResults1 && cursorResults1.getCount()>0) {
			if (cursorResults1.moveToFirst()) {
				do {
					if (cursorResults1.getString(
							cursorResults1.getColumnIndex("Name")).equals(
							boisson)) {
						boisson_exist = true;
					}
				} while (cursorResults1.moveToNext());
			} // end&#65533;if
		}
		cursorResults1.close();
		if (boisson_exist) {
			String[] colonnesARecup = new String[] { "Name", "Prix", "Last_Qte" };
			Cursor cursorResults = db.query("Boisson", colonnesARecup, "Name='"
					+ boisson + "'", null, null, null, "Name asc", null);
			if (null != cursorResults && cursorResults.getCount()>0) {
				if (cursorResults.moveToFirst()) {
					do {
						output.put("Name",
								cursorResults.getString(cursorResults
										.getColumnIndex("Name")));
						output.put("Last_Prix", cursorResults
								.getFloat(cursorResults.getColumnIndex("Prix")));
						output.put("Last_Qte", cursorResults
								.getInt(cursorResults
										.getColumnIndex("Last_Qte")));
					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.delete("Boisson", "Name='" + boisson + "'", null);
		} else {
			output.put("Result", boisson);
		}
		db.close();
		return output;
	}

	public List<HashMap<String, Object>> get_boisson() {
		List<HashMap<String, Object>> List_boisson = new ArrayList<HashMap<String, Object>>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		HashMap<String, Object> map;
		String[] colonnesARecup = new String[] { "Name", "Prix", "Last_Qte" };
		Cursor cursorResults = db.query("Boisson", colonnesARecup, null, null,
				null, null, "Name asc", null);
		if (null != cursorResults) {
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, Object>();
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					map.put("Prix", cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix")));
					map.put("Last_Qte", cursorResults.getInt(cursorResults
							.getColumnIndex("Last_Qte")));
					List_boisson.add(map);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return List_boisson;
	}

	public List<HashMap<String, Object>> get_course(){
		
		List<HashMap<String, Object>> List_boisson = new ArrayList<HashMap<String, Object>>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		HashMap<String, Object> map;
		String[] colonnesARecup = new String[] {"Name", "Prix", "Last_Qte", "Qte_nom", "Qte_par_lot"};
		Cursor cursorResults = db.query("Boisson", colonnesARecup, null, null,
				null, null, "Name asc", null);
		if (null != cursorResults && cursorResults.getCount()>0) { 
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, Object>();
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					map.put("Prix", cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix")));
					map.put("Last_Qte", cursorResults.getInt(cursorResults
							.getColumnIndex("Last_Qte")));
					map.put("Qte_nom", cursorResults.getInt(cursorResults
							.getColumnIndex("Qte_nom")));
					map.put("Qte_par_lot", cursorResults.getInt(cursorResults
							.getColumnIndex("Qte_par_lot")));
					List_boisson.add(map);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return List_boisson;
		
	}
	
	
	
	public List<HashMap<String, Object>> get_allboisson(){
		List<HashMap<String, Object>> List_boisson = new ArrayList<HashMap<String, Object>>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		HashMap<String, Object> map;
		String[] colonnesARecup = new String[] { "Name", "Prix", "Last_Qte", "Prix_vente", "Qte_nom", "Qte_par_lot" };
		Cursor cursorResults = db.query("Boisson", colonnesARecup, null, null,
				null, null, "Name asc", null);
		if (null != cursorResults) {
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, Object>();
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					map.put("Prix", cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix")));
					map.put("Last_Qte", cursorResults.getInt(cursorResults
							.getColumnIndex("Last_Qte")));
					map.put("Prix_vente", cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix_vente")));
					map.put("Qte_nom", cursorResults.getInt(cursorResults
							.getColumnIndex("Qte_nom")));
					map.put("Qte_par_lot", cursorResults.getInt(cursorResults
							.getColumnIndex("Qte_par_lot")));
					List_boisson.add(map);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return List_boisson;
		
		
	}
	
	
	public boolean save_inv(List<HashMap<String, Object>> List_inv) {
		boolean result;
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		String date = df.format(Calendar.getInstance().getTime());
		ContentValues values = new ContentValues();
		values.put("Date", date);
		values.put("Type", "inventaire");
		for (int i = 0; i <= List_inv.size() - 1; i++) {
			String Colprix = List_inv.get(i).get("Name").toString().replaceAll("[^\\w]", "") + "_prix";
			String[] colonnesARecup = new String[] { "Prix_vente" };
			float prix = 0f;
			Cursor cursorResults = db.query("Boisson", colonnesARecup, "Name='"
					+ List_inv.get(i).get("Name").toString() + "'", null, null,
					null, "Name asc", null);
			if (null != cursorResults) {
				if (cursorResults.moveToFirst()) {
					prix = cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix_vente"));
				} // end&#65533;if
			}
			cursorResults.close();
			values.put(List_inv.get(i).get("Name").toString().replaceAll("[^\\w]", ""), Integer
					.parseInt(List_inv.get(i).get("Last_Qte").toString()));
			values.put(Colprix, prix);
		}
		long a = db.insert("Boisson_Qte", null, values);
		Log.i("insert row", String.valueOf(a));
		if (a == -1) {
			result = false;
		} else {
			result = true;
		for (int i = 0; i <= List_inv.size() - 1; i++) {
			ContentValues values2 = new ContentValues();
			String boisson = List_inv.get(i).get("Name").toString();
			values2.put("Last_Qte", Integer.parseInt(List_inv.get(i)
					.get("Last_Qte").toString()));
			db.update("Boisson", values2, "Name ='" + boisson + "'", null);
		}
	}
		db.close();
		return result;
	}

	public boolean save_course(List<HashMap<String, Object>> List_course) {
		boolean result;

		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		String date = df.format(Calendar.getInstance().getTime());
		ContentValues values = new ContentValues();
		values.put("Date", date);
		values.put("Type", "course");
		for (int i = 0; i <= List_course.size() - 1; i++) {
			String Colprix = List_course.get(i).get("Name").toString().replaceAll("[^\\w]", "")
					+ "_prix";
			float prix = Float.parseFloat(List_course.get(i).get("Prix")
					.toString());
			values.put(
					List_course.get(i).get("Name").toString().replaceAll("[^\\w]", ""),
					Integer.parseInt(List_course.get(i).get("Qte_achete")
							.toString()));
			values.put(Colprix, prix);
		}
		long a = db.insert("Boisson_Qte", null, values);
		Log.i("insert row", String.valueOf(a));
		if (a == -1) {
			result = false;
		} else {
			result = true;
		for (int i = 0; i <= List_course.size() - 1; i++) {
			ContentValues values2 = new ContentValues();
			String boisson = List_course.get(i).get("Name").toString();
			values2.put(
					"Last_Qte",
					Integer.parseInt(List_course.get(i).get("New_Qte")
							.toString()));
			values2.put(
					"Prix",
					Float.parseFloat(List_course.get(i).get("Prix")
							.toString()));
			db.update("Boisson", values2, "Name ='" + boisson + "'", null);
		}
		}
		db.close();
		return result;
	}

	public List<String> get_invs() {
		List<String> List_invs = new ArrayList<String>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		String map;
		String[] colonnesARecup = new String[] { "Date" };
		Cursor cursorResults = db.query("Boisson_Qte", colonnesARecup,
				"Type='inventaire'", null, null, null, "Date", null);
		if (null != cursorResults && cursorResults.getCount()>0) { 
			if (cursorResults.moveToFirst()) {
				do {
					map = cursorResults.getString(cursorResults
							.getColumnIndex("Date"));
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
					Date date=new Date();
					try {
						date=df.parse(map);
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
					String dates = df2.format(date);
					List_invs.add(dates);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return List_invs;
	}

	public List<HashMap<String, Object>> get_Stat(String inv1, String inv2) throws java.text.ParseException {
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
		Date Date_inv1=new Date();
		Date_inv1=df2.parse(inv1);
		Date Date_inv2=new Date();
		Date_inv2=df2.parse(inv2);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String Inv1=dateFormat.format(Date_inv1);
		String Inv2=dateFormat.format(Date_inv2);
		
	
		List<HashMap<String, Object>> output = new ArrayList<HashMap<String, Object>>();
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		
		List<HashMap<String, Object>> listboisson = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> boisson;
		String[] colonnesARecup = new String[] {"Name", "Prix_vente", "Qte_par_lot"};
		Cursor cursorResults = db.query("Boisson", colonnesARecup, null, null,
				null, null, "Name asc", null);
		if (null != cursorResults && cursorResults.getCount()>0) { 
			if (cursorResults.moveToFirst()) {
				do {
					boisson = new HashMap<String, Object>();
					boisson.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					boisson.put("Prix_vente", cursorResults.getFloat(cursorResults
							.getColumnIndex("Prix_vente")));
					boisson.put("Qte_par_lot", cursorResults.getInt(cursorResults
							.getColumnIndex("Qte_par_lot")));
					listboisson.add(boisson);
				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		
		HashMap<String, Object> statinv1=new HashMap<String, Object>();
		String[] colonnesARecup1 = new String[2*listboisson.size()];
		for (int i=0; i<=listboisson.size()-1;i++){			
			colonnesARecup1[2*i]=listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "");
			colonnesARecup1[2*i+1]=listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")+"_prix";
		}
		Cursor cursorResults1 = db.query("Boisson_Qte", colonnesARecup1, "Date='"+Inv1+"' AND Type='inventaire'", null,
				null, null, null, null);
		int b=colonnesARecup1.length;
		if (null != cursorResults1 && cursorResults1.getCount()>0) { 
			cursorResults1.moveToFirst();
			for(int i=0; i<=colonnesARecup1.length-1;i++){
				String colname=colonnesARecup1[i];
				int c=cursorResults1.getColumnIndex(colname);				
				if ((i % 2) == 0) 
				{ 
					int qt=cursorResults1.getInt(cursorResults1.getColumnIndex(colname));
					statinv1.put(colname, qt);
				} 
				else 
				{ 
					float px=cursorResults1.getFloat(c);
					statinv1.put(colname, px);
				}

			} // end&#65533;if
		}
		cursorResults1.close();

		HashMap<String, Object> statinv2=new HashMap<String, Object>();
		Cursor cursorResults2 = db.query("Boisson_Qte", colonnesARecup1, "Date='"+Inv2+"' AND Type='inventaire'", null,
				null, null, null, null);
		
		if (null != cursorResults2 && cursorResults2.getCount()>0) { 
			cursorResults2.moveToFirst();
			for(int i=0; i<=colonnesARecup1.length-1;i++){
				if ((i % 2) == 0) 
				{ 
					statinv2.put(colonnesARecup1[i], cursorResults2.getInt(cursorResults2
							.getColumnIndex(colonnesARecup1[i])));
				} 
				else 
				{ 
					statinv2.put(colonnesARecup1[i], cursorResults2.getFloat(cursorResults2
							.getColumnIndex(colonnesARecup1[i])));
				}

			} // end&#65533;if
		}
		cursorResults2.close();
		
		
		HashMap<String, Object> statcourse=null;
		List<HashMap<String, Object>> listcourse=new ArrayList<HashMap<String, Object>>();
		Date converteddatecourse=null;
		String[] colonnesARecup2 = new String[2*listboisson.size()+1];
		for (int i=0; i<=listboisson.size()-1;i++){			
			colonnesARecup2[2*i]=listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "");
			colonnesARecup2[2*i+1]=listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")+"_prix";
		}
		colonnesARecup2[2*listboisson.size()]="Date";
		Cursor cursorResults3 = db.query("Boisson_Qte", colonnesARecup2, "Type='course'", null,
				null, null, null, null);
		
		if (null != cursorResults3 && cursorResults3.getCount()>0) { 
			if (cursorResults3.moveToFirst()) {
				do {
					converteddatecourse = new Date();
					String datecourse=cursorResults3.getString(cursorResults3.getColumnIndex("Date"));
					converteddatecourse = dateFormat.parse(datecourse);
					if (converteddatecourse.after(Date_inv1) && converteddatecourse.before(Date_inv2)){
						statcourse=new HashMap<String, Object>();
						for(int i=0; i<=colonnesARecup1.length-1;i++){
							if ((i % 2) == 0) 
							{ 
								statcourse.put(colonnesARecup2[i], cursorResults3.getInt(cursorResults3
										.getColumnIndex(colonnesARecup2[i])));
							} 
							else 
							{ 
								statcourse.put(colonnesARecup2[i], cursorResults3.getFloat(cursorResults3
										.getColumnIndex(colonnesARecup2[i])));
							}
								
						} // end&#65533;if
						listcourse.add(statcourse);
					}
				} while (cursorResults3.moveToNext());
			} // end&#65533;if
		}
		cursorResults3.close();
		
		
		
		for (int i=0; i<=listboisson.size()-1;i++){			
			float prodcourse=0;
			List<Integer> qte= new ArrayList<Integer>();
			List<Float> prix=new ArrayList<Float>();
			HashMap<String, Object> statboisson=new HashMap<String, Object>();
			statboisson.put("Name", listboisson.get(i).get("Name").toString());
			int sommecourse=0;
			int conso=0;
			float vente=0;
			DecimalFormat df = new DecimalFormat ( ) ;
			df.setMaximumFractionDigits ( 2 ) ;
			
			for(int a=0; a<=listcourse.size()-1;a++){
			qte.add(Integer.parseInt(listcourse.get(a).get(listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")).toString()));
			sommecourse=sommecourse+Integer.parseInt(listcourse.get(a).get(listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")).toString());	
					prix.add(Float.parseFloat(listcourse.get(a).get(listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")+"_prix").toString()));
			}
			// creer le prodcourse en faisant le produit des listes
			for(int a=0; a<=qte.size()-1;a++){
				prodcourse=prodcourse+(qte.get(a)*(prix.get(a)/Integer.parseInt(listboisson.get(i).get("Qte_par_lot").toString())));
			}
			statboisson.put("SommeAchat", sommecourse);
			statboisson.put("CoutAchat", df.format(prodcourse));
			
			conso=sommecourse+Integer.parseInt(statinv1.get(listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")).toString())-Integer.parseInt(statinv2.get(listboisson.get(i).get("Name").toString().replaceAll("[^\\w]", "")).toString());
			vente=conso*Float.parseFloat(listboisson.get(i).get("Prix_vente").toString());
			
			statboisson.put("SommeVente", conso);
			statboisson.put("Vente", df.format(vente));
			
			output.add(statboisson);
		}
		
		
		db.close();

		return output;
	}


	public List<HashMap<String, Object>> getStatprix(String boisson) {
		 List<HashMap<String, Object>> listprix=new  ArrayList<HashMap<String, Object>>();
			Col_Name = boisson.replaceAll("[^\\w]", "");
			Col_Prix = boisson.replaceAll("[^\\w]", "") + "_prix";
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			String[] colonnesARecup = new String[] { "Date", Col_Prix };
			Cursor cursorResults = db.query("Boisson_Qte", colonnesARecup,
					null , null, null, null, "Date", null);
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("Date", cursorResults.getString(cursorResults
								.getColumnIndex("Date")));
						map.put("Prix", cursorResults.getFloat(cursorResults
								.getColumnIndex(Col_Prix)));
						listprix.add(map);

					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		 
		 
		return listprix;
	}

	
	public List<HashMap<String, Object>> getStatvente(String boisson) {
		 List<HashMap<String, Object>> listprix=new  ArrayList<HashMap<String, Object>>();
			Col_Name = boisson.replaceAll("[^\\w]", "");
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			String[] colonnesARecup = new String[] { "Date", "Type", Col_Name };
			Cursor cursorResults = db.query("Boisson_Qte", colonnesARecup,
					null , null, null, null, "Date", null);
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("Date", cursorResults.getString(cursorResults
								.getColumnIndex("Date")));
						map.put("Type", cursorResults.getString(cursorResults
								.getColumnIndex("Type")));
						map.put("Qte", cursorResults.getString(cursorResults
								.getColumnIndex(Col_Name)));
						listprix.add(map);

					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		 
		 
		return listprix;
	}
	
	public List<HashMap<String, Object>> getStatstock(String boisson) {
		 List<HashMap<String, Object>> liststock=new  ArrayList<HashMap<String, Object>>();
			Col_Name = boisson.replaceAll("[^\\w]", "");
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			String[] colonnesARecup = new String[] { "Date", Col_Name };
			Cursor cursorResults = db.query("Boisson_Qte", colonnesARecup,
					"Type='inventaire'" , null, null, null, "Date", null);
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("Date", cursorResults.getString(cursorResults
								.getColumnIndex("Date")));
						map.put("Qte", cursorResults.getString(cursorResults
								.getColumnIndex(Col_Name)));
						liststock.add(map);
					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		 
		 
		return liststock;
	}
	
	
	public List<HashMap<String, Object>> getStatachat(String boisson) {
		 List<HashMap<String, Object>> listachat=new  ArrayList<HashMap<String, Object>>();
			Col_Name = boisson.replaceAll("[^\\w]", "");
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			String[] colonnesARecup = new String[] { "Date", Col_Name };
			Cursor cursorResults = db.query("Boisson_Qte", colonnesARecup,
					"Type='course'" , null, null, null, "Date", null);
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("Date", cursorResults.getString(cursorResults
								.getColumnIndex("Date")));
						map.put("Qte", cursorResults.getString(cursorResults
								.getColumnIndex(Col_Name)));
						listachat.add(map);
					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		 
		 
		return listachat;
	}
	
	
	public List<HashMap<String, Object>> get_arch(String choix) {
		String[] choixstring=new String[] { choix };
		 List<HashMap<String, Object>> list=new  ArrayList<HashMap<String, Object>>();
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			String[] colonnesARecup = new String[] { "Date", "Type"};
			Cursor cursorResults; 
			if (choix.equals("Les deux")){
			cursorResults = db.query("Boisson_Qte", colonnesARecup,
						null, null, null, null, "Date", null);
			} else {
			cursorResults = db.query("Boisson_Qte", colonnesARecup,
					"Type = ?" , choixstring, null, null, "Date", null);
			}
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("Date", cursorResults.getString(cursorResults
								.getColumnIndex("Date")));
						map.put("Type", cursorResults.getString(cursorResults
								.getColumnIndex("Type")));
						list.add(map);
					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		return list;
	}
	
	public List<HashMap<String, Object>> get_archive(String date) {

		 List<HashMap<String, Object>> list=new  ArrayList<HashMap<String, Object>>();
		 String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			
			List<String> listboisson = new ArrayList<String>();
			HashMap<String, Object> boisson;
			String[] colonnesARecup = new String[] {"Name"};
			Cursor cursorResults = db.query("Boisson", colonnesARecup, null, null,
					null, null, "Name asc", null);
			if (null != cursorResults && cursorResults.getCount()>0) { 
				if (cursorResults.moveToFirst()) {
					do {
					
					listboisson.add(cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					
					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			
			String[] colonnesARecup1 = new String[listboisson.size()];
			for (int i=0; i<=listboisson.size()-1;i++){			
				colonnesARecup1[i]=listboisson.get(i).toString().replaceAll("[^\\w]", "");
			}

			Cursor cursorResults1 = db.query("Boisson_Qte", colonnesARecup1,
					"Date='"+date+"'" , null, null, null, null, null);
			if (null != cursorResults1 && cursorResults1.getCount()>0) { 
				if (cursorResults1.moveToFirst()) {
					do {
						for(int i=0; i<=colonnesARecup1.length-1;i++){
							HashMap<String, Object> map=new HashMap<String, Object>();
							String colname=colonnesARecup1[i];
							int c=cursorResults1.getColumnIndex(colname);				
								int qt=cursorResults1.getInt(cursorResults1.getColumnIndex(colname));
								map.put("Name", listboisson.get(i).toString());
								map.put("Qte", qt);

							list.add(map);
						} // end&#65533;if

					} while (cursorResults.moveToNext());
				} // end&#65533;if
			}
			cursorResults.close();
			db.close();	
		return list;
	}
	
	
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

}
