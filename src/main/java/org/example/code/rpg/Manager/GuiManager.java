package org.example.code.rpg.Manager;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.structure.StructureManager;
import org.example.code.rpg.RPG;

import java.util.*;

public class GuiManager {
    private final RPG plugin;

    public GuiManager(RPG plugin) {
        this.plugin = plugin;
    }

    public void openGui(Player player) {
        Inventory basicsInventory = Bukkit.createInventory(null, 27, "메뉴");

        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemStack itemStack1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemStack itemStack2 = new ItemStack(Material.NETHERITE_PICKAXE, 1);
        ItemStack itemStack3 = new ItemStack(Material.PAPER, 1);
        ItemStack itemStack4 = new ItemStack(Material.WRITABLE_BOOK, 1);

        setDisplayName(itemStack1, ChatColor.YELLOW + "" + ChatColor.BOLD + "전직");
        setDisplayName(itemStack2, ChatColor.GOLD + "" + ChatColor.BOLD + "광물");
        setDisplayName(itemStack3, ChatColor.GRAY + "" + ChatColor.BOLD + "단서");
        setDisplayName(itemStack4, ChatColor.GREEN + "" + ChatColor.BOLD + "/도움말");

        for (int i = 0; i < 10; i++) {
            basicsInventory.setItem(i, itemStack);
        }
        basicsInventory.setItem(10, itemStack1);
        basicsInventory.setItem(12, itemStack2);
        basicsInventory.setItem(14, itemStack3);
        basicsInventory.setItem(16, itemStack4);
        basicsInventory.setItem(17, itemStack);

        for (int i = 18; i < 27; i++) {
            basicsInventory.setItem(i, itemStack);
        }

        player.openInventory(basicsInventory);
    }

    private void setDisplayName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
    }

    public void jobShop(Player player) {
        Inventory jobShopInventory = Bukkit.createInventory(null, 45, "전직 상점");

        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);

        JobConfigManager jobConfigManager = plugin.getJobConfig();
        ItemStack customItem1 = createCustomItemForGUI(player, "광부", "1차", 10000, "원");
        ItemStack customItem2 = createCustomItemForGUI(player, "광부", "2차", 30000, "원");
        ItemStack customItem3 = createCustomItemForGUI(player, "광부", "3차", 70000, "원");
        ItemStack customItem4 = createCustomItemForGUI(player, "광부", "4차", 100000, "원");

        for (int i = 0; i < 10; i++) {
            jobShopInventory.setItem(i, itemStack);
        }
        jobShopInventory.setItem(17, itemStack);
        jobShopInventory.setItem(18, itemStack);

        jobShopInventory.setItem(19, customItem1);
        jobShopInventory.setItem(21, customItem2);
        jobShopInventory.setItem(23, customItem3);
        jobShopInventory.setItem(25, customItem4);

        jobShopInventory.setItem(26, itemStack);
        jobShopInventory.setItem(27, itemStack);
        jobShopInventory.setItem(35, itemStack);
        for (int i = 36; i < 45; i++) {
            jobShopInventory.setItem(i, itemStack);
        }
        player.openInventory(jobShopInventory);
    }

    private ItemStack createCustomItemForGUI(Player player, String command, String job, int cost, String unit) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK); // 인챈트된 책으로 커스텀아이템을 생성
        ItemMeta customItemData = customItem.getItemMeta(); // 위에서 생성된 아이템의 데이터를 커스텀아이템데이터로 불러옴.
        String jobColor = "";

        // setDisplayName으로 아이템 이름 설정
        if (command.equals("광부")) {
            jobColor = "&7&l";
        }
        customItemData.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l[전직] " + "&r" + jobColor + command + " " + job));

        // 커스텀아이템데이터 설명을 저장할 리스트를 추가함으로써 기존의 아이템 설명을 덮어씀.
        // 즉, 커스텀아이템데이터의 새로운 설명을 저장하는 게 customItemExplain임.
        List<String> customItemExplain = new ArrayList<>();
        customItemExplain.add(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + command + " " + job + "로 전직합니다."); // 전직책 설명 첫번째 줄
        customItemExplain.add(ChatColor.RESET + "" + ChatColor.YELLOW + "" + ChatColor.BOLD + "가격 : " + ChatColor.RESET + "" + ChatColor.YELLOW + cost + "" + unit); // 전직책 설명 두번째 줄
        customItemData.setLore(customItemExplain); // 커스텀아이템데이터에 커스텀아이템설명을 설정(아직 커스텀 아이템에 커스텀한 설명을 저장 안함)
        customItem.setItemMeta(customItemData); // 커스텀아이템에 커스텀아이템데이터에 저장된 값을 설정함.
        return customItem; // 커스텀 아이템을 반환
    }

    public void mineralShop(Player player) {
        Inventory mineralShopInventory = Bukkit.createInventory(null, 45, "광물 상점");

        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);

        ItemStack itemStack1 = new ItemStack(Material.COAL, 1);
        ItemStack itemStack2 = new ItemStack(Material.COPPER_INGOT, 1);
        ItemStack itemStack3 = new ItemStack(Material.IRON_INGOT, 1);
        ItemStack itemStack4 = new ItemStack(Material.GOLD_INGOT, 1);
        ItemStack itemStack5 = new ItemStack(Material.REDSTONE, 1);
        ItemStack itemStack6 = new ItemStack(Material.LAPIS_LAZULI, 1);
        ItemStack itemStack7 = new ItemStack(Material.EMERALD, 1);
        ItemStack itemStack8 = new ItemStack(Material.DIAMOND, 1);
        ItemStack itemStack9 = new ItemStack(Material.AMETHYST_SHARD, 1);
        ItemStack itemStack10 = new ItemStack(Material.QUARTZ, 1);
        ItemStack itemStack11 = new ItemStack(Material.NETHERITE_INGOT, 1);

        setItemMeta(itemStack1, "석탄", 10, 320, 640);
        setItemMeta(itemStack2, "구리 주괴", 20, 640, 1280);
        setItemMeta(itemStack3, "철 주괴", 30, 960, 1920);
        setItemMeta(itemStack4, "금 주괴", 40, 1280, 2560);
        setItemMeta(itemStack5, "레드스톤 가루", 5, 160, 320);
        setItemMeta(itemStack6, "청금석", 50, 1600, 3200);
        setItemMeta(itemStack7, "에메랄드", 70, 2240, 4480);
        setItemMeta(itemStack8, "다이아몬드", 90, 2880, 5760);
        setItemMeta(itemStack9, "자수정 조각", 100, 3200, 6400);
        setItemMeta(itemStack10, "네더 석영", 120, 3840, 7680);
        setItemMeta(itemStack11, "네더라이트 주괴", 150, 4800, 9600);

        for (int i = 0; i < 10; i++) {
            mineralShopInventory.setItem(i, itemStack);
        }
        mineralShopInventory.setItem(10, itemStack1);
        mineralShopInventory.setItem(12, itemStack2);
        mineralShopInventory.setItem(14, itemStack3);
        mineralShopInventory.setItem(16, itemStack4);
        mineralShopInventory.setItem(17, itemStack);
        mineralShopInventory.setItem(18, itemStack);
        mineralShopInventory.setItem(20, itemStack5);
        mineralShopInventory.setItem(22, itemStack6);
        mineralShopInventory.setItem(24, itemStack7);
        mineralShopInventory.setItem(26, itemStack);
        mineralShopInventory.setItem(27, itemStack);
        mineralShopInventory.setItem(28, itemStack8);
        mineralShopInventory.setItem(30, itemStack9);
        mineralShopInventory.setItem(32, itemStack10);
        mineralShopInventory.setItem(34, itemStack11);
        mineralShopInventory.setItem(35, itemStack);
        for (int i = 36; i < 45; i++) {
            mineralShopInventory.setItem(i, itemStack);
        }
        player.openInventory(mineralShopInventory);
    }

    private void setItemMeta(ItemStack itemStack, String displayName, int leftClickCost, int middleClickCost, int rightClickCost) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + displayName);
            meta.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            List<String> lore = createLoreList(leftClickCost, middleClickCost, rightClickCost);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
    }

    private List<String> createLoreList(int leftClickCost, int middleClickCost, int rightClickCost) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "" + ChatColor.BOLD + " [마우스 좌클릭] " + ChatColor.RESET + "" + ChatColor.YELLOW + "1개 판매 : " + leftClickCost + "원 ");
        lore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "" + ChatColor.BOLD + " [마우스 휠클릭] " + ChatColor.RESET + "" + ChatColor.YELLOW + "32개 판매 : " + middleClickCost + "원 ");
        lore.add(ChatColor.RESET + "" + ChatColor.YELLOW + "" + ChatColor.BOLD + " [마우스 우클릭] " + ChatColor.RESET + "" + ChatColor.YELLOW + "64개 판매 : " + rightClickCost + "원 ");
        return lore;
    }

    public void clues(Player player, boolean clue1Unlocked, boolean clue2Unlocked, boolean clue3Unlocked) {
        Inventory cluesInventory = Bukkit.createInventory(null, 27, "해결 단서");

        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemStack itemStack1 = new ItemStack(Material.PAPER, 1);
        ItemStack itemStack2 = new ItemStack(Material.PAPER, 1);
        ItemStack itemStack3 = new ItemStack(Material.PAPER, 1);
        ItemStack itemStack4 = new ItemStack(Material.NETHER_STAR, 1);

        ItemMeta meta1 = itemStack1.getItemMeta();
        ItemMeta meta2 = itemStack2.getItemMeta();
        ItemMeta meta3 = itemStack3.getItemMeta();
        ItemMeta meta4 = itemStack4.getItemMeta();

        if (clue1Unlocked && meta1 != null) {
            meta1.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "단서1");
            List<String> lore1 = new ArrayList<>();
            lore1.add(ChatColor.DARK_PURPLE + "제단은 월드 스폰 좌표 기준으로 ±3000 좌표 이내에 있습니다.");
            lore1.add(ChatColor.RED + "모든 단서를 개방해야 제단이 생성됩니다.");
            meta1.setLore(lore1);
            itemStack1.setItemMeta(meta1);
        }

        if (clue2Unlocked && meta2 != null) {
            meta2.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "단서2");
            int xCoordinate = plugin.getConfig().getInt("structures.ancient_altar.nearest.x", 0);
            List<String> lore2 = new ArrayList<>();
            lore2.add(ChatColor.DARK_PURPLE + "가장 가까운 제단의 x좌표는 " + xCoordinate + "입니다.");
            lore2.add(ChatColor.RED + "모든 단서를 개방해야 제단이 생성됩니다.");
            meta2.setLore(lore2);
            itemStack2.setItemMeta(meta2);
        }

        if (clue3Unlocked && meta3 != null) {
            meta3.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "단서3");
            int zCoordinate = plugin.getConfig().getInt("structures.ancient_altar.nearest.z", 0);
            List<String> lore3 = new ArrayList<>();
            lore3.add(ChatColor.DARK_PURPLE + "가장 가까운 제단의 z좌표는 " + zCoordinate + "입니다.");
            lore3.add(ChatColor.RED + "모든 단서를 개방해야 제단이 생성됩니다.");
            meta3.setLore(lore3);
            itemStack3.setItemMeta(meta3);
        }


        if (meta4 != null) {
            meta4.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "미개방");
            meta4.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta4.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta4.setLore(Collections.singletonList(ChatColor.DARK_PURPLE + "특정 판매 조건을 완료하면 단서가 개방됩니다."));
            itemStack4.setItemMeta(meta4);
        }

        for (int i = 0; i < 10; i++) {
            cluesInventory.setItem(i, itemStack);
        }
        cluesInventory.setItem(11, clue1Unlocked ? itemStack1 : itemStack4);
        cluesInventory.setItem(13, clue2Unlocked ? itemStack2 : itemStack4);
        cluesInventory.setItem(15, clue3Unlocked ? itemStack3 : itemStack4);
        cluesInventory.setItem(17, itemStack);

        for (int i = 18; i < 27; i++) {
            cluesInventory.setItem(i, itemStack);
        }

        player.openInventory(cluesInventory);
    }
}