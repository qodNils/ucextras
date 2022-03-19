package de.qodnils.ucextras;

import de.qodnils.ucextras.commands.CheckFactionRanksCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.12,1.12.2]")
public class Main {
    public static final String MODID = "ucextras";
    public static final String NAME = "UC Extras";
    public static final String VERSION = "0.1.1";

    public static String prefix = "§3UC Extras§8 >>§7 ";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logger logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CheckFactionRanksCommand());
    }
}