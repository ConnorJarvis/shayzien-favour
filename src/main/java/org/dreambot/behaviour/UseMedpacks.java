package org.dreambot.behaviour;

import org.dreambot.Main;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.framework.Leaf;

import static org.dreambot.api.utilities.Sleep.sleepUntil;

public class UseMedpacks extends Leaf<Main> {

    @Override
    public boolean isValid() {
        return Main.medpackArea.contains(Players.getLocal()) && Inventory.contains("Shayzien medpack") && Players.getLocal().getInteractingIndex() == -1;
    }

    @Override
    public int onLoop() {
        if (!Inventory.isItemSelected()) {
            Logger.log("Interacting with Shayzien medpack");
            if (!Inventory.get("Shayzien medpack").interact()) {
                Logger.log("Failed to interact with Shayzien medpack");
                return 2000;
            }
            Logger.log("Successfully interacted with Shayzien medpack");
        }
        NPC soldierToHeal = NPCs.closest(npc -> npc.getID() % 2 == 0 && npc.getName().equals("Wounded soldier"));
        if (soldierToHeal != null) {
            int medpackCountBefore = Inventory.count("Shayzien medpack");
            Logger.log("Interacting with Wounded Soldier");
            if (!soldierToHeal.interactForceRight("Use")) {
                Logger.log("Failed to interact with Wounded Soldier");
                return 2000;
            }
            Logger.log("Successfully interacted with Wounded Soldier");
            sleepUntil(() -> Inventory.count("Shayzien medpack") < medpackCountBefore, () -> Players.getLocal().isMoving(), 3000, 100);
            if (!(Inventory.count("Shayzien medpack") < medpackCountBefore)) {
                Logger.log("Wounded Soldier was not healed");
                return 2000;
            }
            Logger.log("Wounded Soldier was healed");
        } else {
            Logger.log("No Wounded Soldier to interact with");
        }
        return (int) Calculations.nextGaussianRandom(350, 250);
    }

}
