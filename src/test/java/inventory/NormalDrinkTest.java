package inventory;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Normal drinks")
class NormalDrinkTest {
	
	private Inventory app;
	private final int sellInDefault = 10;
	private final int qualityDefault = 40;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] normalDrinks = new Item[] {
				new Item("beer", sellInDefault, qualityDefault)
		};
		System.out.println("Created normal drinks: " + Arrays.toString(normalDrinks));
		app = new Inventory(normalDrinks);
		System.out.println("Created normal drinks inventory: " + app);
	}
	
	@Test
	@DisplayName ("SellIn days decreases by 1 as update on end of day is processed")
	void sellInDecrementTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault; daysPassed++) {
			app.processDayEnd();
			assertEquals(sellInDefault - daysPassed, getTestItem().sellIn);
			System.out.println("After " + daysPassed + " days item sellIn status: " + getTestItem().sellIn);
		}
	}
	
	@Test
	@DisplayName ("Quality decreases by 1 upto sellIn day expired as update on end of day is processed")
	void QualityDecrementWithinSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault; daysPassed++) {
			app.processDayEnd();
			assertEquals(qualityDefault - daysPassed, getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality decreases by 2 after sellIn date as update on end of day is processed")
	void QualityDecrementAfterSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("Item status after sellIn Days ended: " + getTestItem());
		
		int qualityRemainingAfterSellInDays = getTestItem().quality;
		for (int daysPassed = 1; getTestItem().quality > 0; daysPassed++) {
			app.processDayEnd();
			assertEquals(qualityRemainingAfterSellInDays - daysPassed * 2, getTestItem().quality);
			System.out.println("After " + daysPassed + " days past sellIn days item quality status: " + getTestItem().quality);
		}
	}
	
	@ParameterizedTest (name = "Quality of item doesn''t decrease after {0} days past quality is 0")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityDecrementAfterLowerBoundHitTest(int daysPassedAfterQualityIs0) {
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
	
	/* ================================ UTILITY ==================================== */
	
	private Item getTestItem() {
		return app.items[0];
	}
}
