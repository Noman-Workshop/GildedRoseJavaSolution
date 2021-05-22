package inventory;

import drink.Updaters;

public class ImprovedItem extends Item {
	
	private Updater updater;
	
	private ImprovedItem(String name, int sellIn, int quality) {
		super(name, sellIn, quality);
		this.updater = Updaters.COMMON.getUpdater();
	}
	
	public static ImprovedItem CreateItem(String name, int sellIn, int quality) {
		return new ImprovedItem(name, sellIn, quality);
	}
	
	public ImprovedItem setUpdater(Updater updater) {
		this.updater = updater;
		return this;
	}
	
	public void processDayEnd() {
		this.updater.onDayEnd(this);
	}
	
}
