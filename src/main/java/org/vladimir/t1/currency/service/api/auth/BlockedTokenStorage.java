package org.vladimir.t1.currency.service.api.auth;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlockedTokenStorage {
    Set<UUID> blockedTokensIds = new HashSet<>();
    public boolean isBlocked(UUID tokenId) {
        return blockedTokensIds.contains(tokenId);
    }
    public void block(UUID tokenId) {
        blockedTokensIds.add(tokenId);
    }
}
