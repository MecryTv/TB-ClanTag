package de.mecrytv.tBClanTag.database.ClanTags;

import de.mecrytv.tBClanTag.TBClanTag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClanTagManager {

    public ClanTagManager() {
        try {
            Connection connection = TBClanTag.getInstance().getDatabaseManager().getConnection();

            String SQL = "CREATE TABLE IF NOT EXISTS `ClanTag` (`clanTagId` VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, `clanTagName` VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, `clanTagColor` VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, `clanTagOwnerUUID` VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, `clanTagMembersUUID` JSON NOT NULL DEFAULT '[]', PRIMARY KEY (`clanTagId`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            TBClanTag.getInstance().getDatabaseManager().closeConnection(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createClanTag(ClanTagInfo clanTagInfo) {
        String SQL = "INSERT INTO `ClanTag` " +
                "(`clanTagId`, `clanTagName`, `clanTagColor`, `clanTagOwnerUUID`, `clanTagMembersUUID`) " +
                "VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = TBClanTag.getInstance()
                .getDatabaseManager()
                .getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, clanTagInfo.getClanTagId());
            ps.setString(2, clanTagInfo.getClanTagName());
            ps.setString(3, clanTagInfo.getClanTagColor());
            ps.setString(4, clanTagInfo.getClanTagOwnerUUID());
            ps.setString(5, clanTagInfo.getClanTagMembersJson()); // liefert "[]"
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteClanTag(String clanTagId) {
        try {
            Connection connection = TBClanTag.getInstance().getDatabaseManager().getConnection();

            String SQL = "DELETE FROM `ClanTag` WHERE `clanTagId` = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, clanTagId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            TBClanTag.getInstance().getDatabaseManager().closeConnection(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateClanTag(ClanTagInfo clanTagInfo) {
        try {
            Connection connection = TBClanTag.getInstance().getDatabaseManager().getConnection();

            String SQL = "UPDATE `ClanTag` SET `clanTagName` = ?, `clanTagColor` = ?, `clanTagOwnerUUID` = ?, `clanTagMembersUUID` = ? WHERE `clanTagId` = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, clanTagInfo.getClanTagName());
            preparedStatement.setString(2, clanTagInfo.getClanTagColor());
            preparedStatement.setString(3, clanTagInfo.getClanTagOwnerUUID());
            preparedStatement.setString(4, clanTagInfo.getClanTagMembersJson());
            preparedStatement.setString(5, clanTagInfo.getClanTagId());

            preparedStatement.executeUpdate();
            preparedStatement.close();

            TBClanTag.getInstance().getDatabaseManager().closeConnection(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClanTagExists(String clanTagId) {
        String SQL = "SELECT COUNT(*) FROM `ClanTag` WHERE `clanTagId` = ?;";
        try (Connection connection = TBClanTag.getInstance().getDatabaseManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, clanTagId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;   // tatsächlichen Count prüfen
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public boolean isUserInAnyClanTag(String userUuid) {
        String SQL = "SELECT COUNT(*) FROM `ClanTag` " +
                "WHERE `clanTagOwnerUUID` = ? " +
                "   OR JSON_CONTAINS(`clanTagMembersUUID`, ?, '$');";
        try (Connection connection = TBClanTag.getInstance().getDatabaseManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, userUuid);
            ps.setString(2, "\"" + userUuid + "\"");  // JSON_CONTAINS erwartet ein JSON-String-Literal
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
