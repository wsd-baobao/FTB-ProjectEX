package dev.latvian.mods.projectex.block.entity;

public interface TickingEXBlockEntity {
    default void tickCommonPre() {
    }

    default void tickServer() {
    }

    default void tickClient() {
    }

    default void tickCommonPost() {
    }
}
