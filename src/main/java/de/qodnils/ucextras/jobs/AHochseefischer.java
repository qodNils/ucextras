package de.qodnils.ucextras.jobs;

import de.qodnils.ucextras.listeners.ServerConnectListener;
import de.qodnils.ucextras.utils.ConfigUtils;
import de.qodnils.ucextras.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AHochseefischer {
    private static final String fprefix = "[Fischer] ";
    private static final String catchfish = fprefix + "Fahre nun zu den Fischschwärmen und wirf dein Fischenetz mit /catchfish aus.";
    private static final String findswarm = fprefix + "Mit /findschwarm kannst du dir den nächsten Fischschwarm anzeigen lassen.";
    private static final String thrownet = fprefix + "Du hast ein Fischernetz ausgeworfen.";
    private static final String waithere = fprefix + "Warte nun hier bis du die Fische gefangen hast.";
    private static final String lostnet = fprefix + "Du hast das Fischernetz verloren...";
    private static final String foundswarm = fprefix + "Du hast einen Fischschwarm gefunden!";
    private static final String throwavailable = fprefix + "Wirf nun dein Netz mit /catchfish aus.";
    private static final String leftswarm = fprefix + "Du hast dich dem Fischschwarm zu weit entfernt.";
    private static final String nonets = fprefix + "Du hast keine Netze mehr. Bring den gefangenen Fisch zurück zum Steg.";
    private static final String dropfisch = fprefix + "Dort kannst du den Fisch mit /dropfisch abgeben";
    private static final Pattern FISH_RECEIVED_PATTERN = Pattern.compile("^\\[Fischer] Du hast ([0-9]+)kg frischen Fisch gefangen! \\(([0-9]+)kg\\)$");
    private static final Pattern FINAL_FISH_PATTERN = Pattern.compile("^\\[Fischer] Du hast ([0-9]+)kg Fisch gefangen\\.$");
    public static boolean started;
    public static boolean netout;
    public static boolean inswarm;
    public static int netcount = 0;


    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!ServerConnectListener.connectedUC)
            return;

        String msg = e.getMessage().getUnformattedText();
        final EntityPlayerSP p = Minecraft.getMinecraft().player;

        if (msg.equals(catchfish)) {
            started = true;
            e.setMessage(TextUtils.addPrefix("§9Hochseefischer gestartet!"));
            return;
        }
        if (msg.equals(throwavailable)) {
            inswarm = true;
            if (!netout)
                p.sendChatMessage("/catchfish");
            e.setMessage(TextUtils.addPrefix("§9Fischschwarm erkannt!"));
            return;
        }
        Matcher FISH_RECEIVED_MATCHER = FISH_RECEIVED_PATTERN.matcher(msg);
        if (FISH_RECEIVED_MATCHER.find()) {
            if (inswarm)
                p.sendChatMessage("/catchfish");
            e.setMessage(TextUtils.addPrefix("§9Du hast §6"
                    + FISH_RECEIVED_MATCHER.group(1)
                    + "kg §9Fisch gefangen! §7(§6"
                    + FISH_RECEIVED_MATCHER.group(2)
                    + "kg§7)"));
            return;
        }
        if (msg.equals(lostnet)) {
            if (inswarm)
                p.sendChatMessage("/catchfish");
            e.setMessage(TextUtils.addPrefix("§cFischernetz verloren!"));
            return;
        }
        if (msg.equals(leftswarm)) {
            inswarm = false;
            e.setMessage(TextUtils.addPrefix("§cFischschwarm verlassen!"));
            return;
        }
        if (msg.equals(thrownet)) {
            inswarm = false;
            netout = true;
            netcount++;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    netout = false;
                    if (netcount != 5 && inswarm)
                        p.sendChatMessage("/catchfish");
                }
            }, 20100L);

            e.setMessage(TextUtils.addPrefix("§9Fischernetz ausgeworfen!"));
            return;
        }
        if (msg.equals(dropfisch)) {
            p.sendChatMessage("/dropfisch");
            e.setCanceled(true);
            return;
        }
        Matcher FINAL_FISH_MATCHER = FINAL_FISH_PATTERN.matcher(msg);
        if (FINAL_FISH_MATCHER.find()) {
            inswarm = false;
            netout = false;
            started = false;
            netcount = 0;
            e.setMessage(TextUtils.addPrefix("§9Hochseefischer erfolgreich beendet! §7(§6"
                    + FINAL_FISH_MATCHER.group(1)
                    + "kg§7)"));
            return;
        }
        if (msg.equals(findswarm) || msg.equals(foundswarm) || msg.equals(waithere) || msg.equals(nonets)) {
            e.setCanceled(true);
        }
    }
}