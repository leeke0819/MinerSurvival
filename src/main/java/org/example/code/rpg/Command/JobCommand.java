package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.RPG;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class JobCommand implements CommandExecutor {
    private RPG plugin;
    public JobCommand(RPG plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }
        getLogger().info("1");
        if (args == null) {
            sender.sendMessage(ChatColor.RED + "전직할 직업과 전직 레벨을 입력해주세요.");
        }
        String command2 = command.getName();
        String job = args[0];
        Player player = (Player) sender;
        getLogger().info("2");
        createCustomItem(player, command2, job);
        return true;
    }

    private void createCustomItem(Player player, String command,String job) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK); // 인챈트된 책으로 커스텀아이템을 생성
        ItemMeta customItemData = customItem.getItemMeta(); // 위에서 생성된 아이템의 데이터를 커스텀아이템데이터로 불러옴.
        String jobColor = "";
        getLogger().info("3");

        // setDisplayName으로 아이템 이름 설정
        switch (command) {
            case "광부":
                jobColor = "&7&l";
                break;
            case "나무꾼":
                jobColor = "&6&l";
                break;
            case "농부":
                jobColor = "&a&l";
                break;
            case "사냥꾼":
                jobColor = "&3&l";
                break;
            case "어부":
                jobColor = "&b&l";
                break;
            case "연금술사":
                jobColor = "&d&l";
                break;
            default:
                break;
        }
        getLogger().info("4");
        customItemData.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l[전직] " + "&r" + jobColor + command + " " + job));
        // 커스텀아이템데이터 설명을 저장할 리스트를 추가함으로써 기존의 아이템 설명을 덮어씀.
        // 즉, 커스텀아이템데이터의 새로운 설명을 저장하는 게 customItemExplain임.
        List<String> customItemExplain = new ArrayList<>();
        customItemExplain.add(command + " " + job + "로 전직합니다.");
        customItemData.setLore(customItemExplain); // 커스텀아이템데이터에 커스텀아이템설명을 설정(아직 커스텀 아이템에 커스텀한 설명을 저장 안함)
        customItem.setItemMeta(customItemData); // 커스텀아이템에 커스텀아이템데이터에 저장된 값을 설정함.
        getLogger().info(player.getName());
        player.getInventory().addItem(customItem); // 플레이어 인벤토리에 커스텀 아이템 지급
        // customItemData.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l[전직] " + "&r&7광부 1차"));
    }

    public void giveCustomItemToPlayer(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            // 인벤토리가 가득 찼을 경우
            player.getWorld().dropItem(player.getLocation(), item);
            player.sendMessage(ChatColor.RED + "인벤토리가 가득 차서 아이템이 바닥에 떨어졌습니다!");
        } else {
            // 인벤토리에 공간이 있을 경우
            player.getInventory().addItem(item);
        }
    }

}