import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class JoinPanelTable extends JPanel{
	private final static int MAXIMUM_NUMBER_OF_COLUMNS = 14;
	
	private JLabel labelSelect = new JLabel("Table:");
	private JComboBox<String> comboSelectTable = new JComboBox<String>();
	private JTextArea resultSelect = new JTextArea(10, 50);
	private JScrollPane resultScrollSelect = new JScrollPane(resultSelect);
	private JLabel labelSelectField = new JLabel("Field:");
	private JLabel labelWhereSelect = new JLabel("WHERE: ");
	private JTextField whereSelect = new JTextField(10);
	private JButton buttonOkSelect = new JButton("Launch query");
	private JPanel checkBoxesSelectedFieldsPanel = new JPanel(new GridLayout(0, 1));
	private JCheckBox[] checkBoxesSelectedFields = new JCheckBox[MAXIMUM_NUMBER_OF_COLUMNS];
	private JComboBox<String> keyBox = new JComboBox<String>();
	
	private Connection conn = null;
	
	
	public JoinPanelTable(){
		add(labelSelect);
		
		Fenetre.fillComboWithStrings(comboSelectTable, Fenetre.tables);
		
		comboSelectTable.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("Hello");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String sql = "SELECT * FROM " + comboSelectTable.getSelectedItem();
					
					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int noColumns = rsmd.getColumnCount();
					String[] fields = new String[noColumns];
					
					clearAllCheckBoxes();
					for(int i = 0; i < noColumns; ++i){
						fields[i] = rsmd.getColumnName(i + 1);
						checkBoxesSelectedFields[i].setText(fields[i]);
						checkBoxesSelectedFields[i].setVisible(true);
					}
					Fenetre.fillComboWithStrings(keyBox, fields);
					
					for(int i = noColumns; i < checkBoxesSelectedFields.length; ++i){
						checkBoxesSelectedFields[i].setVisible(false);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						conn.close();
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		add(comboSelectTable);
		add(labelSelectField);
		initializeCheckBoxes();
		
		add(labelWhereSelect);
		add(whereSelect);
		JPanel primaryKeyPanel = new JPanel(new GridLayout(1, 2));
		primaryKeyPanel.add(new JLabel("Primary key: "));
		primaryKeyPanel.add(keyBox);
		add(primaryKeyPanel);
	}
	
	private void initializeCheckBoxes(){
		checkBoxesSelectedFieldsPanel.removeAll();
		for(int i = 0; i < checkBoxesSelectedFields.length; ++i){
			checkBoxesSelectedFields[i] = new JCheckBox();
			checkBoxesSelectedFieldsPanel.add(checkBoxesSelectedFields[i]);
		}
		add(checkBoxesSelectedFieldsPanel);
	}
	
	private void clearAllCheckBoxes(){
		for(int i = 0; i < checkBoxesSelectedFields.length; ++i){
			checkBoxesSelectedFields[i].setText("");
		}
	}
	
	public String selectedFieldsToString(){
		String result = "";
		for(int i = 0; i < checkBoxesSelectedFields.length; ++i){
			if(checkBoxesSelectedFields[i].isVisible() && checkBoxesSelectedFields[i].isSelected()){
				result += comboSelectTable.getSelectedItem() + "." + checkBoxesSelectedFields[i].getText();
				result += ", ";
			}
		}
		if(result.equals("")){
			return "";
		}
		result = result.substring(0, result.length() - 2);
		return result;
	}
	
	public String getTableName(){
		return comboSelectTable.getSelectedItem().toString();
	}
	
	public String getJoinKey(){
		return keyBox.getSelectedItem().toString();
	}
	
	public String whereClause(){
		return whereSelect.getText();
	}
}
