package org.transmart.search.count;

import java.util.HashMap;
import java.util.Map;

public class TotallyBogusCountCache {
	
	public static Map<CountType,Map<String,Integer>> typeMap = new HashMap<CountType,Map<String,Integer>>();
	
	public static void postCount(CountType type, String searchText, int count) {
		Map<String,Integer> map = getMapFor(type);
		if (map == null) return;
		map.put(searchText,new Integer(count));
	}
	
	public static boolean haveCount(CountType type, String searchText) {
		Map<String,Integer> map = getMapFor(type);
		if (map == null) return false;
		return map.get(searchText) != null;
	}
	
	public static int findCount(CountType type, String searchText){
		Map<String,Integer> map = getMapFor(type);
		if (map == null) return 0;
		Integer probe = map.get(searchText);
		if (probe == null) return -1;
		return probe.intValue();
	}

	private static Map<String,Integer> getMapFor(CountType type) {
		Map<String,Integer> map = typeMap.get(type);
		if (map == null) {
			map = new HashMap<String,Integer>();
			typeMap.put(type, map);
		}
		return map;
	}
}
