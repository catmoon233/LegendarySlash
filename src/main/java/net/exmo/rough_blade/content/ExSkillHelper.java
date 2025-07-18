package net.exmo.rough_blade.content;



import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExSkillHelper extends ExHelper {
    public final String  EEID = "skills";
    public ExSkillHelper(ItemStack itemStack) {
        super(itemStack);
    }
    public static ExSkillHelper of(ItemStack itemStack) {
        return new ExSkillHelper(itemStack);
    }
    public boolean ValidElementEntry() {
        return ValidMainNbt() && getMainNbt().contains(EEID);
    }
    public ExSkillHelper createElementNbt() {
        createNbt();
        if (ValidElementEntry()) return this;
        getMainNbt().put(EEID, new ListTag());

        return this;

    }
    public List<CareerSkillInstant> getSkills() {
        List<CareerSkillInstant> skills = new ArrayList<>();
        if (ValidMainNbt()) {
            CompoundTag tag = getMainNbt();
            if (tag.contains(EEID)) {
                ListTag elementList = tag.getList(EEID, 10);
                for (int i = 0; i < elementList.size(); i++) {
                    CompoundTag tag1 = elementList.getCompound(i);
                    CareerSkill skill = SkillHandle.getSkill(tag1.getString(EEID));
                    if (skill != null) {
                        int level = 1;
                        if (tag1.contains("Level")) level = tag1.getInt("Level");
                        CompoundTag tag2 = tag1.copy();
                        tag2.remove("Level");

                        skills.add(CareerSkillInstant.of(skill, level));
                    }
                }
            }
        }
        return skills;


    }
    public boolean gatherElement(CareerSkillInstant skillInstant) {
        List<CareerSkillInstant> elementInstants = getSkills();
        AtomicInteger level = new AtomicInteger(skillInstant.getLevel());

        elementInstants.forEach(x -> {
            if (x.getCareerSkill().LocalName.equals(skillInstant.getCareerSkill().getLocalName())) {
                level.set(skillInstant.getLevel() + x.getLevel());
            }
        });

        if (level.get() == skillInstant.getLevel()) return false;

        removeSkill(skillInstant);
        addSkill(new CareerSkillInstant(skillInstant.getCareerSkill(), level.get()), false);

        return level.get() != skillInstant.getLevel();
    }

    public ListTag getElementsNbt() {
        return getMainNbt().getList(EEID, 10);
    }
    public ExSkillHelper removeSkill(CareerSkillInstant skillInstant) {
        createNbt();
        if (!ValidMainNbt()) return this;
        ListTag elementsNbt = getElementsNbt();
        for (int i = 0; i < elementsNbt.size(); i++) {
            CompoundTag tag1 = elementsNbt.getCompound(i);
            if (tag1.getString(EEID).equals(skillInstant.getCareerSkill().getLocalName())) {
                elementsNbt.remove(i);
                break;

            }
        }
        return this;
    }

    public ExSkillHelper addSkill(CareerSkillInstant exElementInstant, boolean gather){
        if (exElementInstant.getCareerSkill()==null)return this;
        createNbt();
        if (!ValidMainNbt()) createMainNbt();
        createElementNbt();
        if (gather) {
            if (gatherElement(exElementInstant)) return this;
        }
        CompoundTag tag1 = new CompoundTag();
        tag1.putString(EEID, exElementInstant.getCareerSkill().getLocalName());
        tag1.putInt("Level", exElementInstant.getLevel());
        ListTag modifiersList = getElementsNbt();
        modifiersList.add(tag1);
        return this;
    }

    public int getSkillsSize() {
        CompoundTag mainNbt = getMainNbt();
        if (!ValidMainNbt()) return 0;
        return mainNbt.getList(EEID, 10).size();
    }
}
