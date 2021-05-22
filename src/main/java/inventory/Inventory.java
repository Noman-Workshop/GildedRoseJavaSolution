package inventory;

import java.util.Arrays;

class Inventory {
	
	Item[] items;
	
	public Inventory(Item[] items) {
		this.items = items;
	}
	
	
	public void processDayEnd() {
		for (Item item : items) {
			try {
				ImprovedItem improvedItem = (ImprovedItem) item;
				improvedItem.processDayEnd();
			} catch (ClassCastException e) {
				throw new UnsupportedOperationException("Cannot update objects of deprecated Item class. Please consider using ImprovedItem");
			}
		}
	}
	
	@Override
	public String toString() {
		return "Inventory{" +
				"items=" + Arrays.toString(items) +
				'}';
	}
}
