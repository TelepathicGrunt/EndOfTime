package me.isRadiant.endoftime.common.entity;

import java.util.EnumSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.GoToWalkTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MoltenPigEntity extends AnimalEntity implements IAnimatable
{
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private float floatHeightMultiplier = 0.6F;

    public MoltenPigEntity(EntityType<? extends AnimalEntity> entityType, World world)
    {
        super(entityType, world);
        this.intersectionChecked = true;
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0f);
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<MoltenPigEntity>(this, "movementController", 0, this::movePredicate));
        data.addAnimationController(new AnimationController<MoltenPigEntity>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState movePredicate(AnimationEvent<E> event)
    {
        if(event.isMoving())
        {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molten_pig.walk", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molten_pig.idle", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event)
    {
        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped))
        {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.molten_pig.attack", EDefaultLoopTypes.PLAY_ONCE));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected SoundEvent getAmbientSound() 
    {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state)
    {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    public static DefaultAttributeContainer.Builder createDefaultAttributes()
    {
        DefaultAttributeContainer.Builder mobAttributes = HostileEntity.createMobAttributes();
        mobAttributes.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0F);
        mobAttributes.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0F);
        mobAttributes.add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0F);
        mobAttributes.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4F);
        mobAttributes.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0F);
        return mobAttributes;
    }

    public static boolean canSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
    {
        return world.getFluidState(pos.down()).isIn(FluidTags.LAVA) || world.getBlockState(pos.down()).isOf(Blocks.LAVA);
    }

    @Override
    public boolean canWalkOnFluid(FluidState state)
    {
        return state.isIn(FluidTags.LAVA);
    }

    @Override
    public boolean isFireImmune()
    {
        return true;
    }

    @Override
    public void tick()
    {
        World world = this.getWorld();
        if(!world.isClient())
        {
            if(this.isTouchingWater() && world instanceof ServerWorld serverWorld)
            {
                this.convertTo(EntityType.PIG, false);
            }
        }
        super.tick();
    }

    @Override
    protected void mobTick()
    {
        LivingEntity livingEntity;
        if ((livingEntity = this.getTarget()) != null && livingEntity.getEyeY() > this.getEyeY() && this.canTarget(livingEntity))
        {
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(this.getVelocity().add(0.0, ((double)0.3f - vec3d.y) * (double)floatHeightMultiplier, 0.0));
            this.velocityDirty = true;
        }
        super.mobTick();
    }

    @Override
    public void tickMovement()
    {
        if (!this.onGround && this.getVelocity().y < 0.0)
        {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }
        if (this.world.isClient)
        {
            if (this.random.nextInt(24) == 0 && !this.isSilent())
            {
                this.world.playSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, this.getSoundCategory(), 1.0f + this.random.nextFloat(), this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i)
            {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        super.tickMovement();
    }

    @Override
    protected void initGoals()
    {
        this.goalSelector.add(4, new ShootFireballGoal(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 0.4F));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.4F, 1.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
    }

    static class ShootFireballGoal extends Goal
    {
        private final MoltenPigEntity moltenPig;
        private int fireballsFired;
        private int fireballCooldown;
        private int targetNotVisibleTicks;

        public ShootFireballGoal(MoltenPigEntity moltenPig)
        {
            this.moltenPig = moltenPig;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart()
        {
            LivingEntity livingEntity = this.moltenPig.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.moltenPig.canTarget(livingEntity);
        }

        @Override
        public void start()
        {
            this.fireballsFired = 0;
        }

        @Override
        public void stop()
        {
            this.targetNotVisibleTicks = 0;
        }

        @Override
        public boolean shouldRunEveryTick()
        {
            return true;
        }

        @Override
        public void tick()
        {
            --this.fireballCooldown;
            LivingEntity livingEntity = this.moltenPig.getTarget();
            if (livingEntity == null)
            {
                return;
            }
            boolean bl = this.moltenPig.getVisibilityCache().canSee(livingEntity);
            this.targetNotVisibleTicks = bl ? 0 : ++this.targetNotVisibleTicks;
            double d = this.moltenPig.squaredDistanceTo(livingEntity);
            if (d < 4.0)
            {
                if (!bl)
                {
                    return;
                }
                if (this.fireballCooldown <= 0)
                {
                    this.fireballCooldown = 20;
                    this.moltenPig.tryAttack(livingEntity);
                }
                this.moltenPig.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            } else if (d < this.getFollowRange() * this.getFollowRange() && bl)
            {
                double e = livingEntity.getX() - this.moltenPig.getX();
                double f = livingEntity.getBodyY(0.5) - this.moltenPig.getBodyY(0.5);
                double g = livingEntity.getZ() - this.moltenPig.getZ();
                if (this.fireballCooldown <= 0)
                {
                    ++this.fireballsFired;
                    if (this.fireballsFired == 1)
                    {
                        this.fireballCooldown = 60;
                    } else if (this.fireballsFired <= 4)
                    {
                        this.fireballCooldown = 6;
                    } else
                    {
                        this.fireballCooldown = 100;
                        this.fireballsFired = 0;
                    }
                    if (this.fireballsFired > 1)
                    {
                        double h = Math.sqrt(Math.sqrt(d)) * 0.5;
                        if (!this.moltenPig.isSilent())
                        {
                            this.moltenPig.world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, this.moltenPig.getBlockPos(), 0);
                        }
                        for (int i = 0; i < 1; ++i)
                        {
                            SmallFireballEntity smallFireballEntity = new SmallFireballEntity(this.moltenPig.world, this.moltenPig, this.moltenPig.getRandom().nextTriangular(e, 2.297 * h), f, this.moltenPig.getRandom().nextTriangular(g, 2.297 * h));
                            smallFireballEntity.setPosition(smallFireballEntity.getX(), this.moltenPig.getBodyY(0.5) + 0.5, smallFireballEntity.getZ());
                            this.moltenPig.world.spawnEntity(smallFireballEntity);
                        }
                    }
                }
                this.moltenPig.getLookControl().lookAt(livingEntity, 10.0f, 10.0f);
            } else if (this.targetNotVisibleTicks < 5)
            {
                this.moltenPig.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
            }
            super.tick();
        }

        private double getFollowRange()
        {
            return this.moltenPig.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
    }

    @Override
    public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2)
    {
        return null;
    }
}