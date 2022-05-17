package com.github.vaporrrr.btelinked.util;

import com.github.vaporrrr.btelinked.BTELinked;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebsiteUtil {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

    private static JSONObject getRequest(String endpoint) {
        try {
            String URL = "https://buildtheearth.net/" + endpoint;
            java.net.URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + BTELinked.config().getString("BTEAPIKey"));
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            if (code < 200 || code > 299) {
                BTELinked.warn("Request to https://buildtheearth.net/" + endpoint + " not successful. Response code: " + code);
                if (code == 401) {
                    BTELinked.warn("Invalid API key");
                } else if (code == 404) {
                    BTELinked.warn(endpoint + " endpoint not found");
                }
                return null;
            }
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return new JSONObject(response.toString());
        } catch (Exception e) {
            BTELinked.severe("Unexpected exception making GET request to https://buildtheearth.net/" + endpoint);
            e.printStackTrace();
            return null;
        }
    }

    private static JSONArray getWebsiteMembers() {
        JSONObject request = getRequest("website_members");
        if (request == null) return null;
        return request.getJSONArray("members");
    }

    public static void linkWebsiteToDiscord() {
        JSONArray members = getWebsiteMembers();
        if (members == null) {
            BTELinked.warn("Could not get member list");
            return;
        }
        JDA jda = DiscordUtil.getJda();
        String guildID = BTELinked.config().getString("WebsiteDiscordLink.GuildID");
        Guild guild = jda.getGuildById(guildID);
        if (guild == null) {
            BTELinked.warn("Could not get Discord guild from " + guildID);
            return;
        }
        for (int i = 0; i < members.length(); i++) {
            JSONObject webMember = members.getJSONObject(i);
            String discordID = webMember.getString("discordId");
            String discordTag = webMember.getString("discordTag");
            Member member = guild.getMemberById(discordID);
            if (member == null) {
                BTELinked.warn("Could not get Discord member " + discordTag + "/" + discordID + " from Discord guild " + guild.getName());
            } else {
                String matchingDiscordRoleID = (String) BTELinked.config().getMap("WebsiteDiscordLink.Roles").get(webMember.getString("role"));
                if (matchingDiscordRoleID != null) {
                    Role role = guild.getRoleById(matchingDiscordRoleID);
                    if (role != null) {
                        try {
                            guild.addRoleToMember(member, role).queue();
                            BTELinked.info("Added " + role.getName() + " to " + member.getUser().getAsTag() + "/" + discordID);
                        } catch (Exception e) {
                            BTELinked.warn("Exception occurred adding Discord role " + role.getName() + " to " + member.getUser().getAsTag() + "/" + discordID + " in Discord guild " + guild.getName());
                            e.printStackTrace();
                        }
                    } else {
                        BTELinked.warn("Could not get Discord role " + matchingDiscordRoleID + " from Discord guild " + guild.getName());
                    }
                }
            }
        }
    }
}
