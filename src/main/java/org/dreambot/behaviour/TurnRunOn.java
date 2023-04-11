package org.dreambot.behaviour;

import org.dreambot.Main;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;

public class TurnRunOn extends Leaf<Main> {
    @Override
    public boolean isValid() {
        return Walking.getRunEnergy() > 20 && !Walking.isRunEnabled();
    }


    @Override
    public int onLoop() {
        if (Walking.toggleRun()) {
            Logger.log("Failed to turn run on");
            return 2000;
        }
        Logger.log("Turning run on");
        return 0;
    }
}
