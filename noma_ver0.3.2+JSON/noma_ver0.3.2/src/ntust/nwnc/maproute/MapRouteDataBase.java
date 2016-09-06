package ntust.nwnc.maproute;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MapRouteDataBase extends SQLiteOpenHelper {
    
    // Database Name
    public static final String DATABASE_NAME = "myNomaDB.db";
    // Database Version
    public static final int VERSION = 1;    
    // Table Name
    //public static String TABLE_NAME = "myMapRoute";
    // database
    private static SQLiteDatabase database;
    // my value name
    public static final String RouteDay = "RouteDay";
    public static final String RouteLoc = "RouteLoc";
    public static final String RouteTime = "RouteTime";
    
    public MapRouteDataBase(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }
    
    public MapRouteDataBase(Context context) { 
    	   this(context, DATABASE_NAME, null, VERSION); 
    	   database = getDatabase(context);
    } 

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MapRouteDataBase(context, DATABASE_NAME, 
                    null, VERSION).getReadableDatabase();
        }
 
        return database;
    }

    public void createDataBaseTable(String table) {
    	final String SQL = "CREATE TABLE IF NOT EXISTS " + table + "(" +
    			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			RouteDay + " TEXT," +
    			RouteLoc + " TEXT," +
    			RouteTime + " TEXT" +
    			")";
    			database.execSQL(SQL);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 
    	/*final String SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
    			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			RouteDay + " TEXT," +
    			RouteLoc + " TEXT," +
    			RouteTime + " TEXT" +
    			")";
    			db.execSQL(SQL);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*// 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // 
        onCreate(db);*/
    }
    
    public ArrayList<HashMap<String, Object>> getMyData(String table) {
    	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	
    	Cursor cursor = database.query(table, null, null, null, null, null, null, null);
    	
    	while (cursor.moveToNext()) {
    		map = new HashMap<String, Object>();
    		map.put(RouteDay, cursor.getString(1));
    		map.put(RouteLoc, cursor.getString(2));
    		map.put(RouteTime, cursor.getString(3));
    		list.add(map);
    	}
    	 
    	return list;
    }
    
    public void insert(String table, String day, String loc, String time) {
    	 ContentValues cv = new ContentValues(); 
    	 
    	 cv.put(RouteDay, day);
    	 cv.put(RouteLoc, loc);
    	 cv.put(RouteTime, time);
    	 
    	 database.insert(table, null, cv);
    	 cv.clear();
    }
    
    public void delete(String table, int pos) {
    	Cursor cursor = database.query(table, null, null, null, null, null, null, null);
    	
    	cursor.moveToPosition(pos);
    	
    	String where = "_id" + "=" + cursor.getInt(0);
    	database.delete(table, where , null);
    }
    
    
    
    

}



	
	    
