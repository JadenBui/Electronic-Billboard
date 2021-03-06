package GUI;

import ElectronicBillboardObject.Billboard;
import ElectronicBillboardObject.User;
import Request.*;
import Server.SessionToken;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Control panel.
 */
public class ControlPanel extends JFrame implements ActionListener, Runnable {
    /**
     * The constant WIDTH.
     */
    public static final int WIDTH = 1000;
    /**
     * The constant HEIGHT.
     */
    public static final int HEIGHT = 700;
    private static Socket socket;
    /**
     * The Output.
     */
    static ObjectOutputStream output;
    /**
     * The Input.
     */
    static ObjectInputStream input;
    private static SessionToken token;
    private static Object[][] billBoardData;
    private static Object[][] userData = null;
    private static Object[][] scheduleData = null;
    private JPanel pnlMenu;
    private JPanel pnlCenter;
    private JLabel lblName;
    private JLabel lblUserPassword;
    private JTextField tfBillboardName;
    private JTextField tfBillboardUsername;
    private JTextField tfBillboardBGColor;
    private JTextField tfBillboardTitleColor;
    private JTextField tfBillboardDescriptionColor;
    private JTextField tfBillboardURL;
    private JTextField tfBillboardTitle;
    private JTextField tfBillboardDescription;
    private JTextField tfNewBillboardName;
    private JTextField tfNewBillboardUsername;
    private JTextField tfNewBillboardBGColor;
    private JTextField tfNewBillboardTitleColor;
    private JTextField tfNewBillboardDescriptionColor;
    private JTextField tfNewBillboardURL;
    private JTextField tfNewBillboardTitle;
    private JTextField tfNewBillboardDescription;
    private JTextField tfUserName;
    private JTextField tfUserPassword;
    private JTextField tfUserPrivilege;
    private JTextField tfScheduleBillboardName;
    private JTextField tfScheduleStart;
    private JTextField tfScheduleDuration;
    private JTextField tfScheduleDayOfWeek;
    private JPanel pnlBillboard;
    private JPanel pnlBillboardInformation;
    private JPanel pnlBillboardButton;
    private JPanel pnlUserManagement;
    private JPanel pnlDisplayUserManagement;
    private JPanel pnlUserManagementInfo;
    private JPanel pnlUserManagementControl;
    private JPanel pnlSchedule;
    private JPanel pnlNewBillBoard;
    private JPanel pnlBillBoardList;
    private JPanel pnlBillBoardScheduleList;
    private JPanel pnlBillBoardScheduleInformation;
    private JPanel pnlBillBoardScheduleControl;
    private JTable billboardTable;
    private JTable userTable;
    private JTable scheduleTable;
    private DefaultTableModel modelTableBillboard;
    private DefaultTableModel modelTableUser;
    private DefaultTableModel modelTableSchedule;
    private JButton btnBillboard;
    private JButton btnSchedule;
    private JButton btnNewBillBoard;
    private JButton btnUserManagement;
    private JButton btnDeleteBb;
    private JButton btnEditBb;
    private JButton btnExportBb;
    private JButton btnCreateNewBb;
    private JButton btnUserDelete;
    private JButton btnUserEdit;
    private JButton btnUserCreate;
    private JButton btnDeleteSchedule;
    private JButton btnCreateSchedule;
    private JButton btnLogOut;


    /**
     * Instantiates a new Control panel.
     *
     * @param title the title
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public ControlPanel(String title) throws SQLException, IOException {
        super(title);
        socket = new Socket("localhost", 1234);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

    }

    private void createGUI() {
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initializeTextFields();
        initializeButtons();
        initializeLabel();
        initializePanel();

//        btnDeleteBb.setPreferredSize(new Dimension(100,40));
//        btnDeleteBb.setBorderPainted(true);
//        btnDeleteBb.setFont(new Font("Serif", Font.PLAIN, 15));
//        btnDeleteBb.setContentAreaFilled(false);
//        btnDeleteBb.setFocusPainted(false);
//        btnDeleteBb.setVerticalTextPosition(SwingConstants.BOTTOM);
//        btnDeleteBb.setHorizontalTextPosition(SwingConstants.CENTER);

//        btnCreateNewBb.setPreferredSize(new Dimension(100,40));
//        btnCreateNewBb.setBorderPainted(true);
//        btnCreateNewBb.setFont(new Font("Serif", Font.PLAIN, 15));
//        btnCreateNewBb.setContentAreaFilled(false);
//        btnCreateNewBb.setFocusPainted(false);
//        btnCreateNewBb.setVerticalTextPosition(SwingConstants.BOTTOM);
//        btnCreateNewBb.setHorizontalTextPosition(SwingConstants.CENTER);
/**
 *
 * A convenience method to add a component to given grid bag
 * layout locations. Code due to Cay Horstmann
 *
 * @param c the component to add
 * @param constraints the grid bag constraints to use
 * @param x the x grid position
 * @param y the y grid position
 * @param w the grid width of the component
 * @param h the grid height of the component
 */
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;
        constraints.weighty = 100;

        GridBagConstraints constrainNewBillboard = new GridBagConstraints();
        constrainNewBillboard.fill = GridBagConstraints.NONE;
        constrainNewBillboard.anchor = GridBagConstraints.LINE_START;
        constrainNewBillboard.weightx = 10;
        constrainNewBillboard.weighty = 10;


        String[] billBoardCols = new String[] {"Name","User","BackgroundColor","TitleColor","DesColor","URL","Title","Description"};

        addToPanelWithLabelArray(billBoardCols,pnlBillboardInformation,constraints);

        JTextField[] billboardTextFields = new JTextField[] {tfBillboardName, tfBillboardUsername,
                tfBillboardBGColor,tfBillboardTitleColor,tfBillboardDescriptionColor,tfBillboardURL,tfBillboardTitle,tfBillboardDescription};

        addToPanelWithComponentArray(billboardTextFields,pnlBillboardInformation,constraints);

        JButton[] buttons = new JButton[] {btnDeleteBb, btnEditBb,btnExportBb};
        addToPanelWithComponentArray(buttons,pnlBillboardButton,constraints);

        //CREATE BILLBOARD TABLE
        billboardTable = createTable(billBoardCols,billBoardData);
        //SET UP THE SCROLL PANE
        modelTableBillboard = new DefaultTableModel(billBoardData,billBoardCols);
        billboardTable.setModel(modelTableBillboard);
        //CENTER THE CELL'S DATA
        centerRowData(billboardTable);
        JScrollPane billBoardListScrollPane = new JScrollPane(billboardTable);
        billBoardListScrollPane.setPreferredSize(new Dimension(500,200));
        ListSelectionModel modelTableSelection = billboardTable.getSelectionModel();
        //SET UP SELECTION MODEL
        modelTableSelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modelTableSelection.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    setValueBillboardInfo(billboardTable);
                }
            }
        });

        addToPanel(pnlBillBoardList, billBoardListScrollPane, constraints,0,0,1,1 );

        //INITIALIZE BILLBOARD PANEL
        initializeBillboardPanel(constraints);

        //INITIALIZE NEW BILLBOARD PANEL
        initializeNewBillboardPanel(constrainNewBillboard);

        //CREATE USER TABLE
        String[] userTableCols = new String[] {"Username","Privilege"};
        userTable = createTable(userTableCols,userData);
        JScrollPane userPane = new JScrollPane(userTable);
        userPane.setPreferredSize(new Dimension(500,200));
        modelTableUser = new DefaultTableModel(userData,userTableCols);
        userTable.setModel(modelTableUser);
        centerRowData(userTable);
        ListSelectionModel modelTableUser = userTable.getSelectionModel();
        modelTableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modelTableUser.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    setValueUserInfo(userTable);
                }
            }
        });


        JButton[] userButtons = new JButton[] {btnUserCreate,btnUserEdit,btnUserDelete};
        addToPanelWithComponentArray(userButtons,pnlUserManagementControl,constraints);


        JLabel[] userLabels = new JLabel[] {createLabel(Color.BLACK,"Username")
                ,createLabel(Color.BLACK,"Privilege"),lblUserPassword};
        addToPanelWithComponentArray(userLabels,pnlUserManagementInfo,constraints);

        JTextField[] userTextFields = new JTextField[]{tfUserName,tfUserPrivilege,tfUserPassword};
        addToPanelWithComponentArray(userTextFields,pnlUserManagementInfo,constraints);
        addToPanel(pnlDisplayUserManagement,userPane,constraints,1,0,1,1);

        //INITIALIZE USER PANEL
        initializeUserPanel(constraints);

        //CREATE SCHEDULE TABLE
        String[] scheduleTableCols = new String[]{"Billboard Name","Start","Duration","Day Of Week"};
        scheduleTable = createTable(scheduleTableCols,scheduleData);
        modelTableSchedule = new DefaultTableModel(scheduleData,scheduleTableCols);
        scheduleTable.setModel(modelTableSchedule);
        JScrollPane schedulePane = new JScrollPane(scheduleTable);
        schedulePane.setPreferredSize(new Dimension(500,200));
        centerRowData(scheduleTable);
        ListSelectionModel modelTableSchedule = scheduleTable.getSelectionModel();
        modelTableSchedule.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    setValueScheduleInfo(scheduleTable);
                }
            }
        });

        String[] scheduleLabels = new String[] {"Billboard Name","Start","Duration","DayOfWeek"};
        addToPanelWithLabelArray(scheduleLabels,pnlBillBoardScheduleInformation,constraints);
        JTextField[] schedulesTextFields = new JTextField[]{tfScheduleBillboardName,tfScheduleStart
        ,tfScheduleDuration,tfScheduleDayOfWeek};
        addToPanelWithComponentArray(schedulesTextFields,pnlBillBoardScheduleInformation,constraints);

        JButton[] scheduleButtons = new JButton[]{btnCreateSchedule,btnDeleteSchedule};
        addToPanelWithComponentArray(scheduleButtons,pnlBillBoardScheduleControl,constraints);

        addToPanel(pnlBillBoardScheduleList, schedulePane, constraints, 0,0,1,1);

        //INITIALIZE SCHEDULE PANEL
        initializeSchedulePanel(constraints);

        //INITIALIZE MENU PANEL
        initializeMenuPanel(constraints);

        //INITIALIZE CENTRE PANEL
        initializeCenterPanel(constraints);


        this.getContentPane().add(lblName,BorderLayout.NORTH);
        this.getContentPane().add(btnLogOut,BorderLayout.AFTER_LAST_LINE);
        this.getContentPane().add(pnlMenu,BorderLayout.WEST);
        this.getContentPane().add(pnlCenter,BorderLayout.CENTER);

    }

    private void initializeTextFields(){
        tfBillboardName = createTextField();
        tfBillboardUsername = createTextField();
        tfBillboardBGColor = createTextField();
        tfBillboardTitleColor = createTextField();
        tfBillboardDescriptionColor = createTextField();
        tfBillboardURL = createTextField();
        tfBillboardTitle= createTextField();
        tfBillboardDescription = createTextField();
        tfNewBillboardName = new JTextField();
        tfNewBillboardUsername = new JTextField();
        tfNewBillboardBGColor = new JTextField();
        tfNewBillboardTitleColor = new JTextField();
        tfNewBillboardDescriptionColor = new JTextField();
        tfNewBillboardURL = new JTextField();
        tfNewBillboardTitle = new JTextField();
        tfNewBillboardDescription = new JTextField();
        tfUserName = createTextField();
        tfUserPassword = createTextField();
        tfUserPassword.setVisible(false);
        tfUserPrivilege = createTextField();
        tfScheduleBillboardName = createTextField();
        tfScheduleStart = createTextField();
        tfScheduleDuration = createTextField();
        tfScheduleDayOfWeek = createTextField();
        tfNewBillboardName.setPreferredSize(new Dimension(200,20));
        tfNewBillboardUsername.setPreferredSize(new Dimension(200,20));
        tfNewBillboardBGColor.setPreferredSize(new Dimension(200,20));
        tfNewBillboardTitleColor.setPreferredSize(new Dimension(200,20));
        tfNewBillboardDescriptionColor.setPreferredSize(new Dimension(200,20));
        tfNewBillboardURL.setPreferredSize(new Dimension(200,20));
        tfNewBillboardTitle.setPreferredSize(new Dimension(200,20));
        tfNewBillboardDescription.setPreferredSize(new Dimension(200,20));
    }

    private void initializeButtons(){
        btnBillboard = createButton("BillBoard");
        btnSchedule = createButton("Schedule");
        btnNewBillBoard = createButton("Creating Billboard");
        btnUserManagement = createButton("User Management");
        btnLogOut = createButton("Log Out");
        btnLogOut.setBackground(Color.red);
        btnLogOut.setForeground(Color.WHITE);
        ///Components in specific button click
        //Billboard
        btnDeleteBb = createButton("Delete");
        btnDeleteBb.setBackground(Color.RED);
        btnDeleteBb.setFocusPainted(false);
        btnEditBb = createButton("Edit");
        btnEditBb.setBackground(Color.YELLOW);
        btnEditBb.setFocusPainted(false);
        btnExportBb = createButton("Export");
        btnExportBb.setBackground(Color.GREEN);
        //Creating billboard
        btnCreateNewBb = createButton("Create");
        btnCreateNewBb.setBackground(Color.GREEN);
        btnCreateNewBb.setFocusPainted(false);

        //User Buttons
        btnUserDelete = createButton("Delete");
        btnUserDelete.setBackground(Color.RED);
        btnUserDelete.setFocusPainted(false);
        btnUserEdit = createButton("Edit");
        btnUserEdit.setBackground(Color.YELLOW);
        btnUserCreate = createButton("Create");
        btnUserCreate.setBackground(Color.GREEN);
        btnUserCreate.setFocusPainted(false);

        //Schedule Buttons
        btnCreateSchedule = createButton("Create");
        btnCreateSchedule.setBackground(Color.GREEN);
        btnDeleteSchedule = createButton("Delete");
        btnDeleteSchedule.setBackground(Color.RED);

        btnBillboard.setPreferredSize(new Dimension(200,100));
        btnBillboard.setBorderPainted(false);
        btnBillboard.setFont(new Font("Serif", Font.PLAIN, 20));
        btnBillboard.setContentAreaFilled(false);
        btnBillboard.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnBillboard.setHorizontalTextPosition(SwingConstants.CENTER);
        btnBillboard.setIcon(new ImageIcon(getClass().getResource("/resources/billboardIcon.png")));

        btnSchedule.setPreferredSize(new Dimension(200,100));
        btnSchedule.setBorderPainted(false);
        btnSchedule.setFont(new Font("Serif", Font.PLAIN, 20));
        btnSchedule.setContentAreaFilled(false);
        btnSchedule.setIcon(new ImageIcon(getClass().getResource("/resources/scheIcon.png")));
        btnSchedule.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnSchedule.setHorizontalTextPosition(SwingConstants.CENTER);

        btnNewBillBoard.setPreferredSize(new Dimension(200,100));
        btnNewBillBoard.setBorderPainted(false);
        btnNewBillBoard.setFont(new Font("Serif", Font.PLAIN, 20));
        btnNewBillBoard.setContentAreaFilled(false);
        btnNewBillBoard.setIcon(new ImageIcon(getClass().getResource("/resources/newbbIcon.png")));
        btnNewBillBoard.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnNewBillBoard.setHorizontalTextPosition(SwingConstants.CENTER);

        btnUserManagement.setPreferredSize(new Dimension(200,100));
        btnUserManagement.setBorderPainted(false);
        btnUserManagement.setFont(new Font("Serif", Font.PLAIN, 18));
        btnUserManagement.setContentAreaFilled(false);
        btnUserManagement.setIcon(new ImageIcon(getClass().getResource("/resources/userIcon.png")));
        btnUserManagement.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnUserManagement.setHorizontalTextPosition(SwingConstants.CENTER);



    }

    private void initializeLabel(){
        //Adjust the label in Center
        lblName = createLabel(Color.WHITE,"Bill Board Control Panel");
        lblName.setPreferredSize(new Dimension( 900,100));
        lblName.setHorizontalAlignment(JLabel.CENTER);
        lblName.setVerticalTextPosition(JLabel.CENTER);
        lblName.setFont(new Font("Serif", Font.PLAIN, 50));
        lblName.setIcon(new ImageIcon(getClass().getResource("/resources/logo.png")));
        lblUserPassword = createLabel(Color.BLACK,"Password");
        lblUserPassword.setVisible(false);
    }

    private void initializePanel(){
        pnlMenu = createPanel(Color.LIGHT_GRAY);
        pnlMenu.setLayout(new GridBagLayout());
        pnlMenu.setPreferredSize(new Dimension(200, 40));
        pnlSchedule = createPanel(Color.WHITE);
        pnlUserManagement = createPanel(Color.WHITE);
        pnlBillboard = createPanel(Color.WHITE);
        pnlCenter = createPanel(Color.WHITE);
        pnlCenter.setLayout(new GridBagLayout());

        pnlBillboard.setLayout(new GridBagLayout());

        pnlBillboardInformation = new JPanel(new GridLayout(0,8,2,2));
        pnlBillboardInformation.setOpaque(true);
        pnlBillboardInformation.setBorder(BorderFactory.createTitledBorder("Billboard Information"));
        pnlBillboardInformation.setPreferredSize(new Dimension(1,80));


        pnlBillboardButton = new JPanel(new FlowLayout());
        pnlBillboardButton.setOpaque(true);
        pnlBillboardButton.setBorder(BorderFactory.createTitledBorder("Command Panel"));
        pnlBillboardButton.setPreferredSize(new Dimension(1,80));


        pnlBillBoardList = new JPanel(new GridLayout(0,1,1,1));
        pnlBillBoardList.setOpaque(true);
        pnlBillBoardList.setBorder(BorderFactory.createTitledBorder("Billboard List"));

        pnlDisplayUserManagement = new JPanel(new FlowLayout());
        pnlDisplayUserManagement.setOpaque(true);
        pnlDisplayUserManagement.setBorder(BorderFactory.createTitledBorder("User List"));
        pnlUserManagement.setLayout(new GridBagLayout());

        pnlUserManagementInfo = new JPanel(new GridLayout(0,3,1,1));
        pnlUserManagementInfo.setOpaque(true);
        pnlUserManagementInfo.setBorder(BorderFactory.createTitledBorder("User Information"));
        pnlUserManagementInfo.setPreferredSize(new Dimension(1,80));

        pnlUserManagementControl = new JPanel(new FlowLayout());
        pnlUserManagementControl.setOpaque(true);
        pnlUserManagementControl.setBorder(BorderFactory.createTitledBorder("Command Panel"));
        pnlUserManagementControl.setPreferredSize(new Dimension(1,80));

        pnlNewBillBoard = new JPanel(new GridBagLayout());
        pnlNewBillBoard.setOpaque(true);
        pnlNewBillBoard.setBorder(BorderFactory.createTitledBorder("Register Form"));

        pnlSchedule.setLayout(new GridBagLayout());
        pnlBillBoardScheduleList = new JPanel();
        pnlBillBoardScheduleList.setOpaque(true);
        pnlBillBoardScheduleList.setBorder(BorderFactory.createTitledBorder("Schedule Information"));
        //pnlBillBoardScheduleList.setPreferredSize(new Dimension(1,80));

        pnlBillBoardScheduleInformation = new JPanel(new GridLayout(0,4,1,1));
        pnlBillBoardScheduleInformation.setOpaque(true);
        pnlBillBoardScheduleInformation.setBorder(BorderFactory.createTitledBorder("Schedule Information"));
        pnlBillBoardScheduleInformation.setPreferredSize(new Dimension(1,80));

        pnlBillBoardScheduleControl = new JPanel(new FlowLayout());
        pnlBillBoardScheduleControl.setOpaque(true);
        pnlBillBoardScheduleControl.setBorder(BorderFactory.createTitledBorder("Command Panel"));
        pnlBillBoardScheduleControl.setPreferredSize(new Dimension(1,80));

        pnlBillboard.setPreferredSize(new Dimension(680, 460));
        pnlNewBillBoard.setPreferredSize(new Dimension(680, 460));
        pnlSchedule.setPreferredSize(new Dimension(680, 460));
        pnlUserManagement.setPreferredSize(new Dimension(680, 460));

        pnlBillboard.setVisible(true);
        pnlNewBillBoard.setVisible(false);
        pnlUserManagement.setVisible(false);
        pnlSchedule.setVisible(false);

    }

    private void initializeBillboardPanel(GridBagConstraints constraints){
        addToPanel(pnlBillboard, pnlBillBoardList,constraints,1,0,1,1);
        addToPanel(pnlBillboard, pnlBillboardInformation,constraints,1,1,1,1);
        addToPanel(pnlBillboard, pnlBillboardButton,constraints,1,2,1,1);
    }

    private void initializeNewBillboardPanel(GridBagConstraints constrainNewBillboard){
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK,"Billboard Registration:"),constrainNewBillboard,0,0,1,1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Name"),constrainNewBillboard,1,1,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardName,constrainNewBillboard,2, 1 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Username"),constrainNewBillboard,1,2,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardUsername,constrainNewBillboard,2, 2 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Background Color"),constrainNewBillboard,1,3,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardBGColor,constrainNewBillboard,2, 3 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Title Color"),constrainNewBillboard,1,4,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardTitleColor,constrainNewBillboard,2, 4 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Description Color"),constrainNewBillboard,1,5,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardDescriptionColor,constrainNewBillboard,2, 5 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard URL"),constrainNewBillboard,1,6,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardURL,constrainNewBillboard,2, 6 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Title"),constrainNewBillboard,1,7,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardTitle,constrainNewBillboard,2, 7 , 1 , 1);
        addToPanel(pnlNewBillBoard,createLabel(Color.BLACK, "Billboard Description"),constrainNewBillboard,1,8,1,1);
        addToPanel(pnlNewBillBoard, tfNewBillboardDescription,constrainNewBillboard,2, 8 , 1 , 1);
        addToPanel(pnlNewBillBoard, btnCreateNewBb,constrainNewBillboard,3,4,1,1);
    }

    private void initializeUserPanel(GridBagConstraints constraints){
        addToPanel(pnlUserManagement,pnlDisplayUserManagement,constraints,0,0,1,1);
        addToPanel(pnlUserManagement,pnlUserManagementInfo,constraints,0,1,1,1);
        addToPanel(pnlUserManagement,pnlUserManagementControl,constraints,0,2,1,1);
    }

    private void initializeSchedulePanel(GridBagConstraints constraints){
        addToPanel(pnlSchedule,pnlBillBoardScheduleList,constraints,0,0,1,1);
        addToPanel(pnlSchedule,pnlBillBoardScheduleInformation, constraints, 0,1,1,1);
        addToPanel(pnlSchedule,pnlBillBoardScheduleControl,constraints,0,2,1,1);
    }

    private void initializeMenuPanel(GridBagConstraints constraints){
        addToPanel(pnlMenu, btnBillboard, constraints,2,0,3,1);
        addToPanel(pnlMenu, btnSchedule,constraints,2,1,3,1);
        addToPanel(pnlMenu, btnNewBillBoard,constraints,2,2,3,1);
        addToPanel(pnlMenu, btnUserManagement,constraints,2,3,3,1);
    }

    private void initializeCenterPanel(GridBagConstraints constraints){
        addToPanel(pnlCenter, pnlBillboard,constraints,0,0,1,1 );
        addToPanel(pnlCenter, pnlNewBillBoard,constraints,0,0,1,1 );
        addToPanel(pnlCenter, pnlSchedule,constraints,0,0,1,1 );
        addToPanel(pnlCenter, pnlUserManagement,constraints,0,0,1,1 );
    }

    private void addToPanel(JPanel jp,Component c, GridBagConstraints
            constraints,int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

    private void addToPanelWithLabelArray(String[] array, JPanel pnl, GridBagConstraints constraints){
        for(int i = 0; i < array.length; i ++){
            addToPanel(pnl, createLabel(Color.BLACK,array[i]), constraints,1,1,1,1 );
        }
    }

    private void addToPanelWithComponentArray(Component[] array, JPanel pnl, GridBagConstraints constraints){
        for(int i = 0; i < array.length; i ++){
            addToPanel(pnl, array[i], constraints,1,1,1,1 );
        }
    }

    private JLabel createLabel(Color c, String text){
        JLabel lbl = new JLabel();
        lbl.setBackground(c);
        lbl.setText(text);
        return lbl;
    }

    private JTextField createTextField(){
        JTextField textField = new JTextField();
        textField.setEditable(false);
        return textField;
    }

    private JPanel createPanel(Color c) {
        //Create a JPanel object and store it in a local var
        JPanel jp = new JPanel();
        jp.setBackground(c);
        return jp;
        //set the background colour to that passed in c
        //Return the JPanel object
    }

    private static JTable createTable(String[] columns, Object[][] data){
        JTable table;
        table = new JTable(data,columns){
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.setPreferredScrollableViewportSize(new Dimension(500,100));
        table.setPreferredSize(new Dimension(700,300));
        table.setSelectionBackground(Color.CYAN);
        table.setShowGrid(false);
        table.setDefaultEditor(Object.class, null);
        table.setRowHeight(30);

        return table;
    }

    private JButton createButton(String text){
        JButton jButton = new JButton();
        jButton.setText(text);
        jButton.setFocusPainted(false);
        jButton.addActionListener(this);
        return jButton;
    }

    /**
     * Server respond handler boolean.
     *
     * @param o the o
     * @return the boolean
     */
    public static boolean serverRespondHandler(Object o){
        if(o.equals("No Permission")){
            System.out.println("[SERVER] User do not have permision to execute this");
            return false;
        }
        // Error from server
        else if ( o instanceof SQLException) {
            System.out.println("[SERVER] " + ((SQLException) o).getMessage());
            return false;
        }
        return true;
    }

    /**
     * Get billboard data.
     */
    public static void getBillboardData(){
        try{
            output.writeObject(new DisplayAllBillboardsRequest(token));
            output.flush();
            Object list = input.readObject();
            if(serverRespondHandler(list)){
                String[][] table = (String[][]) list;
                billBoardData = new Object[table.length - 1][table[0].length];
                for(int i = 0; i < table.length - 1; i++){
                    billBoardData[i] = table[i+1];
                }
            }
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("ERROR");
        }
    }

    /**
     * Delete a billboard.
     *
     * @param billboardName the billboard name
     */
    public static void deleteABillboard(String billboardName){
        try{
            output.writeObject(new DeleteBillboardRequest(billboardName, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Delete Billboard Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("ERROR");
        }
    }

    /**
     * Edit a billboard.
     *
     * @param billboard the billboard
     */
    public static void editABillboard(Billboard billboard){
        try{
            output.writeObject(new EditBillboardRequest(billboard, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Edit Billboard Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Create a billboard.
     *
     * @param billboard the billboard
     */
    public static void createABillboard(Billboard billboard){
        try{
            output.writeObject(new AddBillboardRequest(billboard, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Create Billboard Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Get user data.
     */
    public static void getUserData(){
        try{
            System.out.println("[ControlPanel] Sending Display all users Request");
            output.writeObject(new DisplayAllUsersRequest(token));
            output.flush();
            System.out.println("[ControlPanel] Waiting for Server respond...");
            Object list = input.readObject();
            if(serverRespondHandler(list)){
                String[][] table = (String[][]) list;
                userData = new Object[table.length - 1][table[0].length];
                for(int i = 0; i < table.length - 1; i++){
                    userData[i] = table[i+1];
                }
            }
        } catch (IOException | ClassNotFoundException ioException) {

            System.out.println("ERROR");
        }
    }

    /**
     * Create a user.
     *
     * @param user the user
     */
    public static void createAUser(User user){
        try{
            output.writeObject(new AddUserResquest(user, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Display Create User Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Delete a user.
     *
     * @param username the username
     */
    public static void deleteAUser(String username){
        try{
            output.writeObject(new DeleteUserRequest(username, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Delete user Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Update a user password.
     *
     * @param username the username
     * @param password the password
     */
    public static void updateAUserPassword(String username, String password){
        try{
            output.writeObject(new SetUserPassword(username,password, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Update password Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Update a user privilege.
     *
     * @param username   the username
     * @param privileges the privileges
     */
    public static void updateAUserPrivilege(String username, String[] privileges){
        try{
            output.writeObject(new SetUserPrivilegesRequest(username, privileges, token));
            output.flush();
            System.out.println("[ControlPanel] Sending Update User Privileges Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException);
        }
    }

    /**
     * Get schedule data.
     */
    public static void getScheduleData(){
        try{
            output.writeObject(new DisplayAllSchedulesRequest(token));
            output.flush();
            Object list = input.readObject();
            if(serverRespondHandler(list)){

                String[][] table = (String[][]) list;
                System.out.println(Arrays.deepToString(table));
                scheduleData = new Object[table.length - 1][table[0].length];
                for(int i = 0; i < table.length - 1; i++){
                    scheduleData[i] = table[i+1];
                }
                System.out.println("[ControlPanel] Sending Display all schedules Request");

            }
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("ERROR");
        }
    }

    /**
     * Create a schedule.
     *
     * @param billboardName the billboard name
     * @param start         the start
     * @param duration      the duration
     * @param dayOfWeek     the day of week
     */
    public static void createASchedule(String billboardName, Time start, Time duration, String dayOfWeek){
        try{
            output.writeObject(new SetScheduleRequest(billboardName,start,duration,dayOfWeek,token));
            output.flush();
            System.out.println("[ControlPanel] Sending Schedule Create Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("ERROR");
        }
    }

    /**
     * Delete a schedule.
     *
     * @param billboardName the billboard name
     * @param start         the start
     */
    public static void deleteASchedule(String billboardName, Time start){
        try{
            output.writeObject(new DeleteScheduleRequest(billboardName,start,token));
            output.flush();
            System.out.println("[ControlPanel] Sending Schedule Delete Request");
            serverRespondHandler(input.readObject());
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("ERROR");
        }
    }


    @Override
    public void run() {
        createGUI();

    }

    /**
     * Start.
     *
     * @param token the token
     * @throws SQLException           the sql exception
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static void start(SessionToken token) throws SQLException, IOException, ClassNotFoundException {
        ControlPanel controlPanel = new ControlPanel("BillboardControlPanel");
        ControlPanel.token =token;
        output.writeObject("ControlPanel");
        output.flush();
        System.out.println("[ControlPanel] Connecting to Server...");
        System.out.println(input.readObject());

        getBillboardData();
        getUserData();
        getScheduleData();
        SwingUtilities.invokeLater(controlPanel);
    }

    /**
     * Main.
     *
     * @throws SQLException           the sql exception
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
//TODO: GET THE WHOLE COLUMN
    public static void main() throws SQLException, IOException, ClassNotFoundException {
        Socket socketControlPanel = new Socket("localhost", 1234);

        output = new ObjectOutputStream(socketControlPanel.getOutputStream());
        input = new ObjectInputStream(socketControlPanel.getInputStream());

        output.writeObject("ControlPanel");
        output.flush();
        System.out.println("Identified!");
        System.out.println(input.readObject());
        
        getBillboardData();
        getUserData();
        getScheduleData();
        SwingUtilities.invokeLater(new ControlPanel("BillboardControlPanel"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //SELECT BILLBOARD SECTION
        if(e.getSource() == btnBillboard){
            clearScreen();
            btnBillboard.setContentAreaFilled(true);
            btnNewBillBoard.setContentAreaFilled(false);
            btnSchedule.setContentAreaFilled(false);
            btnUserManagement.setContentAreaFilled(false);
            pnlBillboard.setVisible(true);

            ;
            //pnlCenter.add();
        }
        //SELECT NEW BILLBOARD SECTION
        else if(e.getSource() == btnNewBillBoard){
            clearScreen();
            btnBillboard.setContentAreaFilled(false);
            btnNewBillBoard.setContentAreaFilled(true);
            btnSchedule.setContentAreaFilled(false);
            btnUserManagement.setContentAreaFilled(false);

            pnlNewBillBoard.setVisible(true);
        }
        //SELECT SCHEDULE SECTION
        else if(e.getSource() == btnSchedule){
            clearScreen();
            btnBillboard.setContentAreaFilled(false);
            btnNewBillBoard.setContentAreaFilled(false);
            btnSchedule.setContentAreaFilled(true);
            btnUserManagement.setContentAreaFilled(false);
            pnlSchedule.setVisible(true);
            //pnlCenter.add();

        }
        //SELECT USER SECTION
        else if(e.getSource() == btnUserManagement){
            clearScreen();
            btnBillboard.setContentAreaFilled(false);
            btnNewBillBoard.setContentAreaFilled(false);
            btnSchedule.setContentAreaFilled(false);
            btnUserManagement.setContentAreaFilled(true);
            pnlUserManagement.setVisible(true);
            //pnlCenter.add();
        }

        //DELETE BILLBOARD
        else if(e.getSource() == btnDeleteBb){
            try{
                String billboardName = billboardTable.getValueAt(billboardTable.getSelectedRow(), 0).toString();
                deleteABillboard(billboardName);
                modelTableBillboard.removeRow(billboardTable.getSelectedRow());
                JTextField[] textFields = new JTextField[]{tfBillboardName,tfBillboardUsername,
                        tfBillboardBGColor,tfBillboardTitleColor,tfBillboardDescriptionColor,
                        tfBillboardURL,tfBillboardTitle,tfBillboardDescription};
                clearInputValues(textFields);
            }catch (ArrayIndexOutOfBoundsException outOfBoundsException){
                alertUnselectedRow();
            }
        }

        //EDIT BILLBOARD
        else if(e.getSource() == btnEditBb){
            if(btnEditBb.getText().compareTo("Edit") == 0){
                editBillBoard(true);
                btnEditBb.setText("Save");
            }else{
                //create a new billboard after modified
                try{
                    JTextField[] textFields = new JTextField[] {tfBillboardName,tfBillboardUsername,tfBillboardBGColor
                            ,tfBillboardTitleColor,tfBillboardDescriptionColor,tfBillboardURL,tfBillboardTitle,tfBillboardDescription};
                    if(checkEmptyInput(textFields)){
                        alertEmptyInput();
                        clearInputValues(textFields);
                    }else{
                        setUpdateValue(modelTableBillboard,billboardTable,textFields);
                        Billboard target = new Billboard(tfBillboardName.getText()
                                ,tfBillboardUsername.getText()
                                ,tfBillboardBGColor.getText(),tfBillboardTitleColor.getText()
                                ,tfBillboardDescriptionColor.getText()
                                ,tfBillboardURL.getText(),tfBillboardTitle.getText()
                                ,tfBillboardDescription.getText());
                        editABillboard(target);
                        editBillBoard(false);
                        btnEditBb.setText("Edit");
                    }
                }catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    alertUnselectedRow();
                }
            }
        }
        //CREATE NEW BILLBOARD
        else if(e.getSource() == btnCreateNewBb){
            JTextField[] textFields = new JTextField[]{tfNewBillboardName,tfNewBillboardUsername,
                    tfNewBillboardBGColor,tfNewBillboardTitleColor,tfNewBillboardDescriptionColor,
                    tfNewBillboardURL,tfNewBillboardTitle,tfNewBillboardDescription};
            if(checkEmptyInput(textFields)){
                alertEmptyInput();
            }else{
                Billboard newBillboard = new Billboard(tfNewBillboardName.getText()
                        ,tfNewBillboardUsername.getText()
                        ,tfNewBillboardBGColor.getText(),tfNewBillboardTitleColor.getText()
                        ,tfNewBillboardDescriptionColor.getText()
                        ,tfNewBillboardURL.getText(),tfNewBillboardTitle.getText()
                        ,tfNewBillboardDescription.getText());
                createABillboard(newBillboard);
                String[] textFieldString = new String[]{tfNewBillboardName.getText(),tfNewBillboardUsername.getText(),
                        tfNewBillboardBGColor.getText(),tfNewBillboardTitleColor.getText(),tfNewBillboardDescriptionColor.getText(),
                        tfNewBillboardURL.getText(),tfNewBillboardTitle.getText(),tfNewBillboardDescription.getText()};
                modelTableBillboard.addRow(textFieldString);
                btnBillboard.doClick();
            }
            clearInputValues(textFields);
        }
        //EDIT USER
        else if(e.getSource() == btnUserEdit){
            if(btnUserEdit.getText().compareTo("Edit") == 0){
                editUser(true);
                tfUserName.setEditable(false);
                btnUserEdit.setText("Save");
                lblUserPassword.setVisible(true);
                tfUserPassword.setVisible(true);
            }else{
                JTextField[] textFields = new JTextField[]{tfUserName,tfUserPrivilege,tfUserPassword};
                if(checkEmptyInput(textFields)){
                    alertEmptyInput();
                }else{
                    try{
                        modelTableUser.setValueAt(tfUserPrivilege.getText(),userTable.getSelectedRow(),1);
                        String privileges = tfUserPrivilege.getText();
                        String[] privilege = privileges.split(",");
                        updateAUserPrivilege(userTable.getValueAt(userTable.getSelectedRow(),0).toString(),privilege);
                        updateAUserPassword(userTable.getValueAt(userTable.getSelectedRow(),0).toString(),tfUserPassword.getText());
                        editUser(false);
                        lblUserPassword.setVisible(false);
                        tfUserPassword.setVisible(false);
                        clearInputValues(new JTextField[]{tfUserName,tfUserPassword,tfUserPrivilege});
                        btnUserEdit.setText("Edit");
                    }catch (IndexOutOfBoundsException indexOutOfBoundsException){
                        alertUnselectedRow();
                    }
                    btnUserEdit.setText("Edit");
                }
            }
        }
        //CREATE NEW USER
        else if(e.getSource() == btnUserCreate){
            if(btnUserCreate.getText().compareTo("Create") == 0){
                editUser(true);
                lblUserPassword.setVisible(true);
                tfUserPassword.setVisible(true);
                tfUserName.setEditable(true);
                tfUserName.setText(null);
                tfUserPassword.setText(null);
                tfUserPrivilege.setText(null);
                btnUserCreate.setText("Save");
            }else{
                JTextField[] textFields = new JTextField[]{tfUserName,tfUserPrivilege,tfUserPassword};
                if(checkEmptyInput(textFields)){
                    alertEmptyInput();
                }else{
                    String privileges = tfUserPrivilege.getText();
                    ArrayList<String> privilege = new ArrayList<>(Arrays.asList(privileges.split(",")));
                    try {
                        User newUser = new User(tfUserName.getText(),tfUserPassword.getText(),privilege);
                        createAUser(newUser);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    modelTableUser.addRow(new String[] {tfUserName.getText(),tfUserPrivilege.getText()});
                }
                lblUserPassword.setVisible(false);
                tfUserPassword.setVisible(false);
                tfUserName.setEditable(false);
                editUser(false);
                btnUserCreate.setText("Create");
            }
        }
        //DELETE A USER
        else if(e.getSource() == btnUserDelete){
                try{
                    String username = userTable.getValueAt(userTable.getSelectedRow(), 0).toString();
                    deleteAUser(username);
                    modelTableUser.removeRow(userTable.getSelectedRow());
                }catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    alertUnselectedRow();
                }
                clearInputValues(new JTextField[]{tfUserName,tfUserPassword,tfUserPrivilege});
        }
        //CREATE NEW SCHEDULE
        else if(e.getSource() == btnCreateSchedule){
            if(btnCreateSchedule.getText().compareTo("Create") == 0){
                editSchedule(true);
                btnCreateSchedule.setText("Save");
                clearInputValues(new JTextField[]{tfScheduleBillboardName,tfScheduleStart,tfScheduleDuration
                        ,tfScheduleDayOfWeek});
            }else{
                JTextField[] textFields = new JTextField[]{ tfScheduleBillboardName,tfScheduleStart,
                tfScheduleDuration,tfScheduleDayOfWeek};
                if(checkEmptyInput(textFields)){
                    alertEmptyInput();
                }else{
                    editSchedule(false);
                    btnCreateSchedule.setText("Create");
                    createASchedule(tfScheduleBillboardName.getText(),Time.valueOf(tfScheduleStart.getText()),Time.valueOf(tfScheduleDuration.getText()),
                            tfScheduleDayOfWeek.getText());
                    modelTableSchedule.addRow(new String[]{tfScheduleBillboardName.getText(),tfScheduleStart.getText(),tfScheduleDuration.getText(),
                            tfScheduleDayOfWeek.getText()});
                }
            }
        }
        else if(e.getSource() == btnDeleteSchedule){
            try{
                JTextField[] textFields = new JTextField[]{tfScheduleBillboardName,
                tfScheduleStart,tfScheduleDuration,tfScheduleDayOfWeek};
                deleteASchedule(tfScheduleBillboardName.getText(),Time.valueOf(tfScheduleStart.getText()));
                modelTableSchedule.removeRow(scheduleTable.getSelectedRow());
                clearInputValues(textFields);
            }catch (IndexOutOfBoundsException indexOutOfBoundsException){
                alertUnselectedRow();
            }
        }
        else if(e.getSource() == btnLogOut){
            try {
                output.writeObject("Exit");
                output.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                output.close();
                input.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            dispose();
            System.exit(0);
        }
    }


    /**
     * Clear screen.
     */
    public void clearScreen(){
        pnlNewBillBoard.setVisible(false);
        pnlUserManagement.setVisible(false);
        pnlSchedule.setVisible(false);
        pnlBillboard.setVisible(false);
    }

    /**
     * Center row data.
     *
     * @param table the table
     */
    public void centerRowData(JTable table){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Edit bill board.
     *
     * @param bool the bool
     */
    public void editBillBoard(boolean bool){
        tfBillboardUsername.setEditable(bool);
        tfBillboardBGColor.setEditable(bool);
        tfBillboardTitleColor.setEditable(bool);
        tfBillboardDescriptionColor.setEditable(bool);
        tfBillboardURL.setEditable(bool);
        tfBillboardTitle.setEditable(bool);
        tfBillboardDescription.setEditable(bool);
    }

    /**
     * Edit user.
     *
     * @param bool the bool
     */
    public void editUser(boolean bool){
        tfUserPassword.setEditable(bool);
        tfUserPrivilege.setEditable(bool);
    }

    /**
     * Edit schedule.
     *
     * @param bool the bool
     */
    public void editSchedule(boolean bool){
        tfScheduleBillboardName.setEditable(bool);
        tfScheduleStart.setEditable(bool);
        tfScheduleDuration.setEditable(bool);
        tfScheduleDayOfWeek.setEditable(bool);
    }

    /**
     * Set update value.
     *
     * @param tableModel the table model
     * @param table      the table
     * @param textFields the text fields
     */
    public void setUpdateValue(TableModel tableModel, JTable table, JTextField[] textFields){
        int selectedRow = table.getSelectedRow();
        for(int i =0; i < textFields.length; i++){
            tableModel.setValueAt(textFields[i].getText(),selectedRow,i);
        }
    }

    /**
     * Set value billboard info.
     *
     * @param table the table
     */
    public void setValueBillboardInfo(JTable table){
        JTextField[] textFields = new JTextField[]{tfBillboardName,tfBillboardUsername,tfBillboardBGColor,
                tfBillboardTitleColor,tfBillboardDescriptionColor,tfBillboardURL,tfBillboardTitle,tfBillboardDescription};
        setTextFields(billboardTable,textFields);
        btnEditBb.setText("Edit");
        editBillBoard(false);
    }

    /**
     * Set value user info.
     *
     * @param table the table
     */
    public void setValueUserInfo(JTable table){
        JTextField[] textFields = new JTextField[]{tfUserName,tfUserPrivilege};
        setTextFields(userTable,textFields);
        editUser(false);
        tfUserName.setEditable(false);
        tfUserPassword.setVisible(false);
        lblUserPassword.setVisible(false);
        btnUserCreate.setText("Create");
        btnUserEdit.setText("Edit");
    }

    /**
     * Set value schedule info.
     *
     * @param table the table
     */
    public void setValueScheduleInfo(JTable table){
        JTextField[] textFields = new JTextField[]{ tfScheduleBillboardName, tfScheduleStart,
        tfScheduleDuration,tfScheduleDayOfWeek};
        setTextFields(scheduleTable,textFields);
        editSchedule(false);
        btnUserCreate.setText("Create");
    }

    /**
     * Set text fields.
     *
     * @param table      the table
     * @param textFields the text fields
     */
    public void setTextFields(JTable table, JTextField[] textFields){
        if(table.getSelectedRow() != -1 ){
            for(int i = 0; i < textFields.length; i++){
                textFields[i].setText(table.getValueAt(table.getSelectedRow(), i).toString());
            }
        }
    }

    /**
     * Check empty input boolean.
     *
     * @param inputs the inputs
     * @return the boolean
     */
    public boolean checkEmptyInput(JTextField[] inputs){
        for(int i = 0 ; i < inputs.length ; i++){
            if(inputs[i].getText().compareTo("") == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Clear input values.
     *
     * @param inputs the inputs
     */
    public void clearInputValues(JTextField[] inputs){
        for(int i = 0; i < inputs.length; i ++){
            inputs[i].setText(null);
        }
    }

    /**
     * Alert empty input.
     */
    public void alertEmptyInput(){
        JOptionPane.showMessageDialog(null,"Please fill in all input values");
    }

    /**
     * Alert unselected row.
     */
    public void alertUnselectedRow(){
        JOptionPane.showMessageDialog(null,"Please select a row!");
    }
}
