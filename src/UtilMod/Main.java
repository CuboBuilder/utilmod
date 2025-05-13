package UtilMod;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.net.Administration.*;
import java.util.*;

public class Main extends Mod {
    private final Map<Player, arc.util.Timer.Task> activeSwitchers = new HashMap<>();

    public Main() {
        Log.info("Loaded SomeUtilMOD.");
        // Listen for chat messages
        Events.on(PlayerChatEvent.class, event -> {
            String message = event.message.toLowerCase();
            Player player = event.player;
            
            if (message.equals(".start teamswitch")) {
                if (!activeSwitchers.containsKey(player)) {
                    startTeamSwitch(player);
                    player.sendMessage("[green]Team switching started!");
                }
            } else if (message.equals(".stop teamswitch")) {
                if (activeSwitchers.containsKey(player)) {
                    stopTeamSwitch(player);
                    player.sendMessage("[red]Team switching stopped!");
                }
            }
        });
    }

    private void startTeamSwitch(Player player) {
        arc.util.Timer.Task task = arc.util.Timer.schedule(() -> {
            if (player.con != null) {
                int randomTeam = (int)(Math.random() * 256);
                Call.sendMessage("/team " + randomTeam);
            } else {
                stopTeamSwitch(player);
            }
        }, 0f, 5f);
        
        activeSwitchers.put(player, task);
    }

    private void stopTeamSwitch(Player player) {
        arc.util.Timer.Task task = activeSwitchers.remove(player);
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void loadContent() {
        Log.info("SomeUtilMOD loaded.");
    }
}
