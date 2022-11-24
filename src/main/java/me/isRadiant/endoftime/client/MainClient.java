package me.isRadiant.endoftime.client;

import me.isRadiant.endoftime.common.entity.renderer.MoltenPigRenderer;
import me.isRadiant.endoftime.core.registration.EntityReg;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MainClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        EntityRendererRegistry.register(EntityReg.MOLTEN_PIG, MoltenPigRenderer::new);
    }
}