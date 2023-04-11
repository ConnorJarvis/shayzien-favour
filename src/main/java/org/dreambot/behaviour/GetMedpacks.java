package org.dreambot.behaviour;

import org.dreambot.Main;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.framework.Leaf;

import static org.dreambot.api.utilities.Sleep.sleepUntil;

public class GetMedpacks extends Leaf<Main> {

    @Override
    public boolean isValid() {
        return !Main.medpackArea.contains(Players.getLocal()) || (!Inventory.contains("Shayzien medpack") && Players.getLocal().getInteractingIndex() == -1 && Inventory.getEmptySlots() > 0);
    }

    @Override
    public int onLoop() {
        GameObject medpackBox = GameObjects.closest("Medpack Box");
        if (medpackBox == null || medpackBox.distance() > 10) {
            if (Walking.shouldWalk(8)) {
                if (!Walking.walk(Main.medpackTile)) {
                    Logger.log("Failed to walk to Medpack Box");
                    return 2000;
                }
                Logger.log("Walking to Medpack Box");
            }
        } else {
            if (!medpackBox.interact("Take-many")) {
                Logger.log("Failed to interact with Medpack Box");
                return 2000;
            }
            Logger.log("Interacting with Medpack Box");
            sleepUntil(() -> Inventory.contains("Shayzien medpack"), 10000, 500);
        }
        return (int) Calculations.nextGaussianRandom(350, 250);
    }

}
