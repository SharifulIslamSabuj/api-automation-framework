package com.grocery.store.api.utils;

import io.qameta.allure.Allure;

public class AllureAttachmentUtil {

    private AllureAttachmentUtil() {}

    public static void attachRequest(String name, String content) {
        Allure.addAttachment("REQUEST - " + name, content);
    }

    public static void attachResponse(String name, String content) {
        Allure.addAttachment("RESPONSE - " + name, content);
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, content);
    }
}