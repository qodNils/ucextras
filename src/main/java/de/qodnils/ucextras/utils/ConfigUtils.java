package de.qodnils.ucextras.utils;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(name = "UC Extras", modid = "ucextras", type = Config.Type.INSTANCE)
public class ConfigUtils {
    @Config.Name("AHochseefischer")
    @Config.Comment("Aktiviere den automatischen Hochseefischer")
    public static boolean AHochseefischer = true;

    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent e) {
        if (e == null || e.getModID().equals("ucextras"))
            ConfigManager.sync("ucextras", Config.Type.INSTANCE);
    }
}