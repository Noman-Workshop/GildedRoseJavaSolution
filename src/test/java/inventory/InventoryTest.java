package inventory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryTest {
	
	Inventory app;
	
	@AfterEach
	public void LogInventory() {
		System.out.println(app);
	}
	
	@Test
	void DeprecatedInventoryItemTest() {
		Item[] items = new Item[] {new Item("foo", 0, 0)};
		app = new Inventory(items);
		assertThrows(UnsupportedOperationException.class, app::processDayEnd);
	}
	
	@Test
	void CreateInventoryTest() {
		ImprovedItem[] items = new ImprovedItem[] {ImprovedItem.CreateImprovedItem("foo", 0, 0)};
		app = new Inventory(items);
		app.processDayEnd();
		assertEquals("foo", app.items[0].name);
	}
	
}
