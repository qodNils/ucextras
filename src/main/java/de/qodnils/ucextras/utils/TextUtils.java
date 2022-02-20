package de.qodnils.ucextras.utils;

import de.qodnils.ucextras.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class TextUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern STRIP_PREFIX_PATTERN = Pattern.compile("\\[\\w+]");

    public static void sendMessage(boolean prefix, String msg) {
        final EntityPlayerSP p = Minecraft.getMinecraft().player;
        if (prefix) {
            p.sendMessage(new TextComponentString(Main.prefix + msg));
        } else
            p.sendMessage(new TextComponentString(msg));
    }

    public static TextComponentString addPrefix(String msg) {
        return new TextComponentString(Main.prefix + msg);
    }

    public static String stripColor(String s) {
        return STRIP_COLOR_PATTERN.matcher(s).replaceAll("");
    }

    public static String stripPrefix(String s) {
        return STRIP_PREFIX_PATTERN.matcher(s).replaceAll("");
    }
}
