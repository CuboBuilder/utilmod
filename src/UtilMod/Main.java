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
    private Player modOwner = null;

    public Main() {
        Log.info("Loaded SomeUtilMOD.");
        // Listen for chat messages
        Events.on(PlayerChatEvent.class, event -> {
            String message = event.message.toLowerCase();
            Player player = event.player;
            
            // Первый, кто напишет .owner, становится владельцем мода
            if (message.equals(".owner") && modOwner == null) {
                modOwner = player;
                player.sendMessage("[green]You are now the mod owner!");
                return;
            }
            
            // Проверяем, является ли игрок владельцем мода
            if (message.equals(".start teamswitch") || message.equals(".stop teamswitch")) {
                if (modOwner == null) {
                    player.sendMessage("[scarlet]No mod owner set! Use .owner to become the mod owner.");
                    return;
                }
                if (player != modOwner) {
                    player.sendMessage("[scarlet]Only the mod owner can use teamswitch commands!");
                    return;
                }
            }
            
            if (message.equals(".start teamswitch")) {
                if (!activeSwitchers.containsKey(player)) {
                    startTeamSwitch(player);
                    player.sendMessage("[green]Team switching started for you!");
                } else {
                    player.sendMessage("[scarlet]You already have team switching enabled!");
                }
            } else if (message.equals(".stop teamswitch")) {
                if (activeSwitchers.containsKey(player)) {
                    stopTeamSwitch(player);
                    player.sendMessage("[red]Team switching stopped for you!");
                } else {
                    player.sendMessage("[scarlet]You don't have team switching enabled!");
                }
            }
        });
    }

    private void startTeamSwitch(Player player) {
        arc.util.Timer.Task task = arc.util.Timer.schedule(() -> {
            if (player.con != null) {
                int randomTeam = (int)(Math.random() * 256);
                // Используем правильный формат команды /team
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
