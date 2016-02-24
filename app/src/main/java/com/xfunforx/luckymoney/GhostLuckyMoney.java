package com.xfunforx.luckymoney;

import java.io.*;
import android.net.*;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import org.xmlpull.v1.*;

public class GhostLuckyMoney implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.contains("tencent.mm")) {
            return;
        }
        try {
            Class b = XposedHelpers.findClass("com.tencent.mm.booter.notification.b", loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod("com.tencent.mm.booter.notification.b", loadPackageParam.classLoader, "a", b, String.class, String.class, Integer.TYPE, Integer.TYPE, Boolean.TYPE, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    if (!methodHookParam.args[3].toString().equals(String.valueOf(436207665L))) {
                        return;
                    }
                    final String string = methodHookParam.args[2].toString();
                    final String substring = string.substring(string.indexOf("<msg>"));
                    String queryParameter = "";
                    final String m = "nativeurl";
                    while (true) {
                        try {
                            final XmlPullParserFactory instance = XmlPullParserFactory.newInstance();
                            instance.setNamespaceAware(true);
                            final XmlPullParser pullParser = instance.newPullParser();
                            pullParser.setInput(new StringReader(substring));
                            int n = pullParser.getEventType();
                            String text;
                            while (true) {
                                text = queryParameter;
                                if (n == 1) {
                                    break;
                                }
                                if (n == 2 && pullParser.getName().equals(m)) {
                                    pullParser.nextToken();
                                    text = pullParser.getText();
                                    break;
                                }
                                n = pullParser.next();
                            }
                            final Uri parse = Uri.parse(text);
                            queryParameter = parse.getQueryParameter("msgtype");
                            final String queryParameter2 = parse.getQueryParameter("sendid");
                            final String queryParameter3 = parse.getQueryParameter("channelid");
                            Class ab = XposedHelpers.findClass("com.tencent.mm.plugin.luckymoney.c.ab", loadPackageParam.classLoader);
                            Class ah = XposedHelpers.findClass("com.tencent.mm.model.ah", loadPackageParam.classLoader);
                            XposedHelpers.callMethod(XposedHelpers.callStaticMethod(ah, "tE"), "d", XposedHelpers.newInstance(ab, Integer.valueOf(queryParameter), Integer.valueOf(queryParameter3), queryParameter2, text, "", "", methodHookParam.args[1].toString(), "v1.0"));
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            final String text = queryParameter;
                            continue;
                        }
                        break;
                    }
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
