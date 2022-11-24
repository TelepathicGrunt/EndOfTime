package me.isRadiant.endoftime.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModHelper 
{
    public static final Logger LOG = LoggerFactory.getLogger(ModConstants.MODID);
    
    public static Identifier createID(String id)
    {
        return new Identifier(ModConstants.MODID, id);
    }

    public static <T extends Entity> EntityType<T> createFixedEntity(EntityType.EntityFactory<T> entity, SpawnGroup group, float width, float height, String id)
    {
        return Registry.register(Registry.ENTITY_TYPE, createID(id), FabricEntityTypeBuilder
        .create(group, entity)
        .dimensions(EntityDimensions.fixed(width, height))
        .build());
    }

    public static void registerAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder)
    {
        FabricDefaultAttributeRegistry.register(type, builder);
    }
}