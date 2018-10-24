/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.JFrame;
import utilities.testclient.ServerConnectionTest;

/**
 *
 * @author Demo
 */
public class ClientInstanceTest extends JFrame
{
    private ServerConnectionTest testClient;
    private Consumer<ClientInstanceTest> closeFunction;
    private int id;
    
    /**
     * Creates new form ClientInstanceTestView
     * @param number
     * @param closeFunction
     */
    public ClientInstanceTest(int number, Consumer<ClientInstanceTest> closeFunction)
    {
        super("Client Session # " + (number + 1));
        initComponents();
        this.setResizable(false);
        try {
            this.testClient = new ServerConnectionTest(this::displayOutput,
                    this::displayError);
            id = number;
            this.closeFunction = closeFunction;
            testClient.start();
        } catch (IOException error) {
            displayError(error.getMessage());
            if (testClient != null) testClient.stop();
            testClient = null;
        }
    }

    public int getID() {
        return id;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        userInput = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        displayArea.setColumns(20);
        displayArea.setLineWrap(true);
        displayArea.setRows(5);
        displayArea.setFocusable(false);
        jScrollPane1.setViewportView(displayArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                sendButtonActionPerformed(evt);
            }
        });

        userInput.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                userInputKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userInput)
                .addGap(18, 18, 18)
                .addComponent(sendButton)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(userInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userInputKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_userInputKeyTyped
    {//GEN-HEADEREND:event_userInputKeyTyped
        if (evt.getKeyChar() == '\n') handleInput();
    }//GEN-LAST:event_userInputKeyTyped

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sendButtonActionPerformed
    {//GEN-HEADEREND:event_sendButtonActionPerformed
        handleInput();
    }//GEN-LAST:event_sendButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        if (testClient != null) {
            testClient.stop();
            closeFunction.accept(this);
        }
    }//GEN-LAST:event_formWindowClosing

    public void closeWindow() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private void handleInput() {
        String cmd = userInput.getText();
        userInput.setText("");
        if (cmd.equalsIgnoreCase("exit") || cmd.equalsIgnoreCase("quit")) {
            closeWindow();
        } else {
            testClient.sendData(cmd);
        }
    }
    
    private void displayOutput(String msg) {
        displayArea.append(msg + "\n");
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }
    
    private void displayError(String err) {
        displayOutput("ERROR: " + err);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea displayArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField userInput;
    // End of variables declaration//GEN-END:variables
}