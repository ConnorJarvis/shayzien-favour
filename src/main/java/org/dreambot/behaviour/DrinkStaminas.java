package org.dreambot.behaviour;

import org.dreambot.Main;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;

import java.time.Instant;

import static org.dreambot.api.utilities.Sleep.sleepUntil;

public class DrinkStaminas extends Leaf<Main> {
    @Override
    public boolean isValid() {
        return (Walking.getRunEnergy() < 5 || (!Walking.isStaminaActive() && Walking.getRunEnergy() < 80)) && Inventory.contains(i -> i.getName().contains("Stamina potion")) && Instant.now().getEpochSecond() - Main.timeSinceEat > 3;
    }


    @Override
    public int onLoop() {
        if (Bank.isOpen()) {
            Logger.log("Closing Bank");
            if (!Bank.close()) {
                Logger.log("Failed to close Bank");
                return 2000;
            }
            Logger.log("Closed bank");
            sleepUntil(() -> !Bank.isOpen(), 5000, 10);
        }
        if (!Inventory.isOpen()) {
            Logger.log("Opening Inventory");
            if (!Inventory.open()) {
                Logger.log("Failed to open Inventory");
                return 2000;
            }
            Logger.log("Opened Inventory");
            sleepUntil(Inventory::isOpen, 900, 10);
        }
        if (Inventory.isOpen()) {
            Logger.log("Drinking stamina");
            if (!Inventory.interact(i -> i.getName().contains("Stamina potion"), "Drink")) {
                Logger.log("Failed to Drink stamina");
                return 2000;
            }
            Logger.log("Drank stamina");
            Main.timeSinceEat = Instant.now().getEpochSecond();
        }

        return 0;
    }
}
