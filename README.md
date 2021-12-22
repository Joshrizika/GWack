# GWack Slack Simulator

This slack simulator uses two programs.  One that creates a Graphical User Interface and is able to connect it to a server, and one that hosts the server that the GUI connects to.  The GUI utilizes sockets to connect to the hosted server, and inputs and outputs information using a BufferedReader object and a PrintWriter object.  To use the GUI, the user must first host a server.  To do this the command below must be entered...

```
java GWackChannel PortNumber
```

The PortNumber should be expressed as an integer.  A working example would be something like

```
java GWackChannel 2021
```

After this the user would launch the GUI with the command...
 
 ```
 java GWackClientGUI
 ```
 
The user will then enter a username for themself, the ip address (in this case assuming the server was hosted from the users own computer, they would use localhost) and the port number that they would like to connect to, then they would click connect.  A user list would appear on the left that will continuously be updated as users join and disconnect from the server, all messages sent since connection will appear in the messages box in the center, and the box on the bottom is editable and its contents will be sent as a message when the send button or the enter key are used.  On the bottom there is also a ComboBox to change the theme of the GUI which changes the color scheme, and a ComboBox that allows the user to select a status update which when changed will send an update to the server.  

The Server can also be accessed from a terminal using netcat.  The process to do this on a mac is shown below...

```
nc ipAddress port
NAME
username
```

The command nc ipAddress port takes the ipAddress and the port number and inputs them in place of the labels above.  And the username is replaced by the users inputted username.  An example of what this would look like would be something like this...

```
nc localhost 2021
NAME
joshrizika
```

A more in-depth description of the code written in each program is shown below.  

### GWackClientGUI.java

First the GUI was created, I used border layouts to place many panels inside of each other to place JButtons, JTextFields, JTextAreas, JComboBoxes, JScrollPanes, and JLabels.  Once i had them fashioned into a working GUI, i added an action listener for the connect button.  Inside this action listener i created a socket with the inputted address and port in the GUI.  Then i created a PrintWriter that i used to input information on the GUI.  Then i did some edits to the GUI's appearance so that it looked connected.  I made sure that all actionListeners from previous connections.  I created a BufferedReader and then i created a new thread that would read in data with the Buffered Reader.  

When the thread was started it would enter a while loop that would run as long as the disconnect button hadn't been clicked.  Inside this while loop i would check if there was a line to read in, if there was no new lines then it would skip this iteration of the while loop and move to the next one, but if there was a new line then it owuld read it in and text it to see if it was the start of a client list or if it was a message.  If it was a client list it would take all the names, append them to a string and output it to the members online textarea, if it was a message then it would append it to a list of messages that had been sent since connection.  

I added another action listener on the send button, if the send button was pressed then i would interupt the thread and use printwriter to send a message to the server and then it would erase the text written into the compose message box.  I also created a KeyAdapter by implementing the KeyListener interface and importing its methods.  Then i used the keyPressed method and the keyReleased method to do the same process if the enter key were to be pressed. 

To implement themes i created a JComboBox that contained a list of all the themes i had.  Then i created an action listener for that combo box.  Inside the action listener i would get the string value of the selected theme in the ComboBox.  Depending on what that string value is i would set two color variables, the primary color and the secondary color.  Then i would go through each panel, text box, and peiece of text, including the insertion point, and change them to either the first or secondary color.  

Status updates were added by creating a Jcombobox that has an action listener attatched to it.  When a specific status is selected in the box, the action listener outputs a string that says "--username is (status)".  It does this by interupting by the thread and using print writer to output the string to the server.  

Lastly i had an action listener that listened to the Disconnect button, if this button were clicked then it would set a boolean value to false that would end the thread, then it would close the PrintWriter, BufferedReader, and Socket and set their values all back to 0.  It cleared text from many of the text boxes and removed all the listeners it could so that it would not be overwritten.

I also included many Try-Catches so that if an error were to occur then i could send an output based on what that error were to be.  I created JOptionPanes that would display an error box, once the user okayed that error then the system would close the GUI.  

### GWackChannel.java

First I created a main method that created an instance of the GWackChannel Class.  In the constructor i created a ServerSocket that used a port number that was given as an argument when running the class.  The main method then called the serve function which would first accept all new connections to the server Socket and then created a Socket and a ClientHandler for each new client.  That ClientHandler was added to a LinkedList of ClientHandlers and then it called the ClientHandlers start function.  The class ClientHandler was an extention of Thread so all ClientHandlers are created in their own thread.  The Client Handlers socket is passed into it as an argument and then a PrintWriter and a BufferedReader are created.  Using the BufferedReader, we read each new line. To give this ClientHandler a name, we want it entered in this format...

```
NAME
username
```

After this we set username as the ClientHandlers name and then using a for loop we use print word to print this to every current ClientHandlers Socket...

```
START_CLIENT_LIST
name1
name2
name3
...
END_CLIENT_LIST
```

each name in the list of ClientHandlers is printed out to each ClientHandler.  

After this the client is fully connected to the server.  A while loop is constantly running in each ClientHandler instance and is waiting for an input.  If an input is given then it is saved to a variable. If that input is a null message it means that a ClientHandler accessing the server from a terminal just disconnected using ^C so if we detect a null then we break out of the for loop, we also test if the thread is still alive to see if the ClientHandlers using the GWackClientGUI have disconnected, if they have then we also break out of the while loop.  But if the message is just a normal message then we output it in this format...

```
[name] message
```

If we break out of the while loop then we go on to close the ClientHandler.  We go through each ClientHandler in the list and again print out a client list starting with START_CLIENT_LIST and ending with END_CLIENT_LIST.  The only difference being if the username is equal to the username of the client disconnecting, i instead remove that ClientHandler from the list. 

Lastly i close the PrintWriter, the BufferedReader, and the Socket.  

