package test;

import static org.junit.Assert.*;
import jsonCsv.Main;

import org.junit.Test;

public class TestArgs {

	@Test
	public void oneArgWithSpace() {
		String[] args = {"New York"};
		assertTrue(Main.checkArgs(args));
	}

	@Test
	public void oneArgWithoutSpace() {
		String[] args = {"NewYork"};
		assertTrue(Main.checkArgs(args));
	}
	
	@Test
	public void specialCharacters() {
		String[] args = {"a*/bs"};
		assertFalse("ERROR: Only string with spaces is allowed and without numbers",Main.checkArgs(args));
	}
	
	@Test
	public void twoStringArgs() {
		String[] args = {"New York","Berlin"};
		assertFalse(Main.checkArgs(args));
	}
	
	@Test
	public void oneNumberWithSpace() {
		String[] args = {"123 355"};
		assertFalse(Main.checkArgs(args));
	}
	
	@Test
	public void oneNumber() {
		String[] args = {"123355"};
		assertFalse(Main.checkArgs(args));
	}
	
	@Test
	public void maximumLength() {
		StringBuilder s = new StringBuilder();
		for (int i=0;i<=1000;i++)
			s.append("a");
		String[] args = new String[1];
		args[0]=s.toString();
		assertFalse(Main.checkArgs(args));
	}
	
}
