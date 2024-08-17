package org.example.code.rpg.Event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import static org.bukkit.Bukkit.getLogger;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        // 커스텀 구조물의 마커 엔티티인지 확인
        if (entity.getType() == EntityType.MARKER && "CustomStructureMarker".equals(entity.getCustomName())) {
            Location location = entity.getLocation();
            getLogger().info("Custom structure entity detected at: " +
                    "X: " + location.getBlockX() +
                    " Y: " + location.getBlockY() +
                    " Z: " + location.getBlockZ());

            // 엔티티가 구조물의 일부로 감지되면 엔티티 위치나 다른 정보를 처리할 수 있습니다.
            // 필요에 따라 엔티티를 제거할 수도 있습니다.
            entity.remove(); // 마커 엔티티이므로 필요 시 제거
        }
    }
}
