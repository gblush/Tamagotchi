
package com.redditandroiddevelopers.tamagotchi.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.redditandroiddevelopers.tamagotchi.mappers.CreatureMapper;
import com.redditandroiddevelopers.tamagotchi.model.Creature;

/**
 * The Data Access Object for the CREATURE_INFO database table.
 * 
 * @author Jeffrey Selk
 */
public class CreatureDao {
    private CreatureDatabase db;

    public CreatureDao(Context context) {
        db = new CreatureDatabase(context);
    }

    public Creature create(Creature creature) {
        ContentValues vals = creature.buildContentValues();
        if (creature.id <= 0) {
            vals.putNull("CI_ID");
        }
        long retId = db.insert(CreatureDatabase.INFO_TABLE_NAME, vals);
        creature.id = retId;
        return creature;
    }

    public Creature update(Creature creature) {
        db.update(CreatureDatabase.INFO_TABLE_NAME,
                creature.buildContentValues(), "CI_ID = " + creature.id, null);
        return creature;
    }

    public void delete(Creature creature) {
        db.delete(CreatureDatabase.INFO_TABLE_NAME, "CI_ID = " + creature.id,
                null);
    }

    public boolean isDatabaseEmpty() {
        Cursor c = db.query(CreatureDatabase.INFO_TABLE_NAME, null, null, null, null, null, null);
        if (c == null) {
            return true;
        }
        return false;
    }
    
    public void seedData(){
        // TODO: debug guard
        db.seedData();
    }
    
    public void purgeData() {
        // TODO: debug guard
        db.deleteDatabase();
    }

    public List<Creature> getAll() {
        Cursor c = db
                .query("CREATURE_INFO join CREATURE_TYPE on (CREATURE_INFO.CT_ID = CREATURE_TYPE.CT_ID)",
                        null, null, null, null, null, null);
        List<Creature> toReturn = new ArrayList<Creature>(c.getCount());
        while (!c.isAfterLast()) {
            toReturn.add(CreatureMapper.mapRow(c));
            c.moveToNext();
        }
        return toReturn;
    }
    public int getNumberOfAlive() {
        Cursor c = db
                .query(CreatureDatabase.INFO_TABLE_NAME,
                        null, "CI_ALIVE = 1", null, null, null, null);
        return c.getCount();
    }

    public Creature findById(long id) {
        Cursor c = db
                .query("CREATURE_INFO join CREATURE_TYPE on (CREATURE_INFO.CT_ID = CREATURE_TYPE.CT_ID)",
                        null, "CI_ID = " + id, null, null, null, null);
        Creature toReturn = CreatureMapper.mapRow(c);
        c.close();
        return toReturn;
    }
}