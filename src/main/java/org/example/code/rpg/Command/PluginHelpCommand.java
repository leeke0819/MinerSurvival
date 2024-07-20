package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.code.rpg.RPG;

public class PluginHelpCommand implements CommandExecutor {
    private RPG plugin;
    public PluginHelpCommand(RPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }
        if (command.getName().equals("도움말") && args.length == 0) {
            Player player = (Player) sender;
            String[] s1 = new String[10];
            s1[0] = "MinerSurvival 플러그인은 '광부로 살아남기' 플러그인입니다.\n";
            s1[1] = "이 플러그인에서 사용할 수 있는 명령어는 다음과 같습니다.\n";
            s1[2] = "&e/돈\n&f: 현재 플레이어의 잔액을 확인합니다.\n\n";
            s1[3] = "&e/돈 입금 (플레이어 닉네임) (입금할 금액)\n&f: 해당 플레이어에게 무한의 돈이 있는 계좌에서 돈을 입금합니다.(이 명령어는 op 명령어 입니다.)\n\n";
            s1[4] = "&e/돈 송금 (플레이어 닉네임) (송금할 금액)\n&f: 해당 플레이어에게 돈을 송금합니다.\n\n";
            s1[5] = "&e/돈 출금 (플레이어 닉네임) (출금할 금액)\n&f: 해당 플레이어의 잔액에서 돈을 출금합니다.(이 명령어는 op 명령어 입니다.)\n\n";
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s1[0] + s1[1] + s1[2] + s1[3] + s1[4] + s1[5]));
        }
        return true;
    }
}