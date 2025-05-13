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

public class Main extends Mod {
    private boolean teamSwitchActive = false;
    private Timer.Task teamSwitchTask;
    private Player activePlayer = null;

    public Main() {
        Log.info("Loaded SomeUtilMOD.");
        // Listen for chat messages
        Events.on(PlayerChatEvent.class, event -> {
            String message = event.message.toLowerCase();
            Player player = event.player;
            
            if (message.equals(".start teamswitch")) {
                if (!teamSwitchActive) {
                    startTeamSwitch(player);
                    player.sendMessage("[green]Team switching started!");
                } else if (activePlayer != player) {
                    player.sendMessage("[red]Team switching is already active for another player!");
                }
            } else if (message.equals(".stop teamswitch")) {
                if (teamSwitchActive && activePlayer == player) {
                    stopTeamSwitch();
                    player.sendMessage("[red]Team switching stopped!");
                } else if (teamSwitchActive && activePlayer != player) {
                    player.sendMessage("[red]You can't stop another player's team switching!");
                }
            }
        });
    }

    private void startTeamSwitch(Player player) {
        teamSwitchActive = true;
        activePlayer = player;
        teamSwitchTask = Timer.schedule(() -> {
            if (teamSwitchActive && activePlayer != null && activePlayer.con != null) {
                int randomTeam = (int)(Math.random() * 256);
                Call.sendChatMessage("/team " + randomTeam);
            } else {
                stopTeamSwitch();
            }
        }, 0f, 5f);
    }

    private void stopTeamSwitch() {
        teamSwitchActive = false;
        activePlayer = null;
        if (teamSwitchTask != null) {
            teamSwitchTask.cancel();
            teamSwitchTask = null;
        }
    }

    @Override
    public void loadContent() {
        Log.info("SomeUtilMOD loaded.");
    }
}
