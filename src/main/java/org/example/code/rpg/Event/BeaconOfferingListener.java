package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.RPG;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeaconOfferingListener implements Listener {

    private final Plugin plugin;
    private final JobConfigManager jobConfigManager; // JobConfigManager 인스턴스 추가
    private final HashMap<Block, Set<Material>> beaconOfferings = new HashMap<>();
    private final Set<Material> requiredOfferings = Set.of(
            Material.COAL, Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT,
            Material.REDSTONE, Material.LAPIS_LAZULI, Material.EMERALD, Material.DIAMOND,
            Material.AMETHYST_SHARD, Material.QUARTZ, Material.NETHERITE_INGOT
    );

    private final Map<Material, String> materialNames = new HashMap<>();

    public BeaconOfferingListener(Plugin plugin, JobConfigManager jobConfigManager) {
        this.plugin = plugin;
        this.jobConfigManager = jobConfigManager; // JobConfigManager 초기화
        initializeMaterialNames();
    }

    private void initializeMaterialNames() {
        materialNames.put(Material.COAL, "석탄");
        materialNames.put(Material.COPPER_INGOT, "구리 주괴");
        materialNames.put(Material.IRON_INGOT, "철 주괴");
        materialNames.put(Material.GOLD_INGOT, "금 주괴");
        materialNames.put(Material.REDSTONE, "레드스톤");
        materialNames.put(Material.LAPIS_LAZULI, "청금석");
        materialNames.put(Material.EMERALD, "에메랄드");
        materialNames.put(Material.DIAMOND, "다이아몬드");
        materialNames.put(Material.AMETHYST_SHARD, "자수정 조각");
        materialNames.put(Material.QUARTZ, "석영");
        materialNames.put(Material.NETHERITE_INGOT, "네더라이트 주괴");
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
        // 플레이어의 직업과 레벨을 확인
        String jobInfo = jobConfigManager.getPlayerJob(player);
        String[] jobDetails = jobInfo.split(",");
        String job = jobDetails[0];
        String level = jobDetails[1];
        if (!job.equals("§7§l광부") || !level.equals("4차")) {
            player.sendMessage(ChatColor.RED + "광부 4차에 도달해야 공양을 시작할 수 있습니다!");
            return;
        }

        // RPG 클래스에서 단서 상태를 가져오는 인스턴스
        RPG pluginInstance = (RPG) this.plugin;

        // 단서1, 단서2, 단서3 상태 검사
        boolean clue1 = pluginInstance.loadClueState(player, "단서1");
        boolean clue2 = pluginInstance.loadClueState(player, "단서2");
        boolean clue3 = pluginInstance.loadClueState(player, "단서3");

        if (!(clue1 && clue2 && clue3)) {
            player.sendMessage(ChatColor.RED + "모든 단서를 발견해야 공양을 시작할 수 있습니다!");
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Material offeringMaterial = itemInHand.getType();

        Set<Material> offerings = beaconOfferings.getOrDefault(beaconBlock, new HashSet<>());
        if (offerings.contains(offeringMaterial)) {
            player.sendMessage(ChatColor.GREEN + getMaterialName(offeringMaterial) + ChatColor.YELLOW + "은(는) 이미 공양되었습니다. 다른 광물을 사용하세요.");
            return;
        }

        if (isValidOfferingItem(offeringMaterial) && itemInHand.getAmount() >= 10) {
            itemInHand.setAmount(itemInHand.getAmount() - 10);
            player.sendMessage(ChatColor.GREEN + getMaterialName(offeringMaterial) + " 10개" + ChatColor.AQUA + "를 공양 성공했습니다!");

            offerings.add(offeringMaterial);
            beaconOfferings.put(beaconBlock, offerings);

            // 모든 광물 공양이 완료되었는지 확인
            if (checkAllOfferingsComplete()) {
                triggerCompletionSequence();
            }
        } else {
            player.sendMessage("공양할 광물이 부족합니다.");
        }
    }

    private boolean isValidOfferingItem(Material material) {
        return requiredOfferings.contains(material);
    }

    private boolean checkAllOfferingsComplete() {
        for (Set<Material> offerings : beaconOfferings.values()) {
            if (!offerings.containsAll(requiredOfferings)) {
                return false;
            }
        }
        return true;
    }

    private void triggerCompletionSequence() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOnline()) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle("§a모든 이상 현상이", "", 10, 60, 20);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle("§a원상복구 되었습니다.", "", 10, 60, 20);
                    }
                }.runTaskLater(plugin, 40L);  // 40L = 2초 후 (20 ticks = 1초)

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle("§a지금까지", "", 10, 60, 20);
                    }
                }.runTaskLater(plugin, 120L);  // 120L = 6초 후 (20 ticks = 1초)

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle("§a'광부로 살아남기'플러그인을", "", 10, 60, 20);
                    }
                }.runTaskLater(plugin, 160L);  // 160L = 8초 후 (20 ticks = 1초)

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle("§a플레이해주셔서 감사합니다.", "§e플러그인 제작자 : 이케", 10, 100, 20);
                    }
                }.runTaskLater(plugin, 200L);  // 200L = 10초 후 (20 ticks = 1초)
            }
        }
    }


    private String getMaterialName(Material material) {
        return materialNames.getOrDefault(material, material.name());
    }
}