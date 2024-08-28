package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;
import org.example.code.rpg.RPG;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class InventoryClickListener implements Listener {
    private final RPG plugin;
    private final GuiManager guiManager;
    private final MoneyManager moneyManager;
    private final PlayerScoreboardManager scoreboardManager;
    private final JobConfigManager jobConfigManager;

    // Material 한글 이름 매핑
    private final Map<Material, String> materialNames;

    // 단서 해금 조건 저장
    private Material clue1Material;
    private Material clue2Material;
    private Material clue3Material;
    private int clue1RequiredSales;
    private int clue2RequiredSales;
    private int clue3RequiredSales;

    private final Random random = new Random();

    public InventoryClickListener(RPG plugin, GuiManager guiManager, MoneyManager moneyManager, PlayerScoreboardManager scoreboardManager, JobConfigManager jobConfigManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.moneyManager = moneyManager;
        this.scoreboardManager = scoreboardManager;
        this.jobConfigManager = jobConfigManager;

        // 한글 이름 매핑 초기화
        this.materialNames = new HashMap<>();
        this.materialNames.put(Material.COAL, "석탄");
        this.materialNames.put(Material.COPPER_INGOT, "구리 주괴");
        this.materialNames.put(Material.IRON_INGOT, "철 주괴");
        this.materialNames.put(Material.GOLD_INGOT, "금 주괴");
        this.materialNames.put(Material.REDSTONE, "레드스톤");
        this.materialNames.put(Material.LAPIS_LAZULI, "청금석");
        this.materialNames.put(Material.EMERALD, "에메랄드");
        this.materialNames.put(Material.DIAMOND, "다이아몬드");
        this.materialNames.put(Material.AMETHYST_SHARD, "자수정 조각");
        this.materialNames.put(Material.QUARTZ, "석영");
        this.materialNames.put(Material.NETHERITE_INGOT, "네더라이트 주괴");

        // 단서 해금 조건 랜덤 초기화
        initializeClueRequirements();
    }

    // 단서 해금 조건을 랜덤하게 설정하는 메서드
    private void initializeClueRequirements() {
        Set<Material> materialSet = materialNames.keySet();
        Material[] materials = materialSet.toArray(new Material[0]);

        clue1Material = materials[random.nextInt(materials.length)];
        clue2Material = materials[random.nextInt(materials.length)];
        clue3Material = materials[random.nextInt(materials.length)];

        clue1RequiredSales = random.nextInt(100) + 1;
        clue2RequiredSales = random.nextInt(100) + 1;
        clue3RequiredSales = random.nextInt(100) + 1;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.equals("메뉴") && !inventoryTitle.equals("전직 상점") && !inventoryTitle.equals("광물 상점") && !inventoryTitle.equals("해결 단서")) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getClickedInventory() == player.getInventory()) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                player.sendMessage("이 GUI에서는 Shift + 좌클릭을 할 수 없습니다.");
            } else if (event.getClick() == ClickType.DOUBLE_CLICK) {
                event.setCancelled(true);
                player.sendMessage("이 GUI에서는 더블클릭을 할 수 없습니다.");
            }
            return;
        }

        event.setCancelled(true);

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        if (clickedItem.getType() == Material.ENCHANTED_BOOK && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            // 현재 플레이어의 직업 및 레벨을 가져옵니다.
            String[] jobInfo = jobConfigManager.getPlayerJob(player).split(",");
            String job = jobInfo[0];
            String level = jobInfo[1];

            if (displayName.equals("전직")) {
                guiManager.jobShop(player);
            } else if (displayName.equals("[전직] 광부 1차")) {
                if (job.equals("직업 없음") && level.equals(" ")) {
                    processJobPurchase(player, 10000, "광부 1차", ChatColor.DARK_PURPLE + "광부 1차로 전직합니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "당신은 이미 직업을 가지고 있습니다.");
                }
            } else if (displayName.equals("[전직] 광부 2차")) {
                if (job.equals("§7§l광부") && level.equals("1차")) {
                    processJobPurchase(player, 40000, "광부 2차", ChatColor.DARK_PURPLE + "광부 2차로 전직합니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "이 전직책을 구매하려면 '광부 1차' 이어야 합니다.");
                }
            } else if (displayName.equals("[전직] 광부 3차")) {
                if (job.equals("§7§l광부") && level.equals("2차")) {
                    processJobPurchase(player, 70000, "광부 3차", ChatColor.DARK_PURPLE + "광부 3차로 전직합니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "이 전직책을 구매하려면 '광부 2차' 이어야 합니다.");
                }
            } else if (displayName.equals("[전직] 광부 4차")) {
                if (job.equals("§7§l광부") && level.equals("3차")) {
                    processJobPurchase(player, 100000, "광부 4차", ChatColor.DARK_PURPLE + "광부 4차로 전직합니다.");
                } else {
                    player.sendMessage(ChatColor.RED + "이 전직책을 구매하려면 '광부 3차' 이어야 합니다.");
                }
            }
        } else if (clickedItem.getType() == Material.NETHERITE_PICKAXE && clickedItem.hasItemMeta()) {
            guiManager.mineralShop(player);
        } else if (clickedItem.getType() == Material.PAPER && clickedItem.hasItemMeta()) {
            boolean clue1Unlocked = plugin.loadClueState(player, "단서1");
            boolean clue2Unlocked = plugin.loadClueState(player, "단서2");
            boolean clue3Unlocked = plugin.loadClueState(player, "단서3");
            guiManager.clues(player, clue1Unlocked, clue2Unlocked, clue3Unlocked);
        } else {
            handleMineralSale(player, clickedItem, event.getClick());
        }
    }

    private void processJobPurchase(Player player, int cost, String jobName, String lore) {
        if (moneyManager.getBalance(player) >= cost) {
            moneyManager.subtractBalance(player, cost);
            player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

            ItemStack customItem = createCustomItem(jobName, lore);
            addCustomItemToPlayer(player, customItem);
            scoreboardManager.setPlayerScoreboard(player);
        } else {
            player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
        }
    }

    private ItemStack createCustomItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = customItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "[전직] " + ChatColor.GRAY + "" + ChatColor.BOLD + name);
            meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', lore)));
            customItem.setItemMeta(meta);
        }
        return customItem;
    }

    private void addCustomItemToPlayer(Player player, ItemStack customItem) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(customItem);
        } else {
            player.getWorld().dropItem(player.getLocation(), customItem);
            player.sendMessage(ChatColor.RED + "인벤토리가 가득 차서 아이템이 바닥에 떨어졌습니다!");
        }
    }

    private void handleMineralSale(Player player, ItemStack clickedItem, ClickType clickType) {
        Material material = clickedItem.getType();
        int leftClickCost = 0, middleClickCost = 0, rightClickCost = 0;

        switch (material) {
            case COAL:
                leftClickCost = 30;
                middleClickCost = 960;
                rightClickCost = 1920;
                break;
            case COPPER_INGOT:
                leftClickCost = 40;
                middleClickCost = 1280;
                rightClickCost = 2560;
                break;
            case IRON_INGOT:
                leftClickCost = 50;
                middleClickCost = 1600;
                rightClickCost = 3200;
                break;
            case GOLD_INGOT:
                leftClickCost = 60;
                middleClickCost = 1920;
                rightClickCost = 3840;
                break;
            case REDSTONE:
                leftClickCost = 20;
                middleClickCost = 640;
                rightClickCost = 1280;
                break;
            case LAPIS_LAZULI:
                leftClickCost = 80;
                middleClickCost = 2560;
                rightClickCost = 5120;
                break;
            case EMERALD:
                leftClickCost = 90;
                middleClickCost = 2880;
                rightClickCost = 5760;
                break;
            case DIAMOND:
                leftClickCost = 100;
                middleClickCost = 3200;
                rightClickCost = 6400;
                break;
            case AMETHYST_SHARD:
                leftClickCost = 120;
                middleClickCost = 3840;
                rightClickCost = 7680;
                break;
            case QUARTZ:
                leftClickCost = 150;
                middleClickCost = 4800;
                rightClickCost = 9600;
                break;
            case NETHERITE_INGOT:
                leftClickCost = 250;
                middleClickCost = 8000;
                rightClickCost = 16000;
                break;
            default:
                return;
        }

        int amountToSell = 0;
        int salePrice = 0;

        switch (clickType) {
            case LEFT:
                amountToSell = 1;
                salePrice = leftClickCost;
                break;
            case MIDDLE:
                amountToSell = 32;
                salePrice = middleClickCost;
                break;
            case RIGHT:
                amountToSell = 64;
                salePrice = rightClickCost;
                break;
            default:
                return;
        }

        String materialName = materialNames.getOrDefault(material, material.name());

        if (player.getInventory().containsAtLeast(new ItemStack(material), amountToSell)) {
            moneyManager.addBalance(player, salePrice);
            player.sendMessage(ChatColor.GREEN + "" + amountToSell + "개를 " + salePrice + "원에 판매했습니다.");
            player.getInventory().removeItem(new ItemStack(material, amountToSell));
            updateSalesCount(player, material, amountToSell);
            scoreboardManager.setPlayerScoreboard(player);
        } else {
            player.sendMessage(ChatColor.RED + "판매할 " + materialName + "이(가) 충분하지 않습니다.");
        }
    }

    private void updateSalesCount(Player player, Material material, int amountSold) {
        checkAndUpdateClue(player, material, amountSold, clue1Material, clue1RequiredSales, "단서1");
        checkAndUpdateClue(player, material, amountSold, clue2Material, clue2RequiredSales, "단서2");
        checkAndUpdateClue(player, material, amountSold, clue3Material, clue3RequiredSales, "단서3");
    }

    private void checkAndUpdateClue(Player player, Material material, int amountSold, Material clueMaterial, int requiredSales, String clueName) {
        if (material == clueMaterial) {
            int currentSales = plugin.getSalesCount(player, clueName) + amountSold; // 플레이어별 판매량 저장 및 누적
            plugin.setSalesCount(player, clueName, currentSales);

            if (currentSales >= requiredSales) {
                player.sendMessage(ChatColor.GREEN + clueName + "이(가) 개방되었습니다!");
                plugin.saveClueState(player, clueName, true);
            }
        }
    }
}