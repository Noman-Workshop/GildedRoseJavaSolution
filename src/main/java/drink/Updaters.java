package drink;

import inventory.Item;
import inventory.Updater;

public enum Updaters {
	COMMON(item -> {
		item.sellIn--;
		if (item.sellIn >= 0) {
			item.quality -= 1;
		} else {
			item.quality -= 2;
		}
		
		Utility.constrainQualityBound(item, 0, item.quality);
	}),
	
	AGED(item -> {
		item.sellIn--;
		if (item.sellIn >= 0) {
			item.quality += 1;
		} else {
			item.quality += 2;
		}
		Utility.constrainQualityBound(item, item.quality, 50);
	}),
	
	LEGENDARY(item -> {
		Utility.constrainQualityBound(item, 80, 80);
	}),
	
	BACKSTAGE_PASSES(item -> {
		item.sellIn--;
		if (item.sellIn < 0) {
			item.quality = 0;
		} else if (item.sellIn < 5) {
			item.quality += 3;
		} else if (item.sellIn < 10) {
			item.quality += 2;
		} else {
			item.quality += 1;
		}
		
		Utility.constrainQualityBound(item, item.quality, 50);
	}),
	
	CONJURED(item -> {
		item.sellIn--;
		if (item.sellIn >= 0) {
			item.quality -= 2;
		} else {
			item.quality -= 4;
		}
		Utility.constrainQualityBound(item, 0, item.quality);
	});
	
	Updaters(Updater updater) {
		this.updater = updater;
	}
	
	private final Updater updater;
	
	public Updater getUpdater() {
		return updater;
	}
	
	public static class Utility {
		
		public static void constrainQualityBound(Item item, int lowerBound, int upperBound) {
			if (item.quality < lowerBound) {
				item.quality = lowerBound;
			}
			
			if (item.quality > upperBound) {
				item.quality = upperBound;
			}
		}
		
	}
}
