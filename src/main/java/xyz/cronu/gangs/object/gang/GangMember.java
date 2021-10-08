package xyz.cronu.gangs.object.gang;

import xyz.cronu.gangs.object.perk.Perk;
import xyz.cronu.gangs.object.perk.PerkType;
import xyz.cronu.gangs.utils.Number;

import java.util.ArrayList;
import java.util.Arrays;
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

	public GangRank getPreviousRank(){
		int hierarchy = getGangRank().getHierarchy();
		switch (hierarchy){
			case 1:
				return GangRank.RECRUIT;
			case 2:
				return GangRank.MEMBER;
			case 3:
				return GangRank.OFFICER;
		}
		return GangRank.RECRUIT;
	}

	public GangRank getNextRank(){
		int hierarchy = getGangRank().getHierarchy();
		switch (hierarchy){
			case 0:
				return GangRank.MEMBER;
			case 1:
			case 2:
				return GangRank.OFFICER;
			case 3:
				return GangRank.LEADER;
		}
		return GangRank.RECRUIT;
	}

	public void removePermission(GangPermissions permission){
		if(!hasPermission(permission)) return;
		memberPermissions.remove(permission);
	}

	public void addPermission(GangPermissions permission){
		if(hasPermission(permission)) return;
		memberPermissions.add(permission);
	}

	public boolean hasTransferPermission(){
		return hasPermission(GangPermissions.TRANSFER);
	}

	public boolean hasDemotePermission(){
		return hasPermission(GangPermissions.DEMOTE);
	}

	public boolean hasPromotePermission(){
		return hasPermission(GangPermissions.PROMOTE);
	}

	public boolean hasKickPermission(){
		return hasPermission(GangPermissions.KICK);
	}

	public boolean hasDisbandPermission(){
		return hasPermission(GangPermissions.DISBAND);
	}

	public boolean hasInvitePermission(){
		return hasPermission(GangPermissions.INVITE);
	}

	public boolean hasAllPermissions(){
		return hasPermission(GangPermissions.ALL);
	}

	public boolean hasPermission(GangPermissions permission){
		return memberPermissions.contains(permission);
	}

	public void addPoints(long amount){
		setPoints(Number.add(getPoints(), amount));
	}

	public void addBlocksMined(long amount){
		setBlocksMined(Number.add(getBlocksMined(), amount));
	}

	public void takeBlocksMined(long amount){
		setBlocksMined(Number.take(getBlocksMined(), amount));
	}

	public void takePoints(long amount){
		setPoints(Number.take(getPoints(), amount));
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

	public void reset(){
		setBlocksMined(0);
		setPoints(0);
		setGangRank(GangRank.RECRUIT);
		setMemberPerks(new ArrayList<>(Arrays.asList(
				new Perk(PerkType.POINT, 1),
				new Perk(PerkType.BLOCK, 1),
				new Perk(PerkType.MONEY, 1)
		)));
		setMemberPermissions(new ArrayList<>());
	}

}
