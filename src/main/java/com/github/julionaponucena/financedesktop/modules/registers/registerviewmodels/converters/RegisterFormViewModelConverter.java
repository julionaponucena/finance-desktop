package com.github.julionaponucena.financedesktop.modules.registers.registerviewmodels.converters;

import com.github.julionaponucena.financedesktop.commons.listmanager.PersistenceConverter;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInputFX;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryOUTFX;

public class RegisterFormViewModelConverter implements PersistenceConverter<CategoryPersistenceInputFX, ListCategoryOUTFX> {
    @Override
    public CategoryPersistenceInputFX convert(ListCategoryOUTFX category) {
        return new CategoryPersistenceInputFX(category.id(),category.name() ,CategoryPersistenceState.CREATED,CategoryPersistenceState.CREATED);
    }
}
