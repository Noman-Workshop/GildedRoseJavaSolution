package inventory;

import drink.Updaters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static inventory.ImprovedItem.CreateItem;
import static inventory.InventoryTest.app;
import static inventory.InventoryTest.getTestItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Legendary Items Test")
public class LegendaryItemsTest {
	
	private static final int SELL_IN_DEFAULT = 10;
	private static final int QUALITY_DEFAULT = 80;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] legendaryItems = new Item[] {
				CreateItem("Sulfuras, Hand of Ragnaros", SELL_IN_DEFAULT, QUALITY_DEFAULT)
						.setUpdater(Updaters.LEGENDARY.getUpdater())
		};
		
		System.out.println("Created sulfurus drinks: " + Arrays.toString(legendaryItems));
		app = new Inventory(legendaryItems);
		System.out.println("Created sulfurus drinks inventory: " + app);
	}
	
	@ParameterizedTest (name = "SellIn and Quality constant of item is constant after {0} days")
	@MethodSource ("generateRandomNumbers")
	void SellInNQualityConstantAfterRandomNumberOfDaysTest(int randomNoOfDays) {
		for (int daysPassed = 0; daysPassed < randomNoOfDays; daysPassed++) {
			app.processDayEnd();
		}
		assertEquals(SELL_IN_DEFAULT, getTestItem().sellIn);
		assertEquals(QUALITY_DEFAULT, getTestItem().quality);
		System.out.println("Item status after " + randomNoOfDays + " days passed: " + getTestItem());
	}
	
	/* ================================ TEAR DOWN ==================================== */
	
	@AfterEach
	void tearDown() {
		System.out.println("\nFinished test.\n");
	}
	
	/* ================================ UTILITY ==================================== */
	static IntStream generateRandomNumbers() {
		Random random = new Random(SELL_IN_DEFAULT);
		return random.ints(10, 0, 1000);
	}
	
}
