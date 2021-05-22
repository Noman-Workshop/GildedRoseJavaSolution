package inventory;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Aged Brie")
public class AgedBrieTest {
	
	private Inventory app;
	private final int sellInDefault = 10;
	private final int qualityDefault = 20;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] normalDrinks = new Item[] {
				new Item("Aged Brie", sellInDefault, qualityDefault)
		};
		System.out.println("Created aged brie drinks: " + Arrays.toString(normalDrinks));
		app = new Inventory(normalDrinks);
		System.out.println("Created inventory: " + app);
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
	@DisplayName ("Quality increases by 1 as update on end of day is processed")
	void QualityDecrementWithinSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault; daysPassed++) {
			app.processDayEnd();
			assertEquals(qualityDefault + daysPassed, getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 2 after sellIn days expired as update on end of day is processed")
	void QualityDecrementAfterSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault; daysPassed++) {
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
	
	@ParameterizedTest (name = "Quality of item doesn''t increase after {0} days past quality is 50")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityDecrementAfterUpperBoundHitTest(int daysPassedAfterQualityIs0) {
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
	
	/* ================================ UTILITY ==================================== */
	
	private Item getTestItem() {
		return app.items[0];
	}
}