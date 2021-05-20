package inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest {
	
	@Test
	void foo() {
		Item[] items = new Item[] { new Item("foo", 0, 0) };
		Inventory app = new Inventory(items);
		app.updateQuality();
		assertEquals("fixme", app.items[0].name);
	}
	
}