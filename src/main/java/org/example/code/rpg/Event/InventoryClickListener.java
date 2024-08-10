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

public class InventoryClickListener implements Listener {
    private RPG plugin;
    private GuiManager guiManager;
    private MoneyManager moneyManager;
    private PlayerScoreboardManager scoreboardManager;

    public InventoryClickListener(RPG plugin, GuiManager guiManager, MoneyManager moneyManager, PlayerScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.moneyManager = moneyManager;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.equals("메뉴") && !inventoryTitle.equals("전직 상점") && !inventoryTitle.equals("광물 상점")) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // 플레이어가 자신의 인벤토리의 아이템을 클릭하면 클릭 취소 안되게 하기
        if (event.getClickedInventory() == player.getInventory()) {
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
                int cost = 10000;
                if (moneyManager.getBalance(player) >= cost) {
                    moneyManager.subtractBalance(player, cost);
                    player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

                    ItemStack customItem = createCustomItem("광부 1차", ChatColor.DARK_PURPLE + "광부 1차로 전직합니다.");
                    addCustomItemToPlayer(player, customItem);
                    scoreboardManager.setPlayerScoreboard(player);
                } else {
                    player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                }
            } else if (displayName.equals("[전직] 광부 2차")) {
                int cost = 30000;
                if (moneyManager.getBalance(player) >= cost) {
                    moneyManager.subtractBalance(player, cost);
                    player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

                    ItemStack customItem = createCustomItem("광부 2차", ChatColor.DARK_PURPLE + "광부 2차로 전직합니다.");
                    addCustomItemToPlayer(player, customItem);
                    scoreboardManager.setPlayerScoreboard(player);
                } else {
                    player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                }
            } else if (displayName.equals("[전직] 광부 3차")) {
                int cost = 70000;
                if (moneyManager.getBalance(player) >= cost) {
                    moneyManager.subtractBalance(player, cost);
                    player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

                    ItemStack customItem = createCustomItem("광부 3차", ChatColor.DARK_PURPLE + "광부 3차로 전직합니다.");
                    addCustomItemToPlayer(player, customItem);
                    scoreboardManager.setPlayerScoreboard(player);
                } else {
                    player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                }
            } else if (displayName.equals("[전직] 광부 4차")) {
                int cost = 100000;
                if (moneyManager.getBalance(player) >= cost) {
                    moneyManager.subtractBalance(player, cost);
                    player.sendMessage(ChatColor.GREEN + "성공적으로 구매 완료했습니다!");

                    ItemStack customItem = createCustomItem("광부 4차", ChatColor.DARK_PURPLE + "광부 4차로 전직합니다.");
                    addCustomItemToPlayer(player, customItem);
                    scoreboardManager.setPlayerScoreboard(player);
                } else {
                    player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                }
            }
        }
        if (clickedItem.getType() == Material.NETHERITE_PICKAXE && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("광물")) {
                guiManager.mineralShop(player);
            }
        } else if (clickedItem.getType() == Material.COAL && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("석탄")) {
                handleMineralSale(player, clickedItem, 10, 320, 640, event.getClick());
            }
        } else if (clickedItem.getType() == Material.COPPER_INGOT && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("구리 주괴")) {
                handleMineralSale(player, clickedItem, 20, 640, 1280, event.getClick());
            }
        } else if (clickedItem.getType() == Material.IRON_INGOT && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("철 주괴")) {
                handleMineralSale(player, clickedItem, 30, 960, 1920, event.getClick());
            }
        } else if (clickedItem.getType() == Material.GOLD_INGOT && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("금 주괴")) {
                handleMineralSale(player, clickedItem, 40, 1280, 2560, event.getClick());
            }
        } else if (clickedItem.getType() == Material.REDSTONE && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("레드스톤 가루")) {
                handleMineralSale(player, clickedItem, 5, 160, 320, event.getClick());
            }
        } else if (clickedItem.getType() == Material.LAPIS_LAZULI && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("청금석")) {
                handleMineralSale(player, clickedItem, 50, 1600, 3200, event.getClick());
            }
        } else if (clickedItem.getType() == Material.EMERALD && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("에메랄드")) {
                handleMineralSale(player, clickedItem, 70, 2240, 4480, event.getClick());
            }
        } else if (clickedItem.getType() == Material.DIAMOND && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("다이아몬드")) {
                handleMineralSale(player, clickedItem, 90, 2880, 5760, event.getClick());
            }
        } else if (clickedItem.getType() == Material.AMETHYST_SHARD && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("자수정 조각")) {
                handleMineralSale(player, clickedItem, 100, 3200, 6400, event.getClick());
            }
        } else if (clickedItem.getType() == Material.QUARTZ && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("네더 석영")) {
                handleMineralSale(player, clickedItem, 120, 3840, 7680, event.getClick());
            }
        } else if (clickedItem.getType() == Material.NETHERITE_INGOT && clickedItem.hasItemMeta()) {
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (displayName.equals("네더라이트 주괴")) {
                handleMineralSale(player, clickedItem, 150, 4800, 9600, event.getClick());
            }
        }

        // 돌 클릭했을때
        if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone!");
        }
    }

    private ItemStack createCustomItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = customItem.getItemMeta();
        if (meta != null) {
            String displayName = ChatColor.YELLOW + "" + ChatColor.BOLD + "[전직] " + ChatColor.GRAY + "" + ChatColor.BOLD + name;
            meta.setDisplayName(displayName);
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

    private void handleMineralSale(Player player, ItemStack clickedItem, int leftClickCost, int middleClickCost, int rightClickCost, ClickType clickType) {
        int amountToSell = 0;
        int salePrice = 0;

        ItemStack checkItem = new ItemStack(clickedItem.getType());

        switch (clickType) {
            case LEFT:
                if (player.getInventory().containsAtLeast(checkItem, 1)) {
                    amountToSell = 1;
                    salePrice = leftClickCost;
                }
                break;
            case MIDDLE:
                if (player.getInventory().containsAtLeast(checkItem, 32)) {
                    amountToSell = 32;
                    salePrice = middleClickCost;
                }
                break;
            case RIGHT:
                if (player.getInventory().containsAtLeast(checkItem, 64)) {
                    amountToSell = 64;
                    salePrice = rightClickCost;
                }
                break;
            default:
                break;
        }

        if (amountToSell > 0) {
            moneyManager.addBalance(player, salePrice);
            String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            player.sendMessage(ChatColor.GREEN + displayName + " " + amountToSell + "개를 " + salePrice + "원에 판매했습니다.");
            player.getInventory().removeItem(new ItemStack(checkItem.getType(), amountToSell));
            scoreboardManager.setPlayerScoreboard(player);
        } else {
            player.sendMessage(ChatColor.RED + "판매할 " + checkItem.getType().name() + "이(가) 충분하지 않습니다.");
        }
    }
}
