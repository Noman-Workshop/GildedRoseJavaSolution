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

@DisplayName ("Conjured Drinks")
public class ConjuredDrinksTest {
	
	private static final int SELL_IN_DEFAULT = 10;
	private static final int QUALITY_DEFAULT = 20;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] conjuredDrinks = new Item[] {
				CreateItem("Cognac", SELL_IN_DEFAULT, QUALITY_DEFAULT)
						.setUpdater(Updaters.CONJURED.getUpdater())
		};
		
		System.out.println("Created conjured drinks: " + Arrays.toString(conjuredDrinks));
		app = new Inventory(conjuredDrinks);
		System.out.println("Created conjured drinks inventory: " + app);
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
	@DisplayName ("Quality decreases by 2 upto sellIn days expired as update on end of day is processed")
	void QualityDecrementWithinSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.max(QUALITY_DEFAULT - 2 * daysPassed, 0), getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality decreases by 4 after sellIn days expired as update on end of day is processed")
	void QualityDecrementAfterSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("Item status after sellIn Days ended: " + getTestItem());
		
		int qualityRemainingAfterSellInDays = getTestItem().quality;
		for (int daysPassed = 1; getTestItem().quality > 0; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.max(qualityRemainingAfterSellInDays - daysPassed * 4, 0), getTestItem().quality);
			System.out.println("After " + daysPassed + " days past sellIn days item quality status: " + getTestItem().quality);
		}
	}
	
	@ParameterizedTest (name = "Quality decrement of item halts after {0} days past quality is 0")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityHaltAfterLowerBoundHitTest(int daysPassedAfterQualityIs0) {
		while (getTestItem().quality > 0) {
			app.processDayEnd();
		}
		
		System.out.println("After item quality hit lower bound item status: " + getTestItem());
		
		for (int daysPassed = 0; daysPassed < daysPassedAfterQualityIs0; daysPassed++) {
			app.processDayEnd();
		}
		assertEquals(0, getTestItem().quality);
		System.out.println("After " + daysPassedAfterQualityIs0 + " days past quality is 0, item quality status: " + getTestItem().quality);
	}
	
	/* ================================ TEAR DOWN ==================================== */
	
	@AfterEach
	void tearDown() {
		System.out.println("\nFinished test.\n");
		
	}
}