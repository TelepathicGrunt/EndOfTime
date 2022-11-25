package me.isRadiant.endoftime.core.registration;

import me.isRadiant.endoftime.common.entity.MoltenPigEntity;
import me.isRadiant.endoftime.core.utils.ModHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.SpawnRestriction.Location;
import net.minecraft.world.Heightmap;
    
public class EntityReg
{
    public static final EntityType<MoltenPigEntity> MOLTEN_PIG = ModHelper.createFixedEntity(MoltenPigEntity::new, SpawnGroup.CREATURE, 0.8F, 1.0F, "molten_pig");


    public static void register()
    {
        ModHelper.registerAttributes(EntityReg.MOLTEN_PIG, MoltenPigEntity.createDefaultAttributes());
        SpawnRestriction.register(MOLTEN_PIG, Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MoltenPigEntity::canSpawn);
    }
}