package inventory;

import drink.Updaters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImprovedItemTest {
	
	private static ImprovedItem item;
	
	@BeforeAll
	static void setUp() {
		item = ImprovedItem
				.CreateImprovedItem("McDowell's Whiskey", 10, 20)
				.setUpdater(item -> {
					item.sellIn--;
					item.quality++;
					Updaters.Utility.constrainQualityBound(item, 0, 40);
				});
		
	}
	
	@Test
	public void CreateImprovedItemTest() {
		assertNotNull(item);
	}
	
	@RepeatedTest (10)
	public void UpdaterTest(RepetitionInfo repetitionInfo) {
		repetitionInfo.getCurrentRepetition();
		item.processDayEnd();
		assertEquals(item.sellIn, 10 - repetitionInfo.getCurrentRepetition());
	}
	
}
