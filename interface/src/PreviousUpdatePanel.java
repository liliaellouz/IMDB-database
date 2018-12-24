import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//update presque comme insert (et select)

public class PreviousUpdatePanel extends JPanel {

	private final static int MAXIMUM_NUMBER_OF_COLUMNS = 14;

	private JComboBox<String> combo = new JComboBox<String>();
	private JLabel label = new JLabel("Table:");
	private JLabel fieldName = new JLabel("Field: ");
	private JTextField fieldValue = new JTextField(10);
	private JList<String> listSelectFields = new JList<String>();
	private JLabel labelWhereSelect = new JLabel("WHERE: ");
	private JTextField whereSelect = new JTextField(10);
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JTextArea updateRow = new JTextArea();
	private String insertString = "";
	private JButton ok = new JButton("Update");
	private JLabel feedback = new JLabel();
	ArrayList<String> columns = new ArrayList<String>();
	ArrayList<Integer> types = new ArrayList<Integer>();

	private JPanel oneElemHandle = new JPanel();

	private Connection conn = null;

	public PreviousUpdatePanel(){
		add(label);
		//fillComboWithStrings(combo, tables);
		Fenetre.fillComboWithStrings(combo, Fenetre.tables);
		add(combo);

		listSelectFields.setModel(model);
		
		add(labelWhereSelect);
		add(whereSelect);
		combo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub

				//System.out.println("Hello");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String sql = "SELECT * FROM " + combo.getSelectedItem();
					if(whereSelect.getText().length() > 0){
						sql += (" WHERE " + whereSelect.getText());
					}

					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int noColumns = rsmd.getColumnCount();
					String[] fields = new String[noColumns];
					model.removeAllElements();
					updateRow.setText("");
					String result = "";
					for(int i = 0; i < noColumns; ++i){
						fields[i] = rsmd.getColumnName(i + 1);
						columns.add(rsmd.getColumnName(i + 1));
						model.addElement(fields[i]);
						System.out.println("Type:"+rsmd.getColumnType(i+1));
						result = getData(rs, rsmd.getColumnType(i+1), i+1);
						types.add(rsmd.getColumnType(i+1));
						if(i==(noColumns-1)) {
							updateRow.append(result);
						}else {
							updateRow.append(result+"\n");
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

		//	oneElemHandle.add(fieldName);
		//	oneElemHandle.add(fieldValue);

		//	add(oneElemHandle);

		add(updateRow);

		//Ok button:
		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				String[] values = updateRow.getText().split("\n");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					
					String sql = "UPDATE " + combo.getSelectedItem() + " SET ";
					for(int i = 0; i < values.length; ++i){
						sql += columns.get(i);
						int type = types.get(i);
			            if (type == Types.VARCHAR || type == Types.CHAR) {
			            		sql+=" = '"+values[i]+"'";
			            }else {
			            		sql+=" = "+values[i];
			            }
						if(i < values.length - 1){
							sql += ", ";
						}
					}

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
	
	public static String getData(ResultSet rs, int type, int colIdx) throws SQLException {
	    switch (type) {
	    case Types.CHAR:
	    case Types.VARCHAR:
	      return rs.getString(colIdx);

	    case Types.INTEGER:
	      int i = rs.getInt(colIdx);
	      return Integer.toString(i);

	    case Types.TIMESTAMP:
	    case Types.DATE:
	      java.sql.Date d = rs.getDate(colIdx);
	      return d.toString();

	    }
		return "Type introuvable";
	  }
	
}
