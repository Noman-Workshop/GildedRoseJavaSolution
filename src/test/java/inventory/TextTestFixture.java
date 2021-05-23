package inventory;

import drink.Updaters;

import static inventory.ImprovedItem.CreateItem;

public class TextTestFixture {
	
	public static void main(String[] args) {
		System.out.println("OMGHAI!");
		
		Item[] items = new Item[] {
				CreateItem("+5 Dexterity Vest", 10, 20),
				CreateItem("Aged Brie", 2, 0).setUpdater(Updaters.AGED.getUpdater()),
				CreateItem("Elixir of the Mongoose", 5, 7),
				CreateItem("Sulfuras, Hand of Ragnaros", 0, 80).setUpdater(Updaters.LEGENDARY.getUpdater()),
				CreateItem("Sulfuras, Hand of Ragnaros", -1, 80).setUpdater(Updaters.LEGENDARY.getUpdater()),
				CreateItem("Backstage passes to a TAFKAL80ETC concert", 15, 20).setUpdater(Updaters.BACKSTAGE_PASSES.getUpdater()),
				CreateItem("Backstage passes to a TAFKAL80ETC concert", 10, 49).setUpdater(Updaters.BACKSTAGE_PASSES.getUpdater()),
				CreateItem("Backstage passes to a TAFKAL80ETC concert", 5, 49).setUpdater(Updaters.BACKSTAGE_PASSES.getUpdater()),
				CreateItem("Conjured Mana Cake", 3, 6).setUpdater(Updaters.CONJURED.getUpdater())};
		
		Inventory app = new Inventory(items);
		
		int days = 2;
		if (args.length > 0) {
			days = Integer.parseInt(args[0]) + 1;
		}
		
		for (int i = 0; i < days; i++) {
			System.out.println("-------- day " + i + " --------");
			System.out.println("name, sellIn, quality");
			for (Item item : items) {
				System.out.println(item);
			}
			System.out.println();
			app.processDayEnd();
		}
	}
	
}