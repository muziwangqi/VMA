package com.soling.utils;

public interface SettingPermissionInterface {
    String[] getOneDeinedPermission(String permission);
    void requestOnePermission(String permission);
    String[] getDeinePermission(String[] permission);
    void requestDeinePermission(String[] permission);
}
