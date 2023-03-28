package gmai.jstyledcomponents.util;

/**
 * The ImageUtil class provides utility methods.
 */
public class CommonsUtil {

	/**
	 * Determines if the given Boolean value is true.
	 * 
	 * @param bool - The Boolean value to check.
	 * @returns A boolean indicating if the given value is true.
	 */
	public static boolean isTrue(Boolean bool) {
		return Boolean.TRUE.equals(bool);
	}

	/**
	 * Determines if the given Boolean value is false.
	 * 
	 * @param bool - The Boolean value to check.
	 * @returns A boolean indicating if the given value is false.
	 */
	public static boolean isFalse(Boolean bool) {
		return Boolean.FALSE.equals(bool);
	}
}