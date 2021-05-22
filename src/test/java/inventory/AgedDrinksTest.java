package inventory;

import drink.Updaters;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static inventory.ImprovedItem.CreateItem;
import static inventory.InventoryTest.app;
import static inventory.InventoryTest.getTestItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Aged Drinks")
public class AgedDrinksTest {
	
	private final int SELL_IN_DEFAULT = 10;
	private final int QUALITY_DEFAULT = 20;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] agedDrinks = new Item[] {
				CreateItem("Aged Brie", SELL_IN_DEFAULT, QUALITY_DEFAULT)
						.setUpdater(Updaters.AGED.getUpdater())
		};
		System.out.println("Created aged brie drinks: " + Arrays.toString(agedDrinks));
		app = new Inventory(agedDrinks);
		System.out.println("Created inventory: " + app);
	}
	
	@Test
	@DisplayName ("SellIn days decreases by 1 as update on end of day is processed")
	void sellInDecrementTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT; daysPassed++) {
			app.processDayEnd();
			assertEquals(SELL_IN_DEFAULT - daysPassed, getTestItem().sellIn);
			System.out.println("After " + daysPassed + " days item sellIn status: " + getTestItem().sellIn);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 1 as update on end of day is processed")
	void QualityIncrementWithinSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(QUALITY_DEFAULT + daysPassed, 50), getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 2 after sellIn days expired as update on end of day is processed")
	void QualityIncrementAfterSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("Item status after sellIn Days ended: " + getTestItem());
		
		int qualityRemainingAfterSellInDays = getTestItem().quality;
		for (int daysPassed = 1; getTestItem().quality < 50; daysPassed++) {
			app.processDayEnd();
			assertEquals(qualityRemainingAfterSellInDays + 2 * daysPassed, getTestItem().quality);
			System.out.println("After " + daysPassed + " days past sellIn days item quality status: " + getTestItem().quality);
		}
	}
	
	@ParameterizedTest (name = "Quality increment of item halts after {0} days past quality is 50")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityHaltAfterUpperBoundHitTest(int daysPassedAfterQualityIs0) {
		while (getTestItem().quality < 50) {
			app.processDayEnd();
		}
		
		System.out.println("After item quality hit upper bound item status: " + getTestItem());
		
		for (int daysPassed = 0; daysPassed < daysPassedAfterQualityIs0; daysPassed++) {
			app.processDayEnd();
		}
		assertEquals(50, getTestItem().quality);
		System.out.println("After " + daysPassedAfterQualityIs0 + " days past quality is 0, item quality status: " + getTestItem().quality);
	}
	
	/* ================================ TEAR DOWN ==================================== */
	
	@AfterEach
	void tearDown() {
		System.out.println("\nFinished test.\n");
	}
	
}
