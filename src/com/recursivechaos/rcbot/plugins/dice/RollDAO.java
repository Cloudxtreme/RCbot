package com.recursivechaos.rcbot.plugins.dice;
/**
 * RollDAO is the interface for RollDAOImpl
 * 
 * @author Andrew Bell
 */
public interface RollDAO {
	public String getFlavorText(String message);
	public int rollFromCheckedString(String string);
	public int rollFromString(String inputDiceString) throws DiceException;
}