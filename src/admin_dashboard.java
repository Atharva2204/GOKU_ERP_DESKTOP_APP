import javafx.application.Application;
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

    public void sceneView1(Stage stage,Scene mainscene)
    {
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
        Button division = new Button("Division");
        Button logout = new Button("LOGOUT");

        view_students.setMaxWidth(100);
        view_teachers.setMaxWidth(100);
        departments.setMaxWidth(100);
        subjects.setMaxWidth(100);
        division.setMaxWidth(100);
        logout.setMaxWidth(100);

        rootnode.getChildren().addAll(view_students,view_teachers,departments,subjects,division,logout);

        view_students.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {


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

        division.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {

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
                        int name_is_blank =0;

                        if(dept_name.getText().isBlank())
                        {
                            name_is_blank =1;
                            response.setTextFill(Color.RED);
                            response.setText("Enter Department Name!");
                        }
                        else
                        {
                            name_is_blank=0;
                            String new_dept_name = dept_name.getText();
                            try {
                                Statement stmt = con.createStatement();
                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate("ALTER TABLE departments AUTO_INCREMENT = 1;");
                                Statement stmt3 = con.createStatement();

                                String q="INSERT INTO departments (Department_Name) VALUES " + "('"+dept_name.getText()+"')"+";";
                                //System.out.println(q);
                                stmt.executeUpdate(q);

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

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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

                                    //Statement remove_department = con.createStatement();
                                    try {
                                        Statement remove_department = con.createStatement();
                                        String removedeptquery = "DELETE FROM departments WHERE SrNo = " + remove_dept_srno + ";";
                                        remove_department.executeUpdate(removedeptquery);
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

    public void Subjects(Stage stage) throws SQLException
    {
        //Stage subj_stage = new Stage();
        GridPane rootnode = new GridPane();
        TableView sub = new TableView();
        TableColumn subj_name = new TableColumn("Subject Name");
        sub.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sub.getColumns().add(subj_name);

        rootnode.setHgap(10);
        rootnode.setVgap(10);

        HBox drop_down_container = new HBox(10);

        VBox options = new VBox(10);
        options.setAlignment(Pos.CENTER);

        Button add = new Button("Add Subject");
        Button edit = new Button("Edit Subject");
        Button remove = new Button("Remove Subject");
        Button back = new Button("BACK");

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

        final ComboBox comboBox1 = new ComboBox(options1);
        comboBox1.setPromptText("Select Department");
        comboBox1.setItems(options1);

        ObservableList<String> options2 = FXCollections.observableArrayList(
                "FE",
                "SE",
                "TE","BE"
        );
        final ComboBox comboBox2 = new ComboBox(options2);
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

        add.setOnAction(new EventHandler<ActionEvent>() {
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

                } catch (SQLException throwables) {
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
                            alert_msg.setText("Subject taak na yedya");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else if(select_department.getSelectionModel().isEmpty())
                        {
                            alert_msg.setText("Department taak na yedya");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else if(year_select.getSelectionModel().isEmpty())
                        {
                            alert_msg.setText("Year taak na yedya");
                            alert_msg.setTextFill(Color.RED);
                        }
                        else {

                            String department = select_department.getSelectionModel().getSelectedItem().toString();
                            String year = year_select.getSelectionModel().getSelectedItem().toString();
                            String subject = subjectname.getText();

                            System.out.println(department);
                            System.out.println(year);
                            System.out.println(subject);

                            addsub.close();
                        }
                    }
                });









            }
        });




    }
}

/*

    1.View Students -> 1.add 2.remove 3.edit
    2.View Teachers -> 1.add 2.remove 3.edit
    3.Departments -> 1.add 2.remove 3.edit     (Subjects will be mapped by admin to the departments)
    4.Subjects -> 1.add 2.remove 3.edit        (these subjects here will be listed while adding new department)
    5.Division -> 1.add 2.remove 3.edit




 */