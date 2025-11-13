package chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MulticastChatGUI {
    private static final String DEFAULT_GROUP = "230.0.0.1";
    private static final int DEFAULT_PORT = 4446;

    private JFrame frame;
    private JPanel chatPanel;
    private JScrollPane chatScroll;
    private JTextField messageField;
    private JButton sendBtn, joinBtn, leaveBtn, themeBtn, kickBtn;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JLabel typingLabel;

    private MulticastSocket socket;
    private InetAddress group;
    private Receiver receiver;
    private Sender sender;
    private LinkedBlockingQueue<String> sendQueue;

    private String username;
    private String adminUser;
    private boolean darkMode = false;
    private final Color bgColorLight = new Color(245, 247, 250);
    private final Color bgColorDark = new Color(34, 34, 34);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private Timer typingTimer;
    private boolean isTyping = false;

    public MulticastChatGUI() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("💬 Multicast Chat");
        frame.setSize(950, 650);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // ===== TOP PANEL =====
        JPanel top = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(33, 150, 243),
                        getWidth(), getHeight(), new Color(156, 39, 176));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.setBorder(new EmptyBorder(6, 10, 6, 10));

        JLabel lblTitle = new JLabel("⚡ UDP Multicast Chat");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JTextField txtGroup = new JTextField(DEFAULT_GROUP, 12);
        txtGroup.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JTextField txtPort = new JTextField(String.valueOf(DEFAULT_PORT), 6);
        txtPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JTextField txtName = new JTextField("User" + (int) (Math.random() * 1000), 10);
        txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));

        themeBtn = styledBtn("🌙 Dark", Color.WHITE, new Color(25, 118, 210));

        top.add(lblTitle);
        top.add(new JLabel("Group:"));
        top.add(txtGroup);
        top.add(new JLabel("Port:"));
        top.add(txtPort);
        top.add(new JLabel("Name:"));
        top.add(txtName);
        top.add(themeBtn);

        frame.add(top, BorderLayout.NORTH);

        // ===== CHAT AREA =====
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(bgColorLight);

        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100, 120);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0,0));
                btn.setMinimumSize(new Dimension(0,0));
                btn.setMaximumSize(new Dimension(0,0));
                return btn;
            }
        });

        // User list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Tahoma", Font.PLAIN, 13));
        userList.setBorder(BorderFactory.createTitledBorder("👥 Người dùng Online"));
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(200, 0));

        // Kick button
        kickBtn = styledBtn("Kick", Color.WHITE, new Color(244, 67, 54));
        kickBtn.setEnabled(false);
        JPanel userBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        userBtnPanel.setOpaque(false);
        userBtnPanel.add(kickBtn);

        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.add(userScroll, BorderLayout.CENTER);
        userListPanel.add(userBtnPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScroll, userListPanel);
        split.setDividerLocation(720);
        frame.add(split, BorderLayout.CENTER);

        // ===== BOTTOM INPUT =====
        JPanel bottom = new JPanel(new BorderLayout(10, 5));
        bottom.setBorder(new EmptyBorder(10, 10, 10, 10));

        typingLabel = new JLabel(" ");
        typingLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        typingLabel.setForeground(Color.GRAY);
        bottom.add(typingLabel, BorderLayout.NORTH);

        messageField = new JTextField();
        messageField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        sendBtn = styledBtn("➤ Gửi", Color.WHITE, new Color(33, 150, 243));
        joinBtn = styledBtn("Tham gia", Color.WHITE, new Color(76, 175, 80));
        leaveBtn = styledBtn("Rời", Color.WHITE, new Color(244, 67, 54));
        leaveBtn.setEnabled(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttons.setOpaque(false);
        buttons.add(joinBtn);
        buttons.add(leaveBtn);
        buttons.add(sendBtn);

        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.EAST);
        frame.add(bottom, BorderLayout.SOUTH);

        sendQueue = new LinkedBlockingQueue<>();

        // ===== ACTIONS =====
        sendBtn.addActionListener(e -> sendMsg());
        messageField.addActionListener(e -> sendMsg());

        messageField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateTyping() {
                if(!isTyping) {
                    isTyping = true;
                    if(sender != null) sendQueue.offer("[TYPING] " + username);
                }
                if(typingTimer != null) typingTimer.stop();
                typingTimer = new Timer(1000, e -> {
                    isTyping = false;
                    if(sender != null) sendQueue.offer("[STOP_TYPING] " + username);
                });
                typingTimer.setRepeats(false);
                typingTimer.start();
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTyping(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTyping(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTyping(); }
        });

        joinBtn.addActionListener(e -> {
            username = txtName.getText().trim();
            if (username.isEmpty()) username = "User";
            adminUser = username;
            try {
                int port = Integer.parseInt(txtPort.getText().trim());
                joinGroup(txtGroup.getText().trim(), port);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Lỗi tham gia: " + ex.getMessage());
            }
        });

        leaveBtn.addActionListener(e -> leaveGroup());
        themeBtn.addActionListener(e -> toggleTheme());

        kickBtn.addActionListener(e -> {
            List<String> selectedUsers = userList.getSelectedValuesList();
            for (String targetUser : selectedUsers) {
                if (!targetUser.equals(username)) {
                    sendQueue.offer("[LEAVE] " + targetUser);
                    addTelegramStyleSystemMessageWithAvatar(targetUser, false);
                }
            }
        });

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userList.getSelectedIndex() >= 0) {
                boolean enable = false;
                for (String u : userList.getSelectedValuesList()) {
                    if (!u.equals(username)) enable = true;
                }
                kickBtn.setEnabled(enable && username.equals(adminUser));
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { safeShutdown(); }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton styledBtn(String text, Color fg, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ===== Toggle Dark/Light Mode chỉ đổi chatPanel =====
    private void toggleTheme() {
        darkMode = !darkMode;
        chatPanel.setBackground(darkMode ? bgColorDark : bgColorLight);

        // Cập nhật màu chữ của tin nhắn hiện có
        for (Component comp : chatPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel wrapper = (JPanel) comp;
                for (Component inner : wrapper.getComponents()) {
                    if (inner instanceof JPanel) {
                        JPanel bubble = (JPanel) inner;
                        for (Component lblComp : bubble.getComponents()) {
                            if (lblComp instanceof JLabel) {
                                JLabel lbl = (JLabel) lblComp;
                                if (!lbl.getText().contains(username + ":")) { 
                                    lbl.setForeground(Color.DARK_GRAY);
                                } else {
                                    lbl.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                                }
                            }
                        }
                    }
                }
            }
        }

        chatPanel.revalidate();
        chatPanel.repaint();

        themeBtn.setText(darkMode ? "☀ Light" : "🌙 Dark");
    }

    private void sendMsg() {
        String text = messageField.getText().trim();
        if(text.isEmpty()) return;
        String time = "[" + timeFormat.format(new Date()) + "]";
        String full = username + ": " + text + " " + time;

        if(text.startsWith("@")) {
            int space = text.indexOf(' ');
            if(space > 0) {
                String targetUser = text.substring(1, space);
                full = "[PM đến " + targetUser + "] " + full;
            }
        }
        if(sender != null) sendQueue.offer(full);
        messageField.setText("");
        isTyping = false;
        if(sender != null) sendQueue.offer("[STOP_TYPING] " + username);
    }

    private void joinGroup(String gAddr, int port) throws Exception {
        group = InetAddress.getByName(gAddr);
        socket = new MulticastSocket(port);
        socket.joinGroup(group);

        receiver = new Receiver(socket, msg -> SwingUtilities.invokeLater(() -> {
            if(msg.startsWith("[JOIN] ")) {
                String user = msg.substring(7);
                if(!userListModel.contains(user)) userListModel.addElement(user);
                addTelegramStyleSystemMessageWithAvatar(user, true);
            } else if(msg.startsWith("[LEAVE] ")) {
                String user = msg.substring(8);
                userListModel.removeElement(user);
                addTelegramStyleSystemMessageWithAvatar(user, false);
                if(user.equals(username)) {
                    JOptionPane.showMessageDialog(frame, "⚠ Bạn đã bị kick khỏi nhóm!", "Bị kick", JOptionPane.WARNING_MESSAGE);
                    safeShutdown();
                }
                if(user.equals(adminUser) && !userListModel.isEmpty()) {
                    adminUser = userListModel.getElementAt(0);
                    addTelegramStyleSystemMessage("🛡 " + adminUser + " đã trở thành Admin mới", true);
                }
            } else if(msg.startsWith("[TYPING] ")) {
                String user = msg.substring(9);
                if(!user.equals(username)) typingLabel.setText(user + " đang gõ...");
            } else if(msg.startsWith("[STOP_TYPING] ")) {
                String user = msg.substring(14);
                if(!user.equals(username)) typingLabel.setText(" ");
            } else {
                String senderName = msg.split(":")[0];
                boolean isSelf = msg.contains(username + ":");
                addMessage(msg, isSelf, senderName);
            }
        }));
        receiver.start();

        sender = new Sender(socket, group, port, sendQueue);
        sender.start();
        sendQueue.offer("[JOIN] " + username);
        addTelegramStyleSystemMessageWithAvatar(username, true);

        joinBtn.setEnabled(false);
        leaveBtn.setEnabled(true);
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

    private JLabel createAvatar(String username) {
        String firstLetter = username.substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(firstLetter, SwingConstants.CENTER);
        avatar.setFont(new Font("Tahoma", Font.BOLD, 16));
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(33, 150, 243));
        avatar.setForeground(Color.WHITE);
        avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        return avatar;
    }

    private void addMessage(String msg, boolean isSelf, String senderName) {
        JPanel bubbleWrapper = new JPanel(new FlowLayout(isSelf ? FlowLayout.RIGHT : FlowLayout.LEFT));
        bubbleWrapper.setOpaque(false);

        JPanel bubble = new JPanel(new BorderLayout(8, 0));
        bubble.setOpaque(false);

        JLabel lbl = new JLabel("<html><body style='width:240px;'>" + msg + "</body></html>");
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lbl.setBorder(new EmptyBorder(8, 12, 4, 12));
        lbl.setForeground(isSelf ? Color.WHITE : (darkMode ? Color.WHITE : Color.BLACK));

        JPanel chatBubble = new JPanel(new BorderLayout());
        chatBubble.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        chatBubble.setBackground(isSelf ? new Color(33,150,243) : new Color(230,230,230));
        chatBubble.setBorder(BorderFactory.createLineBorder(new Color(200,200,200),1,true));
        chatBubble.add(lbl, BorderLayout.CENTER);

        JLabel timeLbl = new JLabel(timeFormat.format(new Date()));
        timeLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
        timeLbl.setForeground(Color.GRAY);
        chatBubble.add(timeLbl, BorderLayout.SOUTH);

        if(isSelf) {
            bubble.add(chatBubble, BorderLayout.EAST);
        } else {
            bubble.add(createAvatar(senderName), BorderLayout.WEST);
            bubble.add(chatBubble, BorderLayout.CENTER);
        }

        bubbleWrapper.add(bubble);
        chatPanel.add(bubbleWrapper);
        chatPanel.add(Box.createVerticalStrut(6));

        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() -> chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum()));
    }

    private void addTelegramStyleSystemMessageWithAvatar(String username, boolean isJoin) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);

        JPanel bubble = new JPanel();
        bubble.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 2));
        bubble.setOpaque(true);
        bubble.setBackground(isJoin ? new Color(200, 230, 201) : new Color(255, 205, 210));
        bubble.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JLabel avatar = createAvatar(username);
        avatar.setPreferredSize(new Dimension(24,24));
        JLabel lbl = new JLabel(isJoin ? "đã tham gia nhóm" : "đã rời nhóm");
        lbl.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lbl.setForeground(Color.DARK_GRAY);

        bubble.add(avatar);
        bubble.add(lbl);
        wrapper.add(bubble);

        chatPanel.add(wrapper);
        chatPanel.add(Box.createVerticalStrut(6));
        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() ->
            chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum())
        );
    }

    private void addTelegramStyleSystemMessage(String msg, boolean isJoin) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);

        JPanel bubble = new JPanel();
        bubble.setLayout(new FlowLayout(FlowLayout.CENTER));
        bubble.setOpaque(true);
        bubble.setBackground(isJoin ? new Color(200, 230, 201) : new Color(255, 205, 210));
        bubble.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lbl.setForeground(Color.DARK_GRAY);

        bubble.add(lbl);
        wrapper.add(bubble);

        chatPanel.add(wrapper);
        chatPanel.add(Box.createVerticalStrut(6));
        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() ->
                chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum())
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MulticastChatGUI::new);
    }
}
