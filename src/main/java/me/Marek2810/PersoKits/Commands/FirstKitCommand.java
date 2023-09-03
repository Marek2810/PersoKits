package me.Marek2810.PersoKits.Commands;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FirstKitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("only-player-command")));
            return true;
        }
        Player p = (Player) sender;
        if (!PersoKits.firstJoinKitStatus) {
            p.sendMessage("first join disabled.");
            return true;
        }
        p.performCommand("kit " + PersoKits.firstJoinKit.getName());
        return true;
    }
}
