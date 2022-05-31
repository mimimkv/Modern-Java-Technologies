package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultCocktailStorage implements CocktailStorage {

    private final Map<String, Cocktail> cocktails;

    public DefaultCocktailStorage() {
        cocktails = new HashMap<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (cocktail == null) {
            throw new IllegalArgumentException("Cocktail cannot be null");
        }
        if (cocktails.containsKey(cocktail.name())) {
            throw new CocktailAlreadyExistsException("Cocktail is already added");
        }

        cocktails.put(cocktail.name(), cocktail);
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktails.values();
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        if (ingredientName == null || ingredientName.isBlank()) {
            throw new IllegalArgumentException("Ingredient name cannot be null or empty");
        }

        Ingredient ingredient = new Ingredient(ingredientName, "0");
        return cocktails.values()
                .stream()
                .filter(cocktail -> cocktail.ingredients().contains(ingredient))
                .toList();
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (!cocktails.containsKey(name)) {
            throw new CocktailNotFoundException("Cocktail not found");
        }

        return cocktails.get(name);
    }
}
