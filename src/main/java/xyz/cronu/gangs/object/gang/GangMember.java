package xyz.cronu.gangs.object.gang;

import xyz.cronu.gangs.object.perk.Perk;

import java.util.List;
import java.util.UUID;

/**
 * @author Cronu
 * @project Gangs
 */

public class GangMember {

	private UUID member;
	private GangRank gangRank;
	private String memberName;
	private List<Perk> memberPerks;
	private List<GangPermissions> memberPermissions;
	private long blocksMined;
	private long points;

	public GangMember(UUID member, GangRank gangRank, String memberName, List<Perk> memberPerks, List<GangPermissions> memberPermissions, long blocksMined, long points) {
		this.member = member;
		this.gangRank = gangRank;
		this.memberName = memberName;
		this.memberPerks = memberPerks;
		this.memberPermissions = memberPermissions;
		this.blocksMined = blocksMined;
		this.points = points;
	}

	public void removePermission(GangPermissions permission){
		if(!hasPermission(permission)) return;
		memberPermissions.remove(permission);
	}

	public void addPermission(GangPermissions permission){
		if(hasPermission(permission)) return;
		memberPermissions.add(permission);
	}

	public boolean hasPermission(GangPermissions permission){
		return memberPermissions.contains(permission);
	}

	public void setMember(UUID member) {
		this.member = member;
	}

	public void setGangRank(GangRank gangRank) {
		this.gangRank = gangRank;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public void setMemberPerks(List<Perk> memberPerks) {
		this.memberPerks = memberPerks;
	}

	public void setMemberPermissions(List<GangPermissions> memberPermissions) {
		this.memberPermissions = memberPermissions;
	}

	public void setBlocksMined(long blocksMined) {
		this.blocksMined = blocksMined;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public UUID getMember() {
		return member;
	}

	public GangRank getGangRank() {
		return gangRank;
	}

	public String getMemberName() {
		return memberName;
	}

	public List<Perk> getMemberPerks() {
		return memberPerks;
	}

	public List<GangPermissions> getMemberPermissions() {
		return memberPermissions;
	}

	public long getBlocksMined() {
		return blocksMined;
	}

	public long getPoints() {
		return points;
	}
}
