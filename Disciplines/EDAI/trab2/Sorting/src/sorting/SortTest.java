package sorting;

import static org.junit.Assert.*;

import org.junit.Test;

public class SortTest {
	
	@Test
	public void testMergeSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.mergeSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBubbleSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.bubbleSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testShellSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.shellSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRadixSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.radixSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRankSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.rankSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHeapSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.heapSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQuickSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.quickSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInsertionSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.insertionSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSelectionSort() {
		try {
			for(int i = 0; i< 1000; i++){
				RandomArray array1000 = new RandomArray(1000, -1000, 1000);
				Sort.selectionSort(array1000.get());
				assertTrue(verify(array1000.get()));
			}
		} catch (RandomArrayException e) {
			e.printStackTrace();
		}
	}
	
	private boolean verify(int[] array){
		boolean result = true;
		for(int i = 0; i< array.length - 1; i++){
			result = array[i] > array[i+1] ? false : true;
			if(result)
				return result;
		}
		return result;
		
	}

}
