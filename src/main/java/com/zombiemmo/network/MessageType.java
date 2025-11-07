package com.zombiemmo.network;

/**
 * Types of network messages
 */
public enum MessageType {
    // Client -> Server
    LOGIN,
    MOVE,
    ATTACK,
    GATHER,
    CHAT,

    // Server -> Client
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    PLAYER_UPDATE,
    WORLD_UPDATE,
    COMBAT_RESULT,
    SKILL_UPDATE,
    CHAT_MESSAGE,
    ERROR
}
