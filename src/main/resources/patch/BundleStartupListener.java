// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.AppUIUtil;
import com.intellij.util.PathUtil;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.intellij.ui.AppUIUtil.showEndUserAgreementText;

public class BundleStartupListener implements StartupListener {
  @Override
  public void before(StartupPhase phase) {
    switch (phase) {
      case FOLDERS_CHECK:
        checkPaths();
        break;
      case USER_AGREEMENT:
        showBundleNoticeIfNeeded();
        break;
      default:
    }
  }

  private static void checkPaths() {
    if (SystemInfo.isMac && PathManager.getSystemPath().contains("/AppTranslocation/")) {
      String message = "Please use Finder to move the application to another location before launching.\n\n" +
                       "Since v10.12 macOS launches downloaded applications as read-only.\n" +
                       "Moving application to any other location lifts that restriction.";
      Main.showMessage("App Translocation detected", message, false);
      System.exit(Main.DIR_CHECK_FAILED);
    }

    if (SystemInfo.isMac && !new File(PathManager.getSystemPath()).canWrite()) {
      String message = "Please copy the application to your system before launching.\n\n" +
                       "The application needs to write data, while DMG images are read-only.\n" +
                       "Copying the program to Applications or to Desktop lifts the restriction.";
      Main.showMessage("Read-only filesystem detected", message, false);
      System.exit(Main.DIR_CHECK_FAILED);
    }
  }

  private void showBundleNoticeIfNeeded() {
    String lib = new File(PathUtil.getJarPathForClass(getClass())).getParent();
    File bundleAgreement = new File(lib + "/../data/config/options/bundleAgreement");

    if (!bundleAgreement.exists()) {
      try (Reader reader = new InputStreamReader(new BufferedInputStream(
              getClass().getResourceAsStream("BundleAgreement.html")), StandardCharsets.UTF_8)) {

        String text = new String(FileUtil.adaptiveLoadText(reader));
        showEndUserAgreementText("IntelliJ Scala Bundle Agreement", text, false);

        File optionsDirectory = bundleAgreement.getParentFile();

        if (!optionsDirectory.exists() && !optionsDirectory.mkdirs()) {
          throw new IOException("Cannot create directory: " + bundleAgreement.getAbsolutePath());
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(bundleAgreement))) {
          stream.write("accepted".getBytes(StandardCharsets.UTF_8));
        }
      }
      catch (Exception e) {
        //noinspection CallToPrintStackTrace
        e.printStackTrace();
        System.exit(Main.INSTALLATION_CORRUPTED);
      }
    }
  }
}
