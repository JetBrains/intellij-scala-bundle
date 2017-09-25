'SLDF_HAS_RELPATH

Set shell = WScript.CreateObject("WScript.Shell")
Set shortcut = shell.CreateShortcut("IDEA.lnk")

shortcut.TargetPath = "%WINDIR%\explorer.exe bin\idea.exe"
shortcut.WorkingDirectory = "%CD%\bin"
shortcut.Description = "IntelliJ IDEA"
shortcut.IconLocation = "bin\idea.exe, 0"
shortcut.Save()