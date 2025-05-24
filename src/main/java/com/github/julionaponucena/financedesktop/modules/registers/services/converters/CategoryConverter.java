package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;

public class CategoryConverter {
    public ListCategoryOUTFX convertToListCategoryOutFX(ListCategoryRelOUT categoryOUT) {
        return new ListCategoryOUTFX(categoryOUT.id(), categoryOUT.name());
    }

    public CategoryPersistenceInput convertToInput(CategoryPersistenceInputFX categoryPersistenceInputFX) {
        return new CategoryPersistenceInput(categoryPersistenceInputFX.getId(),
                categoryPersistenceInputFX.getName(),
                categoryPersistenceInputFX.getPersistenceState(),
                categoryPersistenceInputFX.getViewState()
        );
    }
}
