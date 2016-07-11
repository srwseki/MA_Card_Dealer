package ma.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestFeedAdapter {
	
	public final int TOTAL = 10;
	public final int SIZE = 5;
	public final int[] nums = {1,2,3,4,5,6,7,8,9,10};
	
	public static ArrayList<int[]> results = new ArrayList<int[]>();
	
	public void draw(List<Integer> list, int start, int end, int num){
		if(start == end){
			return;
		}
		
	}
	
	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	public static void printCombination(int arr[], int n, int r) {
		// A temporary array to store all combination one by one
		int[] data = new int[r];

		// Sort array to handle duplicates
		// qsort (arr, n, sizeof(int), compare);
		// Arrays.sort();

		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, data, 0, n - 1, 0, r);
	}

	/*
	 * arr[] ---> Input Array data[] ---> Temporary array to store current
	 * combination start & end ---> Staring and Ending indexes in arr[] index
	 * ---> Current index in data[] r ---> Size of a combination to be printed
	 */
	private static void combinationUtil(int arr[], int data[], int start, int end, int index, int r) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			for (int i = 0; i < r; i++) {
				System.out.printf("%d ", data[i]);
			}
			results.add(data);
			System.out.print("\n");
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i + 1, end, index + 1, r);

//			// Remove duplicates
//			while (arr[i] == arr[i + 1])
//				i++;
		}
	}

	public static List<List<Integer>> permute(int[] num, int base) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		permute(num, 0, result, base);
		return result;
	}
	 
	public static void permute(int[] num, int start, List<List<Integer>> result, int base) {
	 
		if (start >= num.length) {
			ArrayList<Integer> item = convertArrayToList(num);
			boolean add = true;
			//filter out result that is not in order from #1 to #base 
			for(int x = 0; base > 1 && base < num.length && x < base - 1; x++){
				if(item.get(x) > item.get(x+1)){
					add = false;
				}
			}
			if(add){
				result.add(item);
			}
		}
	 
		for (int j = start; j <= num.length - 1; j++) {
			swap(num, start, j);
			permute(num, start + 1, result, base);
			swap(num, start, j);
		}
	}
	 
	public static ArrayList<Integer> convertArrayToList(int[] num) {
		ArrayList<Integer> item = new ArrayList<Integer>();
		for (int h = 0; h < num.length; h++) {
			item.add(num[h]);
		}
		return item;
	}
	 
	public static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static List<List<Integer>>[] search(List<List<Integer>> result, int draw, Pair<Set<Integer>,Integer>[] targetGroup){
		List<List<Integer>>[] resList = new ArrayList[2];
		resList[0] = new ArrayList<List<Integer>>();
		resList[1] = new ArrayList<List<Integer>>();
		
		for(List<Integer> line : result){
			boolean good = true;
			for(Pair<Set<Integer>,Integer> target : targetGroup){
				int found = 0;
				for(int idx = 0 ; idx < draw; idx++){
					Integer item = line.get(idx);
					if(target.first.contains(item)){
						found += 1;
					}
				}
				if(found < target.second){
					good = false;
					break;
				}
			}
			if(good){
				resList[0].add(line);
			}else{
				resList[1].add(line);
			}
		}
		return resList;
	}
	
	public static void main(String[] args) {
		int arr[] = { 1, 2, 3, 4, 5 , 6, 7, 8 ,9 ,0 };
		int r = 5;
		int BASE = 5;//sizeof(arr) / sizeof(arr[0]);
		//printCombination(arr, n, r);
		int i = 1;
		List<List<Integer>> result = permute(arr,BASE);
		int totalSize = result.size();
		System.out.println(totalSize);
		Pair<HashSet<Integer>,Integer> targetCount = new Pair<HashSet<Integer>, Integer>(new HashSet<Integer>(), 2);
		targetCount.first.add(1);
		targetCount.first.add(2);
		targetCount.first.add(4);
		Pair<HashSet<Integer>,Integer> targetCount1 = new Pair<HashSet<Integer>, Integer>(new HashSet<Integer>(), 1);
		targetCount1.first.add(3);
		//targetCount1.first.add(5);
		List<List<Integer>>[] resList = search(result, BASE+3,  new Pair[]{targetCount});
		int count = resList[0].size();
		System.out.println(count+"/"+totalSize+"="+(count*1.0/totalSize)+", bad="+resList[1].size());
//		for(ArrayList<Integer> line : result){
//			System.out.println(i+" - "+String.valueOf(line));
//			i++;
//		}
	}
}




