package net.exmo.rough_blade.utils;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PathGenerator {
public static List<Vec3> generatePath(Vec3 start, Vec3 end) {
	List<Vec3> path = new ArrayList<>();

	double dx = end.x() - start.x();
	double dy = end.y() - start.y();
	double dz = end.z() - start.z();

	double distance = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));

	for (int i = 0; i <= distance; i++) {
		double fraction = (double) i / distance;
		double x = start.x() + dx * fraction;
		double y = start.y() + dy * fraction;
		double z = start.z() + dz * fraction;

		Vec3 point = new Vec3(x, y, z);
		path.add(roundToBlockPos(point));
	}

	return path;
}

/**
 * 将坐标四舍五入到最近的整数坐标。
 *
 * @param vec 输入的坐标
 * @return 四舍五入后的坐标
 */
private static Vec3 roundToBlockPos(Vec3 vec) {
	return new Vec3(Math.floor(vec.x()) + 0.5D, Math.floor(vec.y()) + 0.5D, Math.floor(vec.z()) + 0.5D);
}
}
