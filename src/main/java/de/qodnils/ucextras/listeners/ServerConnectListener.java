package de.qodnils.ucextras.listeners;

import de.qodnils.ucextras.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ServerConnectListener {
    public static boolean connectedUC;
    private static boolean connected;
    private static boolean msgsent;

    @SubscribeEvent
    public static void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        connected = true;
        msgsent = false;
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        if (!connected) return;

        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        if (serverData == null) return;

        connectedUC = serverData.serverIP.toLowerCase().contains("unicacity.de");
        if (!connectedUC) return;
        if (!msgsent) {
            final EntityPlayerSP p = Minecraft.getMinecraft().player;
            TextUtils.sendMessage(true, "Willkommen zurück auf §cU§9nica§cC§9ity §6" + p.getName() + "§7!");

            msgsent = true;
        }
    }
}