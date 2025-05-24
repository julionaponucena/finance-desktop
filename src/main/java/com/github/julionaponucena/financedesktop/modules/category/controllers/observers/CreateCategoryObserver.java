package com.github.julionaponucena.financedesktop.modules.category.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.category.data.out.CreateCategoryOUT;

public interface CreateCategoryObserver {
    void onCreate(CreateCategoryOUT categoryOUT);
}
