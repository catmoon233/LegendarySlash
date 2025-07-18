
package net.exmo.rough_blade.content;


import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.network.CareerWarModVariables;
import net.exmo.rough_blade.network.DashMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import static net.exmo.rough_blade.content.CareerHandle.random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkillHandle {
	public static List<CareerSkill>  registeredSkills = new ArrayList<>();
	public static CareerSkill getSkill(String name) {
		for (CareerSkill skill : registeredSkills) {
			if (skill.LocalName.equals(name)) {
				return skill;
			}
		}
		return null;
	}

	public static void sendParticleCircleN(ServerLevel level,Vec3 pos, SimpleParticleType particleType, float radius, int count) {


		RandomSource random = RandomSource.create(); // 创建随机数生成器

		for (int i = 0; i < count; i++) {
			// 计算角度
			double angle = i * (2 * Math.PI / count);

			// 在圆周上计算粒子位置
			double offsetX = radius * Math.cos(angle);
			double offsetZ = radius * Math.sin(angle);

			// 添加一点随机偏移，使粒子分散
			offsetX += random.nextDouble() * 0.1 - 0.05;
			offsetZ += random.nextDouble() * 0.1 - 0.05;

			// 发送粒子
			level.sendParticles(particleType, pos.x + offsetX, pos.y+0.5, pos.z + offsetZ, 1, 0.0, 0.0, 0.0, 0.05);
		}
	}
	/**
	 * 让一个实体围绕另一个实体做圆周运动。
	 *
	 * @param follower 跟随的实体。
	 * @param leader 被跟随的实体。
	 * @param radius 圆周运动的半径。
	 * @param speed 旋转速度（每秒的旋转角度）。
	 */
	public static void orbitAroundEntity(Entity follower, Entity leader, double radius, float speed) {
		Vec3 leaderPos = leader.position();
		Vec3 followerPos = follower.position();

		// 计算当前的偏移角度
		double offsetX = followerPos.x - leaderPos.x;
		double offsetZ = followerPos.z - leaderPos.z;
		double currentAngle = Math.toDegrees(Math.atan2(offsetZ, offsetX)) - 90.0D;

		// 计算新的角度
		double newAngle = currentAngle + speed;

		// 根据新角度计算新的位置
		double newX = leaderPos.x + radius * Math.cos(Math.toRadians(newAngle));
		double newZ = leaderPos.z + radius * Math.sin(Math.toRadians(newAngle));

		// 设置跟随实体的新位置
		follower.setPos(newX, leaderPos.y, newZ);

		// 设置跟随实体的朝向
		float yaw = (float) newAngle;
		follower.setYRot(yaw);
		follower.setYHeadRot(yaw);
	}
	public static void sendParticleCircle(ServerLevel level, Entity entity, SimpleParticleType particleType, float radius, int count) {
		double posX = entity.getX();
		double posY = entity.getY() + entity.getBbHeight() * 0.5;
		double posZ = entity.getZ();


		for (int i = 0; i < count; i++) {
			// 计算带随机偏移的角度
			double angle = random.nextDouble() * 2 * Math.PI;

			// 在圆周上计算粒子位置
			double offsetX = radius * Math.cos(angle);
			double offsetZ = radius * Math.sin(angle);

			// 添加一点随机偏移，使粒子分散
			offsetX += (random.nextDouble() - 0.5) * 0.2; // 调整随机分散范围
			offsetZ += (random.nextDouble() - 0.5) * 0.2; // 调整随机分散范围

			// 发送粒子
			level.sendParticles(particleType, posX + offsetX, posY, posZ + offsetZ, 1, 0.0, 0.0, 0.0, 0.05);
		}
	}
	/**
	 * 让实体向指定的 BlockPos 靠拢。
	 *
	 * @param entity 实体对象。
	 * @param targetPos 目标位置的 BlockPos。
	 * @param speed 移动速度（每秒移动的方块数）。
	 * @param maxDistance 最大移动距离（防止一次性移动过远）。
	 */
	public static void moveTowardsBlockPos(Entity entity, BlockPos targetPos, double speed, double maxDistance) {
		Vec3 entityPos = entity.position();
		Vec3 targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5); // 将 BlockPos 转换为 Vec3

		// 计算向量差
		Vec3 diff = targetVec.subtract(entityPos);
		double dist = diff.length();

		// 如果距离小于最大移动距离，则直接移动到目标位置
		if (dist <= maxDistance) {
			entity.teleportTo(targetVec.x, targetVec.y, targetVec.z);
			return;
		}

		// 计算移动向量
		Vec3 moveVec = diff.normalize().scale(speed);

		// 更新实体位置
		Vec3 newPos = entityPos.add(moveVec);

		// 限制移动距离不超过最大距离
		if (newPos.distanceTo(entityPos) > maxDistance) {
			newPos = entityPos.add(diff.normalize().scale(maxDistance));
		}

		// 设置实体的新位置
		entity.setPos(newPos.x, newPos.y, newPos.z);
	}
	public static Integer getSkillV(Player player ,String SN){
		 Integer[] return_v = new Integer[1];
		 return_v[0] = 0;
		player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			Map<String,Integer> m = capability.playercooldown;
            return_v[0] = m.getOrDefault(SN, 0);

		});

		return return_v[0];
	}

	public static void ChangeSkillV(Player player,String SN,int amout){
		player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			Map<String,Integer> m = capability.playercooldown;
			if (m.containsKey(SN))m.replace(SN,(amout));
			else m.put(SN,(amout));
			capability.playercooldown = m;
			capability.syncPlayerVariables(player);
		});
	}

	/**
	 * 返回玩家视线方向上最远的非空气方块的位置。
	 * 如果未找到非空气方块，则返回最大距离时的方块位置（可能为空气）。
	 *
	 * @param entity	 用于确定视线方向和位置的玩家实体。
	 * @param maxDistance 视线追踪的最大距离。
	 * @return 最远非空气方块的位置，如果未找到则返回最大距离时的位置。
	 */
	public static BlockPos findFarthestNonAirBlock(LivingEntity entity, double maxDistance) {
		Level world = entity.level();
		Vec3 eyePosition = entity.getEyePosition(1.0F); // 当前时刻玩家眼睛的位置
		Vec3 lookVec = entity.getViewVector(1.0F).scale(maxDistance); // 玩家视线方向的单位向量，乘以最大距离
		Vec3 endPos = eyePosition.add(lookVec); // 眼睛位置加上视线向量得到终点位置

		BlockHitResult result = world.clip(new ClipContext(eyePosition, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

		if (result.getType() == HitResult.Type.BLOCK) {
			BlockPos hitPos = result.getBlockPos();
			Block block = world.getBlockState(hitPos).getBlock();
			if (block != Blocks.AIR) {
				return hitPos;
			}
		}

		// 如果没有找到非空气方块，则返回最大距离时的方块位置
		return BlockPos.containing(endPos);
	}
	private static void sendParticle(Player player,ServerLevel level, Vec3 pos, SimpleParticleType particleType) {
		float yaw = player.getYRot();
		double dx = -Math.sin(Math.toRadians(yaw)) * 0.5;
		double dz = Math.cos(Math.toRadians(yaw)) * 0.5;
		level.sendParticles(particleType, pos.x, pos.y-1.5, pos.z, 2, dx, 0, dz, 0.01);
	}

	/**
	 * 在指定位置生成一个球形粒子效果。
	 *
	 * @param level 服务器世界实例。
	 * @param particleType 粒子类型。
	 * @param center 球心的位置。
	 * @param radius 球的半径。
	 * @param count 粒子的数量。
	 */
	public static void generateSphereParticles(ServerLevel level, SimpleParticleType particleType, Vec3 center, float radius, int count, @Nullable double vy,@Nullable double sp) {
		Random random = new Random();

		for (int i = 0; i < count; i++) {
			double x = center.x + (random.nextFloat() * 2 - 1) * radius;
			double y = center.y + (random.nextFloat() * 2 - 1) * radius;
			double z = center.z + (random.nextFloat() * 2 - 1) * radius;

			if (isPointInSphere(center, x, y, z, radius)) {
				level.sendParticles(particleType, x, y, z, 1, 0, vy, 0, sp);
			}
		}
	}

	/**
	 * 检查一个点是否位于球体内。
	 *
	 * @param center 球心位置。
	 * @param x 点的 x 坐标。
	 * @param y 点的 y 坐标。
	 * @param z 点的 z 坐标。
	 * @param radius 球的半径。
	 * @return 如果点位于球体内返回 true，否则返回 false。
	 */
	private static boolean isPointInSphere(Vec3 center, double x, double y, double z, float radius) {
		double dx = x - center.x;
		double dy = y - center.y;
		double dz = z - center.z;
		return dx * dx + dy * dy + dz * dz <= radius * radius;
	}

	public static void sendDashMessage(Player player, double dy, double dashDistance) {
		DashMessage message = new DashMessage( dy, dashDistance);
		if (player.level().isClientSide)return;
		Rough_blade.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
	}//6
	/**
	 * 在玩家面前生成一个半扇形的粒子效果。
	 *
	 * @param player 生成粒子效果的玩家
	 * @param radius 半扇形的半径
	 * @param particleType 粒子类型
	 */
	public static void generateSemicircleParticles(ServerPlayer player, float radius, SimpleParticleType particleType) {
		if (!(player.level() instanceof  ServerLevel level))return;
		Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
		float yaw = player.getYRot(); // 获取玩家的水平旋转角度

		// 半扇形角度范围：0度到180度
		int angleStart = 0;
		int angleEnd = 180;
		int numberOfParticles = 10; // 控制粒子数量

		// 计算每个粒子的角度间隔
		double angleIncrement = (angleEnd - angleStart) / numberOfParticles;

		for (int i = 0; i < numberOfParticles; i++) {
			double angle = angleStart + i * angleIncrement;

			// 计算粒子位置
			double x = radius * Math.cos(Math.toRadians(angle + yaw));
			double z = radius * Math.sin(Math.toRadians(angle + yaw));

			// 粒子相对于玩家的位置
			Vec3 particlePos = playerPos.add(x, 1, z);

			// 发送粒子
			sendParticle(player,level, particlePos, particleType);

		}
	}
	public static void RegisterSkill(CareerSkill skill) {
		if (skill.LocalName !=null){
			registeredSkills.add(skill);
			Rough_blade.LOGGER.info("Registered Skill: " + skill.LocalName);
		}else Rough_blade.LOGGER.error("Skill name is null");
	}
	public static void useSkill(Player entity,int skill){
		if (entity instanceof ServerPlayer player)if (player.gameMode.getGameModeForPlayer().equals(GameType.SPECTATOR))return;
		ItemStack mainHandItem = entity.getMainHandItem();
		if (!mainHandItem.hasTag())return;
		CareerWarModVariables.PlayerVariables entityvars = ((Player) entity).getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
		ExSkillHelper exSkillHelper = ExSkillHelper.of(mainHandItem);
		if (exSkillHelper != null) {
			List<CareerSkillInstant> skills = exSkillHelper.getSkills();
			if (!skills.isEmpty()&&skill<skills.size()&& skills.get(skill) != null){
				skills.get(skill).careerSkill.use(entity);
			}
		}
	}

	public static void vmove(LivingEntity livingEntity,double dy,double dashDistance){

		float yaw = livingEntity.getYRot();
		double dx = -Math.sin(Math.toRadians(yaw)) * dashDistance;
		double dz = Math.cos(Math.toRadians(yaw)) * dashDistance;
		livingEntity.setDeltaMovement(new Vec3(dx, dy, dz));
	}
	public static void vmove2(LivingEntity livingEntity,double dy,double dashDistance){

		float yaw = livingEntity.getYRot();
		double dx = -Math.sin(Math.toRadians(yaw)) * dashDistance;
		double dz = Math.cos(Math.toRadians(yaw)) * dashDistance;
		livingEntity.addDeltaMovement(new Vec3(dx, dy, dz));
	}

}
