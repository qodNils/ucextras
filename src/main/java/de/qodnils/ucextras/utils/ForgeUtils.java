package de.qodnils.ucextras.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class ForgeUtils {
    public static List<String> getOnlinePlayers() {
        NetHandlerPlayClient con = Minecraft.getMinecraft().getConnection();

        if (con == null)
            return Collections.emptyList();

        Collection<NetworkPlayerInfo> playerInfoCollection = con.getPlayerInfoMap();

        return playerInfoCollection.stream()
                .map(playerInfo -> playerInfo.getGameProfile().getName())
                .map(TextUtils::stripColor)
                .map(TextUtils::stripPrefix)
                .sorted()
                .collect(Collectors.toList());
    }
}