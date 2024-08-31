package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.code.rpg.RPG;

public class PluginHelpCommand implements CommandExecutor {
    private RPG plugin;

    // 생성자를 통해 플러그인을 주입받습니다.
    public PluginHelpCommand(RPG plugin) {
        this.plugin = plugin;
    }

    // 도움말 메시지를 상수로 정의합니다.
    private static final String HELP_MESSAGE = ChatColor.translateAlternateColorCodes('&',
            "MinerSurvival 플러그인은 '광부로 살아남기' 플러그인입니다.\n" +
                    "이 플러그인에서 사용할 수 있는 명령어는 다음과 같습니다.\n" +
                    " \n" +
                    "- 사용 가능한 일반 명령어 목록 -\n\n" +
                    " \n" +
                    "&e/돈 (플레이어 닉네임)\n&f: 해당 플레이어의 잔액을 확인합니다.\n" +
                    " \n" +
                    "&e/돈 송금 (플레이어 닉네임) (송금할 금액)\n&f: 해당 플레이어에게 돈을 송금합니다.\n" +
                    " \n" +
                    "&e/메뉴\n&f: 다양한 기능이 있는 메뉴를 오픈합니다.\n" +
                    " \n" +
                    " \n" +
                    "- 사용 가능한 op 명령어 목록 -\n\n" +
                    " \n" +
                    "&e/돈 입금 (플레이어 닉네임) (입금할 금액)\n&f: 해당 플레이어에게 서버 계좌에서 돈을 입금합니다.(op 명령어)\n" +
                    " \n" +
                    "&e/돈 출금 (플레이어 닉네임) (출금할 금액)\n&f: 해당 플레이어의 잔액에서 돈을 출금합니다.(op 명령어)\n" +
                    " \n" +
                    "&e/광부 (1~4차)\n&f: 광부 전직책을 지급합니다.(op 명령어)\n" +
                    " \n" +
                    "&e/이름변경권\n&f: 이름변경권을 지급합니다.(op 명령어)\n" +
                    " \n" +
                    " \n" +
                    "&e버그는 Discord : leeke_0 으로 연락 주세요."
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 명령어를 플레이어가 호출했는지 확인합니다.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }

        // '도움말' 명령어인지와 인자가 없는지를 확인합니다.
        if (command.getName().equalsIgnoreCase("도움말") && args.length == 0) {
            Player player = (Player) sender;
            player.sendMessage(HELP_MESSAGE);  // 상수로 정의한 메시지를 보냅니다.
        }
        return true;
    }
}