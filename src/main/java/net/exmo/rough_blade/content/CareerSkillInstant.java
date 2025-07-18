package net.exmo.rough_blade.content;

public class CareerSkillInstant {
    public CareerSkill getCareerSkill() {
        return careerSkill;
    }

    public CareerSkillInstant setCareerSkill(CareerSkill careerSkill) {
        this.careerSkill = careerSkill;
        return this;
    }

    public CareerSkill careerSkill;
    public CareerSkillInstant(CareerSkill careerSkill) {
        this.careerSkill = careerSkill;

    }

    public CareerSkillInstant(CareerSkill careerSkill, int i) {
        this.careerSkill = careerSkill;
        level = i;
    }

    public int getLevel() {
        return level;
    }

    public CareerSkillInstant setLevel(int level) {
        this.level = level;
        return this;
    }

    private int level;
    public static CareerSkillInstant of (CareerSkill careerSkill,int level){
        return new CareerSkillInstant(careerSkill).setLevel(level);
    }
}
