package ntust.nwnc.homepage;

import java.util.ArrayList;

import ntust.nwnc.maproute.MapRouteDataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDataBase extends SQLiteOpenHelper {
	
	// Database Name
    public static final String DATABASE_NAME = "myNomaDB.db";
    // Database Version
    public static final int VERSION = 1;    
    // Table Name
    public static String TABLE_NAME = "myAlarmId";
    // database
    private static SQLiteDatabase database;
    // my value name
    public static final String ALARM_ID = "AlarmId";
    

	public AlarmDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public AlarmDataBase(Context context) { 
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

    public void createDataBaseTable() {
		final String SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
    			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			ALARM_ID + " INTEGER" +
    			")";
		database.execSQL(SQL);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
    			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			ALARM_ID + " INTEGER" +
    			")";
		database.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
	
	public ArrayList<Integer> getMyData() {
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	
    	Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null, null);
    	
    	while (cursor.moveToNext()) {
    		list.add(cursor.getInt(1));
    	}
    	 
    	return list;
    }
    
    public void insert(int id) {
    	 ContentValues cv = new ContentValues(); 

    	 cv.put(ALARM_ID, id);
    	 
    	 database.insert(TABLE_NAME, null, cv);
    	 cv.clear();
    }
    
    public void delete(int pos) {
    	Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null, null);
    	
    	cursor.moveToPosition(pos);
    	
    	String where = "_id" + "=" + cursor.getInt(0);
    	database.delete(TABLE_NAME, where , null);
    }
	   
    public void deleteId(int id) {
    	Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null, null);
 	
    	String where = ALARM_ID + "=" + id;
    	database.delete(TABLE_NAME, where , null);
    }
	

}
