package com.example.jumper;

import java.io.IOException;
import java.util.Map;

import com.example.jumper.manager.impl.FileManagerImpl;
import com.example.jumper.manager.interfaces.FileManager;
import com.example.jumper.providers.common.ContactsGroup;
import com.example.jumper.providers.impl.ContactsProviderImpl;
import com.example.jumper.providers.interfaces.ContactsProvider;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseGroup extends ListActivity {
	private static ContactsGroup m_allContacts;
	private static ContactsGroup m_choosenContacts;
	private static ContactsProvider m_contactsProvider;

	private String m_groupName;

	private Button m_ApproveButton;
	private Button m_CancelButton;

	private ArrayAdapter<String> adapter;

	private static FileManager m_FileManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_choose_group);

		/* OUR CODE BEGINS HERE */
		m_FileManager = new FileManagerImpl();
		m_contactsProvider = new ContactsProviderImpl(this);

		listSetUp();
		addListenerOnButton();
		enablingSearchFilter();
	}

	private void enablingSearchFilter() {
		EditText inputSearch = (EditText) findViewById(R.id.edSearch);

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Nothing right now

			}

			@Override
			public void afterTextChanged(Editable s) {
				// Nothing right now
			}
		});
	}

	private void listSetUp() {
		// Display mode of the ListView
		ListView listview= getListView();
		listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		// Enable text filtering
		listview.setTextFilterEnabled(true);

		m_choosenContacts = new ContactsGroup(m_groupName);
		m_allContacts = new ContactsGroup(doChooseGroup());

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, m_allContacts.getNames());
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView parent, View v,int position,long id){
		CheckedTextView item = (CheckedTextView) v;

		boolean isChecked = item.isChecked();

		String name = ((TextView)v).getText().toString();
		String number = m_allContacts.getNumber(name);

		if (isChecked)
		{
			m_choosenContacts.add(number, name);
		}
		else
		{
			m_choosenContacts.removeByNumber(number);
		}

		Toast.makeText(this, (isChecked ? " נוסף " : " הורד " ) + name, Toast.LENGTH_SHORT).show();
	}

	public Map<String, String> doChooseGroup() {
		return m_contactsProvider.provideContacts();
	};

	public void addListenerOnButton() {
		m_ApproveButton = (Button) findViewById(R.id.btnContactsApproval);

		m_ApproveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try
				{
					m_FileManager.write(m_groupName, m_choosenContacts.getNumbersAsString(), false);
				}
				catch (IOException e)
				{
					Toast.makeText(MainActivity.getAppContext(), "נכשלה יצירת קבוצה", Toast.LENGTH_SHORT).show();
				}

				finish();
			}

		});
	}
}
