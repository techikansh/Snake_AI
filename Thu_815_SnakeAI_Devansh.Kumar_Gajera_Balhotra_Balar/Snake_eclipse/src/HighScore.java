import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class HighScore extends JFrame {
    public HighScore() {
        initComponents();
    }

    private void initComponents() {

        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();

        //======== this ========
        setPreferredSize(new Dimension(600, 600));
//        var contentPane = getContentPane();
        this.setLayout(new FlowLayout());

        //======== panel1 ========
        {
            panel1.setPreferredSize(new Dimension(600, 600));
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
                    ( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border
                    . TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt
                    . Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
        propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( )
                ; }} );
            panel1.setLayout(new FlowLayout());

            //======== scrollPane1 ========
            {
                scrollPane1.setPreferredSize(new Dimension(600 , 600));
                scrollPane1.setViewportView(table1);
            }
            panel1.add(scrollPane1);
        }
        this.add(panel1);
        pack();
        setLocationRelativeTo(getOwner());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

        try{
//        	String mysql_pass = "devanshkumar";                                        //DB password
          String mysql_pass = SnakeGame.password;  

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_snake_ai", "root", mysql_pass);
            Statement st = con.createStatement();
            String query = "select * from scoreboard order by score desc";
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();


            DefaultTableModel model = (DefaultTableModel)table1.getModel();
            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];
            for(int i = 0; i < cols; i++){
                colName[i] = rsmd.getColumnName(i+1);
            }
            model.setColumnIdentifiers(colName);

            String _name;
            String _score;
            while(rs.next()){
                _name = rs.getString(1);
                _score = rs.getString(2).toString();
                String[] row = {_name, _score};
                model.addRow(row);
            }



        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }


    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Devansh
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTable table1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
