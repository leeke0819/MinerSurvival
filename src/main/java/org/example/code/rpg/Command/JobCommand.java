package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.Manager.JobConfigManager;
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
        if (args == null) {
            sender.sendMessage(ChatColor.RED + "전직할 직업과 전직 레벨을 입력해주세요.");
        }
        String command2 = command.getName();
        String job = args[0];
        Player player = (Player) sender;
        JobConfigManager jobConfigManager = new JobConfigManager(plugin);
        jobConfigManager.createCustomItem(player, command2, job);
        return true;
    }
}