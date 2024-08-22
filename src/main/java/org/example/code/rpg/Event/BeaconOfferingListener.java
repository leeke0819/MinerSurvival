package org.example.code.rpg.Event;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BeaconOfferingListener implements Listener {

    private final Plugin plugin;
    private final HashMap<Block, Set<Material>> beaconOfferings = new HashMap<>();
    private final Set<Material> requiredOfferings = Set.of(
            Material.COAL, Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT,
            Material.REDSTONE, Material.LAPIS_LAZULI, Material.EMERALD, Material.DIAMOND,
            Material.AMETHYST_SHARD, Material.QUARTZ, Material.NETHERITE_INGOT
    );

    public BeaconOfferingListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.BEACON) {
                handleBeaconInteraction(event.getPlayer(), clickedBlock);
            }
        }
    }

    public void handleBeaconFound(Block beaconBlock) {
        for (Player player : beaconBlock.getWorld().getPlayers()) {
            if (player.getLocation().distance(beaconBlock.getLocation()) < 50) {
                player.sendMessage("코어가 활성화되었습니다! 공양할 광물을 준비하세요.");
                handleBeaconInteraction(player, beaconBlock);
            }
        }
    }

    private void handleBeaconInteraction(Player player, Block beaconBlock) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Material offeringMaterial = itemInHand.getType();

        Set<Material> offerings = beaconOfferings.getOrDefault(beaconBlock, new HashSet<>());
        if (offerings.contains(offeringMaterial)) {
            player.sendMessage(offeringMaterial.name() + "은(는) 이미 공양되었습니다. 다른 광물을 사용하세요.");
            return;
        }

        if (isValidOfferingItem(offeringMaterial) && itemInHand.getAmount() >= 10) {
            itemInHand.setAmount(itemInHand.getAmount() - 10);
            player.sendMessage(offeringMaterial.name() + " 10개를 공양 성공했습니다!");

            offerings.add(offeringMaterial);
            beaconOfferings.put(beaconBlock, offerings);

            // 모든 광물 공양이 완료되었는지 확인
            if (checkAllOfferingsComplete()) {
                triggerCompletionSequence(player);
            }
        } else {
            player.sendMessage("공양할 광물이 부족합니다.");
        }
    }

    private boolean isValidOfferingItem(Material material) {
        return requiredOfferings.contains(material);
    }

    private boolean checkAllOfferingsComplete() {
        // 모든 비콘에 대해 모든 필수 공양물이 공양되었는지 확인
        for (Set<Material> offerings : beaconOfferings.values()) {
            if (!offerings.containsAll(requiredOfferings)) {
                return false;
            }
        }
        return true;
    }

    private void triggerCompletionSequence(Player player) {
        // 플레이어의 게임 모드를 'SPECTATOR'로 변경
        player.setGameMode(GameMode.SPECTATOR);

        player.sendTitle("§a모든 이상 현상이", "", 10, 60, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§a원상복구 되었습니다.", "", 10, 60, 20);
            }
        }.runTaskLater(plugin, 40L); // 40L = 2초 후 (20 ticks = 1초)

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§a지금까지", "", 10, 60, 20);
            }
        }.runTaskLater(plugin, 120L); // 120L = 6초 후 (20 ticks = 1초)

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§a'광부로 살아남기'플러그인을", "", 10, 60, 20);
            }
        }.runTaskLater(plugin, 160L); // 160L = 8초 후 (20 ticks = 1초)

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§a플레이해주셔서 감사합니다.", "§e플러그인 제작자 : 이케", 10, 100, 20);
            }
        }.runTaskLater(plugin, 200L); // 200L = 10초 후 (20 ticks = 1초)
    }
}