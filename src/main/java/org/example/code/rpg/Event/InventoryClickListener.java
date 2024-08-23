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
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;
import org.example.code.rpg.RPG;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {
    private final RPG plugin;
    private final GuiManager guiManager;
    private final MoneyManager moneyManager;
    private final PlayerScoreboardManager scoreboardManager;

    private final Map<Player, Integer> copperSales = new HashMap<>();
    private final Map<Player, Integer> lapisSales = new HashMap<>();
    private final Map<Player, Integer> quartzSales = new HashMap<>();

    public InventoryClickListener(RPG plugin, GuiManager guiManager, MoneyManager moneyManager, PlayerScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.moneyManager = moneyManager;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.equals("메뉴") && !inventoryTitle.equals("전직 상점") && !inventoryTitle.equals("광물 상점") && !inventoryTitle.equals("해결 단서")) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // 플레이어가 자신의 인벤토리의 아이템을 클릭하면 클릭 취소 안되게 하기
        if (event.getClickedInventory() == player.getInventory()) {
            return;
        }

        // Shift + 좌클릭 감지
        if (event.isShiftClick()) {
            event.setCancelled(true);
            player.sendMessage("이 GUI에서는 Shift + 좌클릭을 할 수 없습니다.");
            return;
        }

        event.setCancelled(true);

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        if (clickedItem.getType() == Material.ENCHANTED_BOOK && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("전직")) {
                guiManager.jobShop(player);
            } else if (displayName.equals("[전직] 광부 1차")) {
                processJobPurchase(player, 10000, "광부 1차", ChatColor.DARK_PURPLE + "광부 1차로 전직합니다.");
            } else if (displayName.equals("[전직] 광부 2차")) {
                processJobPurchase(player, 30000, "광부 2차", ChatColor.DARK_PURPLE + "광부 2차로 전직합니다.");
            } else if (displayName.equals("[전직] 광부 3차")) {
                processJobPurchase(player, 70000, "광부 3차", ChatColor.DARK_PURPLE + "광부 3차로 전직합니다.");
            } else if (displayName.equals("[전직] 광부 4차")) {
                processJobPurchase(player, 100000, "광부 4차", ChatColor.DARK_PURPLE + "광부 4차로 전직합니다.");
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
        if (player.getInventory().firstEmpty() != -1) { // 인벤토리에 공간이 있을 경우
            player.getInventory().addItem(customItem);
        } else { // 인벤토리에 공간이 없을 경우
            player.getWorld().dropItem(player.getLocation(), customItem);
            player.sendMessage(ChatColor.RED + "인벤토리가 가득 차서 아이템이 바닥에 떨어졌습니다!");
        }
    }

    private void handleMineralSale(Player player, ItemStack clickedItem, ClickType clickType) {
        Material material = clickedItem.getType();
        int leftClickCost = 0, middleClickCost = 0, rightClickCost = 0;

        switch (material) {
            case COAL:
                leftClickCost = 10;
                middleClickCost = 320;
                rightClickCost = 640;
                break;
            case COPPER_INGOT:
                leftClickCost = 20;
                middleClickCost = 640;
                rightClickCost = 1280;
                break;
            case IRON_INGOT:
                leftClickCost = 30;
                middleClickCost = 960;
                rightClickCost = 1920;
                break;
            case GOLD_INGOT:
                leftClickCost = 40;
                middleClickCost = 1280;
                rightClickCost = 2560;
                break;
            case REDSTONE:
                leftClickCost = 5;
                middleClickCost = 160;
                rightClickCost = 320;
                break;
            case LAPIS_LAZULI:
                leftClickCost = 50;
                middleClickCost = 1600;
                rightClickCost = 3200;
                break;
            case EMERALD:
                leftClickCost = 70;
                middleClickCost = 2240;
                rightClickCost = 4480;
                break;
            case DIAMOND:
                leftClickCost = 90;
                middleClickCost = 2880;
                rightClickCost = 5760;
                break;
            case AMETHYST_SHARD:
                leftClickCost = 100;
                middleClickCost = 3200;
                rightClickCost = 6400;
                break;
            case QUARTZ:
                leftClickCost = 120;
                middleClickCost = 3840;
                rightClickCost = 7680;
                break;
            case NETHERITE_INGOT:
                leftClickCost = 150;
                middleClickCost = 4800;
                rightClickCost = 9600;
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

        if (player.getInventory().containsAtLeast(new ItemStack(material), amountToSell)) {
            moneyManager.addBalance(player, salePrice);
            player.sendMessage(ChatColor.GREEN + ""+ amountToSell + "개를 " + salePrice + "원에 판매했습니다.");
            player.getInventory().removeItem(new ItemStack(material, amountToSell));
            updateSalesCount(player, material, amountToSell);
            scoreboardManager.setPlayerScoreboard(player);
        } else {
            player.sendMessage(ChatColor.RED + "판매할 " + material.name() + "이(가) 충분하지 않습니다.");
        }
    }

    private void updateSalesCount(Player player, Material material, int amountSold) {
        Map<Player, Integer> salesMap = null;
        String clueName = "";

        if (material == Material.COPPER_INGOT) {
            salesMap = copperSales;
            clueName = "단서1";
        } else if (material == Material.LAPIS_LAZULI) {
            salesMap = lapisSales;
            clueName = "단서2";
        } else if (material == Material.QUARTZ) {
            salesMap = quartzSales;
            clueName = "단서3";
        }

        if (salesMap != null) {
            int currentSales = salesMap.getOrDefault(player, 0) + amountSold;
            salesMap.put(player, currentSales);
            if (currentSales >= 100) {
                player.sendMessage(ChatColor.GREEN + clueName + "이(가) 개방되었습니다!");
                plugin.saveClueState(player, clueName, true);
            }
        }
    }
}
