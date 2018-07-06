/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.beveragebuddy.ui.views.categorieslist;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.starter.beveragebuddy.backend.Categoria;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.ui.common.AbstractEditorDialog;

/**
 * A dialog for editing {@link Categoria} objects.
 */
public class CategoryEditorDialog extends AbstractEditorDialog<Categoria> {

    private final TextField categoryNameField = new TextField("Nome");

    public CategoryEditorDialog(BiConsumer<Categoria, Operation> itemSaver,
            Consumer<Categoria> itemDeleter) {
        super("category", itemSaver, itemDeleter);

        addNameField();
    }

    private void addNameField() {
        getFormLayout().add(categoryNameField);

        getBinder().forField(categoryNameField)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Título de categoria deve ter ao menos 3 caracteres",
                        3, null))
                .withValidator(
                        name -> CategoryService.getInstance()
                                .findCategories(name).size() == 0,
                        "Título de categoria deve ser único")
                .bind(Categoria::getName, Categoria::setName);
    }

    @Override
    protected void confirmDelete() {
        int reviewCount = ReviewService.getInstance()
                .findReviews(getCurrentItem().getName()).size();
        if (reviewCount > 0) {
            openConfirmationDialog("Apagar categoria",
                    "Você tem certeza que quer apagar a categoria “"
                            + getCurrentItem().getName()
                            + "” ? Tem " + reviewCount
                            + " anúncios associados com esta categoria.",
                    "Apagar uma categoria irá marcar os itens como “undefined”. "
                            + "Você pode editar um por um depois.");
        } else {
            doDelete(getCurrentItem());
        }
    }
}
