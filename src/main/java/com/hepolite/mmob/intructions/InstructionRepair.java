package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InstructionRepair extends Instruction {
    public InstructionRepair() {
        super("Repair", 0);
    }

    @Override
    protected void addSyntax(final List<String> syntaxes) {
        syntaxes.add("");
    }

    @Override
    protected void addDescription(final List<String> descriptions) {
        descriptions.add("Repairs the item that is held");
    }

    @Override
    protected String getExplanation() {
        return "If used by a player, the item the player is currently holding in the hands will be repaired, provided the item can be repaired.";
    }

    @Override
    protected boolean onInvoke(final CommandSender sender, final List<String> arguments) {
        // Only players can use this instruction, and at least one argument must be provided
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by a player");
            return true;
        }

        // Player must hold an item in the hand
        final Player player = (Player) sender;
        final ItemStack itemInHand = player.getEquipment().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            sender.sendMessage("§cYou must hold an item in your hand");
            return true;
        }

        // Repair the item, if possible
        if (itemInHand.getType().getMaxDurability() != 0) {
            itemInHand.setDurability((short) 0);
            player.getEquipment().setItemInMainHand(itemInHand);
            sender.sendMessage("Repaired the item that was held! Enjoy!");
        } else
            sender.sendMessage("§cYour item can't be repaired!");
        return false;
    }
}
