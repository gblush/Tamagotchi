package com.redditandroiddevelopers.tamagotchi.util;

import android.content.ContentValues;

import com.redditandroiddevelopers.tamagotchi.model.Creature;
import com.redditandroiddevelopers.tamagotchi.model.CreatureEvolution;
import com.redditandroiddevelopers.tamagotchi.model.CreatureRaiseType;
import com.redditandroiddevelopers.tamagotchi.model.CreatureState;
import com.redditandroiddevelopers.tamagotchi.model.CreatureType;
import com.redditandroiddevelopers.tamagotchi.model.Experience;
import com.redditandroiddevelopers.tamagotchi.model.Medicine;
import com.redditandroiddevelopers.tamagotchi.model.Sickness;

public class ContentValueUtils {

    public static <T> ContentValues buildContentValues(T modelObj) {
        // this is ugly, a refactor would be pleasant
        if (modelObj instanceof Creature) {
            return buildCreature((Creature)modelObj);
        } else if (modelObj instanceof CreatureEvolution) {
            return buildCreatureEvolution((CreatureEvolution)modelObj);
        } else if (modelObj instanceof CreatureRaiseType) {
            return buildCreatureRaiseType((CreatureRaiseType)modelObj);
        } else if (modelObj instanceof CreatureState) {
            return buildCreatureState((CreatureState)modelObj);
        } else if (modelObj instanceof CreatureType) {
            return buildCreatureType((CreatureType)modelObj);
        } else if (modelObj instanceof Medicine) {
            return buildMedicine((Medicine)modelObj);
        } else if (modelObj instanceof Sickness) {
            return buildSickness((Sickness)modelObj);
        } else if (modelObj instanceof Experience) {
            return buildExperience((Experience)modelObj);
        } else {
            throw new IllegalArgumentException(
                    "Couldn't build content value map for class "
                            + modelObj.getClass()
                            + ". Did you forget code in ContentValueUtils?");
        }
    }
    
    public static ContentValues buildExperience(Experience modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("E_ID", modelObj.id);
        toReturn.put("CT_ID", modelObj.type.id);
        toReturn.put("E_MIN_XP", modelObj.minXp);
        toReturn.put("E_MAX_XP", modelObj.maxXp);
        return toReturn;
    }

    private static ContentValues buildSickness(Sickness modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("S_ID", modelObj.id);
        toReturn.put("M_ID", modelObj.medicine.id);
        toReturn.put("S_NAME", modelObj.name);
        return toReturn;
    }

    private static ContentValues buildMedicine(Medicine modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("M_ID", modelObj.id);
        toReturn.put("M_NAME", modelObj.name);
        return toReturn;
    }

    private static ContentValues buildCreatureType(CreatureType modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("CT_ID", modelObj.id);
        toReturn.put("CT_NAME", modelObj.name);
        return toReturn;
    }

    private static ContentValues buildCreatureState(CreatureState modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("CS_ID", modelObj.id);
        toReturn.put("CI_ID", (modelObj.creature == null) ? 0 : modelObj.creature.id);
        toReturn.put("CRT_ID", (modelObj.raiseType == null) ? 0 : modelObj.raiseType.id);
        toReturn.put("S_ID", (modelObj.sickness == null) ? 0 : modelObj.sickness.id);
        toReturn.put("CS_HEALTH", modelObj.health);
        toReturn.put("CS_BOWEL", modelObj.bowel);
        toReturn.put("CS_DISCIPLINE", modelObj.discipline);
        toReturn.put("CS_HUNGER", modelObj.hunger);
        toReturn.put("CS_HAPPY", modelObj.happy);
        toReturn.put("CS_SICK", modelObj.sick);
        toReturn.put("CS_EXPERIENCE", modelObj.experience);
        return toReturn;
    }

    private static ContentValues buildCreatureRaiseType(CreatureRaiseType modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("CRT_ID", modelObj.id);
        toReturn.put("CRT_NAME", modelObj.name);
        toReturn.put("CRT_MULTIPLIER", modelObj.multiplier);
        return toReturn;
    }

    private static ContentValues buildCreatureEvolution(CreatureEvolution modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("CE_ID", modelObj.id);
        toReturn.put("CT_ID", modelObj.type.id);
        toReturn.put("CE_NAME", modelObj.name);
        toReturn.put("CE_MAX_HEALTH", modelObj.maxHealth);
        toReturn.put("CE_MAX_BOWEL", modelObj.maxBowel);
        toReturn.put("CE_MAX_DISCIPLINE", modelObj.maxDiscipline);
        toReturn.put("CE_MAX_HUNGER", modelObj.maxHunger);
        toReturn.put("CE_MAX_HAPPY", modelObj.maxHappy);
        toReturn.put("CE_MAX_EXPERIENCE", modelObj.maxExperience);
        return toReturn;
    }

    private static ContentValues buildCreature(Creature modelObj) {
        ContentValues toReturn = new ContentValues();
        toReturn.put("CI_ID", modelObj.id);
        toReturn.put("CI_NAME", modelObj.name);
        toReturn.put("CT_ID", modelObj.type.id);
        toReturn.put("CI_BIRTH_DATE", (modelObj.birthDate != null) ? modelObj.birthDate.getTime()
                : null);
        toReturn.put("CI_DEATH_DATE", (modelObj.deathDate != null) ? modelObj.deathDate.getTime()
                : null);
        toReturn.put("CI_ALIVE", modelObj.alive);
        toReturn.put("CI_GENDER", modelObj.gender.getDbValue());
        return toReturn;
    }
}
