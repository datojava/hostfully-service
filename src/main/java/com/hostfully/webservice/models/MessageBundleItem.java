package com.hostfully.webservice.models;

import java.util.Map;

@SuppressWarnings("unused")
public class MessageBundleItem {

    private String languageCode;

    private Map<String, String> items;

    public MessageBundleItem() {

    }

    public MessageBundleItem(String languageCode, Map<String, String> items) {
        this.languageCode = languageCode;
        this.items = items;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }


}