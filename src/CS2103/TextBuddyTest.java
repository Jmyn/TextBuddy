package CS2103;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;

public class TextBuddyTest{
	private static final String SORT_MESSAGE = "Text Sorted!";
	private static final String ERROR_DELETE = "Error indicating which line to delete.";
	private static final String ADD_MESSAGE = "added to %s: \"%s\"";
	private static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	private static final String CLEAR_MESSAGE = "all content deleted from %s";
	
	@Test
	public void testAdd() throws IOException {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);
		
		assertEquals("Message should be add message",
				String.format(ADD_MESSAGE, "TEST.txt","hello world"),
				tb.executeCommand("ADD hello world"));
		assertEquals(1,tb.getLineCount());
	
	}
	
	@Test
	public void testClear() throws IOException {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);
		
		assertEquals("Message should be clear message",
				String.format(CLEAR_MESSAGE, "TEST.txt"),
				tb.executeCommand("clear"));
		assertEquals(0,tb.getLineCount());
		
	}
	
	@Test
	public void testDelete() throws IOException {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);

		tb.executeCommand("ADD hello world");
		
		assertEquals(ERROR_DELETE,
				tb.executeCommand("delete 2"));
		assertEquals(String.format(DELETE_MESSAGE, "TEST.txt", "hello world"),
				tb.executeCommand("delete 1"));
		assertEquals(0,tb.getLineCount());
		
	}
	
	@Test
	public void testSort() throws Exception {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);

		
		ArrayList<String> testarray = new ArrayList<String>();
		ArrayList<String> actualarray = new ArrayList<String>();
		testarray.add("little brown fox");
		testarray.add("a little brown fox");
		testarray.add("add little brown fox");
		testarray.add("pretty little brown fox");
		testarray.add("bug little brown fox");
		testarray.add("huge little brown fox");
		testarray.add("zzz little brown fox");
		testarray.add("orange little brown fox");
		for(int i = 0; i < testarray.size(); i++) {
			tb.executeCommand("add " + testarray.get(i));
		}
		assertEquals(SORT_MESSAGE, tb.executeCommand("sort"));
		
		BufferedReader br = new BufferedReader(new FileReader("TEST.txt"));
		while(br.ready()) {
			actualarray.add(br.readLine());
		}
		br.close();
		assertEquals(8, actualarray.size());
		assertTrue(isSorted(actualarray));

	}
	
	public boolean isSorted(ArrayList<String> array) {
		for(int i = 1; i < array.size(); i++) {
			if(array.get(i-1).compareTo(array.get(i)) > 0) {
				return false;
			}
		}
		return true;
	}
	
	//Test search everything and search nothing
	@Test
	public void testSearch1() throws Exception {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);
	
		ArrayList<String> testarray = new ArrayList<String>();
		ArrayList<String> actualarray;
		
		testarray.add("little brown fox");
		testarray.add("this is a little brown fox");
		testarray.add("add is little brown fox");
		testarray.add("this pretty little brown fox");
		testarray.add("bug little brown fox");
		testarray.add("huge little brown fox");
		testarray.add("zzz little brown fox");
		testarray.add("orange little brown fox");
		for(int i = 0; i < testarray.size(); i++) {
			tb.executeCommand("add " + testarray.get(i));
		}
		actualarray = tb.doSearch("fox");
		
		assertEquals("Wrong search count",testarray.size(),
				actualarray.size());
		for(int i = 0; i < testarray.size(); i++) {
			if(!testarray.get(i).equals(actualarray.get(i))) {
				assertEquals(testarray.get(i),actualarray.get(i));
			}
		}
		
		actualarray = tb.doSearch("red");
		assertEquals(0, actualarray.size());
	}
	
	@Test
	public void testSearch2() throws Exception {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);
	
		ArrayList<String> testarray = new ArrayList<String>();
		ArrayList<String> actualarray;
		
		testarray.add("little brown fox");
		testarray.add("this is a little brown fox");
		testarray.add("add is little brown fox");
		testarray.add("this pretty little brown fox");
		testarray.add("bug little brown fox");
		testarray.add("huge little brown fox");
		testarray.add("zzz little brown fox");
		testarray.add("orange little brown fox");
		for(int i = 0; i < testarray.size(); i++) {
			tb.executeCommand("add " + testarray.get(i));
		}
		actualarray = tb.doSearch("is");
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("this is a little brown fox");
		ans.add("add is little brown fox");
		
		assertEquals("Wrong search count",ans.size(),
				actualarray.size());
		for(int i = 0; i < ans.size(); i++) {
			assertEquals(ans.get(i),actualarray.get(i));
		}
	}
	
	@Test
	public void testSearch3() throws Exception {
		String[] Args = {"TEST.txt"};
		TextBuddy tb = new TextBuddy(Args);

		ArrayList<String> testarray = new ArrayList<String>();
		ArrayList<String> actualarray;
		
		testarray.add("little brown fox");
		testarray.add("this is a little brown fox");
		testarray.add("add is little brown fox");
		testarray.add("this pretty little brown fox");
		testarray.add("bug little brown fox");
		testarray.add("huge little brown fox");
		testarray.add("zzz little brown fox");
		testarray.add("orange little brown fox");
		for(int i = 0; i < testarray.size(); i++) {
			tb.executeCommand("add " + testarray.get(i));
		}
		actualarray = tb.doSearch("this");
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("this is a little brown fox");
		ans.add("this pretty little brown fox");
		
		assertEquals("Wrong search count",ans.size(),
				actualarray.size());
		for(int i = 0; i < ans.size(); i++) {
			if(!ans.get(i).equals(actualarray.get(i))) {
				assertEquals(ans.get(i),actualarray.get(i));
			}
		}
	}
	
	 @Test 
	 public void testSearch4() throws IOException {
		 String[] Args = {"TEST.txt"};
		 TextBuddy tb = new TextBuddy(Args);
		 tb.executeCommand("add basketball, my fav charmander ahhahahah!");
		 tb.executeCommand("add Pikachu, lets go!");
		 tb.executeCommand("add piggy-car.");
		 tb.executeCommand("add car! in town hahaha");
		 tb.executeCommand("add Great Car. Good in everything!");
		 tb.executeCommand("add yolo my car around ahahah");
		  
		 ArrayList<String> ans = new ArrayList<String>();
		 ans.add("piggy-car.");
		 ans.add("car! in town hahaha");
		 ans.add("Great Car. Good in everything!");
		 ans.add("yolo my car around ahahah");
		  
		 ArrayList<String> actual = tb.doSearch("Car       ");
		 assertEquals("Wrong search count",ans.size(),
					actual.size());
	 	for(int i = 0; i < ans.size(); i++) {
			if(!ans.get(i).equals(actual.get(i))) {
				assertEquals(ans.get(i),actual.get(i));
			}
		}	  
	 }

}
