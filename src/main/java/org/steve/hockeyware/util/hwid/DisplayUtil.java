package org.steve.hockeyware.util.hwid;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class DisplayUtil {

    public static void Display() {
        Frame frame = new Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Verification was unsuccessful!");
    }


    public static class Frame extends JFrame {
        public Frame() {
            this.setTitle("Verification failed.");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            copyToClipboard();
            String message = "Nice Try! : DM CatgirlHockey#0943 on Discord if you believe this is a mistake." + "\n" + "HWID: " + SystemUtil.getSystemInfo() + "\n(Copied to clipboard.)";
            JOptionPane.showMessageDialog(this, message, "Could not verify your HWID successfully.", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
        }

        public static void copyToClipboard() {
            StringSelection selection = new StringSelection(SystemUtil.getSystemInfo());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
}
