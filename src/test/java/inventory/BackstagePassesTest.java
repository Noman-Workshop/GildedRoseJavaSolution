package inventory;

import drink.Updaters;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static inventory.ImprovedItem.CreateItem;
import static inventory.InventoryTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName ("Backstage Passes")
public class BackstagePassesTest {
	
	private final int SELL_IN_DEFAULT = 20;
	private final int QUALITY_DEFAULT = 10;
	
	/* ================================ SETUP ==================================== */
	
	@BeforeEach
	void setup(TestInfo testInfo) {
		System.out.println("\nStarting test: " + testInfo.getDisplayName() + "\n");
		Item[] backstagePassesDrinks = new Item[] {
				CreateItem("Backstage passes to a TAFKAL80ETC concert", SELL_IN_DEFAULT, QUALITY_DEFAULT)
						.setUpdater(Updaters.BACKSTAGE_PASSES.getUpdater())
		};
		System.out.println("Created backstage passes drinks: " + Arrays.toString(backstagePassesDrinks));
		app = new Inventory(backstagePassesDrinks);
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
	@DisplayName ("Quality increases by 1 upto 10 days of sellIn as update on end of day is processed")
	void QualityIncrementWithinSellInDaysTest() {
		int currentQuality = getTestItem().quality;
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT - 10; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(currentQuality + daysPassed, 50), getTestItem().quality);
			System.out.println("After " + daysPassed + " days item quality status: " + getTestItem().quality);
		}
	}
	
	@Test
	@DisplayName ("Quality increases by 2 from 10~5 sellIn days as update on end of day is processed")
	void QualityIncrementWithin10SellInDaysTest() {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT - 10; daysPassed++) {
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
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT - 5; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("With 5 days left of sellIn item status: " + getTestItem());
		
		int qualityRemaining = getTestItem().quality;
		for (int daysPassed = 1; daysPassed <= 5; daysPassed++) {
			app.processDayEnd();
			assertEquals(Math.min(qualityRemaining + daysPassed * 3, 50), getTestItem().quality);
			System.out.println("With " + getTestItem().sellIn + " days remaining item quality status: " + getTestItem().quality);
		}
	}
	
	@ParameterizedTest (name = "Quality increment of item halts after {0} days past quality is 50")
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
	
	@ParameterizedTest (name = "Quality update of item halts to 0 after {0} days past sellIn")
	@ValueSource (ints = {10, 20, 40, 70, 1000})
	void QualityHaltAfterSellInDaysTest(int daysPassedAfterSellIn) {
		for (int daysPassed = 1; daysPassed <= SELL_IN_DEFAULT + 1; daysPassed++) {
			app.processDayEnd();
		}
		System.out.println("Item status after sellIn Days ended: " + getTestItem());
		
		for (int daysPassed = 1; daysPassed < daysPassedAfterSellIn; daysPassed++) {
			app.processDayEnd();
		}
		
		assertEquals(0, getTestItem().quality);
		System.out.println("After " + daysPassedAfterSellIn + " days past sellIn days item quality status: " + getTestItem().quality);
	}
	
	@TestFactory
	Collection<DynamicTest> AlwaysIncrementQualityWhenPossibleTest() {
		return Arrays.asList(
				DynamicTest.dynamicTest("Always decrease quality when possible", () -> {
					setup(CreateDynamicTestInfo("Always decrease quality when possible", null, Optional.of(this.getClass()), Optional.empty()));
					getTestItem().quality = -70;
					System.out.println("Quality of test item altered to: " + getTestItem().quality);
					QualityIncrementWithinSellInDaysTest();
					tearDown();
				}),
				DynamicTest.dynamicTest("Always Decrease Quality when possible", () -> {
					setup(CreateDynamicTestInfo("Always decrease quality when possible even after sellIn days", null, Optional.of(this.getClass()), Optional.empty()));
					getTestItem().quality = -70;
					System.out.println("Quality of test item altered to: " + getTestItem().quality);
					QualityIncrementWithin10SellInDaysTest();
					tearDown();
				}),
				DynamicTest.dynamicTest("Always Decrease Quality when possible", () -> {
					setup(CreateDynamicTestInfo("Always decrease quality when possible even after sellIn days", null, Optional.of(this.getClass()), Optional.empty()));
					getTestItem().quality = -70;
					System.out.println("Quality of test item altered to: " + getTestItem().quality);
					QualityIncrementWithin5sellInDaysTest();
					tearDown();
				})
		);
		
	}
	
	/* ================================ TEAR DOWN ==================================== */
	
	@AfterEach
	void tearDown() {
		System.out.println("\nFinished test.\n");
	}
	
}
