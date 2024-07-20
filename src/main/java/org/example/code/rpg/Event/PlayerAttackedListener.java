package org.example.code.rpg.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerAttackedListener implements Listener {

    private Map<UUID, Double> player02;
    public PlayerAttackedListener(Map<UUID, Double> player02) {
        this.player02 = player02;
    }
    @EventHandler
    public void onPlayerAttacked(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = ((Player) ((Player) event.getEntity()).getPlayer());
            if(player.getLocation().getY() <= 60) {
                 double damage = event.getDamage();
                 UUID uuid = player.getUniqueId();
                 double now = player02.get(uuid);
                 now -= damage * 10;
                 player02.put(uuid, now);
            }
        }
    }
}
