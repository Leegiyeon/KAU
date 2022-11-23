import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Employee extends JPanel {
	
	String fname;
	String minit;
	String lname;
	String ssn;
	String bdate;
	String address;
	String sex;
	Double salary;
	String superSsn;
	Integer dno;
	
	JTextField firstNameBox = new JTextField();
	JTextField middleInitBox = new JTextField();
	JTextField lastNameBox = new JTextField();
	JTextField ssnBox = new JTextField();
	JTextField bdateBox = new JTextField();
	JTextField addressBox = new JTextField();
	
	String sexRange[] = {"F", "M"};
	JComboBox<String> sexBox = new JComboBox<String>(sexRange);
	
	JTextField salaryBox = new JTextField();
	JTextField superSsnBox = new JTextField();
	JComboBox dnoBox;
	
	public Employee(Integer[] dnos) {
		dnoBox = new JComboBox<Integer>(dnos);
		design();
	}
	
	public void design() {
		JFrame frame = new JFrame();
		frame.setSize(530,450);
		
		Container container = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JLabel title = new JLabel("새로운 직원 정보 추가");
		title.setBounds(7, 10, 200, 30);
		panel.add(title);
		
		JLabel label1 = new JLabel("First Name: ");
		label1.setBounds(7, 50, 100, 30);
		firstNameBox.setBounds(110, 50, 300, 30);
		panel.add(label1);
		panel.add(firstNameBox);
		
		JLabel label2 = new JLabel("Middle Init.: ");
		label2.setBounds(7, 80, 100, 30);
		middleInitBox.setBounds(110, 80, 300, 30);
		panel.add(label2);
		panel.add(middleInitBox);
		
		JLabel label3 = new JLabel("Last Name: ");
		label3.setBounds(7, 110, 100, 30);
		lastNameBox.setBounds(110, 110, 300, 30);
		panel.add(label3);
		panel.add(lastNameBox);
		
		JLabel label4 = new JLabel("Ssn: ");
		label4.setBounds(7, 140, 100, 30);
		ssnBox.setBounds(110, 140, 300, 30);
		panel.add(label4);
		panel.add(ssnBox);
		
		JLabel label5 = new JLabel("Birthdate: ");
		label5.setBounds(7, 170, 100, 30);
		JLabel etcLabel = new JLabel("(xxxx-xx-xx)");
		etcLabel.setBounds(410, 170, 100, 30);
		bdateBox.setBounds(110, 170, 300, 30);
		panel.add(label5);
		panel.add(etcLabel);
		panel.add(bdateBox);
		
		JLabel label6 = new JLabel("Address: ");
		label6.setBounds(7, 200, 100, 30);
		addressBox.setBounds(110, 200, 300, 30);
		panel.add(label6);
		panel.add(addressBox);
		
		JLabel label7 = new JLabel("Sex: ");
		label7.setBounds(7, 230, 100, 30);
		sexBox.setBounds(110, 230, 300, 30);
		panel.add(label7);
		panel.add(sexBox);
		
		JLabel label8 = new JLabel("Salary: ");
		label8.setBounds(7, 260, 100, 30);
		salaryBox.setBounds(110, 260, 300, 30);
		panel.add(label8);
		panel.add(salaryBox);
		
		JLabel label9 = new JLabel("Super_ssn: ");
		label9.setBounds(7, 290, 100, 30);
		superSsnBox.setBounds(110, 290, 300, 30);
		panel.add(label9);
		panel.add(superSsnBox);
		
		JLabel label10 = new JLabel("Dno: ");
		label10.setBounds(7, 320, 100, 30);
		dnoBox.setBounds(110, 320, 300, 30);
		panel.add(label10);
		panel.add(dnoBox);
		
		JButton addButton = new JButton("정보 추가하기");
		addButton.setBounds(200, 380, 150, 30);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fname = firstNameBox.getText();
				minit = middleInitBox.getText();
				lname = lastNameBox.getText();
				ssn = ssnBox.getText();
				bdate = bdateBox.getText();
				address = addressBox.getText();
				sex = (String) sexBox.getSelectedItem();
				String salaryString = salaryBox.getText();
				superSsn = superSsnBox.getText();
				dno = (Integer) dnoBox.getSelectedItem();
				
				if (fname.isEmpty()) {
					JOptionPane.showMessageDialog(null, "First Name 입력을 확인해주세요.");
					return;
				} else if (minit.length() > 1) {
					JOptionPane.showMessageDialog(null, "Middle Init.은 한 글자여야 합니다.");
					return;
				} else if (lname.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Last Name 입력을 확인해주세요.");
					return;
				} else if (ssn.length() != 9) {
					JOptionPane.showMessageDialog(null, "Ssn 입력을 확인해주세요.");
					return;
				} else if (bdate.split("-").length != 3 && !bdate.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Birthdate 입력을 확인해주세요.");
					return;
				} else if (superSsn.length() != 9 && !superSsn.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Super_Ssn 입력을 확인해주세요.");
					return;
				}
				
				if (minit.isEmpty()) {
					minit = null;
				}
				if (address.isEmpty()) {
					address = null;
				} 
				if (superSsn.isEmpty()) {
					superSsn = null;
				}
				if (bdate.isEmpty()) {
					bdate = null;
				} else {
					String date[] = bdate.split("-");
					try {
						Integer year = Integer.parseInt(date[0]);
						Integer month = Integer.parseInt(date[1]);
						Integer day = Integer.parseInt(date[2]);
						bdate = year + "-" + month + "-" + day;
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Birthdate 입력을 확인해주세요.");
						return;
					}
				}
				
				if (salaryString.isEmpty()) {
					salary = null;
				} else {
					try {
						salary = Double.parseDouble(salaryString); 
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Salary 입력을 확인해주세요.");
						return;
					}
				}
				
				String sql = String.format("insert into EMPLOYEE values ('%s', ", fname);
				sql = sql + ((minit == null) ? "null, '" : "'"+minit+"', '")+lname +"', '"+ssn+"', ";
				sql = sql + ((bdate == null) ? "null, " : "'"+bdate+"', ");
				sql = sql + ((address == null) ? "null, '":"'"+address+"', '")+sex+"', ";
				sql = sql + ((salary == null) ? "null, ": salary+", ");
				sql = sql + ((superSsn == null) ? "null, ": "'"+superSsn+"', ") + dno;
				sql = sql + ", now(), now());";
				Company.runInsertSQL(sql);
				frame.dispose();
			}
		});
		panel.add(addButton);
		
		panel.setVisible(true);
		container.add(panel);
	}
}