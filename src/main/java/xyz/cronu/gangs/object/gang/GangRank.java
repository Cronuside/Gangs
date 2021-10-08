package xyz.cronu.gangs.object.gang;

/**
 * @author Cronu
 * @project Gangs
 */

public enum GangRank {

	RECRUIT(0), MEMBER(1), OFFICER(2), LEADER(3);

	private int hierarchy;

	GangRank(int hierarchy){
		this.hierarchy = hierarchy;
	}

	public int getHierarchy() {
		return hierarchy;
	}
}
