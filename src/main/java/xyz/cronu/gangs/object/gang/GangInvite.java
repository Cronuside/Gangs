package xyz.cronu.gangs.object.gang;

import java.util.UUID;

/**
 * @author Cronu
 * @project Gangs
 */

public class GangInvite {

	private UUID gangInvitee;
	private UUID gangInviter;

	public GangInvite(UUID gangInvitee, UUID gangInviter) {
		this.gangInvitee = gangInvitee;
		this.gangInviter = gangInviter;
	}

	public void setGangInvitee(UUID gangInvitee) {
		this.gangInvitee = gangInvitee;
	}

	public void setGangInviter(UUID gangInviter) {
		this.gangInviter = gangInviter;
	}

	public UUID getGangInvitee() {
		return gangInvitee;
	}

	public UUID getGangInviter() {
		return gangInviter;
	}
}
