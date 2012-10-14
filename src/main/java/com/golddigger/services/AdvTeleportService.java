package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.TeleportTile;

/**
 * Advantageous Teleport Service allows players to grab, drop, and activate
 * teleports.
 * 
 * Teleporting is free, however to grab a teleport will remove some of the users score.
 * 
 */
/*
 * There is a great risk of issues in the way this is constructed.
 * each teleport pair are not linked directly, they only point to
 * the map coordinate where the other pair "should" be. If the tile
 * is moved for what ever reason without the other tiles destination
 * position being changed, it will break this service. Unfortunately
 * I don't think I will have time to come back and correct this.
 * 
 * This issue should be addressed as soon as the system starts modifying
 * tile locations.
 * 
 * Brett Wandel - 4/10/2012
 */
public class AdvTeleportService extends GameService {
	public static final int COST = 3;

	public AdvTeleportService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		String action = parseURL(url, URL_ACTION);
		if (action == null) return false;
		else return action.equalsIgnoreCase("teleport");
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String extra = parseURL(url, URL_EXTRA1);
		String name = parseURL(url, URL_PLAYER);
		if (name == null || extra == null) {
			out.println("FAILED: invalid command");
			return true;
		}

		Player player = this.game.getPlayer(name);
		if (player == null) {
			out.println("FAILED: no player with that name");
			return true;
		}

		Unit unit = this.game.getUnit(player);
		if (unit == null) {
			out.println("FAILED: you have no unit");
			return true;
		}

		String output = null;
		extra = extra.toLowerCase();
		synchronized (game) { // ensure no funny business with the threads.
			Tile tile = game.getMap().get(unit.getPosition());
			if (extra.equals("grab")) {
				if (tile instanceof TeleportTile){
					output = grab(unit, (TeleportTile) tile);
				} else {
					output = "FAILED: Unit not on teleport tile";
				}
			} else if (extra.equals("drop")) {
				output = drop(unit, tile);
			} else if (extra.equals("activate")) {
				if (tile instanceof TeleportTile){
					output = activate(unit, (TeleportTile) tile);
				} else {
					output = "FAILED: Unit not on teleport tile";
				}
			}
		}

		if (output != null) {
			out.println(output);
		}
		return true;
	}

	/**
	 * <p>Drop the teleport tile on the ground.</p>
	 * <p>Dropping a teleport tile should fail if you try to drop it to
	 * another TeleportTile</p>
	 * 
	 * @param unit The unit holding the tile.
	 * @param tile The tile that the unit is standing on.
	 * @return The output to be sent to the user.
	 */
	private String drop(Unit unit, Tile tile) {
		Coordinate position = unit.getPosition();
		if (!unit.isCarryingTeleportTile()) {
			return"FAILED: not carrying a teleport tile";
		} else if (tile instanceof TeleportTile){
			return "FAILED: Can't drop a teleport tile on a teleport tile";
		} else if (tile instanceof BaseTile){
			return "FAILED: Can't drop a teleport tile on a base tile";
		} else if (tile instanceof GoldTile){
			if (((GoldTile) tile).getGold() > 0) {
				return "FAILED: Can't drop a teleport tile on a tile that currently holds gold";
			}
		}
		
		TeleportTile teleportTile = unit.getTeleportTile();
		if (teleportTile.getDestinationPos() == null) {
			// THIS case shouldn't happen but will leave in for testing reminder
			System.out.println("A TELEPORT TILE'S DESTINATION WAS SOMEHOW SET TO NULL");
		}
		
		TeleportTile destinationTile = (TeleportTile) this.game.getMap().get(teleportTile.getDestinationPos());
		teleportTile.setMountedTile(tile);
		destinationTile.setDestinationPos(position);
		unit.setTeleportTile(null);
		this.game.getMap().set(position.lat, position.lng, teleportTile);
		return "OK: Teleport dropped";
	}

	/**
	 * <p>Grab a teleport tile off the ground</p>
	 * <p>Grabbing will fail if the unit is carrying another, or the desitnation
	 * tile is currently being carried by another unit/player.</p>
	 * 
	 * @param unit The unit that is "grabbing" the tile
	 * @param tile the tile being grabbed
	 * @return The output to be send to the user.
	 */
	private String grab(Unit unit, TeleportTile tile) {
		if (unit.isCarryingTeleportTile()){
			return "FAILED: Already carrying a teleport tile";
		}

		Coordinate dest = tile.getDestinationPos();
		if (dest == null){
			return "FAILED: Destination teleport tile being held";
		}

		if (unit.getOwner().getScore() < COST){
			return "FAILED: You can not affort to move this teleport";
		}
		Tile original = tile.getMountedTile();
		Tile destination = game.getMap().get(dest);

		if (destination instanceof TeleportTile){
			this.game.getMap().set(unit.getLat(), unit.getLng(), original);
			tile.setMountedTile(null);
			unit.setTeleportTile(tile);
			((TeleportTile)destination).setDestinationPos(null);
			unit.getOwner().setScore(unit.getOwner().getScore() - COST);
			return "OK: Teleport grabbed";	
		} else {
			return "ERROR: Teleportation's destination is not a teleport tile, please report this to the admin.";
		}
	}

	/**
	 * <p>Activate the teleport, that is, teleport the unit.</p>
	 * <p>Activation should fail if the destination tile is currently being held
	 * by another unit, or if another unit is on the destination tile.</p>
	 * 
	 * @param unit The unit activating the teleport
	 * @param tile the teleport tile being activated
	 * @return the output to be sent to the user
	 */
	private String activate(Unit unit, TeleportTile tile) {
		Coordinate dest = tile.getDestinationPos();
		if (dest == null){
			return "FAILED: Destination teleport tile being held";
		}

		Tile destinationTile = this.game.getMap().get(dest);
		boolean isBlocked = this.game.isUnitAt(dest);
		if (destinationTile == null || isBlocked){
			return "FAILED: Destination currrently blocked";
		} else {
			unit.setPosition(dest);
			return "OK: Teleported";
		}
	}

}
