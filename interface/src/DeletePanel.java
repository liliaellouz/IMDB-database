import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class DeletePanel extends JPanel{
	private final static int MAXIMUM_NUMBER_OF_COLUMNS = 20;
	
	private JLabel labelSelect = new JLabel("Table:");
	private JComboBox<String> comboSelectTable = new JComboBox<String>();
	private JTextArea resultSelect = new JTextArea(10, 50);
	private JScrollPane resultScrollSelect = new JScrollPane(resultSelect);
	private JLabel labelSelectField = new JLabel("Field:");
	private JLabel labelWhereSelect = new JLabel("WHERE: ");
	private JTextField whereSelect = new JTextField(10);
	private JButton buttonOkSelect = new JButton("Launch query");
	private JTextArea checkBoxesSelectedFieldsPanel = new JTextArea();
	private JLabel feedback = new JLabel();
	
	private Connection conn = null;
	
	public DeletePanel(){
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
					checkBoxesSelectedFieldsPanel.setText("");
					for(int i = 0; i < noColumns; ++i){
						fields[i] = rsmd.getColumnName(i + 1);
						if(i==(noColumns-1)) {
							checkBoxesSelectedFieldsPanel.append(fields[i]);
						}else {
							checkBoxesSelectedFieldsPanel.append(fields[i] + "\n");
						}
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
		
		add(checkBoxesSelectedFieldsPanel);
		add(labelWhereSelect);
		add(whereSelect);
		//add(resultScrollSelect);
		buttonOkSelect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
				String sql = "DELETE FROM " + comboSelectTable.getSelectedItem();
				if(whereSelect.getText().length() > 0){
					sql += (" WHERE " + whereSelect.getText());
				}
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					feedback.setText("Done");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					feedback.setText("Error while inserting the data");
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
		add(buttonOkSelect);
		
		add(feedback);
	}

}