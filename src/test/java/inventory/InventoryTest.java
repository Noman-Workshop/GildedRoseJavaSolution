package inventory;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static inventory.ImprovedItem.CreateItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryTest {
	
	public static Inventory app;
	
	@Test
	void InventoryCoreAPITest() {
		Item[] items = new Item[] {CreateItem("foo", 0, 0)};
		System.out.println("Items created: " + Arrays.toString(items));
		app = new Inventory(items);
		System.out.println("Inventory created: " + app);
		app.processDayEnd();
		assertEquals("foo", getTestItem().name);
		assertEquals(-1, getTestItem().sellIn);
		assertEquals(0, getTestItem().quality);
		System.out.println("After 1 call to update quality inventory status: " + app);
	}
	
	@Test
	void DeprecatedItemUseTest() {
		Item[] items = new Item[] {new Item("foo", 0, 0)};
		System.out.println("Items created: " + Arrays.toString(items));
		app = new Inventory(items);
		System.out.println("Inventory created: " + app);
		assertThrows(Exception.class, () -> app.processDayEnd());
		assertEquals(0, getTestItem().sellIn);
		assertEquals(0, getTestItem().quality);
		System.out.println("After 1 failed call to update quality inventory status: " + app);
		System.out.println("update using of direct use of Item class is now deprecated forcefully.");
	}
	
	/* ================================ UTILITY ==================================== */
	
	public static Item getTestItem() {
		return app.items[0];
	}
}