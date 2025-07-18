
package net.exmo.rough_blade.content;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.exmo.rough_blade.network.CareerWarModVariables;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public  class CareerSkill {
	public static final int sendCondTick = 40;
	public CareerSkill(String Name){
		super();
		this.LocalName = Name;
		SkillHandle.RegisterSkill(this);

	}
	public static void changeCombo(ItemStack itemStack, ResourceLocation combo, LivingEntity livingEntity){
		ISlashBladeState slashBlade = ExUtils.getSlashBlade(itemStack);
		if (slashBlade!=null){
			slashBlade.updateComboSeq(livingEntity ,combo);
		}
	}
	public static List<Entity> getEntities(Level level, Vec3 pos, int radius,Entity entity){
		return level.getEntities(entity, AABB.ofSize(pos, radius * 2, radius, radius * 2));
	}
	public String getLocalName(){
		return this.LocalName;
	}
	public DamageSource getDamageSoure(Player player, ResourceKey<DamageType> type){
		return  new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), player);
	}
	public Runnable getDamageSoureNOI(Player player, ResourceKey<DamageType> type, Consumer<DamageSource> consumer){
		Holder.Reference<DamageType> holderOrThrow = player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type);

		// 保存原始标签
		List<TagKey<DamageType>> originalTags = holderOrThrow.tags().toList();

		// 添加新的标签
		holderOrThrow.bindTags(List.of(
				DamageTypeTags.BYPASSES_COOLDOWN,
				DamageTypeTags.BYPASSES_INVULNERABILITY
		));
		Objects.requireNonNull(consumer);
		consumer.accept(new DamageSource(holderOrThrow, player));
	return () -> holderOrThrow.bindTags(originalTags);

	}

	public float getAttackDamage(Player player){
		return   (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
	}

 	 public String LocalName;

	 public int SkillLevel;
	 public Item Icon;
	 public boolean isSelfCooldown;
	 public ResourceLocation IconTexture = null;
	 public boolean isPassive = false;
	 public String LocalDescription;
	 public int CoolDown;
	 public boolean setCoolDown(Player player){
		 if (isPassive)return false;
		 MinecraftForge.EVENT_BUS.post(new UseSkillEvent(player,this.LocalName,this.CoolDown));
		 CareerWarModVariables.PlayerVariables v = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
		 if (v.playercooldown.containsKey(this.LocalName) && v.playercooldown.get(this.LocalName) !=0) return false;
		 player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

			 Map<String, Integer> map = capability.playercooldown;
			 if (map.containsKey(this.LocalName))map.remove(this.LocalName);
			 map.put(this.LocalName, (this.CoolDown));
			 capability.playercooldown = map;
			 capability.syncPlayerVariables(player);
		 });
		return true;
	 }
	 public boolean isInCooldown(Player player){
		 if (isPassive)return false;
		 CareerWarModVariables.PlayerVariables v = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
         return v.playercooldown.containsKey(this.LocalName) && Integer.valueOf(v.playercooldown.get(this.LocalName)) != 0;
     }

	 public boolean use(Player player){
         return setCoolDown(player);


     }
}
