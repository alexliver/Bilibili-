package crc;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


public class TableData extends AbstractTableModel {

	List<String[]> data = new ArrayList<String[]>();
	String [] colNames = new String[]{"name","comment"};


	@Override
	public int getColumnCount() {
		
		return 2;
	}



	@Override
	public Object getValueAt(int arg0, int arg1) {
		
		return data.get(arg0)[arg1+1];
	}



	@Override
	public int getRowCount() {
		return data.size();
	}



	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}
	public void addRow(String id, String name, String comment)
	{
		this.data.add(new String[]{id,name,comment});
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	public void clear()
	{
		this.data.clear();
		this.fireTableDataChanged();
	}
	public String getUrl(int row)
	{
		return "http://space.bilibili.com/"+data.get(row)[0];
	}
}
