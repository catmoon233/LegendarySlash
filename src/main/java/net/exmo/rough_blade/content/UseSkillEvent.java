package net.exmo.rough_blade.content;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class UseSkillEvent extends LivingEvent {
	public String Skill;
	public int Cooldown;
	public UseSkillEvent(LivingEntity entity, String Skill, int Cooldown)
	{
		super(entity);
		this.Skill = Skill;
		this.Cooldown = Cooldown;
	}
	public String getSkill()
	{
		return Skill;
	}
}
