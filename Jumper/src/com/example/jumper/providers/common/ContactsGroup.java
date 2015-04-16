package com.example.jumper.providers.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.jumper.MainActivity;
import com.example.jumper.providers.impl.ContactsProviderImpl;
import com.example.jumper.providers.interfaces.ContactsProvider;

public class ContactsGroup {
	static ContactsProvider m_contactsProvider = new ContactsProviderImpl(MainActivity.getAppContext());

	static final String DEFAULT_NAME = "DEFAULT_NAME";
	static final String GROUP_NAME_DELIMETER = "~";
	static final String NUMBERS_DELIMETER = "|";

	private String m_name = DEFAULT_NAME;
	private Map<String, String> m_group;

	/**
	 * Constructors
	 */
	public ContactsGroup() {
		this.m_group = new HashMap<String, String>();
	}

	public ContactsGroup(String groupName) {
		m_name = groupName;
		this.m_group = new HashMap<String, String>();
	}

	public ContactsGroup(Map<String, String> group) {
		this.m_group = group;
	}

	public ContactsGroup(String groupName, Map<String, String> group) {
		this.m_name = groupName;
		this.m_group = group;
	}

	/**
	 * Gets & Sets
	 */
	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		this.m_name = name;
	}

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

	public String getNumber(String name)
	{
		for (String number : getNumbers())
		{
			if (0 == getName(number).compareTo(name))
			{
				return number;
			}
		}

		return null;
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
		String number = null;
		if (null != (number = getNumber(name)))
		{
			this.m_group.remove(number);
		}
	}

	/**
	 * Import & Export related
	 */
	public Map<String, String> getContactsWithNumbers(String[] numbers)
	{
		Map<String, String> groupContacts = new HashMap<String, String>();
		ContactsGroup allContacts = m_contactsProvider.provideContacts();

		for (String number : allContacts.getNumbers())
		{
			if (isNumberExists(number))
			{
				groupContacts.put(number, getName(number));
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

	public void importGroup(String toImport)
	{
		String[] importTokens = toImport.split(GROUP_NAME_DELIMETER);
		this.m_name = importTokens[0];
		this.m_group = getContactsWithNumbers(getNumbersFromString(importTokens[1]));
	}

	public String exportGroup()
	{
		return m_name + GROUP_NAME_DELIMETER + getNumbersAsString();
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
