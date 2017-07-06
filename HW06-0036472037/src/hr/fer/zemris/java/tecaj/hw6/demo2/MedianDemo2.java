package hr.fer.zemris.java.tecaj.hw6.demo2;

/**
 * Second demonstration of {@code LikeMedian}.
 */
public class MedianDemo2 {
	
	/**
	 * Entry point.
	 * 
	 * @param args		not used.
	 */
	public static void main(String[] args) {
		
		LikeMedian<String> likeMedian = new LikeMedian<String>();
		likeMedian.add("Joe");
		likeMedian.add("Jane");
		likeMedian.add("Adam");
		likeMedian.add("Zed");
		String result = likeMedian.get().get();
		System.out.println(result); // Writes: Jane
	}

}
