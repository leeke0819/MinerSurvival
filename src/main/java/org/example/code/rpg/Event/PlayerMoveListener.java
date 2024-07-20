package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private HashMap<UUID, BossBar> playerBossBars;
    private Map<UUID, Boolean> playerOnGround;
    private double time;
    private boolean onGround;

    public PlayerMoveListener(HashMap<UUID, BossBar> playerBossBars, Map<UUID, Boolean> playerOnGround) {
        this.playerBossBars = playerBossBars;
        this.playerOnGround = playerOnGround;
        this.onGround = true;
        this.time = 0.0001;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        double y = event.getPlayer().getLocation().getY();

        Player player = event.getPlayer();
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if(y < 60) {
            if(time > 1) {
                player.setHealth(0);
            }
            bossBar.setProgress(1-time);
            time += 0.001;
            bossBar.setVisible(true);
            onGround = false;
        } else {
            onGround = true;
            time = 0;
            bossBar.setProgress(1);
            bossBar.setVisible(false);
        }
    }
}
