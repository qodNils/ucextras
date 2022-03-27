package de.qodnils.ucextras.commands;

import de.qodnils.ucextras.listeners.ServerConnectListener;
import de.qodnils.ucextras.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class EmptyATMCommand extends CommandBase implements IClientCommand {

    private static final Pattern ATM_MSG_PATTERN = Pattern.compile("^ATM [0-9]+: ([0-9]+)\\/100.000\\$$");
    private static long startedTime;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!ServerConnectListener.connectedUC || System.currentTimeMillis() - startedTime > 200L)
            return;
        final EntityPlayerSP p = Minecraft.getMinecraft().player;
        String msg = e.getMessage().getUnformattedText();
        Matcher ATM_MSG_MATCHER = ATM_MSG_PATTERN.matcher(msg);

        if (ATM_MSG_MATCHER.find()) {
            p.sendChatMessage("/bank abbuchen " + ATM_MSG_MATCHER.group(1));
        }
    }

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    public String getName() {
        return "emptyatm";
    }

    public String getUsage(ICommandSender sender) {
        return "/emptyatm";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer) || !ServerConnectListener.connectedUC)
            return;
        if (args.length != 0) {
            TextUtils.sendMessage(true, getUsage(sender));
            return;
        }
        final EntityPlayerSP p = Minecraft.getMinecraft().player;
        p.sendChatMessage("/atminfo");
        startedTime = System.currentTimeMillis();
    }
}