package com.github.julionaponucena.financedesktop.modules.category.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.category.data.out.UpdateCategoryOUT;

public interface UpdateCategoryObserver {
    void onUpdate(UpdateCategoryOUT categoryOUT);
}
