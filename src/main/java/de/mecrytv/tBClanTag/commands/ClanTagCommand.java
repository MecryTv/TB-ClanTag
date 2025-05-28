package de.mecrytv.tBClanTag.commands;

import de.mecrytv.tBClanTag.TBClanTag;
import de.mecrytv.tBClanTag.database.ClanTags.ClanTagInfo;
import de.mecrytv.tBClanTag.database.ClanTags.ClanTagManager;
import de.mecrytv.tBClanTag.utils.GenerateClanTagId;
import de.mecrytv.tBClanTag.utils.McColorCodes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanTagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage(McColorCodes.RED + "Usage: /tag <create|delete|edit|list|leave>");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 3) {
                player.sendMessage(McColorCodes.RED + "Usage: /tag create <name> <color>");
                return true;
            }

            ClanTagManager manager = TBClanTag.getInstance().getClanTagManager();

            String playerUUID = player.getUniqueId().toString();

            if (manager.isUserInAnyClanTag(playerUUID)) {
                player.sendMessage(TBClanTag.getInstance().getPrefix()
                        + McColorCodes.RED + "Du hast bereits einen Tag! "
                        + "Bitte lösche oder verlasse zuerst deinen bestehenden Tag.");
                return true;
            }

            String name = args[1];
            String color = args[2].toUpperCase();
            String randomId = GenerateClanTagId.generate();

            ClanTagInfo info = new ClanTagInfo(randomId, name, color, playerUUID);
            manager.createClanTag(info);

            player.sendMessage(TBClanTag.getInstance().getPrefix() + McColorCodes.GREEN + "Clan tag created: " + McColorCodes.YELLOW + name + " " + McColorCodes.GRAY + "(" + McColorCodes.AQUA + color + McColorCodes.GRAY + ")");
            return true;
        }

        return false;
    }
}
