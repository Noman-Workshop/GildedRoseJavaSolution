package inventory;

import drink.Updaters;

public class ImprovedItem extends Item {
	
	public interface Updater {
		
		void onDayEnd(ImprovedItem item);
	}
	
	private Updater updater = Updaters.DEFAULT.getUpdater();
	
	private ImprovedItem(String name, int sellIn, int quality) {
		super(name, sellIn, quality);
	}
	
	public static ImprovedItem CreateImprovedItem(String name, int sellIn, int quality) {
		return new ImprovedItem(name, sellIn, quality);
	}
	
	public void setQualityUpdater(Updater updater) {
		this.updater = updater;
	}
	
	public void processDayEnd() {
		this.updater.onDayEnd(this);
	}
	
}
