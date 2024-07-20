package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;
import org.example.code.rpg.RPG;

public class MoneyCommand implements CommandExecutor {
    private RPG plugin;
    public MoneyCommand(RPG plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MoneyManager moneyManager = new MoneyManager(this.plugin);
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }
        if (command.getName().equals("돈") && args.length == 0) {
            Player player = ((Player) sender).getPlayer();
            player.sendMessage("현재 잔액 : " + moneyManager.getBalance(player));
        }

        Player player = (Player) sender;

        //   /돈 입금 (유저 이름) (금액)
        // 돈 = (얜 그냥 명령어), 입금 = args[0], 유저 이름 = args[1], 금액 = args[2]

        // 플레이어가 op라면
        if (args.length == 3) {
            Player targetPlayer = plugin.getServer().getPlayer(args[1]);
            if (command.getName().equals("돈") && args[0].equals("입금")) {
                // 서버에 있는 플레이어들 중에서 해당 이름을 찾을 수 없다면
                if (targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                    return true;
                }
                try {
                    if (player.isOp()) {
                        double sendMoney = Double.parseDouble(args[2]); //args[2]를 double로 변환한 걸 money에 저장
                        //만약 입금할 금액이 양수가 아니면
                        if (sendMoney < 0) {
                            player.sendMessage(ChatColor.RED + "입금할 금액은 양수로 입력해주세요.");
                            return true;
                        }
                        moneyManager.addBalance(targetPlayer, sendMoney); //타겟 플레이어의 잔액에 sendMoney만큼의 금액을 추가
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a성공적으로 &e" + sendMoney + "&a원을 &e" + targetPlayer.getName() + "&a에게 입금했습니다."));
                        targetPlayer.sendMessage(ChatColor.YELLOW + Double.toString(sendMoney) + ChatColor.GREEN + "이(가) " + ChatColor.YELLOW + "입금" + ChatColor.GREEN + "되었습니다.");
                    } else {
                        player.sendMessage(ChatColor.RED + "이 명령어는 op만 사용 가능한 명령어 입니다.");
                    }
                    // 스코어보드 업데이트
                    if (targetPlayer.isOnline()) {
                        PlayerScoreboardManager scoreboardManager = new PlayerScoreboardManager(plugin);
                        scoreboardManager.setPlayerScoreboard(targetPlayer);
                    }
                    // 유효하지 않은 형식의 문자열(숫자를 제외한 한글, 영어 등)
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "금액 형식이 올바르지 않습니다.");
                }
            } else if(command.getName().equals("돈") && args[0].equals("송금")) {
                if(targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                    return true;
                }
                // 자기 자신에게는 송금 못하도록 설정
                if (player.equals(targetPlayer)) {
                    player.sendMessage(ChatColor.RED + "자기 자신에게는 송금할 수 없습니다.");
                    return true;
                }
                try {
                    double transferAmount = Double.parseDouble(args[2]);
                    if (transferAmount < 0) {
                        player.sendMessage(ChatColor.RED + "송금할 금액은 양수로 입력해주세요.");
                        return true;
                    }
                    // 송금하는 플레이어의 잔액을 확인
                    if (moneyManager.getBalance(player) < transferAmount) {
                        player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                        return true;
                    }
                    // 송금하는 플레이어의 잔액에서 차감
                    moneyManager.subtractBalance(player, transferAmount);
                    // 타겟 플레이어의 잔액에 추가
                    moneyManager.addBalance(targetPlayer, transferAmount);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a성공적으로 &e" + transferAmount + "&a원을 &e" + targetPlayer.getName() + "&a에게 송금했습니다."));
                    targetPlayer.sendMessage(ChatColor.YELLOW + Double.toString(transferAmount) + ChatColor.GREEN + "이(가) " + ChatColor.YELLOW + "송금" + ChatColor.GREEN + "되었습니다.");

                    // 스코어보드 업데이트
                    if (targetPlayer.isOnline()) {
                        PlayerScoreboardManager scoreboardManager = new PlayerScoreboardManager(plugin);
                        scoreboardManager.setPlayerScoreboard(targetPlayer);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "금액 형식이 올바르지 않습니다.");
                }
            } else if(command.getName().equals("돈") && args[0].equals("출금")) {
                // 서버에 있는 플레이어들 중에서 해당 이름을 찾을 수 없다면
                if(targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                    return true;
                }
                try {
                    if(player.isOp()) {
                        double withdrawMoney = Double.parseDouble(args[2]);
                        if (withdrawMoney < 0) {
                            player.sendMessage(ChatColor.RED + "출금할 금액은 양수로 입력해주세요.");
                            return true;
                        }
                        if (moneyManager.getBalance(targetPlayer) < withdrawMoney) {
                            player.sendMessage(ChatColor.RED + "잔액이 부족합니다.");
                            return true;
                        }
                        moneyManager.subtractBalance(targetPlayer, withdrawMoney);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a성공적으로 &e" + withdrawMoney + "&a원을 &e" + targetPlayer.getName() + "&a의 잔액에서 출금했습니다."));
                        targetPlayer.sendMessage(ChatColor.RED + Double.toString(withdrawMoney) + ChatColor.GREEN + "이(가) " + ChatColor.RED + "출금" + ChatColor.GREEN + "되었습니다.");
                    } else {
                        player.sendMessage(ChatColor.RED + "이 명령어는 op만 사용 가능한 명령어 입니다.");
                    }
                    // 스코어보드 업데이트
                    if (targetPlayer.isOnline()) {
                        PlayerScoreboardManager scoreboardManager = new PlayerScoreboardManager(plugin);
                        scoreboardManager.setPlayerScoreboard(targetPlayer);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "금액 형식이 올바르지 않습니다.");
                }
                return true;

            }
        }
        return true;
    }
}