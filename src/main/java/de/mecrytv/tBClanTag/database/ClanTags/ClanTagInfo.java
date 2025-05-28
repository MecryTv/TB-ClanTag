package de.mecrytv.tBClanTag.database.ClanTags;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClanTagInfo {

    private static final Gson GSON = new Gson();
    private static final Type LIST_OF_UUIDS = new TypeToken<List<String>>() {}.getType();

    private String clanTagId;
    private String clanTagName;
    private String clanTagColor;
    private String clanTagOwnerUUID;

    private List<String> clanTagMembers;

    public ClanTagInfo(String clanTagId,
                       String clanTagName,
                       String clanTagColor,
                       String clanTagOwnerUUID,
                       String clanTagMembersJson) {
        this.clanTagId        = clanTagId;
        this.clanTagName      = clanTagName;
        this.clanTagColor     = clanTagColor;
        this.clanTagOwnerUUID = clanTagOwnerUUID;
        this.clanTagMembers   = clanTagMembersJson == null || clanTagMembersJson.isEmpty()
                ? new ArrayList<>()
                : GSON.fromJson(clanTagMembersJson, LIST_OF_UUIDS);
    }

    public ClanTagInfo(String clanTagId,
                       String clanTagName,
                       String clanTagColor,
                       String clanTagOwnerUUID) {
        this(clanTagId, clanTagName, clanTagColor, clanTagOwnerUUID, "[]");
    }

    public String getClanTagId() {
        return clanTagId;
    }

    public void setClanTagId(String clanTagId) {
        this.clanTagId = clanTagId;
    }

    public String getClanTagName() {
        return clanTagName;
    }

    public void setClanTagName(String clanTagName) {
        this.clanTagName = clanTagName;
    }

    public String getClanTagColor() {
        return clanTagColor;
    }

    public void setClanTagColor(String clanTagColor) {
        this.clanTagColor = clanTagColor;
    }

    public String getClanTagOwnerUUID() {
        return clanTagOwnerUUID;
    }

    public void setClanTagOwnerUUID(String clanTagOwnerUUID) {
        this.clanTagOwnerUUID = clanTagOwnerUUID;
    }

    public List<String> getClanTagMembers() {
        return clanTagMembers;
    }

    public void setClanTagMembers(List<String> clanTagMembers) {
        this.clanTagMembers = clanTagMembers;
    }

    public void addMember(String uuid) {
        if (!clanTagMembers.contains(uuid)) {
            clanTagMembers.add(uuid);
        }
    }

    public void removeMember(String uuid) {
        clanTagMembers.remove(uuid);
    }

    public String getClanTagMembersJson() {
        return GSON.toJson(clanTagMembers);
    }
}
