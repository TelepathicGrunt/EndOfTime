package me.isRadiant.endoftime.common.entity.renderer;

import me.isRadiant.endoftime.common.entity.MoltenPigEntity;
import me.isRadiant.endoftime.common.entity.model.MoltenPigModel;
import me.isRadiant.endoftime.core.utils.ModHelper;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MoltenPigRenderer extends GeoEntityRenderer<MoltenPigEntity>
{
    public MoltenPigRenderer(Context renderManager)
    {
        super(renderManager, new MoltenPigModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public Identifier getTextureResource(MoltenPigEntity object)
    {
        return ModHelper.createID("textures/entity/molten_pig/molten_pig.png");
    }
}