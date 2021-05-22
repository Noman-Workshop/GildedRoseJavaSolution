package inventory;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Backstage Passes")
public class BackstagePassesTest {
	
	private Inventory app;
	private final int sellInDefault = 20;
	private final int qualityDefault = 10;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] normalDrinks = new Item[] {
				new Item("Backstage passes to a TAFKAL80ETC concert", sellInDefault, qualityDefault)
		};
		System.out.println("Created backstage passes drinks: " + Arrays.toString(normalDrinks));
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
	@DisplayName ("Quality increases by 1 upto 10 days of sellIn as update on end of day is processed")
	void QualityIncrementWithinSellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault - 10; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(qualityDefault + daysPassed, 50), getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 2 from 10~5 sellIn days as update on end of day is processed")
	void QualityIncrementWithin10SellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault - 10; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("With 10 days left of sellIn item status: " + getTestItem());
		
		int qualityRemaining = getTestItem().quality;
		for (int daysPassed = 1; daysPassed <= 5; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(qualityRemaining + daysPassed * 2, 50), getTestItem().quality);
			System.out.println("With " + getTestItem().sellIn + " days remaining item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 3 from 5~0 sellIn as update on end of day is processed")
	void QualityIncrementWithin5sellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= sellInDefault - 5; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("With 10 days left of sellIn item status: " + getTestItem());
		
		int qualityRemaining = getTestItem().quality;
		for (int daysPassed = 1; daysPassed <= 5; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(qualityRemaining + daysPassed * 3, 50), getTestItem().quality);
			System.out.println("With " + getTestItem().sellIn + " days remaining item quality status: " + getTestItem().quality);
		}
	}
	
	@ParameterizedTest (name = "Quality update of item halts after {0} days past quality is 50")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityHaltAfterUpperBoundHitWithinSellInTest(int daysPassedAfterQualityIs0) {
		getTestItem().quality = 50;
		getTestItem().sellIn = daysPassedAfterQualityIs0 + 10;
		
		System.out.println("After item quality hit upper bound item status: " + getTestItem());
		
		for (int daysPassed = 0; daysPassed < daysPassedAfterQualityIs0; daysPassed++) {
			app.processDayEnd();
		}
		assertEquals(50, getTestItem().quality);
		System.out.println("After " + daysPassedAfterQualityIs0 + " days past quality is 50, item quality status: " + getTestItem().quality);
	}
	
	@DisplayName ("Quality halts to 0 after sellIn days expired as update on end of day is processed")
	@ParameterizedTest (name = "Quality update of item halts after {0} days past sellIn")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityHaltAfterSellInDaysTest(int daysPassedAfterSellIn) {
		for (int daysPassed = 1; daysPassed <= sellInDefault + 1; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("Item status after sellIn Days ended: " + getTestItem());
		
		for (int daysPassed = 1; daysPassed < daysPassedAfterSellIn; daysPassed++) {
			app.processDayEnd();
		}
		
		assertEquals(0, getTestItem().quality);
		System.out.println("After " + daysPassedAfterSellIn + " days past sellIn days item quality status: " + getTestItem().quality);
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
