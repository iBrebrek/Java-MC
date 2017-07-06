package hr.fer.zemris.java.simplecomp;


/**
 * Offers methods to calculate:
 * - register index
 * - is addressing indirect
 * - register offset (only matters if indirect)
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.5.2016.)
 */
public class RegisterUtil {
	
	/**
	 * Calculates register index.
	 * 
	 * @param registerDescriptor	register descriptor.
	 * @return register index.
	 */
	public static int getRegisterIndex(int registerDescriptor) {
		final int MASK = 0xFF; // indexes: [0, 7]
		
		return registerDescriptor & MASK;
	}
	
	/**
	 * Checks bit representing indirect flag.
	 * 
	 * @param registerDescriptor	register descriptor.
	 * @return {@code true} if indirect addressing.
	 */
	public static boolean isIndirect(int registerDescriptor) {
		final int FLAG_INDEX = 0x1000000; // index: [24]
		
		return (registerDescriptor & FLAG_INDEX) != 0;
	}
	
	/**
	 * Calculates offset for indirect addressing.
	 * Offset is used only if addressing is indirect.
	 * 
	 * @param registerDescriptor	register descriptor.
	 * @return addressing offset.
	 */
	public static int getRegisterOffset(int registerDescriptor) {
		final int MASK_POSITIVE = 0x7FFF00; // indexes: [8, 22]
		final int NEGATIVE_INDEX = 0x800000; // index: [23]
		final int NEGATIVE_VALUE = 32768; // 2^15
		
		int result = (registerDescriptor & MASK_POSITIVE) >> 8;
		
		if((registerDescriptor & NEGATIVE_INDEX) != 0) {
			result -= NEGATIVE_VALUE; // 2^15
		}
			
		return result;
	}
}
