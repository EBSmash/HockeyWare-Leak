package org.steve.hockeyware.manager;

import org.steve.hockeyware.util.hwid.DisplayUtil;
import org.steve.hockeyware.util.hwid.NoStackTraceThrowable;
import org.steve.hockeyware.util.hwid.SystemUtil;
import org.steve.hockeyware.util.hwid.URLReader;

import java.util.ArrayList;
import java.util.List;

public class HWIDManager {


    public static final String pastebinURL = "https://pastebin.com/raw/iffQ1n6k";
    //

    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(SystemUtil.getSystemInfo());
        if (!isHwidPresent) {
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("joe");
        }
    }
}
