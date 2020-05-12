import com.sun.glass.ui.Window;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class admin_dashboard
{
    Scene scene;
    Connection con = Database_Connection.getInstance().con;
    Stage stage;
    String dept="",year="",deptname="";

    public void sceneView1(Stage stage,Scene mainscene) throws SQLException {


        Statement statement = con.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS departments (`SrNo` INT NOT NULL AUTO_INCREMENT,`Department_Name` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNO`)); ");
        this.stage = stage;

        VBox rootnode = new VBox(10);

        rootnode.setAlignment(Pos.CENTER);
        //rootnode.setHgap(10);
        //rootnode.setVgap(10);
        rootnode.setPadding(new Insets(25, 25, 25, 25));

        scene = new Scene(rootnode, 500, 500);

        //setting the stage to this previously created scene
        stage.setScene(scene);

        Button view_students = new Button("View Students");
        //view_students.setMaxWidth(50);
        Button view_teachers = new Button("View Teachers");
        Button departments = new Button("Departments");
        Button subjects = new Button("Subjects");
        Button logout = new Button("LOGOUT");

        view_students.setMaxWidth(100);
        view_teachers.setMaxWidth(100);
        departments.setMaxWidth(100);
        subjects.setMaxWidth(100);
        logout.setMaxWidth(100);

        rootnode.getChildren().addAll(view_students,view_teachers,departments,subjects,logout);

        view_students.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try {
                    student(stage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        view_teachers.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {

            }
        });

        departments.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try {
                    Department(stage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

        subjects.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try {
                    Subjects(stage);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });


        logout.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                try
                {
                    con.close();
                    System.out.println("Connection closed");
                    //System.out.println(con.getSchema());
                }
                catch (SQLException throwables)
                {
                    throwables.printStackTrace();
                    System.out.println("Connection not closed");
                }

                try {
                    new Main_page().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //stage.setScene(mainscene);
                //stage.show();
            }
        });
    }

    private void Department(Stage stage) throws SQLException {

        GridPane rootnode = new GridPane();

        rootnode.setAlignment(Pos.TOP_LEFT);
        rootnode.setHgap(10);
        rootnode.setVgap(10);
        rootnode.setPadding(new Insets(25, 25, 25, 25));

        TableView tableView = new TableView();
        //ObservableList<String> list = FXCollections.observableArrayList();
        //tableView.setItems(list);
        //TableView tableView = new TableView<>();

        TableColumn column1 = new TableColumn("Sr.no");
        column1.setResizable(true);
        column1.setCellValueFactory(new PropertyValueFactory<>("srno"));
        //column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn column2 = new TableColumn("Department");
        column2.setResizable(true);
        column2.setCellValueFactory(new PropertyValueFactory<>("dept_name"));
        //column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /*By doing this you ensure that the extra space in table
         column header will be distributed among the columns.
         In this distribution the columns' max and min widths are taken into account of course.
         By default the TableView.UNCONSTRAINED_RESIZE_POLICY is used where the tablecolumns will
         take their preferred width initially.*/


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        VBox vBox = new VBox(10);


        Button add = new Button("Add Department");
        Button edit = new Button("Edit Department");
        Button remove = new Button("Remove Department");
        Button back = new Button("BACK");

        back.setTranslateY(-190);

        vBox.setAlignment(Pos.CENTER);

        add.setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);

        vBox.getChildren().addAll(add,edit,remove);

        //tableView.getItems().add(0, "Doe");
        //tableView.getItems().add(1, "Deer");

        //VBox vbox = new VBox(tableView);
        rootnode.add(tableView,1,0);
        rootnode.add(back,0,0);
        rootnode.add(vBox,2,0);

        // rootnode.add(edit,1,0);
        //rootnode.add(remove,1,0);
        Scene scene_dept = new Scene(rootnode,500,500);

        stage.setScene(scene_dept);

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM departments;");

        while(resultSet.next())
        {
            int srno = resultSet.getInt("SrNo");
            String dept_name = resultSet.getString("department_Name");
            department newdept = new department(srno,dept_name);
            tableView.getItems().add(newdept);

        }

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                Stage add_department = new Stage();

                VBox vBox1 = new VBox(20);

                vBox1.setAlignment(Pos.CENTER);

                TextField dept_name = new TextField();
                dept_name.setMaxWidth(200);
                Button add_dept = new Button("Add");
                add_dept.setMaxWidth(70);
                Label response = new Label();


                vBox1.getChildren().addAll(dept_name,add_dept,response);


                add_dept.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent)
                    {
                        int name_is_blank;

                        if(dept_name.getText().isBlank())
                        {
                            name_is_blank =1;
                            response.setTextFill(Color.RED);
                            response.setText("Enter Department Name!");
                        }
                        else
                        {
                            name_is_blank=0;
                            String new_dept_name = dept_name.getText().trim();
                            String fword="",sword="";

                            if(new_dept_name.indexOf(' ') == -1)
                            {
                                fword=new_dept_name;
                                sword = "Engineering";
                                new_dept_name=new_dept_name+" "+sword;

                            }
                            else
                            {
                                fword = new_dept_name.substring(0,new_dept_name.indexOf(' '));
                                sword = new_dept_name.substring(new_dept_name.indexOf(' ')+1);
                                new_dept_name=fword+" "+sword;
                            }

                            try
                            {
                                Statement stmt = con.createStatement();
                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate("ALTER TABLE departments AUTO_INCREMENT = 1;");
                                Statement stmt3 = con.createStatement();

                                String q="INSERT INTO departments (Department_Name) VALUES " + "('"+new_dept_name+"')"+";";
                                //System.out.println(q);
                                stmt.executeUpdate(q);


                                String dept = fword+"_"+sword;

                                Statement statement1 = con.createStatement();
                                statement1.executeUpdate("CREATE TABLE fe_"+dept+" (`SrNo` INT NOT NULL AUTO_INCREMENT,`Subjects` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNo`));");
                                statement1.executeUpdate("CREATE TABLE se_"+dept+" (`SrNo` INT NOT NULL AUTO_INCREMENT,`Subjects` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNo`));");
                                statement1.executeUpdate("CREATE TABLE te_"+dept+" (`SrNo` INT NOT NULL AUTO_INCREMENT,`Subjects` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNo`));");
                                statement1.executeUpdate("CREATE TABLE be_"+dept+" (`SrNo` INT NOT NULL AUTO_INCREMENT,`Subjects` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNo`));");

                                ResultSet resultSet = stmt3.executeQuery("SELECT * FROM departments");

                                resultSet.last();


                                int SrNo = resultSet.getInt("SrNo");
                                String department_name = resultSet.getString("Department_Name");
                                department newdept = new department(SrNo,department_name);

                                //tableView.getItems().add(SrNo,department_name);

                                //TableView<String> table = new TableView<>();
                                // tableView.getItems().add(department_name);
                                //list.add(department_name);
                                //tableView.getItems().add(list);


                                tableView.getItems().add(newdept);
                            }
                            catch (SQLException throwables)
                            {
                                throwables.printStackTrace();
                            }
                        }
                        if(name_is_blank==0)
                            add_department.close();
                    }
                });

                Scene scene3 = new Scene(vBox1,300,300);

                add_department.setScene(scene3);

                add_department.initModality(Modality.APPLICATION_MODAL);

                add_department.show();




            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                edit.setDisable(true);
                add.setDisable(true);
                remove.setDisable(true);
                if (edit.isDisabled() == true)
                {
                    //tableView.setMouseTransparent(false);
                    tableView.setEditable(true);

                column2.setCellFactory(TextFieldTableCell.forTableColumn());

                column2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent cellEditEvent) {

                        String old_dept = (String) cellEditEvent.getOldValue();
                        String new_dept = (String) cellEditEvent.getNewValue();
                        //System.out.println(old_dept+new_dept);
                        department dept = (department) cellEditEvent.getRowValue();
                        //System.out.println(dept.srno);
                        int sr_no_of_edited_dept = dept.srno;

                        try {
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate("UPDATE departments SET Department_Name =" + "'" + new_dept + "'" + "WHERE SrNo = " + sr_no_of_edited_dept + ";");
                            stmt.executeUpdate("RENAME TABLE fe_"+old_dept+" TO "+"fe_"+new_dept);
                            stmt.executeUpdate("RENAME TABLE se_"+old_dept+" TO "+"fe_"+new_dept);
                            stmt.executeUpdate("RENAME TABLE te_"+old_dept+" TO "+"fe_"+new_dept);
                            stmt.executeUpdate("RENAME TABLE be_"+old_dept+" TO "+"fe_"+new_dept);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                        tableView.setEditable(false);
                        tableView.getSelectionModel().clearSelection();
                        edit.setDisable(false);
                        add.setDisable(false);
                        remove.setDisable(false);


                    }
                });
            }


            }
        });


        remove.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                remove.setDisable(true);
                add.setDisable(true);
                edit.setDisable(true);

                //remove.setDisable(true);
                //tableView.setMouseTransparent(false);
                tableView.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent mouseEvent)
                    {
                        if (mouseEvent.getClickCount()==2 && remove.isDisabled()==true)
                        {
                            Stage prompt = new Stage();
                            //GridPane promptgrid = new GridPane();
                            //promptgrid.setVgap(10);
                            //promptgrid.setHgap(10);
                            //promptgrid.setAlignment(Pos.CENTER);

                            VBox promptbox = new VBox(10);
                            promptbox.setAlignment(Pos.CENTER);
                            HBox hBox = new HBox(10);
                            hBox.setAlignment(Pos.CENTER);


                            prompt.initModality(Modality.APPLICATION_MODAL);

                            Label displaymsg = new Label();
                            displaymsg.setWrapText(true);
                            displaymsg.setTextAlignment(TextAlignment.CENTER);
                            displaymsg.setText("Are you sure you want to delete this selected Department?");
                            Button yes = new Button("Yes");
                            Button no = new Button("No");


                            hBox.getChildren().addAll(yes, no);
                            promptbox.getChildren().addAll(displaymsg, hBox);

                            Scene promptscene = new Scene(promptbox, 200, 200);

                            prompt.setScene(promptscene);
                            prompt.show();

                            yes.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    department remov_dept = (department) tableView.getSelectionModel().getSelectedItem();
                                    int remove_dept_srno = remov_dept.srno;
                                    String removed_dept = remov_dept.dept_name;
                                    String fword = removed_dept.substring(0,removed_dept.indexOf(' '));
                                    String sword = removed_dept.substring(removed_dept.indexOf(' ')+1);
                                    String dept = fword+"_"+sword;

                                    //Statement remove_department = con.createStatement();
                                    try {
                                        Statement remove_department = con.createStatement();
                                        String removedeptquery = "DELETE FROM departments WHERE SrNo = " + remove_dept_srno + ";";
                                        remove_department.executeUpdate(removedeptquery);
                                        remove_department.executeUpdate("DROP TABLE fe_"+dept+";");
                                        remove_department.executeUpdate("DROP TABLE se_"+dept+";");
                                        remove_department.executeUpdate("DROP TABLE te_"+dept+";");
                                        remove_department.executeUpdate("DROP TABLE be_"+dept+";");
                                        tableView.getItems().clear();

                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }

                                    try {
                                        Statement get_fromdb = con.createStatement();
                                        ResultSet updated_table = get_fromdb.executeQuery("SELECT * FROM departments");
                                        Statement set_proper_order = con.createStatement();

                                        set_proper_order.executeUpdate("ALTER TABLE departments AUTO_INCREMENT = 1;");

                                        while (updated_table.next()) {
                                            int srno = updated_table.getInt("SrNo");
                                            String dept_name = updated_table.getString("department_Name");
                                            department newdept = new department(srno, dept_name);
                                            tableView.getItems().add(newdept);

                                        }
                                        //tableView.setMouseTransparent(true);
                                        remove.setDisable(false);
                                        add.setDisable(false);
                                        edit.setDisable(false);


                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }


                                    prompt.close();

                                }
                            });
                            no.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    prompt.close();
                                    //tableView.setMouseTransparent(true);
                                    //remove.setDisable(true);
                                    //tableView.getSelectionModel().clearSelection();
                                    remove.setDisable(false);
                                    add.setDisable(false);
                                    edit.setDisable(false);

                                }
                            });


                        }
                    }
                });


            }
        });

        back.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                //back.getScene().setRoot(rootnodeprev);

                //stage.setScene(scene);
                //stage.show();
                stage.setScene(scene);
                stage.show();
            }

        });

    }

    public void student(Stage stage) throws SQLException
    {

        GridPane root = new GridPane();

        root.setHgap(10);
        root.setVgap(10);

        ComboBox dept_combobox = new ComboBox();
        ComboBox year_combobox = new ComboBox();
        ComboBox division_combobox = new ComboBox();

        ObservableList years = FXCollections.observableArrayList("FE","SE","TE","BE");
        ObservableList division = FXCollections.observableArrayList("A","B");

        ObservableList depts = FXCollections.observableArrayList();

        Statement statement = con.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT Department_Name FROM departments");

        while(resultSet.next())
        {
            String dept_name = resultSet.getString("Department_Name");
            depts.add(dept_name);
        }

        dept_combobox.setItems(depts);
        division_combobox.setItems(division);
        year_combobox.setItems(years);

        Button add = new Button("ADD");
        Button info = new Button("STUDENT INFO");
        Button remove = new Button("REMOVE");
        Button back = new Button("BACK");
        add.setMaxWidth(Double.MAX_VALUE);
        info.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);

        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(add,info,remove);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(dept_combobox,year_combobox,division_combobox);


        TableView stud_detail = new TableView();

        root.add(back,2,2);
       /* root.add(dept_combobox,1,0);
        root.add(year_combobox,2,0);
        root.add(division_combobox,3,0);*/
        root.add(hBox,3,2);
        root.add(stud_detail,3,3);
        root.add(vBox,4,3);

        TableColumn UID = new TableColumn("UID");
        TableColumn first_name = new TableColumn("FIRST NAME");
        TableColumn middle_name = new TableColumn("MIDDLE NAME");
        TableColumn last_name = new TableColumn("LAST NAME");
        TableColumn dob = new TableColumn("DOB");
        TableColumn mob = new TableColumn("MOBILE NO.");
        TableColumn email_id = new TableColumn("EMAIL-ID");

        UID.setPrefWidth(100);
        first_name.setPrefWidth(200);
        middle_name.setPrefWidth(200);
        last_name.setPrefWidth(200);
        dob.setPrefWidth(100);
        mob.setPrefWidth(100);
        email_id.setPrefWidth(200);

        stud_detail.getColumns().addAll(UID,first_name,middle_name,last_name,dob,mob,email_id);




        Scene students = new Scene(root,600,600);

        stage.setScene(students);
        stage.show();
        stage.setMaximized(true);

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                //Scene add_student = new Scene();
                stage.setMaximized(false);
                GridPane root = new GridPane();
                root.setHgap(10);
                root.setVgap(10);

                Button back = new Button("BACK");



                VBox vBox = new VBox(20);

                Label first_name = new Label("First Name");
                TextField first_name_text = new TextField();

                first_name_text.setPrefWidth(200);

                HBox fname = new HBox(75);

                fname.getChildren().addAll(first_name,first_name_text);

                Label middle_name = new Label("Middle Name");
                TextField middle_name_text = new TextField();

                middle_name_text.setPrefWidth(200);

                HBox mname = new HBox(60);

                mname.getChildren().addAll(middle_name,middle_name_text);

                Label last_name = new Label("Last Name");
                TextField last_name_text = new TextField();

                last_name_text.setPrefWidth(200);

                HBox lname = new HBox(76);

                lname.getChildren().addAll(last_name,last_name_text);

                Label  date_of_birth= new Label("Date Of Birth");
                DatePicker datePicker = new DatePicker();


                HBox dob = new HBox(62);

                dob.getChildren().addAll(date_of_birth,datePicker);

                Label mobile_no = new Label("Mobile no.");
                TextField mob_no_text = new TextField();

                mob_no_text.setPrefWidth(200);

                HBox mob_no = new HBox(75);

                mob_no.getChildren().addAll(mobile_no,mob_no_text);

                Label alt_mob_no = new Label("Alternative Mobile No.");
                TextField alt_mob_no_text = new TextField();

                alt_mob_no_text.setPrefWidth(200);

                HBox alt_mob = new HBox(10);

                alt_mob.getChildren().addAll(alt_mob_no,alt_mob_no_text);

                Label email = new Label("Email");
                TextField email_text = new TextField();


                email_text.setPrefWidth(200);

                HBox email_id = new HBox(100);

                email_id.getChildren().addAll(email,email_text);

                vBox.getChildren().addAll(fname,mname,lname,dob,mob_no,alt_mob,email_id);


                ComboBox dept = new ComboBox();
                ObservableList deptnames = FXCollections.observableArrayList();

                try {
                    Statement stmt = con.createStatement();
                    ResultSet resultSet1 = stmt.executeQuery("SELECT Department_Name FROM departments");
                    while (resultSet1.next())
                    {
                        String dept_name = resultSet1.getString("Department_Name");
                        deptnames.add(dept_name);

                    }

                    dept.setItems(deptnames);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Label department = new Label("Department");

                HBox deptfield = new HBox(30);

                deptfield.getChildren().addAll(department,dept);

                vBox.getChildren().add(deptfield);


                root.add(vBox,1,1);
                root.add(back,0,0);

                Scene add_student = new Scene(root,600,600);

                stage.setScene(add_student);

                stage.setMaximized(true);

                stage.show();



                //stage.setMaximized(true);


            }
        });

    }

    public void Subjects(Stage stage) throws SQLException
    {
        //Stage subj_stage = new Stage();
        GridPane rootnode = new GridPane();
        TableView sub = new TableView();
        TableColumn subj_name = new TableColumn("Subject Name");
        subj_name.setCellValueFactory(new PropertyValueFactory<>("subject_name"));
        TableColumn sr_no = new TableColumn("SrNo");
        sr_no.setCellValueFactory(new PropertyValueFactory<>("SrNo"));
        sub.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sub.getColumns().add(sr_no);
        sub.getColumns().add(subj_name);
        subj_name.setResizable(true);


        rootnode.setHgap(10);
        rootnode.setVgap(10);

        HBox drop_down_container = new HBox(10);

        VBox options = new VBox(10);
        options.setAlignment(Pos.CENTER);

        Button add = new Button("Add Subject");
        Button edit = new Button("Edit Subject");
        Button remove = new Button("Remove Subject");
        Button back = new Button("BACK");

        edit.setDisable(true);
        remove.setDisable(true);

        add.setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);

        options.getChildren().addAll(add,edit,remove);

        ObservableList<String> options1 = FXCollections.observableArrayList();

        Statement show_dept = con.createStatement();

        ResultSet resultSet=show_dept.executeQuery("SELECT Department_Name FROM departments");

        while (resultSet.next())
        {
            String dept_name = resultSet.getString("Department_Name");
            options1.add(dept_name);
        }

         ComboBox comboBox1 = new ComboBox(options1);
        comboBox1.setPromptText("Select Department");
        comboBox1.setItems(options1);

        ObservableList<String> options2 = FXCollections.observableArrayList(
                "FE",
                "SE",
                "TE","BE"
        );
         ComboBox comboBox2 = new ComboBox(options2);
       comboBox2.setPromptText("Select Year");
        comboBox2.setItems(options2);





        drop_down_container.getChildren().addAll(comboBox1,comboBox2);


        rootnode.add(drop_down_container,3,3);
        rootnode.add(sub,3,4);
        rootnode.add(options,4,4);
        rootnode.add(back,2,3);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(scene);
                stage.show();
            }
        });

        Scene subjects = new Scene(rootnode,500,500);

        stage.setScene(subjects);
        stage.show();


        //if(comboBox1.getValue()!=null)

        comboBox1.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                sub.getItems().clear();


                dept = comboBox1.getSelectionModel().getSelectedItem().toString();

                if(!(comboBox2.getSelectionModel().isEmpty()))
                    year = comboBox2.getSelectionModel().getSelectedItem().toString();

                String firstword = dept.substring(0,dept.indexOf(' '));
                String secondword = dept.substring(dept.indexOf(' ')+1,dept.length());

                deptname = firstword+"_"+secondword;

            }
        });

        comboBox2.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                sub.getItems().clear();
                edit.setDisable(false);
                remove.setDisable(false);
                year = comboBox2.getSelectionModel().getSelectedItem().toString();
                dept = comboBox1.getSelectionModel().getSelectedItem().toString();

                retrieve_subjects(sub);





            }
        });




        add.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                Stage addsub = new Stage();

                GridPane root = new GridPane();
                root.setVgap(10);
                root.setVgap(10);
                root.setAlignment(Pos.CENTER);

                VBox vBox = new VBox(15);
                vBox.setAlignment(Pos.CENTER);
                TextField subjectname = new TextField();
                subjectname.setAlignment(Pos.CENTER);
                subjectname.setPrefWidth(50);
                vBox.getChildren().add(subjectname);

                ComboBox select_department = new ComboBox();
                select_department.setPromptText("Select Department");
                ObservableList departments = FXCollections.observableArrayList();

                try {
                    Statement depts = con.createStatement();
                    ResultSet getdepts =show_dept.executeQuery("SELECT Department_Name FROM departments");
                    while (getdepts.next())
                    {
                        String dept_name = getdepts.getString("Department_Name");
                        departments.add(dept_name);
                    }

                } catch (SQLException throwables)
                {
                    throwables.printStackTrace();
                }


                select_department.setItems(departments);

                ComboBox year_select = new ComboBox();
                year_select.setPromptText("Select Year");

                ObservableList years = FXCollections.observableArrayList("FE","SE","TE","BE");

                year_select.setItems(years);


                Button add = new Button("Add");
                Label alert_msg = new Label("");          // Message for subject not entered
                vBox.getChildren().addAll(select_department,year_select,add,alert_msg);
                root.add(vBox,0,0);

                Scene addsubscene = new Scene(root,400,400);

                addsub.initModality(Modality.APPLICATION_MODAL);

                addsub.setScene(addsubscene);
                addsub.show();



                add.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        if(subjectname.getText().trim().isEmpty() && select_department.getSelectionModel().isEmpty()
                        && year_select.getSelectionModel().isEmpty())
                        {
                            alert_msg.setText("Enter all required fields");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else if(subjectname.getText().trim().isEmpty())
                        {
                            alert_msg.setText("Subject Name not mentioned!!");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else if(select_department.getSelectionModel().isEmpty())
                        {
                            alert_msg.setText("Department not selected!!");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else if(year_select.getSelectionModel().isEmpty())
                        {
                            alert_msg.setText("Year not selected!!");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else
                            {

                            String department = select_department.getSelectionModel().getSelectedItem().toString();
                            String year = year_select.getSelectionModel().getSelectedItem().toString();
                            String subject = subjectname.getText();
                            int spaceindex = department.indexOf(' ');
                            String firstword = department.substring(0,spaceindex);
                            String secondword = department.substring(spaceindex+1,department.length());
                            String dept = firstword+"_"+secondword;
                            String tablename = year+"_"+dept;

                            try {
                                Statement sub_table = con.createStatement();
                                sub_table.executeUpdate("CREATE TABLE IF NOT EXISTS "+tablename+"(`SrNo` INT NOT NULL AUTO_INCREMENT,`Subjects` VARCHAR(100) NOT NULL,PRIMARY KEY(`SrNo`));");
                                sub_table.executeUpdate("INSERT INTO "+tablename+ " (Subjects) "+ "VALUES"+"('"+subject+"');");

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }


                            //System.out.println(department);
                            //System.out.println(year);
                            //System.out.println(subject);

                            addsub.close();
                            sub.getItems().clear();
                            if(!(comboBox1.getSelectionModel().isEmpty()) && !(comboBox2.getSelectionModel().isEmpty()))
                            {
                                if (department.contentEquals(comboBox1.getSelectionModel().getSelectedItem().toString())
                                        && year.contentEquals(comboBox2.getSelectionModel().getSelectedItem().toString()))
                                {
                                    retrieve_subjects(sub);
                                }
                            }
                        }
                    }
                });
            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                edit.setDisable(true);
                add.setDisable(true);
                remove.setDisable(true);

                if(edit.isDisabled()==true)
                {
                    sub.setEditable(true);
                    subj_name.setCellFactory(TextFieldTableCell.forTableColumn());

                    subj_name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>()
                    {
                        @Override
                        public void handle(TableColumn.CellEditEvent cellEditEvent)
                        {
                            String old_subject_name = (String) cellEditEvent.getOldValue();
                            String new_subject_name = (String) cellEditEvent.getNewValue();

                            subject edited_subj= (subject) cellEditEvent.getRowValue();
                            int srno = edited_subj.SrNo;
                            String selected_dept = comboBox1.getSelectionModel().getSelectedItem().toString().toLowerCase();
                            String selected_year = comboBox2.getSelectionModel().getSelectedItem().toString().toLowerCase();
                            String fword = selected_dept.substring(0,selected_dept.indexOf(' '));
                            String sword = selected_dept.substring(selected_dept.indexOf(' ')+1);
                            String deptname = fword+"_"+sword;
                            String tablename = selected_year+"_"+deptname;

                            Statement edit_suject = null;
                            try {
                                edit_suject = con.createStatement();
                                edit_suject.executeUpdate("UPDATE "+tablename+" SET Subjects ='"+new_subject_name+"' WHERE SrNo="+srno+";");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            sub.setEditable(false);
                            sub.getSelectionModel().clearSelection();
                            add.setDisable(false);
                            edit.setDisable(false);
                            remove.setDisable(false);



                        }
                    });
                }




            }
        });

        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                add.setDisable(true);
                remove.setDisable(true);
                edit.setDisable(true);

                sub.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent)
                    {
                        String selected_dept = comboBox1.getSelectionModel().getSelectedItem().toString().toLowerCase();
                        String selected_year = comboBox2.getSelectionModel().getSelectedItem().toString().toLowerCase();
                        String fword = selected_dept.substring(0,selected_dept.indexOf(' '));
                        String sword = selected_dept.substring(selected_dept.indexOf(' ')+1);
                        String deptname = fword+"_"+sword;
                        String tablename = selected_year+"_"+deptname;

                        if(mouseEvent.getClickCount() == 2 && remove.isDisabled() == true)
                        {
                            Stage deleteprompt = new Stage();

                            VBox promptbox = new VBox(10);
                            promptbox.setAlignment(Pos.CENTER);
                            HBox hBox = new HBox(10);
                            hBox.setAlignment(Pos.CENTER);


                            deleteprompt.initModality(Modality.APPLICATION_MODAL);

                            Label displaymsg = new Label();
                            displaymsg.setWrapText(true);
                            displaymsg.setTextAlignment(TextAlignment.CENTER);
                            displaymsg.setText("Are you sure you want to delete this selected Subject ?");
                            Button yes = new Button("Yes");
                            Button no = new Button("No");


                            hBox.getChildren().addAll(yes, no);
                            promptbox.getChildren().addAll(displaymsg, hBox);

                            Scene promptscene = new Scene(promptbox, 200, 200);

                            deleteprompt.setScene(promptscene);
                            deleteprompt.show();

                            yes.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent)
                                {
                                    subject remove_subject = (subject) sub.getSelectionModel().getSelectedItem();
                                    int srno = remove_subject.SrNo;

                                    try {
                                        Statement statement = con.createStatement();

                                        statement.executeUpdate("DELETE FROM "+tablename+" WHERE SrNo = "+ srno+";");

                                        ResultSet resultSet1 = statement.executeQuery("SELECT * FROM "+tablename+";");

                                        sub.getItems().clear();

                                        while(resultSet1.next())
                                        {
                                            int SrNo = resultSet1.getInt("SrNo");
                                            String subject_name = resultSet1.getString("Subjects");

                                            subject s = new subject(SrNo, subject_name);

                                            sub.getItems().add(s);
                                        }

                                    } catch (SQLException throwables)
                                    {
                                        throwables.printStackTrace();
                                    }
                                    add.setDisable(false);
                                    edit.setDisable(false);
                                    remove.setDisable(false);
                                    deleteprompt.close();
                                }
                            });
                            no.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent)
                                {
                                    add.setDisable(false);
                                    edit.setDisable(false);
                                    remove.setDisable(false);
                                    deleteprompt.close();


                                }
                            });




                        }
                    }
                });
            }
        });
    }

    public void retrieve_subjects(TableView sub)
    {
        if (dept != "" && year != "")
        {
            //System.out.println("inside if");
            String tablename = year + "_" + deptname;

            Statement showtable = null;
            try {

                showtable = con.createStatement();
                ResultSet resultSet1 = showtable.executeQuery("SELECT * FROM " + tablename + ";");

                while (resultSet1.next())
                {
                    int SrNo = resultSet1.getInt("SrNo");
                    String subject_name = resultSet1.getString("Subjects");

                    subject s = new subject(SrNo, subject_name);

                    sub.getItems().add(s);

                }
            } catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }


        }
    }
}

/*

    1.View Students -> 1.add 2.remove 3.edit
    2.View Teachers -> 1.add 2.remove 3.edit
    3.Departments -> 1.add 2.remove 3.edit     (Subjects will be mapped by admin to the departments)
    4.Subjects -> 1.add 2.remove 3.edit        (these subjects here will be listed while adding new department)
    5.Division -> 1.add 2.remove 3.edit




 */