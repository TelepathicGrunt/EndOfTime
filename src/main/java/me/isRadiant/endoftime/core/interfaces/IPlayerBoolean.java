package me.isRadiant.endoftime.core.interfaces;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface IPlayerBoolean extends ComponentV3
{
    void setHasReceivedSpawnItems(boolean flag);
    boolean getHasReceivedSpawnItems();
}