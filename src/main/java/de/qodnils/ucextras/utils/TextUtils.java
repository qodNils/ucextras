package de.qodnils.ucextras.utils;

import de.qodnils.ucextras.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;

public class TextUtils {
    public static void sendMessage(boolean prefix, String msg) {
        final EntityPlayerSP p = Minecraft.getMinecraft().player;
        if (prefix) {
            p.sendMessage(new TextComponentString(Main.prefix + msg));
        } else
        p.sendMessage(new TextComponentString(msg));
    }
}
