package com.github.julionaponucena.financedesktop.modules.registers.data.vos;

import java.util.Set;

public record CategoryPersistenceWrapperDTO(Set<Integer> insertIds, Set<Integer> deleteIds) {
}
