package com.hostfully.webservice.utils;

import com.hostfully.webservice.constants.HostfullyConstants;
import com.hostfully.webservice.models.MessageBundleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class MessageBundleReader {

    private static volatile MessageBundleReader instance = null;

    private final Logger log = LoggerFactory.getLogger(MessageBundleReader.class);

    private final String[] messageFiles = new String[]{"messages_en.properties"};

    private final Map<String, MessageBundleItem> bundlesMap = new ConcurrentHashMap<>();


    private MessageBundleReader() {
        load();
    }

    public static MessageBundleReader getInstance() {
        if (instance == null) {
            synchronized (MessageBundleReader.class) {
                if (instance == null) {
                    instance = new MessageBundleReader();
                }
            }
        }

        return instance;
    }

    private void load() {

        try {
            log.info("Loading messages");

            Properties deviceInfoProps = new Properties();


            MessageBundleItem bundleItem;

            Map<String, String> items = new HashMap<>();

            for (String fileName : messageFiles) {
                deviceInfoProps.load(this.getClass().getClassLoader().getResourceAsStream(fileName));

                bundleItem = new MessageBundleItem(CommonUtils.extractLanguageCode(fileName), items);

                for (Map.Entry<Object, Object> e : deviceInfoProps.entrySet()) {
                    bundleItem.getItems().put(e.getKey().toString(), e.getValue().toString());
                }

                bundlesMap.put(bundleItem.getLanguageCode(), bundleItem);
            }

            log.info("Messages loaded");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

    }

    public String getMessage(String key) {
        return bundlesMap.get(HostfullyConstants.DEFAULT_LANG_CODE).getItems().get(key);
    }

    public String getMessage(String key, String langCode) {
        if (bundlesMap.containsKey(langCode)) {
            return bundlesMap.get(langCode).getItems().get(key);
        } else {
            return null;
        }
    }

}
