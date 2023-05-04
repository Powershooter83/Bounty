package me.prouge.bounty.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class BountyPlayer {

    private UUID uuid;
    private UUID killer;
    private LocalDateTime killDate;
    private List<String> clients;

    private String hash;

}
