package com.example.tylerbwong.awaken.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tylerbwong.awaken.components.Connection;

import java.util.ArrayList;

/**
 * @author Tyler Wong
 */
public class ConnectionDatabaseHelper extends SQLiteOpenHelper {
   private final static String DATABASE_NAME = "Awaken.db";
   private final static String CONNECTIONS_TABLE = "Connections";
   private final static String NICKNAME_COL = "nickname";
   private final static String HOST_COL = "host";
   private final static String MAC_COL = "mac";
   private final static String WOL_PORT_COL = "portWol";
   private final static String DEV_PORT_COL = "portDev";
   private final static String CITY_COL = "city";
   private final static String STATE_COL = "state";
   private final static String COUNTRY_COL = "country";
   private final static String STATUS_COL = "status";
   private final static String DATE_COL = "lastWoken";
   private final static String CREATE_CONNECTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " +
         "Connections(nickname VARCHAR, host VARCHAR, mac VARCHAR, portWol VARCHAR, " +
         "portDev VARCHAR, city VARCHAR, state VARCHAR, country VARCHAR, status, VARCHAR, " +
         "lastWoken VARCHAR)";
   private final static String DROP_CONNECTIONS_TABLE = "DROP TABLE IF EXISTS Connections";
   private final static String QUERY_ALL_CONNECTIONS = "SELECT * FROM Connections";

   public ConnectionDatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_CONNECTIONS_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(DROP_CONNECTIONS_TABLE);
      onCreate(db);
   }

   public boolean insertConnection(String nickname, String host, String mac, String portWol,
                                   String portDev, String city, String state, String country,
                                   String status, String lastWoken) {
      SQLiteDatabase database = getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(NICKNAME_COL, nickname);
      contentValues.put(HOST_COL, host);
      contentValues.put(MAC_COL, mac);
      contentValues.put(WOL_PORT_COL, portWol);
      contentValues.put(DEV_PORT_COL, portDev);
      contentValues.put(CITY_COL, city);
      contentValues.put(STATE_COL, state);
      contentValues.put(COUNTRY_COL, country);
      contentValues.put(STATUS_COL, status);
      contentValues.put(DATE_COL, lastWoken);
      database.insert(CONNECTIONS_TABLE, null, contentValues);
      return true;
   }

   public void updateDate(String mac, String date) {
      SQLiteDatabase database = getWritableDatabase();
      String macFilter = MAC_COL + "=" + "'" + mac + "'";
      ContentValues contentValues = new ContentValues();
      contentValues.put(DATE_COL, date);
      database.update(CONNECTIONS_TABLE, contentValues, macFilter, null);
   }

   public void updateStatus(String mac, String status) {
      SQLiteDatabase database = getWritableDatabase();
      String macFilter = MAC_COL + "=" + "'" + mac + "'";
      ContentValues contentValues = new ContentValues();
      contentValues.put(STATUS_COL, status);
      database.update(CONNECTIONS_TABLE, contentValues, macFilter, null);
   }

   public ArrayList<Connection> getAllConnections() {
      ArrayList<Connection> connections = new ArrayList<>();
      Connection tempConnection;
      SQLiteDatabase database = getReadableDatabase();
      String nickname, host, mac, wolPort, devPort, city, state, country, status, date;
      Cursor cursor = database.rawQuery(QUERY_ALL_CONNECTIONS, null);
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         nickname = cursor.getString(cursor.getColumnIndex(NICKNAME_COL));
         host = cursor.getString(cursor.getColumnIndex(HOST_COL));
         mac = cursor.getString(cursor.getColumnIndex(MAC_COL));
         wolPort = cursor.getString(cursor.getColumnIndex(WOL_PORT_COL));
         devPort = cursor.getString(cursor.getColumnIndex(DEV_PORT_COL));
         city = cursor.getString(cursor.getColumnIndex(CITY_COL));
         state = cursor.getString(cursor.getColumnIndex(STATE_COL));
         country = cursor.getString(cursor.getColumnIndex(COUNTRY_COL));
         status = cursor.getString(cursor.getColumnIndex(STATUS_COL));
         date = cursor.getString(cursor.getColumnIndex(DATE_COL));
         tempConnection = new Connection(nickname, host, mac, wolPort, devPort,
               city, state, country, status, date);
         connections.add(tempConnection);
         cursor.moveToNext();
      }
      return connections;
   }
}
