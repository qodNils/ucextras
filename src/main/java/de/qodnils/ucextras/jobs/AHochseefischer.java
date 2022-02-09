package de.qodnils.ucextras.jobs;

import de.qodnils.ucextras.listeners.ServerConnectListener;
import de.qodnils.ucextras.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class AHochseefischer {
    public static boolean started;
    public static boolean netout;
    public static boolean inswarm;

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent e) {
        if (!ServerConnectListener.connectedUC)
            return;

        String msg = e.getMessage().getUnformattedText();
        final EntityPlayerSP p = Minecraft.getMinecraft().player;

        String fprefix = "[Fischer]";
        String catchfish = fprefix + " Fahre nun zu den Fischschwärmen und wirf dein Fischenetz mit /catchfish aus.";
        String findswarm = fprefix + " Mit /findschwarm kannst du dir den nächsten Fischschwarm anzeigen lassen.";
        String thrownet = fprefix + " Du hast ein Fischernetz ausgeworfen.";
        String waithere = fprefix + " Warte nun hier bis du die Fische gefangen hast.";
        String lostnet = fprefix + " Du hast ein Fischernetz verloren...";
        String foundswarm = fprefix + " Du hast einen Fischschwarm gefunden!";
        String throwavailable = fprefix + " Wirf nun dein Netz mit /catchfish aus.";
        String leftswarm = fprefix + " Du hast dich dem Fischschwarm zu weit entfernt.";
        String fishrecieved = "frischen Fisch gefangen!";
        String nonets = fprefix + " Du hast keine Netze mehr. Bring den gefangenen Fisch zurück zum Steg.";
        String dropfisch = fprefix + " Dort kannst du den Fisch mit /dropfisch abgeben";

        if (msg.equals(catchfish)) {
            TextUtils.sendMessage(true, "§2Hochseefischer gestartet!");
            started = true;
            e.setCanceled(true);
        }
        if (msg.equals(throwavailable)) {
            TextUtils.sendMessage(true, "§2Fischschwarm erkannt!");
            inswarm = true;
            if (!netout) {
                p.sendChatMessage("/catchfish");
            }
            e.setCanceled(true);
        }
        if (msg.contains(fprefix) && msg.contains(fishrecieved)) {
            netout = false;
            if (inswarm) {
                p.sendChatMessage("/catchfish");
            }
        }
        if (msg.equals(lostnet)) {
            TextUtils.sendMessage(true, "§4Fischernetz verloren!");
            netout = false;
            if (inswarm) {
                p.sendChatMessage("/catchfish");
            }
            e.setCanceled(true);
        }
        if (msg.equals(leftswarm)) {
            TextUtils.sendMessage(true, "§4Fischschwarm verlassen!");
            inswarm = false;
            e.setCanceled(true);
        }
        if (msg.equals(thrownet)) {
            TextUtils.sendMessage(true, "§2Fischernetz ausgeworfen!");
            inswarm = false;
            netout = true;
            e.setCanceled(true);
        }
        if (msg.equals(dropfisch)) {
            p.sendChatMessage("/dropfisch");
            e.setCanceled(true);
        }
        if (msg.contains(fprefix) && msg.contains("Du hast") && msg.contains("Fisch gefangen.")) {
            TextUtils.sendMessage(true, "§2Hochseefischer erfolgreich beendet!");
            inswarm = false;
            netout = false;
            started = false;
            e.setCanceled(true);
        }
        if (msg.equals(findswarm) || msg.equals(foundswarm) || msg.equals(waithere) || msg.equals(nonets)) {
            e.setCanceled(true);
        }
    }
}