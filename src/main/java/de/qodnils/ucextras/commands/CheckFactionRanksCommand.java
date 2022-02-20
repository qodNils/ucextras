package de.qodnils.ucextras.commands;

import de.qodnils.ucextras.listeners.ServerConnectListener;
import de.qodnils.ucextras.utils.ForgeUtils;
import de.qodnils.ucextras.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CheckFactionRanksCommand extends CommandBase implements IClientCommand {

    public static long startedTime;
    public static String[] checkedPlayers;
    public static int checkedPlayersIndex = 0;
    private static final Pattern FACTION_PLAYER_PATTERN = Pattern.compile("^ » ((?:\\[UC])?\\w+) (\\w+|O'Brien Familie|Calderón Kartell|Kerzakov Familie|La Cosa Nostra|Westside Ballas|News Agency) Rang (\\d)(?: \\| ((?:Nicht )?(?i)im Dienst))?$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!ServerConnectListener.connectedUC || System.currentTimeMillis() - startedTime > 200L)
            return;
        String msg = e.getMessage().getUnformattedText();
        if (msg.equals("Spieler nicht gefunden.")) {
            e.setCanceled(true);
            checkedPlayersIndex++;
        }
        if (msg.equals("Der Spieler ist in keiner Fraktion.")) {
            e.setMessage(TextUtils.addPrefix(checkedPlayers[checkedPlayersIndex] + " ➟ Zivilist"));
            checkedPlayersIndex++;
        }
        Matcher FACTION_PLAYER_MATCHER = FACTION_PLAYER_PATTERN.matcher(msg);
        if (FACTION_PLAYER_MATCHER.find()) {
            if (FACTION_PLAYER_MATCHER.group(4) != null) {
                msg = FACTION_PLAYER_MATCHER.group(1)
                        + " ➟ "
                        + FACTION_PLAYER_MATCHER.group(2)
                        + " ["
                        + FACTION_PLAYER_MATCHER.group(3)
                        +"] | "
                        + FACTION_PLAYER_MATCHER.group(4);
            } else
                msg = FACTION_PLAYER_MATCHER.group(1)
                        + " ➟ "
                        + FACTION_PLAYER_MATCHER.group(2)
                        + " ["
                        + FACTION_PLAYER_MATCHER.group(3)
                        +"]";
            e.setMessage(TextUtils.addPrefix(msg));
            checkedPlayersIndex++;
        }
    }

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    public String getName() {
        return "checkfactionranks";
    }

    public String getUsage(ICommandSender sender) {
        return "/checkfactionranks [Spieler...]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer) || !ServerConnectListener.connectedUC)
            return;
        if (args.length < 1) {
            TextUtils.sendMessage(true, getUsage(sender));
            return;
        }
        final EntityPlayerSP p = Minecraft.getMinecraft().player;
        checkedPlayers = args;
        if (args.length > 4) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                private int i = 0;

                @Override
                public void run() {
                    if (i == args.length) {
                        cancel();
                        return;
                    }
                    startedTime = System.currentTimeMillis();
                    p.sendChatMessage("/memberinfo " + args[i]);
                    i++;
                }
            },0L, 1000L);
        } else {
            startedTime = System.currentTimeMillis();
            for (String player : args) {
                p.sendChatMessage("/memberinfo " + player);
            }
        }
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        String inputLowerCase = args[args.length - 1].toLowerCase();

        List<String> completions = new ArrayList<>(ForgeUtils.getOnlinePlayers());
        completions.removeIf(name -> !name.toLowerCase().startsWith(inputLowerCase));

        return completions;
    }
}