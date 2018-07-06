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
package com.vaadin.starter.beveragebuddy.ui.views.reviewslist;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.starter.beveragebuddy.backend.Categoria;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.ui.common.AbstractEditorDialog;

/**
 * A dialog for editing {@link Review} objects.
 */
public class ReviewEditorDialog extends AbstractEditorDialog<Review> {

    private transient CategoryService categoryService = CategoryService
            .getInstance();

    private ComboBox<Categoria> categoryBox = new ComboBox<>();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private DatePicker dataPublicacao = new DatePicker();
    private TextField livroNome = new TextField();
    private TextField qtdPublicacao = new TextField();
    private TextField nomeAnunciante = new TextField();

    public ReviewEditorDialog(BiConsumer<Review, Operation> saveHandler,
            Consumer<Review> deleteHandler) {
        super("review", saveHandler, deleteHandler);

        createNameField();
        createCategoryBox();
        createDatePicker();
        creatorName();
        
       // createTimesField();   //Desabilitado por enquanto
       // createScoreBox();     //Desabilitado por enquanto
    }
/*          ////Método seŕa avaliado posteriormente
    private void createScoreBox() {    //desabilitado por enquanto
        scoreBox.setLabel("Avaliação");
        scoreBox.setRequired(true);
        scoreBox.setAllowCustomValue(false);
        scoreBox.setItems("1", "2", "3", "4", "5");
        getFormLayout().add(scoreBox);

        getBinder().forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "A avaliação deve ser um número entre 1 e 5."))
                .withValidator(new IntegerRangeValidator(
                        "", 1, 5))
                .bind(Review::getScore, Review::setScore);
    }
*/
    private void creatorName() {
    	nomeAnunciante.setMinLength(2);  //O nome deve ter tamanho mínimo de 2 caracteres
    	nomeAnunciante.setLabel("Nome do anunciante");
    	nomeAnunciante.setRequired(true);; //prevenir entrada inválida
    	nomeAnunciante.setRequiredIndicatorVisible(true);
        getFormLayout().add(nomeAnunciante);
        
        getBinder().forField(nomeAnunciante)
        .withConverter(String::trim, String::trim)
        .withValidator(new StringLengthValidator(
                "O nome deve ter no mínimo 2 caracteres.",
                2, null))
        .bind(Review::getName, Review::setName);
        
    }
    private void createDatePicker() {
        dataPublicacao.setLabel("Ultimo adicionado");
        dataPublicacao.setRequired(true);
        dataPublicacao.setMax(LocalDate.now());
        dataPublicacao.setMin(LocalDate.of(1, 1, 1));
        dataPublicacao.setValue(LocalDate.now());
        getFormLayout().add(dataPublicacao);

        getBinder().forField(dataPublicacao)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(new DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind(Review::getDate, Review::setDate);

    }

    private void createCategoryBox() {
        categoryBox.setLabel("Categoria");
        categoryBox.setRequired(true);
        categoryBox.setItemLabelGenerator(Categoria::getName);
        categoryBox.setAllowCustomValue(false);
        categoryBox.setItems(categoryService.findCategories(""));
        getFormLayout().add(categoryBox);

        getBinder().forField(categoryBox)
                .withValidator(Objects::nonNull,
                        "A categoria deve ser definida!")
                .bind(Review::getCategory, Review::setCategory);
    }
///////////////MÉTODO PARA QUANTIDADE DE VEZES (DEVE SER ALTERADO DEPOIS)
/*    private void createTimesField() {
        qtdPublicacao.setLabel("Número de exemplares");
        qtdPublicacao.setRequired(true);
        qtdPublicacao.setPattern("[0-9]*");
        qtdPublicacao.setPreventInvalidInput(true);
        getFormLayout().add(qtdPublicacao);

        getBinder().forField(qtdPublicacao)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(new IntegerRangeValidator(
                        "The tasting count must be between 1 and 99.", 1, 99))
                .bind(Review::getCount, Review::setCount);
    }*/

    private void createNameField() {
        livroNome.setLabel("Titulo");
        livroNome.setRequired(true);
        getFormLayout().add(livroNome);

        getBinder().forField(livroNome)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "O título deve ter no mínimo 3 caracteres.",
                        3, null))
                .bind(Review::getName, Review::setName);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Apagar anúncio",
                "Você tem certeza que quer apagar esse anúncio: “" + getCurrentItem().getName() + "”?", "");
    }

}
