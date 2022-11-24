package me.isRadiant.endoftime.core.registration;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import me.isRadiant.endoftime.common.components.PlayerBooleans;
import me.isRadiant.endoftime.core.interfaces.IPlayerBoolean;
import me.isRadiant.endoftime.core.utils.ModHelper;

public class ComponentReg implements EntityComponentInitializer
{
    public static final ComponentKey<IPlayerBoolean> PLAYER_BOOLEANS = ComponentRegistry.getOrCreate(ModHelper.createID("player_booleans"), IPlayerBoolean.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
    {
        registry.registerForPlayers(PLAYER_BOOLEANS, PlayerBooleans::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}