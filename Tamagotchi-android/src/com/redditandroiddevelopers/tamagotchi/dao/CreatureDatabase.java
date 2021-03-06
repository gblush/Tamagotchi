package com.redditandroiddevelopers.tamagotchi.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.redditandroiddevelopers.tamagotchi.mappers.Mapper;
import com.redditandroiddevelopers.tamagotchi.model.CommonModel;
import com.redditandroiddevelopers.tamagotchi.util.ContentValueUtils;

public class CreatureDatabase<T extends CommonModel> implements
        CommonDatabase<T> {

    public static final String INFO_TABLE_NAME = "CREATURE_INFO";
    public static final String STATE_TABLE_NAME = "CREATURE_STATE";
    public static final String EVOLUTION_TABLE_NAME = "CREATURE_EVOLUTION";
    public static final String TYPE_TABLE_NAME = "CREATURE_TYPE";
    public static final String RAISE_TYPE_TABLE_NAME = "CREATURE_RAISE_TYPE";
    public static final String MEDICINE_TABLE_NAME = "MEDICINE";
    public static final String SICKNESS_TABLE_NAME = "SICKNESS";
    public static final String EXPERIENCE_TABLE_NAME = "EXPERIENCE";

    private final DatabaseHelper databaseHelper;

    public CreatureDatabase(Context context) {
        databaseHelper = new DatabaseHelper(context);
        // TODO: remove this call to getWritableDatabase before production
        databaseHelper.getWritableDatabase();
        //seedData();
    }

    public T queryUnique(Mapper<T> rowMapper, String table,
            String[] projectionIn, String selection, String[] selectionArgs,
            String groupBy, String having, String sortOrder) {
        Cursor c = query(table, projectionIn, selection, selectionArgs,
                groupBy, having, sortOrder);
        T toReturn = rowMapper.mapRow(c);
        c.close();
        return toReturn;
    }

    public List<T> queryList(Mapper<T> rowMapper, String table,
            String[] projectionIn, String selection, String[] selectionArgs,
            String groupBy, String having, String sortOrder) {
        Cursor c = query(table, projectionIn, selection, selectionArgs,
                groupBy, having, sortOrder);
        List<T> toReturn = new ArrayList<T>(c.getCount());
        while (!c.isAfterLast()) {
            toReturn.add(rowMapper.mapRow(c));
            c.moveToNext();
        }
        c.close();
        return toReturn;
    }

    public int getResultCount(String table, String[] projectionIn,
            String selection, String[] selectionArgs, String groupBy,
            String having, String sortOrder) {
        return query(table, projectionIn, selection, selectionArgs, groupBy,
                having, sortOrder).getCount();
    }

    private Cursor query(String table, String[] projectionIn, String selection,
            String[] selectionArgs, String groupBy, String having,
            String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);
        Cursor c = builder.query(databaseHelper.getReadableDatabase(),
                projectionIn, selection, selectionArgs, groupBy, having,
                sortOrder);
        if (c == null) {
            return null;
        } else if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        return c;
    }

    public T insert(T toCreate, String table) {
        long res = databaseHelper.getWritableDatabase().insert(table, null,
                ContentValueUtils.buildContentValues(toCreate));
        toCreate.id = res;
        return toCreate;
    }

    public void update(String table, T toUpdate, String whereClause,
            String[] whereArgs) {
        databaseHelper.getWritableDatabase().update(table,
                ContentValueUtils.buildContentValues(toUpdate), whereClause, whereArgs);
    }

    public void delete(String table, String whereClause, String[] whereArgs) {
        databaseHelper.getWritableDatabase().delete(table, whereClause,
                whereArgs);
    }

    /**
     * For testing purposes during development!
     */
    public void deleteDatabase() {
        // TODO: debug guard
        databaseHelper.deleteDatabase();
    }

    /**
     * This is a basic method for use by developers during development.
     */
    public void seedData() {
        // TODO: This needs to be set up with the actual data for the
        // game
        // seed data for constants, i.e. medicine, sickness, etc.
        // TODO: debug guard
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues vals;
        
        for (int i = 0; i < 5; i++) {
            vals = new ContentValues();
            vals.put("M_ID", i);
            vals.put("M_NAME", "MEDICINE" + i);
            db.insert(MEDICINE_TABLE_NAME, null, vals);
        }

        for (int i = 0; i < 5; i++) {
            vals = new ContentValues();
            vals.put("S_ID", i);
            vals.put("M_ID", i);
            vals.put("S_NAME", "SICKNESS" + i);
            db.insert(SICKNESS_TABLE_NAME, null, vals);
        }

        vals = new ContentValues();
        vals.put("CRT_ID", 1);
        vals.put("CRT_NAME", "HEALTHY");
        vals.put("CRT_MULTIPLIER", "1.00");
        db.insert(RAISE_TYPE_TABLE_NAME, null, vals);

        vals = new ContentValues();
        vals.put("CT_ID", 1);
        vals.put("CT_NAME", "DEFAULT");
        db.insert(TYPE_TABLE_NAME, null, vals);

        vals = new ContentValues();
        vals.put("CE_ID", 1);
        vals.put("CT_ID", 1);
        vals.put("CE_NAME", "Name");
        vals.put("CE_MAX_HEALTH", 100);
        vals.put("CE_MAX_BOWEL", 100);
        vals.put("CE_MAX_DISCIPLINE", 100);
        vals.put("CE_MAX_HUNGER", 100);
        vals.put("CE_MAX_HAPPY", 100);
        // The min and max needs to be replaced based on creature type
        vals.put("CE_MAX_EXPERIENCE", 70000 + (int)(Math.random()*((70000 + 120000) + 1)));
        db.insert(EVOLUTION_TABLE_NAME, null, vals);
        
        vals = new ContentValues();
        vals.put("E_ID", 1);
        vals.put("CT_ID", 1);
        vals.put("E_MIN_XP", 70000);
        vals.put("E_MAX_XP", 120000);
        db.insert(EXPERIENCE_TABLE_NAME, null, vals);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "RAD_CREATURES.db";
        private static final int DB_VERSION = 2;

        private static final String CREATE_INFO = "" + "CREATE TABLE "
                + INFO_TABLE_NAME + " (" + "CI_ID INTEGER PRIMARY KEY, "
                + "CT_ID INTEGER, " + "CE_ID INTEGER, "
                + "CI_NAME VARCHAR2(50), " + "CI_BIRTH_DATE INTEGER, "
                + "CI_DEATH_DATE INTEGER, " + "CI_ALIVE BOOLEAN, "
                + "CI_GENDER BOOLEAN, "
                + "FOREIGN KEY(CT_ID) REFERENCES " +  TYPE_TABLE_NAME + "(CT_ID), "
                + "FOREIGN KEY(CE_ID) REFERENCES " + EVOLUTION_TABLE_NAME + "(CE_ID) "
                + ");";
        private static final String CREATE_STATE = ""
                + "CREATE TABLE "
                + STATE_TABLE_NAME
                + " ("
                + "CS_ID INTEGER PRIMARY KEY, "
                + "CI_ID INTEGER, "
                + "CRT_ID INTEGER, "
                + "S_ID INTEGER, "
                + "CS_HEALTH INTEGER, "
                + "CS_BOWEL INTEGER, "
                + "CS_DISCIPLINE INTEGER, "
                + "CS_HUNGER INTEGER, "
                + "CS_HAPPY INTEGER, "
                + "CS_SICK BOOLEAN, "
                + "CS_EXPERIENCE INTEGER, "
                + "FOREIGN KEY(CI_ID) REFERENCES " + INFO_TABLE_NAME + "(CI_ID), "
                + "FOREIGN KEY(CRT_ID) REFERENCES " + RAISE_TYPE_TABLE_NAME + "(CRT_ID), "
                + "FOREIGN KEY(S_ID) REFERENCES " + SICKNESS_TABLE_NAME + "(S_ID) " + ");";
        private static final String CREATE_EVOLUTION = "" + "CREATE TABLE "
                + EVOLUTION_TABLE_NAME + " (" + "CE_ID INTEGER PRIMARY KEY, "
                + "CT_ID INTEGER, " + "CE_NAME VARCHAR2(100), "
                + "CE_MAX_HEALTH INTEGER, " + "CE_MAX_BOWEL INTEGER, "
                + "CE_MAX_DISCIPLINE INTEGER, " + "CE_MAX_HUNGER INTEGER, "
                + "CE_MAX_HAPPY INTEGER, " + "CE_MAX_EXPERIENCE INTEGER, "
                + "FOREIGN KEY(CT_ID) REFERENCES " + TYPE_TABLE_NAME + "(CT_ID) " + ");";
        private static final String CREATE_CREATURE_TYPE = "" + "CREATE TABLE "
                + TYPE_TABLE_NAME + " (" + "CT_ID INTEGER PRIMARY KEY, "
                + "CT_NAME VARCHAR2(100) " + ");";
        private static final String CREATE_CREATURE_RAISE_STATE = ""
                + "CREATE TABLE " + RAISE_TYPE_TABLE_NAME + " ("
                + "CRT_ID INTEGER PRIMARY KEY, " + "CRT_NAME TEXT, "
                + "CRT_MULTIPLIER REAL " + ");";
        private static final String CREATE_MEDICINE = "" + "CREATE TABLE "
                + MEDICINE_TABLE_NAME + " (" + "M_ID INTEGER PRIMARY KEY, "
                + "M_NAME TEXT " + ");";
        private static final String CREATE_SICKNESS = "" + "CREATE TABLE "
                + SICKNESS_TABLE_NAME + " (" + "S_ID INTEGER PRIMARY KEY, "
                + "M_ID INTEGER, " + "S_NAME TEXT, "
                + "FOREIGN KEY (M_ID) REFERENCES " + MEDICINE_TABLE_NAME + "(M_ID) " + ");";
        private static final String CREATE_EXPERIENCE = "" + "CREATE TABLE "
                + EXPERIENCE_TABLE_NAME + " (" + "E_ID INTEGER PRIMARY KEY, "
                + "CT_ID INTEGER, " + "E_MIN_XP INTEGER, " + "E_MAX_XP INTEGER, "
                + "FOREIGN KEY (CT_ID) REFERENCES " + TYPE_TABLE_NAME + "(CT_ID)" + ");";

        private Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MEDICINE);
            db.execSQL(CREATE_SICKNESS);
            db.execSQL(CREATE_CREATURE_RAISE_STATE);
            db.execSQL(CREATE_CREATURE_TYPE);
            db.execSQL(CREATE_INFO);
            db.execSQL(CREATE_STATE);
            db.execSQL(CREATE_EVOLUTION);
            db.execSQL(CREATE_EXPERIENCE);
            // turn on foreign keys
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void deleteDatabase() {
            mContext.deleteDatabase(DB_NAME);
        }

    }
}
