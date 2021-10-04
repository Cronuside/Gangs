package xyz.cronu.gangs.object.perk;

/**
 * @author Cronu
 * @project Gangs
 */

public class Perk {

	private PerkType perkType;
	private double multiplier;
	private long baseCost;
	private long costMultiplier;

	public Perk(PerkType perkType, double multiplier) {
		this.perkType = perkType;
		this.multiplier = multiplier;
		// TODO: Add config getters for these values ->
		//		this.baseCost = baseCost;
		//		this.costMultiplier = costMultiplier;
	}

	public void setPerkType(PerkType perkType) {
		this.perkType = perkType;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public void setBaseCost(long baseCost) {
		this.baseCost = baseCost;
	}

	public void setCostMultiplier(long costMultiplier) {
		this.costMultiplier = costMultiplier;
	}

	public PerkType getPerkType() {
		return perkType;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public long getBaseCost() {
		return baseCost;
	}

	public long getCostMultiplier() {
		return costMultiplier;
	}
}
