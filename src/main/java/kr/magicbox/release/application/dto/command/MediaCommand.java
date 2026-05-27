package kr.magicbox.release.application.dto.command;

public record MediaCommand(
        String mediaUrl,
        int sortOrder
) {}
