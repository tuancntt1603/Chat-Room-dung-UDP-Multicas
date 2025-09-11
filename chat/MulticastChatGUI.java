package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MulticastChatGUI {
    private static final String DEFAULT_GROUP = "230.0.0.1";
    private static final int DEFAULT_PORT = 4446;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendBtn, joinBtn, leaveBtn;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;

    private MulticastSocket socket;
    private InetAddress group;
    private Receiver receiver;
    private Sender sender;
    private LinkedBlockingQueue<String> sendQueue;

    private String username;

    public MulticastChatGUI() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Multicast Chat - UDP (GUI)");
        frame.setSize(700, 480);
        frame.setLayout(new BorderLayout(8, 8));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Top panel (cấu hình)
        JPanel top = new JPanel(new FlowLayout());
        JTextField txtGroup = new JTextField(DEFAULT_GROUP, 12);
        JTextField txtPort = new JTextField(String.valueOf(DEFAULT_PORT), 6);
        JTextField txtName = new JTextField("User" + (int)(Math.random()*1000), 10);
        top.add(new JLabel("Group:")); top.add(txtGroup);
        top.add(new JLabel("Port:"));  top.add(txtPort);
        top.add(new JLabel("Name:"));  top.add(txtName);
        frame.add(top, BorderLayout.NORTH);

        // Chat area
        chatArea = new JTextArea(); chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        // User list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150,0));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScroll, userScroll);
        split.setDividerLocation(520);
        frame.add(split, BorderLayout.CENTER);

        // Bottom input
        JPanel bottom = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendBtn = new JButton("Send");
        joinBtn = new JButton("Join");
        leaveBtn = new JButton("Leave"); leaveBtn.setEnabled(false);
        JPanel buttons = new JPanel();
        buttons.add(joinBtn); buttons.add(leaveBtn); buttons.add(sendBtn);

        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.EAST);
        frame.add(bottom, BorderLayout.SOUTH);

        sendQueue = new LinkedBlockingQueue<>();

        // Action: send message
        sendBtn.addActionListener(e -> sendMsg());
        messageField.addActionListener(e -> sendMsg());

        // Action: join group
        joinBtn.addActionListener(e -> {
            username = txtName.getText().trim();
            if(username.isEmpty()) username = "User";
            try {
                int port = Integer.parseInt(txtPort.getText().trim());
                joinGroup(txtGroup.getText().trim(), port);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Lỗi join: " + ex.getMessage());
            }
        });

        // Action: leave group
        leaveBtn.addActionListener(e -> leaveGroup());

        // Window closing
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                safeShutdown();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMsg() {
        String text = messageField.getText().trim();
        if(text.isEmpty()) return;
        String full = username + ": " + text;
        if(sender != null) sendQueue.offer(full);
        messageField.setText("");
    }

    private void joinGroup(String gAddr, int port) throws Exception {
        group = InetAddress.getByName(gAddr);
        socket = new MulticastSocket(port);
        socket.joinGroup(group);

        receiver = new Receiver(socket, msg -> {
            chatArea.append(msg + "\n");
            if(msg.startsWith("[JOIN] ")) userListModel.addElement(msg.substring(7));
            else if(msg.startsWith("[LEAVE] ")) userListModel.removeElement(msg.substring(8));
        });
        receiver.start();

        sender = new Sender(socket, group, port, sendQueue);
        sender.start();

        sendQueue.offer("[JOIN] " + username);
        chatArea.append("[SYSTEM] Joined " + gAddr + ":" + port + "\n");

        joinBtn.setEnabled(false); leaveBtn.setEnabled(true);
    }

    private void leaveGroup() {
        if(sender != null) sendQueue.offer("[LEAVE] " + username);
        safeShutdown();
    }

    private void safeShutdown() {
        try { if(sender!=null){ sender.shutdown(); sender.join(200);} } catch(Exception ignored){}
        try { if(receiver!=null){ receiver.shutdown(); receiver.join(200);} } catch(Exception ignored){}
        try { if(socket!=null && group!=null) socket.leaveGroup(group); } catch(Exception ignored){}
        try { if(socket!=null) socket.close(); } catch(Exception ignored){}
        frame.dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MulticastChatGUI::new);
    }
}
