package example;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.net.Administration.*;

public class ExampleJavaMod extends Mod {
    private boolean teamSwitchActive = false;
    private Timer.Task teamSwitchTask;

    public ExampleJavaMod() {
        Log.info("Loaded TeamSwitch Mod.");

        // Listen for chat messages
        Events.on(PlayerChatEvent.class, event -> {
            String message = event.message.toLowerCase();
            
            if (message.equals(".start teamswitch")) {
                if (!teamSwitchActive) {
                    startTeamSwitch();
                    event.player.sendMessage("[green]Team switching started!");
                }
            } else if (message.equals(".stop teamswitch")) {
                if (teamSwitchActive) {
                    stopTeamSwitch();
                    event.player.sendMessage("[red]Team switching stopped!");
                }
            }
        });
    }

    private void startTeamSwitch() {
        teamSwitchActive = true;
        teamSwitchTask = Timer.schedule(() -> {
            if (teamSwitchActive) {
                int randomTeam = (int)(Math.random() * 256);
                Call.sendChatMessage("/team " + randomTeam);
            }
        }, 0f, 5f);
    }

    private void stopTeamSwitch() {
        teamSwitchActive = false;
        if (teamSwitchTask != null) {
            teamSwitchTask.cancel();
            teamSwitchTask = null;
        }
    }

    @Override
    public void loadContent() {
        Log.info("TeamSwitch Mod loaded.");
    }
}
