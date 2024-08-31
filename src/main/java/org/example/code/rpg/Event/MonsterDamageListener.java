package org.example.code.rpg.Event;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
                monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Monster) {
            Monster monster = (Monster) event.getDamager();
            // 몬스터 종류별로 데미지 증가

            // 좀비
            if (monster instanceof Zombie) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 좀비 주민
            if (monster instanceof ZombieVillager) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 허스크
            if (monster instanceof Husk) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 드라운드
            if (monster instanceof Drowned) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 위더 스켈레톤
            if (monster instanceof WitherSkeleton) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 거미
            if (monster instanceof Spider) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 팬텀
            if (monster instanceof Phantom) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 피글린
            if (monster instanceof Piglin) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 난폭한 피글린
            if (monster instanceof PiglinBrute) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 좀벌레
            if (monster instanceof Silverfish) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 호글린
            if (monster instanceof Hoglin) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }

            // 조글린
            if (monster instanceof Zoglin) {
                event.setDamage(event.getDamage() * 2.0); // 기존 데미지에 2배
            }
        }

        // 만약 화살에 맞았다면
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            // 아래 if문 직역하자면 화살을 쏜 존재가 스켈레톤 또는 스트레이이고 피해를 입은 존재가 플레이어일 경우
            if ((arrow.getShooter() instanceof Skeleton || arrow.getShooter() instanceof Stray) && event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                // 플레이어에게 위더 효과 부여하기
                PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 200, 0); // 10초 지속
                player.addPotionEffect(wither);
            }
        }

        // 만약 화염구에 맞았다면
        if (event.getDamager() instanceof Fireball) {
            Fireball fireball = (Fireball) event.getDamager();
            // 아래 if문 직역하자면 화살을 쏜 존재가 가스트 또는 블레이즈이고 피해를 입은 존재가 플레이어일 경우
            if ((fireball.getShooter() instanceof Ghast || fireball.getShooter() instanceof Blaze) && event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                // 플레이어에게 어둠 효과 부여하기
                PotionEffect wither = new PotionEffect(PotionEffectType.DARKNESS, 200, 0); // 10초 지속
                player.addPotionEffect(wither);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        // 이벤트가 발생한 엔티티 가져오기
        Entity entity = event.getEntity();

        // 엔티티가 크리퍼인지 확인
        if (entity.getType() == EntityType.CREEPER) {
            float increasedPower = 2.0f;  // 2배

            // 폭발의 힘 증가시키기
            event.setYield(increasedPower);
        }
    }
}