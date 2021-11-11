package com.xinto.opencord.util

/**
 * @param memberPermissions Permissions in the Member object.
 * @param permission Permission to check.
 * @return Whether or not member has the permission.
 */
fun hasPermission(
    memberPermissions: Int,
    permission: Int
) = memberPermissions and permission == permission