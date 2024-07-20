package org.example.code.rpg.Event;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.Bukkit.getLogger;

public class MonsterDamageListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Monster) {
            Monster monster = (Monster) event.getEntity();
            // 몬스터 속도 증가
            if (monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Monster) {
            Monster monster = (Monster) event.getDamager();
            // 몬스터 종류별로 데미지 증가
            if (monster instanceof Zombie) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }
            getLogger().info("좀비");
        }

            // 만약 화살에 맞았다면
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            getLogger().info("a");
            // 아래 if문 직역하자면 화살을 쏜 존재가 스켈레톤이고 피해를 입은 존재가 플레이어일 경우
            if (arrow.getShooter() instanceof Skeleton && event.getEntity() instanceof Player) {
                getLogger().info("b");
                Player player = (Player) event.getEntity();
                // 플레이어에게 위더 효과 부여하기
                PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 200, 0); // 10초 지속
                player.addPotionEffect(wither);
            }
        }

    }
}
