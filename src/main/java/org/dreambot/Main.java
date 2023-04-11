package org.dreambot;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.behaviour.*;
import org.dreambot.framework.Branch;
import org.dreambot.framework.Tree;
import org.dreambot.paint.CustomPaint;
import org.dreambot.paint.PaintInfo;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ScriptManifest(category = Category.MISC, author = "Pyfa", name = "Pyfa Shayzien Favour", description = "Gains Shayzien favour", version = 0.1)
public class Main extends AbstractScript implements PaintInfo {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static long timeSinceEat = Instant.now().getEpochSecond();
    public static Tile medpackTile = new Tile(1528, 3625, 0);
    public static int targetFavour = 1000;
    public static Area medpackArea = new Area(1533, 3638, 1507, 3613, 0);
    public static List<World> validWorlds;
    private final Tree<Main> tree = new Tree<>();
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.TOP_LEFT_PLAY_SCREEN, new Color[]{new Color(255, 251, 255)},
            "Trebuchet MS",
            new Color[]{new Color(50, 50, 50, 175)},
            new Color[]{new Color(28, 28, 29)},
            1, false, 5, 3, 0);
    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    @Override
    public void onStart(String... args) {
        buildValidWorlds();
        instantiateTree();
    }

    @Override
    public void onStart() {
        buildValidWorlds();
        instantiateTree();
    }

    private void instantiateTree() {
        Branch<Main> favourBranch = new FavourBranch();
        tree.addBranches(
                favourBranch.addLeafs(new DrinkStaminas(), new TurnRunOn(), new HideFromPlayers(), new GetMedpacks(), new UseMedpacks())
        );

    }

    private void buildValidWorlds() {
        List<World> worlds = Worlds.getNormalizedWorlds();
        validWorlds = new ArrayList<>();
        for (World world : worlds) {
            if (!world.isF2P()) {
                validWorlds.add(world);
            }
        }
    }

    @Override
    public int onLoop() {
        if (PlayerSettings.getBitValue(4894) == targetFavour && Client.getGameState().equals(GameState.LOGGED_IN)) {
            Logger.log("Reached 100% favour - stopping");
            this.getScriptManager().stop();
        }
        return this.tree.onLoop();

    }

    @Override
    public String[] getPaintInfo() {
        return new String[]{
                "Current Favour: " + df.format((float) PlayerSettings.getBitValue(4894) / 10.0),
        };
    }

    @Override
    public void onPaint(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHints(aa);
        CUSTOM_PAINT.paint(gg);
    }
}
