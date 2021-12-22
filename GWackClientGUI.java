import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


public class GWackClientGUI extends JFrame implements KeyListener{
    protected Socket sock = null;
    protected PrintWriter pw = null;
    protected BufferedReader in = null;
    protected boolean exit = false;
    protected Thread updateGUI = null;


    public static void main(String args[]){
        GWackClientGUI f = new GWackClientGUI();
        f.setVisible(true);
    }

    public GWackClientGUI(){
        super();
        this.setTitle("GWack -- GW Slack Simulator - Disconnected");
        this.setSize(1000, 500);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000,500));

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        this.add(top, BorderLayout.NORTH);

        JPanel topRight = new JPanel();
        topRight.setLayout(new BorderLayout());
        top.add(topRight, BorderLayout.EAST);

        JPanel topRightLineStart = new JPanel();
        topRightLineStart.setLayout(new BorderLayout());
        topRight.add(topRightLineStart, BorderLayout.LINE_START);

        JPanel topRightLineEnd = new JPanel();
        topRightLineEnd.setLayout(new BorderLayout());
        topRight.add(topRightLineEnd, BorderLayout.LINE_END);

        JPanel topRightLineEndConnections = new JPanel();
        topRightLineEndConnections.setLayout(new BorderLayout(0,3));
        topRightLineEnd.add(topRightLineEndConnections, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout(10,0));
        this.add(center, BorderLayout.CENTER);

        JPanel centerLeft = new JPanel();
        centerLeft.setLayout(new BorderLayout());
        center.add(centerLeft, BorderLayout.WEST);

        JPanel centerCenter = new JPanel();
        centerCenter.setLayout(new BorderLayout());
        center.add(centerCenter, BorderLayout.CENTER);

        JPanel centerCenterCenter = new JPanel();
        centerCenterCenter.setLayout(new BorderLayout());
        centerCenter.add(centerCenterCenter, BorderLayout.CENTER);

        JPanel centerCenterSouth = new JPanel();
        centerCenterSouth.setLayout(new BorderLayout());
        centerCenter.add(centerCenterSouth, BorderLayout.SOUTH);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        this.add(bottom, BorderLayout.SOUTH);

        JPanel bottomWest = new JPanel();
        bottomWest.setLayout(new BorderLayout());
        bottom.add(bottomWest, BorderLayout.WEST);
        
        JPanel bottomCenter = new JPanel();
        bottomCenter.setLayout(new BorderLayout());
        bottom.add(bottomCenter, BorderLayout.CENTER);

        JPanel bottomCenterWest = new JPanel();
        bottomCenterWest.setLayout(new BorderLayout());
        bottomCenter.add(bottomCenterWest, BorderLayout.WEST);

        JButton connect = new JButton("Connect");
        JButton disconnect = new JButton("Disconnect");
        JButton send = new JButton("Send");
        JLabel name = new JLabel(" Name ");
        JLabel ipAddress = new JLabel(" IP Address ");
        JLabel port = new JLabel(" Port ");
        JTextField nameTextBox = new JTextField(10);
        JTextField ipTextBox = new JTextField(16);
        JTextField portTextBox = new JTextField(8);
        JLabel membersOnlineLabel = new JLabel("Members Online");
        JLabel messagesLabel = new JLabel("Messages");
        JLabel composeLabel = new JLabel("Compose");
        JTextArea membersOnlineText = new JTextArea(5, 15);
        JTextArea messagesText = new JTextArea(5,5);
        JTextArea composeText = new JTextArea(3,5);
        String[] themeList = {"Light", "Dark", "Gray", "Ocean", "Buff and Blue", "Sunny", "Christmas", "Halloween"};
        JComboBox<String> themes = new JComboBox<String>(themeList);
        JLabel themeTitle = new JLabel("Themes");
        String[] statusList = {"Avaliable", "Busy", "Away"};
        JComboBox<String> statuses = new JComboBox<String>(statusList);
        JLabel statusTitle = new JLabel("Status");

        JFrame typographyFrame = new JFrame();
        typographyFrame.setTitle("GWack -- GW Slack Simulator - Disconnected");
        typographyFrame.setSize(100, 100);
        typographyFrame.setLocation(100, 100);
        typographyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        typographyFrame.setResizable(false);

        
        membersOnlineText.setEditable(false);
        messagesText.setEditable(false);

        JScrollPane membersOnline = new JScrollPane(membersOnlineText);
        JScrollPane messages = new JScrollPane(messagesText);
        JScrollPane compose = new JScrollPane(composeText);

        JScrollBar vertical = messages.getVerticalScrollBar();
        

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        nameTextBox.setBorder(border);
        ipTextBox.setBorder(border);
        portTextBox.setBorder(border);
        membersOnline.setBorder(border);
        messages.setBorder(border);
        compose.setBorder(border);

        
        topRightLineStart.add(name, BorderLayout.WEST);
        topRightLineStart.add(nameTextBox, BorderLayout.CENTER);
        topRightLineStart.add(ipAddress, BorderLayout.EAST);
        topRight.add(ipTextBox, BorderLayout.CENTER);
        topRightLineEnd.add(port, BorderLayout.WEST);
        topRightLineEnd.add(portTextBox, BorderLayout.CENTER);
        topRightLineEndConnections.add(disconnect, BorderLayout.EAST);
        topRightLineEndConnections.add(connect, BorderLayout.WEST);
        centerLeft.add(membersOnlineLabel, BorderLayout.NORTH);
        centerLeft.add(membersOnline, BorderLayout.CENTER);
        centerCenterCenter.add(messagesLabel, BorderLayout.NORTH);
        centerCenterCenter.add(messages, BorderLayout.CENTER);
        centerCenterSouth.add(composeLabel, BorderLayout.NORTH);
        centerCenterSouth.add(compose, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);
        bottomWest.add(themes, BorderLayout.WEST);
        bottomWest.add(themeTitle, BorderLayout.EAST);
        bottomCenterWest.add(statuses, BorderLayout.WEST);
        bottomCenterWest.add(statusTitle, BorderLayout.EAST);

        themes.addActionListener(l -> {
            String selectedTheme = (String)themes.getSelectedItem();

            Color color1 = Color.WHITE;
            Color color2 = Color.BLACK;
            if (selectedTheme.equals("Light")){
                color1 = Color.WHITE;
                color2 = Color.BLACK;
            }
            else if (selectedTheme.equals("Dark")){
                color1 = Color.BLACK;
                color2 = Color.WHITE;
            }
            else if (selectedTheme.equals("Gray")){
                color1 = Color.GRAY;
                color2 = Color.WHITE;
            }
            else if (selectedTheme.equals("Ocean")){
                color1 = Color.CYAN;
                color2 = Color.BLUE;
            }
            else if (selectedTheme.equals("Buff and Blue")){
                color1 = Color.BLUE;
                color2 = Color.YELLOW;   
            }
            else if (selectedTheme.equals("Sunny")){
                color1 = Color.CYAN;
                color2 = Color.YELLOW;   
            }
            else if (selectedTheme.equals("Christmas")){
                color1 = Color.RED;
                color2 = Color.GREEN;   
            }
            else if (selectedTheme.equals("Halloween")){
                color1 = Color.BLACK;
                color2 = Color.ORANGE;   
            }

            
            this.setBackground(color1);
            //Panels
            top.setBackground(color1);
            topRight.setBackground(color1);
            topRightLineStart.setBackground(color1);
            topRightLineEnd.setBackground(color1);
            topRightLineEndConnections.setBackground(color1);
            center.setBackground(color1);
            centerLeft.setBackground(color1);
            centerCenter.setBackground(color1);
            centerCenterCenter.setBackground(color1);
            centerCenterSouth.setBackground(color1);
            bottom.setBackground(color1);
            bottomWest.setBackground(color1);
            bottomCenter.setBackground(color1);
            bottomCenterWest.setBackground(color1);
            //Labels
            name.setBackground(color1);
            ipAddress.setBackground(color1);
            port.setBackground(color1);
            membersOnlineLabel.setBackground(color1);
            messagesLabel.setBackground(color1);
            composeLabel.setBackground(color1);
            themeTitle.setBackground(color1);
            statusTitle.setBackground(color1);

            
            //Text Fields
            nameTextBox.setBackground(color1);
            ipTextBox.setBackground(color1);
            portTextBox.setBackground(color1);
            //Text Areas
            membersOnlineText.setBackground(color1);
            messagesText.setBackground(color1);
            composeText.setBackground(color1);
            //Scroll Panes
            membersOnline.setBackground(color1);
            messages.setBackground(color1);
            compose.setBackground(color1);

            //Other Color
            nameTextBox.setBorder(BorderFactory.createLineBorder(color2, 1));
            ipTextBox.setBorder(BorderFactory.createLineBorder(color2, 1));
            portTextBox.setBorder(BorderFactory.createLineBorder(color2, 1));
            membersOnline.setBorder(BorderFactory.createLineBorder(color2, 1));
            messages.setBorder(BorderFactory.createLineBorder(color2, 1));
            compose.setBorder(BorderFactory.createLineBorder(color2, 1));

            name.setForeground(color2);
            ipAddress.setForeground(color2);
            port.setForeground(color2);
            membersOnlineLabel.setForeground(color2);
            messagesLabel.setForeground(color2);
            composeLabel.setForeground(color2);
            themeTitle.setForeground(color2);
            statusTitle.setForeground(color2);

            nameTextBox.setForeground(color2);
            ipTextBox.setForeground(color2);
            portTextBox.setForeground(color2);
            membersOnlineText.setForeground(color2);
            messagesText.setForeground(color2);
            composeText.setForeground(color2);

            nameTextBox.setCaretColor(color2);
            ipTextBox.setCaretColor(color2);
            portTextBox.setCaretColor(color2);
            membersOnlineText.setCaretColor(color2);
            messagesText.setCaretColor(color2);
            composeText.setCaretColor(color2);
        });

        disconnect.setVisible(false);

        connect.addActionListener(l -> {
            try{
                sock = new Socket(ipTextBox.getText(), Integer.parseInt(portTextBox.getText()));
                
            }
            catch(Exception e){
                JFrame error = new JFrame("Error");
                JOptionPane.showMessageDialog(error, "Cannot Connect", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            try{
                pw = new PrintWriter(sock.getOutputStream());
                if (ipTextBox.getText().equals("ssh-cs2113.adamaviv.com")){
                    pw.println("SECRET");
                    pw.println("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
                    pw.println("NAME");
                    pw.println(nameTextBox.getText());
                }
                else{
                    pw.println("NAME");
                    pw.println(nameTextBox.getText());
                }
                pw.flush();

                connect.setVisible(false);
                disconnect.setVisible(true);

                this.setTitle("GWack -- GW Slack Simulator - Connected");
                
                for(ActionListener al: disconnect.getActionListeners()){
                    disconnect.removeActionListener(al);
                }

                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                updateGUI = new Thread(() -> {
                    try{
                        String allMessages = "";
                        String newLine;
                        while(!exit){
                            
                            if(in.ready()){
                                newLine = in.readLine();
                            }
                            else{
                                continue;
                            }
                            if (newLine.equals("START_CLIENT_LIST")){
                                newLine = in.readLine();
                                String names = "";
                                while(!newLine.equals("END_CLIENT_LIST")){
                                    names = names.concat(newLine + "\n");
                                    newLine = in.readLine();
                                }
                                membersOnlineText.setText(names);
                            }
                            else{
                                allMessages = allMessages.concat(newLine + "\n");
                                messagesText.setText(allMessages);
                            }
                            vertical.setValue(vertical.getMaximum());
                        }
                    }
                    catch(Exception e){
                        JFrame error = new JFrame("Error");
                        JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                });
                

                ActionListener statusListener = new ActionListener(){
                    public void actionPerformed(ActionEvent l){

                        String currentStatus = (String)statuses.getSelectedItem();

                        String statusOutput = "";
                        if (currentStatus.equals("Avaliable")){
                            statusOutput = "--" + nameTextBox.getText() + " is avaliable--";
                        }
                        else if (currentStatus.equals("Busy")){
                            statusOutput = "--" + nameTextBox.getText() + " is busy--";
                        }
                        else if (currentStatus.equals("Away")){
                            statusOutput = "--" + nameTextBox.getText() + " is away--";
                        }
                        try{
                            updateGUI.interrupt();
                        }
                        catch(Exception e){
                            JFrame error = new JFrame("Error");
                            JOptionPane.showMessageDialog(error, "Interrupt Exception", "Error", JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                        try{
                            pw.println(statusOutput);
                            pw.flush();
                        }
                        catch(Exception e){
                            JFrame error = new JFrame("Error");
                            JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                    }
                };

                statuses.addActionListener(statusListener);
                
                ActionListener sendListener = new ActionListener(){
                    public void actionPerformed(ActionEvent l){
                        try{
                            updateGUI.interrupt();
                        }
                        catch(Exception e){
                            JFrame error = new JFrame("Error");
                            JOptionPane.showMessageDialog(error, "Interrupt Exception", "Error", JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                        try{
                            pw.println(composeText.getText());
                            pw.flush();
                            composeText.setText("");
                        }
                        catch(Exception e){
                            JFrame error = new JFrame("Error");
                            JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                    }
                };
                
                send.addActionListener(sendListener);

                KeyAdapter enterKey = new KeyAdapter() {
                    public void keyPressed(KeyEvent k) {
                        if (k.getKeyCode() == KeyEvent.VK_ENTER){
                            try{
                                updateGUI.interrupt();
                            }
                            catch(Exception e){
                                JFrame error = new JFrame("Error");
                                JOptionPane.showMessageDialog(error, "Interrupt Exception", "Error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            try{
                                pw.println(composeText.getText());
                                pw.flush();
                            }
                            catch(Exception e){
                                JFrame error = new JFrame("Error");
                                JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                        }
                    }
                    public void keyReleased(KeyEvent k){
                        if (k.getKeyCode() == KeyEvent.VK_ENTER){
                            composeText.setText("");
                        }
                    }
                };

                composeText.addKeyListener(enterKey);
                
                updateGUI.start();

                disconnect.addActionListener(disconnectListener -> {
                    try{
                        exit = true;
                        pw.close();
                        in.close();
                        sock.close();
                        pw = null;
                        in = null;
                        sock = null;
                        updateGUI = null;
                        membersOnlineText.setText("");
                        messagesText.setText("");
                        // ipTextBox.setText("");
                        portTextBox.setText("");
                        connect.setVisible(true);
                        disconnect.setVisible(false);
                        exit = false;
                        composeText.removeKeyListener(enterKey);
                        send.removeActionListener(sendListener);
                        statuses.removeActionListener(statusListener);
                        
                        this.setTitle("GWack -- GW Slack Simulator - Disconnected");

    
                    }
                    catch(Exception e){
                        JFrame error = new JFrame("Error");
                        JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                });


            }
            catch(Exception e){
                JFrame error = new JFrame("Error");
                JOptionPane.showMessageDialog(error, "IO Exception", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
        });

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
