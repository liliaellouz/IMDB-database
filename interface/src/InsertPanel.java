import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class InsertPanel extends JPanel{
	private JComboBox<String> combo = new JComboBox<String>();
	private JLabel label = new JLabel("Table:");
	private JLabel fieldName = new JLabel("Field: ");
	private JTextField fieldValue = new JTextField(10);
	private JList<String> listSelectFields = new JList<String>();
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JTextArea insertRow = new JTextArea();
	private String insertString = "";
	private JButton ok = new JButton("Insert");
	private JLabel feedback = new JLabel();
	
	private JPanel oneElemHandle = new JPanel();
	
	private Connection conn = null;
	
	public InsertPanel(){
		add(label);
		//fillComboWithStrings(combo, tables);
		Fenetre.fillComboWithStrings(combo, Fenetre.tables);
		add(combo);
		
		listSelectFields.setModel(model);
		combo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("Hello");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String sql = "SELECT * FROM " + combo.getSelectedItem();
					
					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int noColumns = rsmd.getColumnCount();
					String[] fields = new String[noColumns];
					model.removeAllElements();
					insertRow.setText("");
					for(int i = 0; i < noColumns; ++i){
						fields[i] = rsmd.getColumnName(i + 1);
						model.addElement(fields[i]);
						if(i==(noColumns-1)) {
							insertRow.append("null");
						}else {
							insertRow.append("null\n");
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
		
		add(new JLabel("Field:"));
		add(listSelectFields);
		
		listSelectFields.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Hello2");
				fieldName.setText(listSelectFields.getSelectedValue());
			}
		});
		
		fieldValue.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Hello3");
			}
		});
		
//		oneElemHandle.add(fieldName);
//		oneElemHandle.add(fieldValue);
		
//		add(oneElemHandle);
		
		add(insertRow);
		
		//Ok button:
		ok.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				String[] values = insertRow.getText().split("\n");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String sql = "INSERT INTO " + combo.getSelectedItem() + " VALUES (";
					for(int i = 0; i < values.length; ++i){
						sql += values[i];
						if(i < values.length - 1){
							sql += ", ";
						}
					}
					sql += ")";
					
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
		
		add(ok);
		add(feedback);
	}
}
