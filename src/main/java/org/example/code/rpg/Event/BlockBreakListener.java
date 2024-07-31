package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.RPG;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class BlockBreakListener implements Listener {
    private RPG plugin;
    private Map<UUID, Double> playerO2 = new HashMap<>();
    private final HashMap<UUID, Integer> playerBlockCount = new HashMap<>();
    private final List<Material> trackedBlocks = Arrays.asList(
            Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.GRANITE,
            Material.DIORITE, Material.ANDESITE, Material.DEEPSLATE, Material.COBBLED_DEEPSLATE,
            Material.REINFORCED_DEEPSLATE, Material.TUFF, Material.COAL_ORE, Material.IRON_ORE,
            Material.COPPER_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE,
            Material.EMERALD_ORE, Material.DIAMOND_ORE, Material.RAW_IRON_BLOCK, Material.RAW_GOLD_BLOCK,
            Material.RAW_COPPER_BLOCK, Material.NETHERRACK, Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE,
            Material.ANCIENT_DEBRIS, Material.AMETHYST_CLUSTER
    );
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    public BlockBreakListener(RPG plugin, Map<UUID, Double> playerO2){
        this.plugin = plugin;
        this.playerO2 = playerO2;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        Player player = event.getPlayer();
        JobConfigManager jobConfigManager = new JobConfigManager(plugin);
        String[] a = jobConfigManager.getPlayerJob(player).split(",");
        String job = a[0];
        String level = a[1];

        // 광석 블록을 캤을 때, 광석 대신 주괴로 나오게끔 설정
        if(blockType == Material.COPPER_ORE) { // 구리 광석
            event.setDropItems(false); // 기존 드롭 아이템 제거
            ItemStack itemStack = new ItemStack(Material.COPPER_INGOT);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);

        } else if(blockType == Material.IRON_ORE) { // 철 광석
            event.setDropItems(false);
            ItemStack itemStack = new ItemStack(Material.IRON_INGOT);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);

        } else if(blockType == Material.GOLD_ORE || blockType == Material.NETHER_GOLD_ORE) { // 금 광석, 네더 금 광석
            event.setDropItems(false);
            ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);

        } else if(blockType == Material.ANCIENT_DEBRIS) { // 고대 잔해
            event.setDropItems(false);
            ItemStack itemStack = new ItemStack(Material.NETHERITE_INGOT);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
        }

        // 광부 1차
        if (job.equals("§7§l광부") && level.equals("1차")) {
            int c = (int)(Math.random()*100);
            switch (blockType) {
                case COAL_ORE: // 석탄 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.COAL, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e석탄&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case COPPER_ORE: // 구리 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.COPPER_INGOT, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e구리 주괴&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case IRON_ORE: // 철 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.IRON_INGOT, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e철 주괴&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case GOLD_ORE:
                case NETHER_GOLD_ORE: // 금 광석, 네더 금 광석(지급되는 거 동일하여 묶어놓음)
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e금 주괴&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case REDSTONE_ORE: // 레드스톤 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.REDSTONE, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e레드스톤 가루&a를 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case LAPIS_ORE: // 청금석 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.LAPIS_LAZULI, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e청금석&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case EMERALD_ORE: // 에메랄드 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.EMERALD, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e에메랄드&a를 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case DIAMOND_ORE: // 다이아몬드 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.DIAMOND, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e다이아몬드&a를 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case AMETHYST_CLUSTER: // 자수정 군집
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.AMETHYST_SHARD, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e자수정 조각&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case NETHER_QUARTZ_ORE: // 네더 석영 광석
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.QUARTZ, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e네더 석영&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                case ANCIENT_DEBRIS: // 고대 잔해
                    if(0 < c && c <= 10){
                        ItemStack itemStack = new ItemStack(Material.NETHERITE_INGOT, 5);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e네더라이트 주괴&a을 &e5개&a 더 얻었습니다!"));
                    }
                    break;

                default:
                    break;
            }

            if (trackedBlocks.contains(blockType)) {
                UUID playerId = player.getUniqueId();
                int blockCount = playerBlockCount.getOrDefault(playerId, 0) + 1; // 초기값 0으로 설정
                playerBlockCount.put(playerId, blockCount);
                if(blockCount >= 50) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 0)); // 30초 동안 성급함 1 부여
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a돌이나 광물을 50개 캐서 &e성급함 1 효과&a를 &e30초&a 동안 받았습니다!"));
                    playerBlockCount.put(playerId, 0);
                }
            }
        }

        // 광부 2차
        if (job.equals("§7§l광부") && level.equals("2차")) {
            if (trackedBlocks.contains(blockType)) {
                UUID playerId = player.getUniqueId();
                Long effectEndTime = cooldowns.get(playerId);
                long currentTime = System.currentTimeMillis();
                if (effectEndTime == null || currentTime > effectEndTime + 30 * 1000) {
                    PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
                    if (currentEffect == null || currentEffect.getDuration() <= 0) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0));
                        cooldowns.put(playerId, currentTime + 20 * 1000);
                    }
                } else {
                    player.sendMessage("야간투시 효과의 쿨타임은 " + ((effectEndTime + 30 * 1000 - currentTime) / 1000) + "초 남았습니다.");
                }
            }
        }

        // 광부 3차
        if (job.equals("§7§l광부") && level.equals("3차")) {
            if (trackedBlocks.contains(blockType)) {
                UUID playerId = player.getUniqueId();
                int blockCount = playerBlockCount.getOrDefault(playerId, 0) + 1; // 초기값 0으로 설정
                playerBlockCount.put(playerId, blockCount);
                if(blockCount >= 50) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 0)); // 30초 동안 성급함 2 부여
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a돌이나 광물을 50개 캐서 &e성급함 2 효과&a를 &e30초&a 동안 받았습니다!"));
                    playerBlockCount.put(playerId, 0);
                }
            }
        }

        // 광부 4차
        if (job.equals("§7§l광부") && level.equals("4차")) {
            int potionMaxLevel = Integer.MAX_VALUE; // spigot api에서 포션 지속효과 무한으로 하려면 이 방법밖에 없음.
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, potionMaxLevel, 0));
        }

        // 산소 회복
        double time = playerO2.get(player.getUniqueId());
        switch (blockType) {
            case COAL_ORE:
                time += 10.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("석탄 광석을 부숴서 산소 에너지가 10(초)만큼 더 높아졌습니다!");
                break;
            case COPPER_ORE:
                time += 20.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("구리 광석을 부숴서 산소 에너지가 20(초)만큼 더 높아졌습니다!");
                break;
            case IRON_ORE:
                time += 30.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("철 광석을 부숴서 산소 에너지가 30(초)만큼 더 높아졌습니다!");
                break;
            case GOLD_ORE:
                time += 40.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("금 광석을 부숴서 산소 에너지가 40(초)만큼 더 높아졌습니다!");
                break;
            case REDSTONE_ORE:
                time += 15.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("레드스톤 광석을 부숴서 산소 에너지가 15(초)만큼 더 높아졌습니다!");
                break;
            case LAPIS_ORE:
                time += 60.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("청금석 광석을 부숴서 산소 에너지가 60(초)만큼 더 높아졌습니다!");
                break;
            case EMERALD_ORE:
                time += 240.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("에메랄드 광석을 부숴서 산소 에너지가 240(초)만큼 더 높아졌습니다!");
                break;
            case DIAMOND_ORE:
                time += 120.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("다이아몬드 광석을 부숴서 산소 에너지가 120(초)만큼 더 높아졌습니다!");
                break;
            case AMETHYST_CLUSTER:
                time += 50.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("자수정 결정을 부숴서 산소 에너지가 50(초)만큼 더 높아졌습니다!");
                break;
            case NETHER_GOLD_ORE:
                time += 45.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("네더 금 광석을 부숴서 산소 에너지가 45(초)만큼 더 높아졌습니다!");
                break;
            case NETHER_QUARTZ_ORE:
                time += 55.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("네더 석영 광석을 부숴서 산소 에너지가 55(초)만큼 더 높아졌습니다!");
                break;
            case ANCIENT_DEBRIS:
                time += 300.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("고대 잔해를 부숴서 산소 에너지가 300(초)만큼 더 높아졌습니다!");
                break;
            case WHITE_CONCRETE:
                time -= 60.0;
                playerO2.put(player.getUniqueId(), time);
                player.sendMessage("[테스트] 산소 에너지가 60(초)만큼 줄었습니다");
                break;
        }
    }
}