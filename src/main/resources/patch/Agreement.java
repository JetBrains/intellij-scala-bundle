// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.idea;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.impl.ApplicationInfoImpl;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.PathUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.SwingHelper;

import com.intellij.util.ThrowableRunnable;
import com.intellij.openapi.util.Ref;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.intellij.idea.AppExitCodes.*;
import static com.intellij.idea.StartupErrorReporter.showMessage;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public final class Agreement {
    public static void showBundleNoticeIfNeeded() {
        if (SystemInfo.isMac) {
            checkPaths();
        }
        runInEdtAndWait(() -> new Agreement().showBundleNotice());
    }

    private static void checkPaths() {
        if (PathManager.getSystemPath().contains("/AppTranslocation/")) {
            String message = "Please use Finder to move the application to another location before launching.\n\n" +
                    "Since v10.12 macOS launches downloaded applications as read-only.\n" +
                    "Moving application to any other location lifts that restriction.";
            showMessage("App Translocation detected", message, false);
            System.exit(DIR_CHECK_FAILED);
        }

        if (!new File(PathManager.getSystemPath()).canWrite()) {
            String message = "Please copy the application to your system before launching.\n\n" +
                    "The application needs to write data, while DMG images are read-only.\n" +
                    "Copying the program to Applications or to Desktop lifts the restriction.";
            showMessage("Read-only filesystem detected", message, false);
            System.exit(DIR_CHECK_FAILED);
        }
    }

    // copied from com.intellij.testFramework to avoid a suspicious import
    private static <T extends Throwable> void runInEdtAndWait(@NotNull ThrowableRunnable<T> runnable) throws T {
        final Application app = ApplicationManager.getApplication();
        if (app != null ? app.isDispatchThread()
                : SwingUtilities.isEventDispatchThread()) {
            // reduce stack trace
            runnable.run();
            return;
        }

        final Ref<T> exception = new Ref<>();
        final Runnable r = () -> {
            try {
                runnable.run();
            }
            catch (Throwable e) {
                //noinspection unchecked
                exception.set((T)e);
            }
        };

        if (app != null) {
            app.invokeAndWait(r);
        }
        else {
            try {
                SwingUtilities.invokeAndWait(r);
            }
            catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);  // must not happen
            }
        }

        if (!exception.isNull()) {
            throw exception.get();
        }
    }

    private void showBundleNotice() {
        String lib = new File(PathUtil.getJarPathForClass(getClass())).getParent();
        File textAgreement = new File(lib + "/../data/config/options/BundleAgreement.html");
        if (!textAgreement.exists()) System.out.println("BundleAgreement.html not found");
        File bundleAgreement = new File(lib + "/../data/config/options/bundleAgreement");

        if (!bundleAgreement.exists()) {
            try  {
                String text = new String(FileUtil.loadFileText(textAgreement));
                if (text.isEmpty()) {
                    throw new IOException("Cannot read from BundleAgreement.html");
                }

                showAgreementText("IntelliJ Scala Bundle Agreement", text);

                File optionsDirectory = bundleAgreement.getParentFile();

                if (!optionsDirectory.exists() && !optionsDirectory.mkdirs()) {
                    throw new IOException("Cannot create directory: " + bundleAgreement.getAbsolutePath());
                }

                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(bundleAgreement))) {
                    stream.write("accepted".getBytes(StandardCharsets.UTF_8));
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
                System.exit(INSTALLATION_CORRUPTED);
            }
        }
    }

    private static void showAgreementText(@NotNull String title, @NotNull String htmlText) {
        DialogWrapper dialog = new DialogWrapper(true) {
            private JEditorPane myViewer;

            @Override
            protected JComponent createCenterPanel() {
                JPanel centerPanel = new JPanel(new BorderLayout(0, JBUIScale.scale(8)));
                myViewer = SwingHelper.createHtmlViewer(true, null, JBColor.WHITE, JBColor.BLACK);
                myViewer.setFocusable(true);
                myViewer.addHyperlinkListener(new HyperlinkAdapter() {
                    @Override
                    protected void hyperlinkActivated(@NotNull HyperlinkEvent e) {
                        URL url = e.getURL();
                        if (url != null) {
                            BrowserUtil.browse(url);
                        }
                        else {
                            SwingHelper.scrollToReference(myViewer, e.getDescription());
                        }
                    }
                });
                myViewer.setText(htmlText);
                StyleSheet styleSheet = ((HTMLDocument)myViewer.getDocument()).getStyleSheet();
                styleSheet.addRule("body {font-family: \"Segoe UI\", Tahoma, sans-serif;}");
                styleSheet.addRule("body {margin-top:0;padding-top:0;}");
                styleSheet.addRule("body {font-size:" + JBUIScale.scaleFontSize((float)13) + "pt;}");
                styleSheet.addRule("h2, em {margin-top:" + JBUIScale.scaleFontSize((float)20) + "pt;}");
                styleSheet.addRule("h1, h2, h3, p, h4, em {margin-bottom:0;padding-bottom:0;}");
                styleSheet.addRule("p, h1 {margin-top:0;padding-top:" + JBUIScale.scaleFontSize((float)6) + "pt;}");
                styleSheet.addRule("li {margin-bottom:" + JBUIScale.scaleFontSize((float)6) + "pt;}");
                styleSheet.addRule("h2 {margin-top:0;padding-top:" + JBUIScale.scaleFontSize((float)13) + "pt;}");
                myViewer.setCaretPosition(0);
                myViewer.setBorder(JBUI.Borders.empty(0, 5, 5, 5));
                centerPanel.add(JBUI.Borders.emptyTop(8).wrap(
                        new JLabel("Please read and accept these terms and conditions. Scroll down for full text:")), BorderLayout.NORTH);
                JBScrollPane scrollPane = new JBScrollPane(myViewer, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
                centerPanel.add(scrollPane, BorderLayout.CENTER);
                JPanel bottomPanel = new JPanel(new BorderLayout());
                if (ApplicationInfoImpl.getShadowInstance().isEAP()) {
                    JPanel eapPanel = new JPanel(new BorderLayout(8, 8));
                    eapPanel.setBorder(JBUI.Borders.empty(8));
                    //noinspection UseJBColor
                    eapPanel.setBackground(new Color(0xDCE4E8));
                    JLabel label = new JLabel(AllIcons.General.BalloonInformation);
                    label.setVerticalAlignment(SwingConstants.TOP);
                    eapPanel.add(label, BorderLayout.WEST);
                    JEditorPane html = SwingHelper.createHtmlLabel(
                            "EAP builds report usage statistics by default per "+
                                    "the <a href=\"https://www.jetbrains.com/company/privacy.html\">JetBrains Privacy Policy</a>." +
                                    "<br/>No personal or sensitive data are sent. You may disable this in the settings.", null, null
                    );
                    eapPanel.add(html, BorderLayout.CENTER);
                    bottomPanel.add(eapPanel, BorderLayout.NORTH);
                }
                JCheckBox checkBox = new JCheckBox("I confirm that I have read and accept the terms of this User Agreement");
                bottomPanel.add(JBUI.Borders.empty(24, 0, 16, 0).wrap(checkBox), BorderLayout.CENTER);
                centerPanel.add(JBUI.Borders.emptyTop(8).wrap(bottomPanel), BorderLayout.SOUTH);
                checkBox.addActionListener(e -> setOKActionEnabled(checkBox.isSelected()));
                centerPanel.setPreferredSize(JBUI.size(520, 450));
                return centerPanel;
            }

            @Nullable
            @Override
            public JComponent getPreferredFocusedComponent() {
                return myViewer;
            }

            @Override
            protected void createDefaultActions() {
                super.createDefaultActions();
                init();
                setOKButtonText("Continue");
                setOKActionEnabled(false);
                setCancelButtonText("Reject and Exit");
                setAutoAdjustable(false);
            }

            @Override
            public void doCancelAction() {
                super.doCancelAction();
                Application application = ApplicationManager.getApplication();
                if (application == null) {
                    System.exit(PRIVACY_POLICY_REJECTION);
                }
                else {
                    application.exit(true, true, false);
                }
            }
        };
        dialog.setModal(true);
        dialog.setTitle(title);
        dialog.pack();

        SplashManager.executeWithHiddenSplash(dialog.getWindow(), () -> dialog.show());
    }
}
