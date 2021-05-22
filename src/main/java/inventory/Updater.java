package inventory;

@FunctionalInterface
public interface Updater {
	
	void onDayEnd(ImprovedItem item);
	
}
