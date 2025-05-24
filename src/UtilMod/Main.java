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
import mindustry.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import java.util.*;

public class Main extends Mod {
    private final Map<Player, arc.util.Timer.Task> activeSwitchers = new HashMap<>();
    private Table buttonTable;

    public Main() {
        Log.info("Loaded SomeUtilMOD.");
        
        Events.on(ClientLoadEvent.class, e -> {
            // Create a table for the button
            buttonTable = new Table();
            buttonTable.top().right();
            buttonTable.defaults().size(130f, 45f).pad(4f);
            
            // Add toggle button
            TextButton toggleButton = new TextButton("TeamSwitch: OFF");
            toggleButton.clicked(() -> {
                if (activeSwitchers.containsKey(Vars.player)) {
                    stopTeamSwitch(Vars.player);
                    toggleButton.setText("TeamSwitch: OFF");
                    Vars.ui.showInfoToast("[red]Team switching stopped!", 3f);
                } else {
                    startTeamSwitch(Vars.player);
                    toggleButton.setText("TeamSwitch: ON");
                    Vars.ui.showInfoToast("[green]Team switching started!", 3f);
                }
            });
            
            buttonTable.add(toggleButton);
            
            // Add the table to the UI
            Core.scene.add(buttonTable);
        });

        Events.on(PlayerLeave.class, event -> {
            stopTeamSwitch(event.player);
        });
    }

    private void startTeamSwitch(Player player) {
        if (activeSwitchers.containsKey(player)) return;
        
        arc.util.Timer.Task task = arc.util.Timer.schedule(() -> {
            if (player != null && player.team() != null) {
                int randomTeam = (int)(Math.random() * 256);
                Call.sendChatMessage("/team " + randomTeam);
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
        }
    }

    @Override
    public void loadContent() {
        Log.info("SomeUtilMOD loaded.");
    }
}
