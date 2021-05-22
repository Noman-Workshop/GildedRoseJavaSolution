package inventory;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest {
	
	
	@Test
	void InventoryCoreAPITest() {
		Item[] items = new Item[] {new Item("foo", 0, 0)};
		System.out.println("Items created: " + Arrays.toString(items));
		Inventory app = new Inventory(items);
		System.out.println("Inventory created: " + app);
		app.processDayEnd();
		assertEquals("foo", app.items[0].name);
		assertEquals(-1, app.items[0].sellIn);
		assertEquals(0, app.items[0].quality);
		System.out.println("After 1 call to update quality inventory status: " + app);
	}
	
	
}