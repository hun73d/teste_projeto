package com.vaadin.starter.beveragebuddy.backend;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Simple backend service to store and retrieve {@link Categoria} instances.
 */
public class CategoryService {

    /**
     * Helper class to initialize the singleton Service in a thread-safe way
     * and to keep the initialization ordering clear between the two services.
     * See also:
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class SingletonHolder {
        static final CategoryService INSTANCE = createDemoCategoryService();

        /** This class is not meant to be instantiated. */
        private SingletonHolder() {
        }

        private static CategoryService createDemoCategoryService() {
            CategoryService categoryService = new CategoryService();
            Set<String> categoryNames = new LinkedHashSet<>(
                    StaticData.LIVROS.values());

            categoryNames.forEach(name -> categoryService.saveCategory(new Categoria(name)));

            return categoryService;
        }
    }

    private Map<Long, Categoria> categories = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);

    /**
     * Declared private to ensure uniqueness of this Singleton.
     */
    private CategoryService() {
    }

    /**
     * Gets the unique instance of this Singleton.
     * @return  the unique instance of this Singleton
     */
    public static CategoryService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Fetches the categories whose name matches the given filter text.
     *
     * The matching is case insensitive. When passed an empty filter text,
     * the method returns all categories. The returned list is ordered
     * by name.
     *
     * @param filter    the filter text
     * @return          the list of matching categories
     */
    public List<Categoria> findCategories(String filter) {
        String normalizedFilter = filter.toLowerCase();

        // Make a copy of each matching item to keep entities and DTOs separated
        return categories.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(normalizedFilter))
                .map(Categoria::new)
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Searches for the exact category whose name matches the given filter text.
     *
     * The matching is case insensitive.
     *
     * @param name  the filter text
     * @return      an {@link Optional} containing the category if found,
     *              or {@link Optional#empty()}
     * @throws IllegalStateException    if the result is ambiguous
     */
    public Optional<Categoria> findCategoryByName(String name) {
        List<Categoria> categoriesMatching = findCategories(name);

        if (categoriesMatching.isEmpty()) {
            return Optional.empty();
        }
        if (categoriesMatching.size() > 1) {
            throw new IllegalStateException("Categoria " + name + " é ambígua");
        }
        return Optional.of(categoriesMatching.get(0));
    }

    /**
     * Fetches the exact category whose name matches the given filter text.
     *
     * Behaves like {@link #findCategoryByName(String)}, except that returns
     * a {@link Categoria} instead of an {@link Optional}. If the category
     * can't be identified, an exception is thrown.
     *
     * @param name  the filter text
     * @return      the category, if found
     * @throws IllegalStateException    if not exactly one category matches the given name
     */
    public Categoria findCategoryOrThrow(String name) {
        return findCategoryByName(name)
                .orElseThrow(() -> new IllegalStateException("Category " + name + " does not exist"));
    }

    /**
     * Searches for the exact category with the given id.
     *
     * @param id    the category id
     * @return      an {@link Optional} containing the category if found,
     *              or {@link Optional#empty()}
     */
    public Optional<Categoria> findCategoryById(Long id) {
        Categoria category = categories.get(id);
        return Optional.ofNullable(category);
    }

    /**
     * Deletes the given category from the category store.
     * @param category  the category to delete
     * @return  true if the operation was successful, otherwise false
     */
    public boolean deleteCategory(Categoria category) {
        return categories.remove(category.getId()) != null;
    }

    /**
     * Persists the given category into the category store.
     *
     * If the category is already persistent, the saved category will get updated
     * with the name of the given category object.
     * If the category is new (i.e. its id is null), it will get a new unique id
     * before being saved.
     *
     * @param dto   the category to save
     */
    public void saveCategory(Categoria dto) {
        Categoria entity = categories.get(dto.getId());

        if (entity == null) {
            // Make a copy to keep entities and DTOs separated
            entity = new Categoria(dto);
            if (dto.getId() == null) {
                entity.setId(nextId.incrementAndGet());
            }
            categories.put(entity.getId(), entity);
        } else {
            entity.setName(dto.getName());
        }
    }

}
