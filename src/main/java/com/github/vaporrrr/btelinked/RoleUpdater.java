package com.github.vaporrrr.btelinked;

import com.github.vaporrrr.btelinked.util.WebsiteUtil;
import de.leonhard.storage.Config;

import java.util.concurrent.TimeUnit;

public class RoleUpdater extends Thread {
    @Override
    public void run() {
        try {
            while (!interrupted()) {
                Config config = BTELinked.config();
                long interval = config.getLong("WebsiteDiscordLink.IntervalInMins");
                WebsiteUtil.linkWebsiteToDiscord();
                Thread.sleep(TimeUnit.MINUTES.toMillis(interval));
            }
        } catch (InterruptedException ignored) {
        }
    }
}
