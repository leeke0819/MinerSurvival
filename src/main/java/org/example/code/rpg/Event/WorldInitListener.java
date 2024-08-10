package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.Random;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getWorld;

public class WorldInitListener implements Listener {
    @EventHandler
    public void createTemple(WorldInitEvent event) {
        Bukkit.getLogger().info("World가 새로 생성되었습니다.");
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        String worldName = event.getWorld().getName();
        Bukkit.getLogger().info("월드가 로드되었습니다: " + worldName);
        World world = getWorld(worldName);
        assert world != null;
        Location location = world.getSpawnLocation();
        double x = location.getX();
        double z = location.getZ();
        double a = Math.random();
        double b = Math.random();
        a *= 3000;
        b *= 3000;
        double c = Math.random();
        c *= 4;
        c = (int)c;
        if(c % 4 == 0) {
            x += a;
            z += b;
        } else if(c % 4 == 1) {
            x -= a;
            z -= b;
        } else if(c % 4 == 2) {
            x += a;
            z -= b;
        } else if(c % 4 == 3) {
            x -= a;
            z += b;
        }
        String posX = String.valueOf(x);
        Bukkit.getLogger().info(posX);
        String posZ = String.valueOf(z);
        Bukkit.getLogger().info(posZ);

<<<<<<< HEAD
        int intX = (int) x;
        int intZ = (int) z;

        // y좌표 61~75 랜덤
        Random random = new Random();
        int intY = 61 + random.nextInt(75 - 61 + 1);

        // Create and place the building
        StructureBuilding building = new StructureBuilding();
        building.generate(world, intX, intY, intZ);
    }
}
// 블럭 하나 찍고, 서버 들어가서 확인
// 스위칭 처리(0과 1로만) 1회성 제한 해보기
=======
        // 블럭 하나 찍고, 서버 들어가서 확인
        // 스위칭 처리(0과 1로만) 1회성 제한 해보기
    }
}
>>>>>>> fff834ad3e1bf702bfaff49ad9ef7bbd141dbd72
