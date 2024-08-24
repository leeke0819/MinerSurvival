package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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

    public BlockBreakListener(RPG plugin, Map<UUID, Double> playerO2) {
        this.plugin = plugin;
        this.playerO2 = playerO2;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = block.getType();

        JobConfigManager jobConfigManager = new JobConfigManager(plugin);
        String[] jobData = jobConfigManager.getPlayerJob(player).split(",");
        String job = jobData.length > 0 ? jobData[0] : "직업 없음";
        String level = jobData.length > 1 ? jobData[1] : "";

        handleOreDrops(event, player, blockType);
        handleJobEffects(player, blockType, job, level);
        handleOxygenRecovery(player, blockType);
    }

    private void handleOreDrops(BlockBreakEvent event, Player player, Material blockType) {
        Map<Material, ItemStack> oreToIngotMap = new HashMap<>();
        oreToIngotMap.put(Material.COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        oreToIngotMap.put(Material.DEEPSLATE_COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        oreToIngotMap.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        oreToIngotMap.put(Material.DEEPSLATE_IRON_ORE, new ItemStack(Material.IRON_INGOT));
        oreToIngotMap.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        oreToIngotMap.put(Material.DEEPSLATE_GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        oreToIngotMap.put(Material.NETHER_GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        oreToIngotMap.put(Material.ANCIENT_DEBRIS, new ItemStack(Material.NETHERITE_INGOT));

        if (oreToIngotMap.containsKey(blockType)) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), oreToIngotMap.get(blockType));
        }
    }

    private void handleJobEffects(Player player, Material blockType, String job, String level) {
        if (!job.equals("§7§l광부")) return;

        int bonusChance = 10;
        Map<Material, ItemStack> bonusItems = new HashMap<>();
        bonusItems.put(Material.COAL_ORE, new ItemStack(Material.COAL, 5));
        bonusItems.put(Material.DEEPSLATE_COAL_ORE, new ItemStack(Material.COAL, 5));
        bonusItems.put(Material.COPPER_ORE, new ItemStack(Material.COPPER_INGOT, 5));
        bonusItems.put(Material.DEEPSLATE_COPPER_ORE, new ItemStack(Material.COPPER_INGOT, 5));
        bonusItems.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT, 5));
        bonusItems.put(Material.DEEPSLATE_IRON_ORE, new ItemStack(Material.IRON_INGOT, 5));
        bonusItems.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT, 5));
        bonusItems.put(Material.DEEPSLATE_GOLD_ORE, new ItemStack(Material.GOLD_INGOT, 5));
        bonusItems.put(Material.NETHER_GOLD_ORE, new ItemStack(Material.GOLD_INGOT, 5));
        bonusItems.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE, 5));
        bonusItems.put(Material.DEEPSLATE_REDSTONE_ORE, new ItemStack(Material.REDSTONE, 5));
        bonusItems.put(Material.LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, 5));
        bonusItems.put(Material.DEEPSLATE_LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, 5));
        bonusItems.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD, 5));
        bonusItems.put(Material.DEEPSLATE_EMERALD_ORE, new ItemStack(Material.EMERALD, 5));
        bonusItems.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND, 5));
        bonusItems.put(Material.DEEPSLATE_DIAMOND_ORE, new ItemStack(Material.DIAMOND, 5));
        bonusItems.put(Material.AMETHYST_CLUSTER, new ItemStack(Material.AMETHYST_SHARD, 5));
        bonusItems.put(Material.NETHER_QUARTZ_ORE, new ItemStack(Material.QUARTZ, 5));
        bonusItems.put(Material.ANCIENT_DEBRIS, new ItemStack(Material.NETHERITE_INGOT, 5));

        if (bonusItems.containsKey(blockType) && Math.random() * 100 < bonusChance) {
            player.getInventory().addItem(bonusItems.get(blockType));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + bonusItems.get(blockType).getType().name() + "&a을 &e5개&a 더 얻었습니다!"));
        }

        if (trackedBlocks.contains(blockType)) {
            UUID playerId = player.getUniqueId();
            playerBlockCount.put(playerId, playerBlockCount.getOrDefault(playerId, 0) + 1);

            if (playerBlockCount.get(playerId) >= 50) {
                int hasteLevel = level.equals("3차") || level.equals("4차") ? 1 : 0;
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, hasteLevel));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a돌이나 광물을 50개 캐서 &e성급함 " + (hasteLevel + 1) + " 효과&a를 &e30초&a 동안 받았습니다!"));
                playerBlockCount.put(playerId, 0);
            }

            if (level.equals("2차") || level.equals("3차") || level.equals("4차")) {
                handleNightVisionEffect(player, playerId);
            }
        }

        if (level.equals("4차")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        }
    }

    private void handleNightVisionEffect(Player player, UUID playerId) {
        long currentTime = System.currentTimeMillis();
        long effectCooldown = 30 * 1000;
        Long effectEndTime = cooldowns.get(playerId);

        if (effectEndTime == null || currentTime > effectEndTime) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0));
            cooldowns.put(playerId, currentTime + effectCooldown);
        } else {
            long remainingCooldown = (effectEndTime - currentTime) / 1000;
            player.sendMessage("야간투시 효과의 쿨타임은 " + remainingCooldown + "초 남았습니다.");
        }
    }

    private void handleOxygenRecovery(Player player, Material blockType) {
        Map<Material, Double> oxygenRecoveryMap = new HashMap<>();
        oxygenRecoveryMap.put(Material.COAL_ORE, 10.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_COAL_ORE, 10.0);
        oxygenRecoveryMap.put(Material.COPPER_ORE, 20.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_COPPER_ORE, 20.0);
        oxygenRecoveryMap.put(Material.IRON_ORE, 30.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_IRON_ORE, 30.0);
        oxygenRecoveryMap.put(Material.GOLD_ORE, 40.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_GOLD_ORE, 40.0);
        oxygenRecoveryMap.put(Material.REDSTONE_ORE, 15.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_REDSTONE_ORE, 15.0);
        oxygenRecoveryMap.put(Material.LAPIS_ORE, 60.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_LAPIS_ORE, 60.0);
        oxygenRecoveryMap.put(Material.EMERALD_ORE, 240.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_EMERALD_ORE, 240.0);
        oxygenRecoveryMap.put(Material.DIAMOND_ORE, 120.0);
        oxygenRecoveryMap.put(Material.DEEPSLATE_DIAMOND_ORE, 120.0);
        oxygenRecoveryMap.put(Material.AMETHYST_CLUSTER, 50.0);
        oxygenRecoveryMap.put(Material.NETHER_GOLD_ORE, 45.0);
        oxygenRecoveryMap.put(Material.NETHER_QUARTZ_ORE, 55.0);
        oxygenRecoveryMap.put(Material.ANCIENT_DEBRIS, 300.0);

        if (oxygenRecoveryMap.containsKey(blockType)) {
            double newOxygenTime = playerO2.get(player.getUniqueId()) + oxygenRecoveryMap.get(blockType);
            playerO2.put(player.getUniqueId(), newOxygenTime);
            player.sendMessage(blockType.name() + "을(를) 부숴서 산소 에너지가 " + oxygenRecoveryMap.get(blockType) + "(초)만큼 더 높아졌습니다!");
        }
    }
}