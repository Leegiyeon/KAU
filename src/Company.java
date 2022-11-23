import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Company {
    static final String url = "jdbc:mysql://localhost:3306/Company?serverTimezone=UTC";
    static final String user = "root";
    static final String password = "0000";

    static final String searchRange[] = {"전체", "부서", "성별", "연봉", "생일", "부하직원", "가족"};
    static final JComboBox searchRangeBox = new JComboBox<String>(searchRange);
    static final JComboBox departmentBox = new JComboBox<String>();
    static final JComboBox sexBox = new JComboBox<String>();
    static final JTextField salaryBox = new JTextField();
    static final Integer bdateRange[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    static final JComboBox bdateBox = new JComboBox<Integer>(bdateRange);
    static final JTextField subordinateBox = new JTextField();

    static final JTextField dependentBox = new JTextField();
    static final JLabel selectedEmployeeLabel = new JLabel("선택한 직원:");
    static final JLabel selectedEmployeeCountLabel = new JLabel("인원수:");
    static final DefaultTableModel currentData = new DefaultTableModel();
    static final Map<String, String> selectedEmployee = new HashMap<String, String>();

    static final String updateRange[] = {"Address", "Sex", "Salary"};
    static final JComboBox updateRangeBox = new JComboBox<String>(updateRange);
    static final JTextField updateBox = new JTextField();
    static Boolean dependent_search_flag = false;


    static final ArrayList<JCheckBox> searchItemBoxes = new ArrayList<JCheckBox>();
    static final JTable resultTable = new JTable(new DefaultTableModel());

    public static void setFrame() {
        JFrame frame = new JFrame();
        frame.setSize(1000, 800);

        Container container = frame.getContentPane();

        resultTable.setVisible(false);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(50, 65, 900, 550);
        container.add(scrollPane);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        setSearchRanges(panel);
        setSearchItems(panel);
        setSearchButton(panel);
        setResultBar(panel);
        setUpdateField(panel);
        setOptionField(panel);
        setUpdateByDep(panel);

        panel.setVisible(true);
        container.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        search();
    }

    public static void setSearchRanges(JPanel panel) {
        JLabel searchRangeLabel = new JLabel("검색 범위");
        searchRangeLabel.setBounds(7, 5, 50, 25);
        panel.add(searchRangeLabel);

        searchRangeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) searchRangeBox.getSelectedItem();
                departmentBox.setVisible(false);
                sexBox.setVisible(false);
                salaryBox.setText("");
                salaryBox.setVisible(false);
                bdateBox.setVisible(false);
                subordinateBox.setText("");
                subordinateBox.setVisible(false);
                dependentBox.setText("");
                dependentBox.setVisible(false);
                dependent_search_flag = false;
                if (s == searchRange[1]) {
                    String sql = "Select dname from DEPARTMENT;";
                    runSelectSQL(sql, (rs) -> {
                        try {
                            DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
                            while (rs.next()) {
                                model.addElement(rs.getString("dname"));
                            }
                            departmentBox.setModel(model);
                            departmentBox.setVisible(true);
                        } catch (SQLException e1) {
                            System.out.println("select dname error " + e1.getLocalizedMessage());
                        }
                        return null;
                    });
                } else if (s == searchRange[2]) {
                    String sql = "Select distinct sex from EMPLOYEE;";
                    runSelectSQL(sql, (rs) -> {
                        try {
                            DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
                            while (rs.next()) {
                                model.addElement(rs.getString("sex"));
                            }
                            sexBox.setModel(model);
                            sexBox.setVisible(true);
                        } catch (SQLException e1) {
                            System.out.println("select sex error " + e1.getLocalizedMessage());
                        }
                        return null;
                    });
                } else if (s == searchRange[3]) {
                    salaryBox.setVisible(true);
                } else if (s == searchRange[4]) {
                    bdateBox.setVisible(true);
                } else if (s == searchRange[5]) {
                    subordinateBox.setVisible(true);
                } else if (s == searchRange[6]) {
                    dependentBox.setVisible(true);
                    dependent_search_flag = true;
                    //Todo 하단 체크박스를 안보이게 만들려고 함.
                    for (int i = 0; i < searchItemBoxes.size(); i++) {

                    }
                }
            }
        });

        searchRangeBox.setBounds(60, 5, 100, 30);
        panel.add(searchRangeBox);

        departmentBox.setBounds(170, 5, 200, 30);
        departmentBox.setVisible(false);
        panel.add(departmentBox);

        sexBox.setBounds(170, 5, 200, 30);
        sexBox.setVisible(false);
        panel.add(sexBox);

        salaryBox.setBounds(170, 5, 200, 30);
        salaryBox.setVisible(false);
        panel.add(salaryBox);

        bdateBox.setBounds(170, 5, 200, 30);
        bdateBox.setVisible(false);
        panel.add(bdateBox);

        subordinateBox.setBounds(170, 5, 200, 30);
        subordinateBox.setVisible(false);
        panel.add(subordinateBox);

        dependentBox.setBounds(170, 5, 200, 30);
        dependentBox.setVisible(false);
        panel.add(dependentBox);
    }
    
    public static void setUpdateByDep(JPanel panel) {
		JButton updateByDep = new JButton("부서별 월급 일괄 업데이트");
		updateByDep.setBounds(180, 730, 200, 30);
		updateByDep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSalaryByDep();
			}
		});
		panel.add(updateByDep);
	}
    
    public static void updateSalaryByDep() {

		String depNames[] = {"Research", "Headquarters","Administration"};
		JComboBox <String> depBox = new JComboBox<String>(depNames);
		JLabel selectDepLabel = new JLabel("Select Department");
		JLabel inSalLabel = new JLabel("Input salary");
		JButton updateButton = new JButton("Update");
		JTextField salBox = new JTextField();
		
		JFrame frame = new JFrame();
		frame.setSize(400,180);
		
		Container container = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		selectDepLabel.setBounds(10,5,150,30 );
		selectDepLabel.setVisible(true);
		panel.add(selectDepLabel);
		
		depBox.setBounds(10, 40, 100,30);
		depBox.setVisible(true);
		panel.add(depBox);
		
		inSalLabel.setBounds(180,5,100,30);
		inSalLabel.setVisible(true);
		panel.add(inSalLabel);
		
		salBox.setBounds(180,40,100,30);
		salBox.setVisible(true);
		panel.add(salBox);
		
		updateButton.setBounds(300, 40, 80,30);
		updateButton.setVisible(true);
		panel.add(updateButton);
		
		
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				String sal = salBox.getText();
				String depName = (String) depBox.getSelectedItem();
				int depValue = 0;
				if (depName == "Research") {
					depValue = 5;
				}else if(depName == "Administration") {
					depValue = 4;
				}else if(depName == "Headquarters") {
					depValue = 1;
				}
				Connection con = null;
				PreparedStatement ps = null;
				try {
					con = DriverManager.getConnection(url,user,password);
				         String query = "update employee set salary=? where dno=? ;";
				         ps = con.prepareStatement(query);
				         ps.setString(1, sal);
				         ps.setInt(2, depValue);
				         ps.executeUpdate();
				         System.out.println("Record is updated successfully......");
				         frame.dispose();
				         search();
				} 
				catch (SQLException a) {
				            a.printStackTrace();
				}
			}
				
		});
		
		panel.setVisible(true);
		container.add(panel);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

    public static void setSearchItems(JPanel panel) {
        JLabel searchItemLabel = new JLabel("검색 항목");
        searchItemLabel.setBounds(7, 35, 50, 25);
        panel.add(searchItemLabel);

        JCheckBox name = new JCheckBox("Name");
        name.setBounds(60, 35, 90, 25);
        name.setSelected(true);
        panel.add(name);
        searchItemBoxes.add(name);

        JCheckBox ssn = new JCheckBox("Ssn");
        ssn.setBounds(140, 35, 60, 25);
        ssn.setSelected(true);
        panel.add(ssn);
        searchItemBoxes.add(ssn);

        JCheckBox bdate = new JCheckBox("Bdate");
        bdate.setBounds(200, 35, 90, 25);
        bdate.setSelected(true);
        panel.add(bdate);
        searchItemBoxes.add(bdate);

        JCheckBox address = new JCheckBox("Address");
        address.setBounds(280, 35, 90, 25);
        address.setSelected(true);
        panel.add(address);
        searchItemBoxes.add(address);

        JCheckBox sex = new JCheckBox("Sex");
        sex.setBounds(370, 35, 70, 25);
        sex.setSelected(true);
        panel.add(sex);
        searchItemBoxes.add(sex);

        JCheckBox salary = new JCheckBox("Salary");
        salary.setBounds(440, 35, 90, 25);
        salary.setSelected(true);
        panel.add(salary);
        searchItemBoxes.add(salary);

        JCheckBox supervisor = new JCheckBox("Supervisor");
        supervisor.setBounds(530, 35, 110, 25);
        supervisor.setSelected(true);
        panel.add(supervisor);
        searchItemBoxes.add(supervisor);

        JCheckBox department = new JCheckBox("Department");
        department.setBounds(630, 35, 110, 25);
        department.setSelected(true);
        panel.add(department);
        searchItemBoxes.add(department);
    }

    public static void setSearchButton(JPanel panel) {
        JButton searchButton = new JButton("검색");
        searchButton.setBounds(750, 35, 70, 25);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        panel.add(searchButton);
    }

    public static void setUpdateField(JPanel panel) {
        JLabel updateLabel = new JLabel("수정: ");
        updateLabel.setBounds(250, 680, 30, 30);
        panel.add(updateLabel);

        updateRangeBox.setBounds(280, 670, 120, 50);
        panel.add(updateRangeBox);

        updateBox.setBounds(400, 680, 200, 30);
        panel.add(updateBox);

        JButton updateButton = new JButton("UPDATE");
        updateButton.setBounds(600, 680, 80, 30);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedEmployee.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "업데이트할 직원을 선택해주세요.");
                    return;
                }
                String sql = "Update EMPLOYEE SET modified = now()," + (String) updateRangeBox.getSelectedItem() + " = ";
                if ((Integer) updateRangeBox.getSelectedIndex() == 2) {
                    sql = sql + updateBox.getText();
                } else {
                    sql = sql + "'" + updateBox.getText() + "'";
                }
                sql = sql + " WHERE Ssn in (";
                for (String strKey : selectedEmployee.keySet()) {
                    sql = sql + "'" + strKey + "',";
                }
                sql = sql.substring(0, sql.length() - 1) + ");";
                runSQL(sql, (rs) -> {
                    System.out.print(rs);
                    search();
                    return null;
                });
                updateBox.setText("");
            }
        });

        panel.add(updateButton);
    }

    public static void setResultBar(JPanel panel) {
        selectedEmployeeLabel.setBounds(7, 650, 800, 30);
        selectedEmployeeLabel.setVisible(true);
        panel.add(selectedEmployeeLabel);

        selectedEmployeeCountLabel.setBounds(7, 680, 100, 30);
        selectedEmployeeCountLabel.setVisible(true);
        panel.add(selectedEmployeeCountLabel);

        JButton insertButton = new JButton("데이터 추가");
        insertButton.setBounds(800, 650, 150, 30);
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sql = "Select distinct Dnumber from department order by dnumber;";
                runSelectSQL(sql, (result) -> {
                    try {
                        ArrayList<Integer> departmentNums = new ArrayList<Integer>();
                        while (result.next()) {
                            Integer dno = result.getInt("Dnumber");
                            departmentNums.add(dno);
                        }
                        Employee employee = new Employee(departmentNums.toArray(new Integer[0]));
                    } catch (SQLException error) {
                        System.out.println("showResult error " + error.getLocalizedMessage());
                    }
                    return null;
                });
            }
        });
        panel.add(insertButton);

        JButton deleteButton = new JButton("선택한 데이터 삭제");
        deleteButton.setBounds(800, 680, 150, 30);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedEmployee.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "삭제할 직원을 선택해주세요.");
                    return;
                }
                String sql = "Delete from EMPLOYEE where ssn IN (";
                for (String strKey : selectedEmployee.keySet()) {
                    sql = sql + "'" + strKey + "',";
                }
                sql = sql.substring(0, sql.length() - 1) + ");";
                runSQL(sql, (rs) -> {
                    search();
                    return null;
                });
            }
        });

        panel.add(deleteButton);
    }
    public static void set_emp_query_result_to_table(ArrayList<String> column, ResultSet result, DefaultTableModel model) throws SQLException {
        while (result.next()) {
            Object[] data = new Object[column.size() - 2];
            Object[] pkData = new Object[column.size()];
            //체크박스 초깃 값 false
            data[0] = false;

            String name = null;

            for (int i = 1; i < column.size() - 2; i++) {

                if (column.get(i) == "Name") {
                    String fname = result.getString("eFname");
                    String minit = result.getString("eMinit");
                    String lname = result.getString("eLname");
                    name = fname + (minit == null ? "" : " " + minit) + " " + lname;
                    data[i] = name;
                } else if (column.get(i) == "Supervisor") {
                    // 상사 없는 경우
                    if (result.getString("sFname") == null) {
                        data[i] = "";
                        // 상사 있는 경우
                    } else {
                        String sfname = result.getString("sFname");
                        String sminit = result.getString("sMinit");
                        String slname = result.getString("sLname");
                        data[i] = sfname + " " + sminit + " " + slname;
                    }
                } else {
                    data[i] = result.getString(column.get(i));
                }
                pkData[i] = data[i];
            }

            pkData[column.size() - 2] = result.getString("Ssn");
            pkData[column.size() - 1] = name;
//            System.out.println(Arrays.toString(data));
            model.addRow(data);
            currentData.addRow(pkData);
        }
    }

    public static void set_dependent_query_result_to_table(ArrayList<String> column, ResultSet result, DefaultTableModel model) throws SQLException {
        while (result.next()) {
            Object[] data = new Object[column.size() - 2];
            Object[] pkData = new Object[column.size()];

            //체크박스 초깃 값 false
            data[0] = false;

            for (int i = 1; i < column.size() - 2; i++) {
                System.out.println(column.get(i));
                data[i] = result.getString(column.get(i));
                System.out.println(data[i]);
                pkData[i] = data[i];
            }

            System.out.println(Arrays.toString(data));
            model.addRow(data);
            currentData.addRow(pkData);
        }
    }
    public static Void showResult(ArrayList<String> column, ResultSet result) {
        try {
            DefaultTableModel model = new DefaultTableModel(column.toArray(), 0) {
                @Override
                public void setValueAt(Object value, int row, int col) {
                    super.setValueAt(value, row, col);
                    if (col == 0) {
                        String ssn = (String) currentData.getValueAt(row, currentData.getColumnCount() - 2);
                        String name = (String) currentData.getValueAt(row, currentData.getColumnCount() - 1);
                        if ((Boolean) value) {
                            selectedEmployee.put(ssn, name);
                        } else {
                            selectedEmployee.remove(ssn);
                        }
                        String str = "선택한 직원: ";
                        for (String strKey : selectedEmployee.keySet()) {
                            String val = selectedEmployee.get(strKey);
                            str = str + val + ", ";
                        }
                        selectedEmployeeLabel.setText(str.substring(0, str.length() - 2));
                    }
                }
            };
            column.add("pk");
            column.add("pkName");
            System.out.println(column);

            currentData.setColumnIdentifiers(column.toArray());
            currentData.setRowCount(0);
            System.out.println(dependent_search_flag);
            if (dependent_search_flag)
            {
                //dependent 결과 출력의 경우
                set_dependent_query_result_to_table(column, result, model);
                dependent_search_flag = false;
            } else {
                //employee 결과 출력의 경우
                set_emp_query_result_to_table(column, result, model);
            }

            resultTable.setModel(model);

            resultTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JCheckBox check = new JCheckBox();
                    check.setSelected((Boolean) value);
                    return check;
                }
            });

            resultTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
            resultTable.setVisible(true);

            selectedEmployeeCountLabel.setText("인원수 : " + model.getRowCount());
            selectedEmployee.clear();
            selectedEmployeeLabel.setText("선택한 사람 이름 : ");

        } catch (SQLException e) {
            System.out.println("showResult error " + e.getLocalizedMessage());
        }
        return null;
    }


    public static void runInsertSQL(String sql) {
        runSQL(sql, (rs) -> {
            System.out.println(rs + "insert");
            search();
            return null;
        });
    }

    public static void runSelectSQL(String sql, Function<ResultSet, Void> completion) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("정상적으로 연결되었습니다");
            try (Statement stmt = conn.createStatement()) {
                System.out.println(sql);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    completion.apply(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("runSQL error " + e.getLocalizedMessage());
        }
    }

    public static void runSQL(String sql, Function<Void, Void> completion) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("정상적으로 연결되었습니다");
            try (Statement stmt = conn.prepareStatement(sql)) {
                System.out.println(sql);
                int rs = stmt.executeUpdate(sql);
                System.out.println(rs);
                if (rs > 0) {
                    completion.apply(null);
                    search();
                }
            }
        } catch (SQLException e) {
            System.out.println("runSQL error " + e.getLocalizedMessage());
        }
    }

    public static void search() {
        String sql = "Select e.Ssn, e.Fname as eFname,e.Minit as eMinit,e.Lname as eLname,";
        ArrayList<String> column = new ArrayList<String>();
        column.add("선택");

        for (JCheckBox item : searchItemBoxes) {
            if (item.isSelected() == false) {
                continue;
            } else if (item.getText() == "Ssn" || item.getText() == "Name") {
                column.add(item.getText());
                continue;
            } else if (item.getText() == "Supervisor") {
                sql = sql + "s.Fname as sFname,s.Minit as sMinit,s.Lname as sLname,";
            } else if (item.getText() == "Department") {
                sql = sql + "d.Dname as Department,";
            } else {
                sql = sql + "e." + item.getText() + ",";
            }
            column.add(item.getText());
        }

        sql = sql.substring(0, sql.length() - 1) + " from EMPLOYEE e left join EMPLOYEE s ON e.Super_ssn = s.Ssn join DEPARTMENT d ON e.Dno = d.Dnumber ";

        Integer searchRangeIdx = searchRangeBox.getSelectedIndex();
        if (searchRangeIdx == 1) {
            sql = sql + "where d.Dname = '" + (String) departmentBox.getSelectedItem() + "'";
        } else if (searchRangeIdx == 2) {
            sql = sql + "where e.Sex = '" + (String) sexBox.getSelectedItem() + "'";
        } else if (searchRangeIdx == 3) {
            try {
                Integer salary = Integer.parseInt(salaryBox.getText());
                sql = sql + "where e.Salary > " + salary;
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "숫자를 입력해주세요");
            }
        } else if (searchRangeIdx == 4) {
            Integer bmonth = (Integer) bdateBox.getSelectedItem();
            sql = sql + "where MONTH(e.bdate) = " + bmonth;
        } else if (searchRangeIdx == 5) {
            String[] name = ((String) subordinateBox.getText()).split(" ");
            if (name.length == 3) {
                String fname = name[0];
                String minit = name[1];
                String lname = name[2];
                sql = sql + "where s.fname = '" + fname + "' AND s.minit = '" + minit + "' AND s.lname = '" + lname + "'";
            } else {
                JOptionPane.showMessageDialog(null, "이름을 정상적으로 입력해주세요\n(ex Jennifer S Wallace");
            }
        } else if (searchRangeIdx == 6) {
            column.clear();
            column.add("선택");
            column.add("dependent_name");
            column.add("sex");
            column.add("bdate");
            column.add("relationship");
            String[] name = ((String) dependentBox.getText()).split(" ");
            if (name.length == 3) {
                String fname = name[0];
                String minit = name[1];
                String lname = name[2];
//                sql = "select * From EMPLOYEE";
                sql = "select d.dependent_name,d.bdate,d.sex,d.relationship from DEPENDENT d where d.Essn = (select s.Ssn from EMPLOYEE s where s.fname = '" + fname + "' AND s.minit = '" + minit + "' AND s.lname = '" + lname + "')";
            } else {
                JOptionPane.showMessageDialog(null, "이름을 정상적으로 입력해주세요\n(ex Jennifer S Wallace");
            }
        }
        runSelectSQL(sql + ";", rs -> showResult(column, rs));
    }

	public static void setOptionField(JPanel panel) {
		JButton showDepartmentButton = new JButton("부서별 직원 보기");
		showDepartmentButton.setBounds(7, 730, 150, 30);
		showDepartmentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDepartmentGroup();
			}
		});
		
		panel.add(showDepartmentButton);
	}	

	public static void showDepartmentGroup() {
		String sql = "Select e.dno, d.dname, e.fname, e.minit, e.lname, e.ssn, e.salary from employee e left join department d on d.dnumber = e.dno group by e.dno, d.dname, e.fname, e.minit, e.lname, e.ssn, e.salary order by dno;";
		runSelectSQL(sql, (result) -> {
			try {
				JFrame frame = new JFrame();
				frame.setSize(530,450);
				
				Container container = frame.getContentPane();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JPanel panel = new JPanel();
				panel.setLayout(null);
				
				ArrayList<String> column = new ArrayList<>(Arrays.asList("Name", "Ssn", "Salary"));
				Integer dno = -1;
				Integer currentY = 0;
				DefaultTableModel model = new DefaultTableModel();
				JTable table = new JTable(model);
				
				while (result.next()) {
					Object[] data = new Object[column.size()];
					Object[] pkData = new Object[column.size()];
					Integer tempDno = result.getInt("dno"); 
					if (dno != tempDno) {
						if (dno != -1) {
							table.setModel(model);
						}
						dno = tempDno;
						String dname = result.getString("dname");
						JLabel title = new JLabel(dname);
						title.setBounds(7, currentY, 200, 30);
						panel.add(title);
						
						model = new DefaultTableModel(column.toArray(), 0);
						table = new JTable(model);
						JScrollPane scrollPane = new JScrollPane(table);
						scrollPane.setBounds(7, currentY+30, 400, 100);
						currentY += 150;
						panel.add(scrollPane);
					}
					String fname = result.getString("fname");
					String minit = result.getString("minit");
					String lname = result.getString("lname");
					String name = fname+(minit==null ? "": " "+minit)+" "+lname;
					for(int i = 0; i < column.size(); i++) {
						if (column.get(i) == "Name") {
							data[i] = name;
						} else {
							data[i] = result.getString(column.get(i));				
						}
						pkData[i] = data[i];
					}
					model.addRow(data);
				}
				panel.setPreferredSize(new Dimension(530, currentY));
				panel.setVisible(true);
				table.setModel(model);
				table.setVisible(true);
				JScrollPane spanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				container.add(spanel);
				frame.setVisible(true);
			} catch (SQLException e1) {
				System.out.println("select dname error " + e1.getLocalizedMessage());
			}
			return null;
		});
	}
}

