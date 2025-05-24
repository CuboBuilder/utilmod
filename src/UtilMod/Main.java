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
import java.util.*;

public class Main extends Mod {
    private final Map<Player, arc.util.Timer.Task> activeSwitchers = new HashMap<>();

    public Main() {
        Log.info("Loaded SomeUtilMOD.");
        Events.on(ClientLoadEvent.class, e -> {
            Table table = new Table();
            table.button(b -> {
                b.label(() -> activeSwitchers.containsKey(Vars.player) ? "Stop TeamSwitch" : "Start TeamSwitch");
                b.clicked(() -> {
                    if (activeSwitchers.containsKey(Vars.player)) {
                        stopTeamSwitch(Vars.player);
                        Vars.ui.showInfoToast("[red]Team switching stopped!", 3f);
                    } else {
                        startTeamSwitch(Vars.player);
                        Vars.ui.showInfoToast("[green]Team switching started!", 3f);
                    }
                });
            }, new TextButtonStyle()).size(200f, 50f).pad(2f);
            
            Vars.ui.menuGroup.addChild(table);
            table.setPosition(Vars.ui.menuGroup.getWidth() - 220f, 50f);
        });

        Events.on(PlayerLeave.class, event -> {
            stopTeamSwitch(event.player);
        });
    }

    private void startTeamSwitch(Player player) {
        arc.util.Timer.Task task = arc.util.Timer.schedule(() -> {
            if (player.con != null) {
                int randomTeam = (int)(Math.random() * 256);
                player.sendMessage("/team " + randomTeam);
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
