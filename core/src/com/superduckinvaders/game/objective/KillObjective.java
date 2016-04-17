package com.superduckinvaders.game.objective;

import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.mob.Mob;

import java.util.ArrayList;

/**
 * Represents an enemy defeating objective.
 */
public class KillObjective extends Objective {

    /**
     * A list of targets the player must kill before progressing.
     */
    private ArrayList<Mob> targets;

    /**
     * The objective's provided description.
     */
    private String description;

    /**
     * Create a new KillObjective
     * @param parent      the parent round
     * @param targets     an ArrayList of targets to kill
     * @param description a description string
     */
    public KillObjective(Round parent, ArrayList<Mob> targets, String description) {
        super(parent);
        this.description = description;
        this.targets = targets;

    }

    @Override
    public void update(float delta) {
        for (int i = 0; i<targets.size();) {
            Mob mob = targets.get(i);
            if (mob.isRemoved()){
                targets.remove(mob);
            }
            else {
                i++;
            }
        }

        if (targets.size() == 0) {
            status = ObjectiveStatus.COMPLETED;
        }
    }

    @Override
    public String getObjectiveString() {
        return description;
    }
}

