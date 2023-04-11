package org.dreambot.behaviour;

import org.dreambot.Main;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.utilities.Logger;
import org.dreambot.framework.Leaf;

import static org.dreambot.api.utilities.Sleep.sleepUntil;

public class HideFromPlayers extends Leaf<Main> {
    @Override
    public boolean isValid() {
        return Players.all(player -> player != null && !player.getName().equals(Players.getLocal().getName()) && Main.medpackArea.contains(player)).size() > 0;
    }


    @Override
    public int onLoop() {
        Logger.log("Hopping worlds to hide from players");
        World newWorld = Worlds.getRandomWorld(Main.validWorlds);
        if (!WorldHopper.hopWorld(newWorld)) {
            Logger.log("Failed to hop worlds");
            return 2000;
        }
        Logger.log("Hopping worlds");
        sleepUntil(() -> Worlds.getCurrentWorld() == newWorld.getWorld(), 15000, 100);
        if (Worlds.getCurrentWorld() == newWorld.getWorld()) {
            Logger.log("Hopped worlds successfully");
        }
        return 0;
    }
}
