package me.isRadiant.endoftime.core;

import me.isRadiant.endoftime.core.registration.*;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

public class Main implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        GeckoLib.initialize();
        
        EntityReg.register();
    }
}