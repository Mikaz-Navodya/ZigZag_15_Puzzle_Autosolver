
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;


/**
 *
 * @author Mikaz
 */
public class BoardGUI implements ActionListener {
    JFrame fr;
    JPanel mainPanel;
    JComboBox<String> comboBox;
    JButton [][] button;
    int rows ;
    int cols ;
    JButton reshuffleButton;
    JButton autoSolveButtonInitialPosition;
    JButton autoSolveButtonPlayerPosition;
    JButton LeaderBoardsButton;
    JLabel [][] label;
    int [][] board;
    int [][] CPUboard;
    JButton [][] initialButton;
    JLabel [][] initialLabel;
    int[] order;
    int [] MoveDirectoryArray ;
    JPanel initialPuzzlePanel = new JPanel(new GridLayout(4, 4));
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Timer AutosolveTimer;
    ArrayList<Player>  Leaderboard = new ArrayList<Player>();
    ArrayList<Integer>  playerMoves = new ArrayList<Integer>();
    int NoOfMoves=0;
    JLabel NoOfMoveLabel = new JLabel("Number of moves = "+Integer.toString(NoOfMoves));;
    int NoOfShuffles;
    int MovesLimit ;
    int TimeLimit ;
    JLabel DifficultyWarningLabel;
    boolean playerLoosed = false;
    
    File SlideSound = new File("Sounds/ButtonSound.wav");
    File ButtonSound = new File("Sounds/menuButtons.wav");
    File WinSound = new File("Sounds/WinSoundB.wav");
    File LoseSound = new File("Sounds/LoseSound.wav");
    File GameStartSound = new File("Sounds/GameStart.wav");
    
    //timer
    JLabel timeLabel = new JLabel();
    int elapsedTime = 0;
    int seconds =0;
    int minutes =0;
    int kk = TimeLimit;
    boolean started = false;
    String seconds_string = String.format("%02d", seconds);
    String minutes_string = String.format("%02d", minutes);
    Timer PlayerTimer = new Timer(1000, new ActionListener() {
  
    public void actionPerformed(ActionEvent e) {
       
        elapsedTime=elapsedTime+1000;
        minutes = (elapsedTime/60000) % 60;
        seconds = (elapsedTime/1000) % 60;
        seconds_string = String.format("%02d", seconds);
        minutes_string = String.format("%02d", minutes);
        timeLabel.setText(minutes_string+":"+seconds_string+" / 0"+TimeLimit/60000+":00");
         if(TimeLimit==elapsedTime){
             if(playerLoosed!=true){
             PlayerTimer.stop(); 
             playerLoosed=true;
            JFrame frame = new JFrame("You Lose");            
            frame.setLayout(new BorderLayout()); 
            
            playSound(4);
            
            JLabel label = new JLabel("Out of time. Try again. ", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setForeground(Color.red);
            ImageIcon icon = new ImageIcon("Images/game-overs.png"); 
            JLabel imageLabel = new JLabel(icon);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            frame.add(label, BorderLayout.NORTH);
            frame.add(imageLabel, BorderLayout.CENTER); 

            frame.setSize(300, 300);
            frame.setBackground(Color.WHITE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
             System.out.println("Out");
             }
         }
  }
  
 });
    
    public BoardGUI(int NoOfShuffles) 
    {   
        this.NoOfShuffles=NoOfShuffles;
        rows = 4;
        cols = 4;
        board = new int[rows][cols];
        CPUboard = new int[rows][cols];
        initGUI();
    }
    public void initGUI()
    {
        //previous players
        Player p1 = new Player("Upali",12,"00:00:12",12)    ;
        Player p2 = new Player("Nayana",32,"00:00:32",1)    ;
        Player p3 = new Player("Kumara",22,"00:00:22",16)    ;    
        Player p4 = new Player("Nimal",62,"00:00:62",17)    ;    
        Leaderboard.add(p1);
        Leaderboard.add(p2);
        Leaderboard.add(p3);
        Leaderboard.add(p4);

        
        fr = new JFrame("15 Puzzle Game");
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1,2));
        
        NoOfMoveLabel.setFont(new Font("Arial", Font.BOLD, 19));
        
        JPanel InitialpuzzleLabelPanel = new JPanel(new BorderLayout()); 
        JPanel SolvingpuzzleLabelPanel = new JPanel(new BorderLayout()); 
        
        JPanel solvingPuzzlePanel = new JPanel(new GridLayout(4, 4));
        
        initialPuzzlePanel.setBackground(Color.white);
        solvingPuzzlePanel.setBackground(Color.white);
        
        JLabel initialLabelHeading = new JLabel("CPU Puzzle", JLabel.CENTER);
        JLabel solvingLabelHeading = new JLabel("Player Puzzle", JLabel.CENTER);
        JLabel playerDetailLabelHeading = new JLabel("Game Panel", JLabel.CENTER);
        JLabel ChangeDifficultyLabel = new JLabel("Change Difficulty : ", JLabel.CENTER);
        DifficultyWarningLabel = new JLabel("", JLabel.CENTER);
        
        //timer label
        timeLabel.setText(minutes_string+":"+seconds_string+" / 0"+TimeLimit/60000+":00");
        timeLabel.setBounds(100,100,200,100);
        timeLabel.setFont(new Font("Verdana",Font.PLAIN,35));
        timeLabel.setBorder(BorderFactory.createBevelBorder(1));
        timeLabel.setOpaque(true);
        
        JPanel initialHeadingPanel = new JPanel(new BorderLayout());
        JPanel solvingHeadingPanel = new JPanel(new BorderLayout());
        JPanel playerDetailHeadingPanel = new JPanel(new BorderLayout());
        JPanel playerAllDetailPanel = new JPanel();
        playerAllDetailPanel.setLayout( new BoxLayout(playerAllDetailPanel, BoxLayout.Y_AXIS));
        
        
        JPanel comboBoxPanel = new JPanel();
        String[] values = {"Easy", "Medium", "Hard"};
        comboBox = new JComboBox<>(values);
        if(NoOfShuffles==50){
            comboBox.setSelectedItem("Easy");
            TimeLimit = 60000;
            MovesLimit = 110;
        }else if(NoOfShuffles==75)
        {
             comboBox.setSelectedItem("Medium");
             TimeLimit = 120000;
            MovesLimit = 175;
        }
        else{
            comboBox.setSelectedItem("Hard"); 
            TimeLimit = 240000;
            MovesLimit = 250;
        }
        

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String selectedOption = (String) comboBox.getSelectedItem();
                
                if (selectedOption.equals("Easy")) {
                    NoOfShuffles=75;
                    DifficultyWarningLabel.setText("You need to restart a new game after change difficulty");
                } else if (selectedOption.equals("Medium")) {
                    NoOfShuffles=100;
                    DifficultyWarningLabel.setText("You need to restart a new game after change difficulty");
                } else if (selectedOption.equals("Hard")) {
                    NoOfShuffles=150;
                    DifficultyWarningLabel.setText("You need to restart a new game after change difficulty");
                }
                System.out.println("Selected value: " + comboBox.getSelectedItem());
            }
        });
        
        
        JPanel playerDetailPanel = new JPanel(new BorderLayout());
        playerDetailPanel.setBounds(250, 250, 250, 250);
        
        initialLabelHeading.setFont(new Font("Arial", Font.BOLD, 18)); 
        initialHeadingPanel.setBounds(250, 250, 250, 250);
        solvingLabelHeading.setFont(new Font("Arial", Font.BOLD, 18));
        solvingHeadingPanel.setBounds(250, 250, 250, 250);
        playerDetailLabelHeading.setFont(new Font("Arial", Font.BOLD, 18));
        playerDetailLabelHeading.setBounds(250, 250, 250, 250);
        
        reshuffleButton = new JButton("New Game");
        autoSolveButtonInitialPosition = new JButton("Auto solve from initial position");
        autoSolveButtonPlayerPosition = new JButton("Auto solve from current position");
        LeaderBoardsButton=new JButton("Leaderboards");
        reshuffleButton.addActionListener(this);
        autoSolveButtonInitialPosition.addActionListener(this);
        autoSolveButtonPlayerPosition.addActionListener(this);
        LeaderBoardsButton.addActionListener(this);
        
        Dimension buttonSize = new Dimension(150, 30); // Adjust button size
        reshuffleButton.setPreferredSize(buttonSize);
        autoSolveButtonInitialPosition.setPreferredSize(buttonSize);
        LeaderBoardsButton.setPreferredSize(buttonSize);
        comboBoxPanel.setPreferredSize(new Dimension(150, 10));

        
        //Player detail button set       
        reshuffleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        autoSolveButtonInitialPosition.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        autoSolveButtonPlayerPosition.setAlignmentX(Component.CENTER_ALIGNMENT);
        LeaderBoardsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ChangeDifficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        NoOfMoveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBoxPanel.add(ChangeDifficultyLabel);
        comboBoxPanel.add(comboBox);
        DifficultyWarningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
        playerAllDetailPanel.add(Box.createVerticalGlue());
        playerAllDetailPanel.add(reshuffleButton);       
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(LeaderBoardsButton); 
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(autoSolveButtonInitialPosition); 
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(autoSolveButtonPlayerPosition); 
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(timeLabel); 
        playerAllDetailPanel.add(Box.createVerticalStrut(10));        
        playerAllDetailPanel.add(NoOfMoveLabel); 
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(comboBoxPanel);       
        playerAllDetailPanel.add(Box.createVerticalStrut(10));
        playerAllDetailPanel.add(DifficultyWarningLabel);
        playerAllDetailPanel.add(Box.createVerticalGlue());
        
        initialHeadingPanel.add(initialLabelHeading);
        solvingHeadingPanel.add(solvingLabelHeading);
        playerDetailHeadingPanel.add(playerDetailLabelHeading);
      
        button = new JButton[rows][cols];
        label  = new JLabel[rows][cols];
        
        initialButton = new JButton[rows][cols];
        initialLabel  = new JLabel[rows][cols];
        
        this.ArrangeOrder();       
        CPUpuzzle();
        
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                button[i][j] = new JButton();
                String text = i+","+j; 
                button[i][j].setText(text);
                button[i][j].setFont(new Font("TimesRoman",Font.PLAIN,0));
                button[i][j].addActionListener(this);
                int val = board[i][j];
                String fileName;
                if(val!=-1)
                {
                    fileName = "Pics/" + val + ".png";
                    label[i][j] = new JLabel(new ImageIcon(fileName), JLabel.CENTER);                
        
                }
                else
                {
                    label[i][j] = new JLabel("");                
                }
                button[i][j].add(label[i][j]);
                button[i][j].setBorder(BorderFactory.createLineBorder(Color.black,2));
                button[i][j].setBackground(Color.LIGHT_GRAY);
                solvingPuzzlePanel.add(button[i][j]);
            }
        }
        
        
        initialLabelHeading.setPreferredSize(new Dimension(200,50));
        solvingLabelHeading.setPreferredSize(new Dimension(200,50));
        playerDetailLabelHeading.setPreferredSize(new Dimension(200,50));
        
        InitialpuzzleLabelPanel.add(initialHeadingPanel, BorderLayout.NORTH);
        InitialpuzzleLabelPanel.add(initialPuzzlePanel, BorderLayout.SOUTH);
        SolvingpuzzleLabelPanel.add(solvingHeadingPanel, BorderLayout.NORTH);
        SolvingpuzzleLabelPanel.add(solvingPuzzlePanel, BorderLayout.SOUTH);
        
        playerDetailPanel.add(playerDetailHeadingPanel, BorderLayout.NORTH);
        playerDetailPanel.add(playerAllDetailPanel, BorderLayout.CENTER);
        playerDetailPanel.setPreferredSize(new Dimension(50,fr.getHeight()));
        
        mainPanel.add(InitialpuzzleLabelPanel);
        mainPanel.add(playerDetailPanel);
        mainPanel.add(SolvingpuzzleLabelPanel);       
        fr.add(mainPanel);
       
        fr.setVisible(true);
        fr.setSize(1650,510);
        fr.setLocationRelativeTo(null); 
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


       shuffle(NoOfShuffles);
       filter();
       PlayerTimer.start();
       NoOfMoves=0;
       NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
       playSound(5);
    }
    
    public void CPUpuzzle(){
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                
            initialButton[i][j] = new JButton();
            String text = i + "," + j;
            initialButton[i][j].setText(text);
            initialButton[i][j].setFont(new Font("TimesRoman", Font.PLAIN, 0));
            initialButton[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 2));
            initialButton[i][j].setBackground(Color.LIGHT_GRAY);
            int val = CPUboard[i][j];            
            String fileName = "Pics/" + val + ".png";
            initialLabel [i][j] = new JLabel(new ImageIcon(fileName), JLabel.CENTER);
            initialButton[i][j].add(initialLabel[i][j]);
            initialPuzzlePanel.add(initialButton[i][j]);
            }
        }
    }
    public void updateCPUpuzzle(){
        
        for (int k = 0; k < rows; k++) {
            
            for (int j = 0; j < cols; j++) {
                CPUboard[k][j] = board[k][j];
            }
            
        }
        
        int[][] kj = CPUboard;
        System.out.println(Arrays.deepToString(kj));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String text = kj[i][j] + "";
                initialButton[i][j].setText(text);

                int val = kj[i][j];
                String fileName = "Pics/" + val + ".png";
                initialLabel[i][j].setIcon(new ImageIcon(fileName));
            }
        }
    }
    public void ArrangeOrder()
    {
        Random rand = new Random();
        int [] array = {1, 2, 3, 4, 8, 7, 6, 5, 9, 10, 11, 12, -1, 15, 14, 13};
        int count = 0;
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                board[i][j] = array[count];
                count = count + 1;
            }
        }
        
        playerMoves.clear();
        
    }
    void resetTimer() {
        PlayerTimer.stop();
        elapsedTime=0;
        seconds =0;
        minutes=0;
        seconds_string = String.format("%02d", seconds);
        minutes_string = String.format("%02d", minutes);     
        timeLabel.setText(minutes_string+":"+seconds_string+" / 0"+TimeLimit/60000+":00");
  
 }
    Boolean isWin()
    {   int [][] winningArray = {{1,2,3,4},{8,7,6,5},{9,10,11,12},{-1,15,14,13}};
        if (playerLoosed!=true){
            if (NoOfMoves<=MovesLimit){
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if(board[i][j]!=winningArray[i][j])
                        {
                            return false;
                        }
                    }
                }
            } else{
                playerLoosed=true;
                PlayerTimer.stop(); 
                playSound(4);
                JFrame frame = new JFrame("You Lose");            
                frame.setLayout(new BorderLayout()); 

                JLabel label = new JLabel("Out of Moves. Try again. ", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 24));
                label.setForeground(Color.red);
                
                ImageIcon icon = new ImageIcon("Images/loses.png"); 
                JLabel imageLabel = new JLabel(icon);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                frame.add(label, BorderLayout.NORTH);
                frame.add(imageLabel, BorderLayout.CENTER); 

                frame.setSize(400, 300);
                frame.setBackground(Color.WHITE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                 System.out.println("Out");
                
                return false;
            }   
        PlayerTimer.stop();
        
        return true;
        }else{return false;}
    }
    public void displayWinMsg()
    {    playSound(3);
         JFrame frame = new JFrame("Game Win");
         JLabel label = new JLabel("You Solve The Puzzle ",JLabel.CENTER);
         label.setForeground(Color.green);
         JLabel TimeTakenlabel = new JLabel("Time you've taken is ",JLabel.CENTER);
         label.setFont(new Font("TimesRoman",Font.BOLD,20));
         
        ImageIcon icon = new ImageIcon("Images/wins.png"); 
        JLabel ImageLabel = new JLabel(icon);
        ImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     
         JLabel Timelabel = new JLabel(timeLabel.getText(),JLabel.CENTER);
         Timelabel.setFont(new Font("TimesRoman",Font.BOLD,20));
         
        JTextField nameField = new JTextField(20);
        JLabel nameLabel = new JLabel("Enter Your Name:", JLabel.CENTER);
        System.out.println(elapsedTime);
        JButton SubmitPlayerName = new JButton("Submit");
        SubmitPlayerName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound(2);
                String userName = nameField.getText();
                System.out.println("Submitted: " + userName);
                Player ply = new Player(userName,elapsedTime/1000,timeLabel.getText(),NoOfMoves);
                Leaderboard.add(ply);
                frame.dispose(); 
            }
        });
        
        
         frame.add(label);
         frame.add(ImageLabel);
         frame.add(TimeTakenlabel);
         frame.add(Timelabel);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(SubmitPlayerName);
        
        
        frame.setLayout(new GridLayout(7,3));
        frame.setSize(300,300);
        frame.setBackground(Color.white);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    
    public void displayLeaderboard()
    {
        JFrame frame = new JFrame("Leaderboards");
        Collections.sort(Leaderboard, Comparator.comparingInt(Player::Score));
        for (Player player : Leaderboard) {
            JLabel label = new JLabel(player.getName()+" = " +player.getTimeInString()+" + "+player.getAmountOfMoves()+" = " +player.Score());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(label);
        }
       
        frame.setLayout(new GridLayout(7,1));
        frame.setSize(300,300);
        frame.setBackground(Color.white);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public  void playSound(int type) {
        try {
            File MyFile =new File("");
            switch(type){
                case 1:MyFile=SlideSound;
                    break;
                case 2:MyFile=ButtonSound;
                    break;
                case 3:MyFile=WinSound;
                    break;
                case 4:MyFile=LoseSound;
                    break;
                case 5:MyFile=GameStartSound;
                    break;    
            }
            
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(MyFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent ae) 
    {  
        if(ae.getSource()==autoSolveButtonInitialPosition){
            playSound(2);
            autoSolveButtonPlayerPosition.setEnabled(false);
             autoSolveButtonInitialPosition.setEnabled(false);
            reshuffleButton.setEnabled(false);
            PlayerTimer.stop();
            autoSolve();
            AutosolveTimer = new Timer(150*MoveDirectoryArray.length+2000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            reshuffleButton.setEnabled(true);
            AutosolveTimer.stop();
                }
           });
            AutosolveTimer.setRepeats(false); 
            AutosolveTimer.start(); 
            
        }else if(ae.getSource()==reshuffleButton){
            playSound(2);
            DifficultyWarningLabel.setText("");
            autoSolveButtonPlayerPosition.setEnabled(true);
            autoSolveButtonInitialPosition.setEnabled(true);
            ArrangeOrder();            
            shuffle(NoOfShuffles);
            filter();
            playerLoosed=false;
            
            if(comboBox.getSelectedItem()=="Easy"){              
                    TimeLimit = 60000;
                    MovesLimit =110;
            }else if(comboBox.getSelectedItem()=="Medium"){
                    TimeLimit = 120000;
                    MovesLimit = 170;
            }else if(comboBox.getSelectedItem()=="Hard"){
                    TimeLimit = 240000;
                    MovesLimit = 250;
            }
            
            resetTimer();
            NoOfMoves=0;
            NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
            PlayerTimer.start();
            
            
        }else if(ae.getSource()==LeaderBoardsButton){
            playSound(2);
            displayLeaderboard();
            
            
        }else if(ae.getSource()==  autoSolveButtonPlayerPosition )
        {
            playSound(2);
            autoSolveButtonInitialPosition.setEnabled(false);
            autoSolveButtonPlayerPosition.setEnabled(false);
            reshuffleButton.setEnabled(false);
            PlayerTimer.stop();
            updateCPUpuzzle();
            autoSolveFromPlayerPosition();
            AutosolveTimer = new Timer(150*MoveDirectoryArray.length+2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reshuffleButton.setEnabled(true);
                AutosolveTimer.stop(); 
                    }
               });
                AutosolveTimer.setRepeats(false);
                AutosolveTimer.start(); 
        }
        else{
            if(playerLoosed!=true){
         playSound(1);
        Boolean flag = isWin();
        if(flag==false)
        {
             String s = ae.getActionCommand().toString();
            int r = Integer.parseInt(s.split(",")[0]);
            int c = Integer.parseInt(s.split(",")[1]);
            
            if(board[r][c]!=-1)
            {
                
                if(r+1<rows && board[r+1][c]==-1)   //for up
                {
                    up(r,c);
                   
                }
                else if(r-1>=0 && board[r-1][c]==-1) //for down
                {
                    down(r,c);
                  
                }
                else if(c+1<cols && board[r][c+1]==-1) // for right
                {
                    left(r,c);
                  
                }
                else if(c-1>=0 && board[r][c-1]==-1) // for left
                {
                    right(r,c);
                  
                }
            }  
            flag = isWin();
            if(flag==true)
            {
               displayWinMsg();
            }
        }}
        }  
        
        
    }
    
    public void up(int r, int c){
                    label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r+1][c].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r+1][c];
                    board[r+1][c] = temp;
                    NoOfMoves++;
                    NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
                    playerMoves.add(1);
                    
    }
    public void down(int r, int c){
       label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r-1][c].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r-1][c];
                    board[r-1][c] = temp;  
                    NoOfMoves++;
                    NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
                    playerMoves.add(0);
    }
    public void left(int r, int c){
       label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r][c+1].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r][c+1];
                    board[r][c+1] = temp; 
                    NoOfMoves++;
                    NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
                    playerMoves.add(3);
    }
    public void right(int r, int c){ 
       label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r][c-1].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r][c-1];
                    board[r][c-1] = temp; 
                    NoOfMoves++;
                    NoOfMoveLabel.setText("Number of moves = "+Integer.toString(NoOfMoves)+" / "+MovesLimit);
                    playerMoves.add(2);
    }
    
    //CPU solving
    public void upAuto(int r, int c){
    

    executor.schedule(() -> {
    try{
        Thread.sleep(150);
            initialLabel[r][c].setIcon(new ImageIcon(""));
            String fileName = "Pics/" + CPUboard[r][c] + ".png";
            initialLabel[r+1][c].setIcon(new ImageIcon(fileName));
            int temp = CPUboard[r][c];
            CPUboard[r][c] = CPUboard[r+1][c];
            CPUboard[r+1][c] = temp;
             } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 500, TimeUnit.MILLISECONDS);


        }
    public void downAuto(int r, int c){
executor.schedule(() -> {
try{
        Thread.sleep(150);
       initialLabel[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + CPUboard[r][c] + ".png";
                    initialLabel[r-1][c].setIcon(new ImageIcon(fileName));
                    int temp = CPUboard[r][c];
                    CPUboard[r][c] = CPUboard[r-1][c];
                    CPUboard[r-1][c] = temp;  
         } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500, TimeUnit.MILLISECONDS);

                    
    }
    public void leftAuto(int r, int c){
        executor.schedule(() -> {
            try {
        Thread.sleep(150);
        initialLabel[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + CPUboard[r][c] + ".png";
                    initialLabel[r][c+1].setIcon(new ImageIcon(fileName));
                    int temp = CPUboard[r][c];
                    CPUboard[r][c] = CPUboard[r][c+1];
                    CPUboard[r][c+1] = temp;  
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500, TimeUnit.MILLISECONDS);
    }
    public void rightAuto(int r, int c){ 

        executor.schedule(() -> {
            try {
                Thread.sleep(150);
        initialLabel[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + CPUboard[r][c] + ".png";
                    initialLabel[r][c-1].setIcon(new ImageIcon(fileName));
                    int temp = CPUboard[r][c];
                    CPUboard[r][c] = CPUboard[r][c-1];
                    CPUboard[r][c-1] = temp; 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500, TimeUnit.MILLISECONDS);
    }
                      
    public void filter(){

        MoveDirectoryArray = new int[playerMoves.size()];
        for (int i = 0; i < playerMoves.size(); i++) {
            MoveDirectoryArray[i] = playerMoves.get(i);
        }

        Reversing(MoveDirectoryArray); 



        System.out.println(Arrays.toString(MoveDirectoryArray));
        
        
    }
    
    public int[] Reversing(int[] array){
        int start = 0;
        int end = array.length - 1;
        
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            
            start++;
            end--;
        }
        return array;
    }
    
    public void autoSolveFromPlayerPosition(){

        
        filter();
        autoSolve();
    }
    
   
        
    public void autoSolve(){
        final int[] emptyPosition = new int[2];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (CPUboard[i][j] == -1) {
                    emptyPosition[0] = i;
                    emptyPosition[1] = j;
                    break;
                }
            }
        }

   

            int emptyRowg = emptyPosition[0];
            int emptyColg = emptyPosition[1];
          for (int i = 0; i < MoveDirectoryArray.length; i++) { 
                switch (MoveDirectoryArray[i]) {
                 case 0: // Up
                     if (emptyRowg > 0) {
                        upAuto(emptyRowg-1, emptyColg);
                        emptyRowg--;
                    }
                    break;
                case 1: // Down
                    if  (emptyRowg < 3){
                        downAuto(emptyRowg+1, emptyColg);
                        emptyRowg++;
                    }
                    break;
                case 2: // Left
                    if (emptyColg > 0) {
                        leftAuto(emptyRowg, emptyColg-1);
                        emptyColg--;
                    }
                    break;
                case 3: // Right
                     if (emptyColg < 3) {
                        rightAuto(emptyRowg, emptyColg+1);
                        emptyColg++;
                    }
                    break;
            }
                
            
               
            }

   }
    
    public void shuffle(int moves) {
        Random rand = new Random();
        int emptyRow = 3;
        int emptyCol = 0;
        order = new int[moves];
        for (int i=0;i<order.length-1;i++){
            order[i]=-1;
        }
        int i =0;
        int previous = -1;
        while(i<moves){
            int direction = rand.nextInt(4);
            System.out.println(direction);
            switch (direction) {
                case 0: // Up
                    if (emptyRow > 0 && previous != 1) {
                        up(emptyRow-1, emptyCol);
                        emptyRow--;
                        previous = 0;
                        order[i] = direction;
                        i++;
                      //  System.out.println("up");
                    }
                    break;
                case 1: // Down
                    if (emptyRow < 3 && previous != 0) {
                        down(emptyRow+1, emptyCol);
                        emptyRow++;
                        previous = 1;
                        order[i] = direction;
                        i++;
                      //  System.out.println("down");
                    }
                    break;
                case 2: // Left
                    if (emptyCol > 0 && previous != 3 ) {
                        left(emptyRow, emptyCol-1);
                        emptyCol--;
                        previous = 2;
                        order[i] = direction;
                        i++;
                     //   System.out.println("left");
                    }
                    break;
                case 3: // Right
                    if (emptyCol < 3 && previous != 2) {
                        right(emptyRow, emptyCol+1);
                        emptyCol++;
                        previous = 3;
                        order[i] = direction;
                        i++;
                      //  System.out.println("right");
                    }
                    break;
            }

 //       }
     }   
        
        
        System.out.println(Arrays.toString(order));
        updateCPUpuzzle();
        
    }
    
    
    
}
