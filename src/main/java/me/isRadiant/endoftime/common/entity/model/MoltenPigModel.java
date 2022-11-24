package me.isRadiant.endoftime.common.entity.model;

import me.isRadiant.endoftime.common.entity.MoltenPigEntity;
import me.isRadiant.endoftime.core.utils.ModHelper;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MoltenPigModel extends AnimatedGeoModel<MoltenPigEntity>
{

    @Override
    public Identifier getAnimationResource(MoltenPigEntity animatable)
    {
        return ModHelper.createID("animations/molten_pig.animation.json");
    }

    @Override
    public Identifier getModelResource(MoltenPigEntity object)
    {
        return ModHelper.createID("geo/molten_pig.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoltenPigEntity object)
    {
        return ModHelper.createID("textures/entity/molten_pig/molten_pig.png");
    }
}