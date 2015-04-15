package com.example.jumper.providers.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContactsGroup {
	static final String NUMBERS_DELIMETER = "|";
	private Map<String, String> m_group;

	/**
	 * Constructors
	 */
	public ContactsGroup() {
		this.m_group = new HashMap<String, String>();
	}

	public ContactsGroup(Map<String, String> group) {
		this.m_group = group;
	}

	/**
	 * Gets & Sets
	 */
	public String[] getNumbers() {
		return ContactsGroup.collectionToArray(this.m_group.keySet());
	}

	public String[] getNames() {
		return ContactsGroup.collectionToArray(this.m_group.values());
	}

	public boolean isNumberExists(String number)
	{
		return m_group.containsKey(number);
	}

	public String getName(String number)
	{
		if (number == null || !isNumberExists(number))
		{
			return "";
		}

		return this.m_group.get(number);
	}

	/**
	 * Actions
	 */
	public void add(String number, String name)
	{
		this.m_group.put(number, name);
	}

	public void removeByNumber(String number)
	{
		if (isNumberExists(number))
		{
			this.m_group.remove(number);
		}
	}

	public void removeByName(String name)
	{
		for (String number : getNumbers())
		{
			if (0 == getName(number).compareTo(name))
			{
				this.m_group.remove(number);

				break;
			}
		}
	}

	/**
	 * Import & Export related
	 */
	public ContactsGroup getContactsWithNumbers(String[] numbers)
	{
		ContactsGroup groupContacts = new ContactsGroup();

		for (String number : numbers)
		{
			if (isNumberExists(number))
			{
				groupContacts.add(number, getName(number));
			}
		}

		return groupContacts;
	}

	public String getNumbersAsString()
	{
		return ContactsGroup.strJoin(getNumbers(), NUMBERS_DELIMETER);
	}

	public String[] getNumbersFromString(String numbers)
	{
		return numbers.split(NUMBERS_DELIMETER);
	}

	/**
	 * Utils
	 */
	private static String[] collectionToArray(Collection<String> c) {
		return c.toArray(new String[c.size()]);
	}

	public static String strJoin(String[] aArr, String sSep) {
		StringBuilder sbStr = new StringBuilder();
		for (int i = 0, il = aArr.length; i < il; i++) {
			if (i > 0)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
}
