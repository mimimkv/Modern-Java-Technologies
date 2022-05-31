package bg.sofia.uni.fmi.mjt.cocktail.server.storage;


import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultCocktailStorageTest {

    private final CocktailStorage storage = new DefaultCocktailStorage();

    @Test
    public void testCreateCocktailNull() {
        assertThrows(IllegalArgumentException.class, () -> storage.createCocktail(null),
                "Exception expected when method called with null argument");
    }

    @Test
    public void testCreateCocktailOneCocktail() throws CocktailAlreadyExistsException {
        storage.createCocktail(new Cocktail("drink1", Set.of(new Ingredient("vodka", "100ml"))));
        Collection<Cocktail> cocktails = storage.getCocktails();

        assertEquals(1, cocktails.size(), "Returned collection does not have correct size");
    }

    @Test
    public void testCreateCocktailAddOneCocktailTwice() throws CocktailAlreadyExistsException {
        storage.createCocktail(new Cocktail("drink1", Set.of(new Ingredient("vodka", "100ml"))));

        assertThrows(CocktailAlreadyExistsException.class,
                () -> storage.createCocktail(new Cocktail("drink1", Set.of(new Ingredient("vodka", "100ml")))),
                "Exception expected when trying to add the same cocktail twice");
    }

    @Test
    public void testGetCocktailsWithIngredientNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> storage.getCocktailsWithIngredient(null),
                "Exception expected when method called with null argument");
    }

    @Test
    public void testGetCocktailsWithIngredient() throws CocktailAlreadyExistsException {
        storage.createCocktail(new Cocktail("drink1", Set.of(new Ingredient("vodka", "100ml"))));
        storage.createCocktail(new Cocktail("drink2", Set.of(new Ingredient("vodka", "100ml"))));
        storage.createCocktail(new Cocktail("drink3", Set.of(new Ingredient("whiskey", "100ml"))));

        assertEquals(2, storage.getCocktailsWithIngredient("vodka").size(),
                "Method does not return correct cocktails with the given ingredient");
    }

    @Test
    public void testGetCocktailNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> storage.getCocktail(null),
                "Exception expected when method called with null argument");
    }

    @Test
    public void testGetCocktailNoSuchCocktail() {
        assertThrows(CocktailNotFoundException.class,
                () -> storage.getCocktail("some random"),
                "Exception expected when method called with null argument");
    }


    @Test
    public void testGetCocktail() throws CocktailAlreadyExistsException, CocktailNotFoundException {
        storage.createCocktail(new Cocktail("drink1", Set.of(new Ingredient("vodka", "100ml"))));
        storage.createCocktail(new Cocktail("drink2", Set.of(new Ingredient("vodka", "100ml"))));
        storage.createCocktail(new Cocktail("drink3", Set.of(new Ingredient("whiskey", "100ml"))));

        Cocktail expected = new Cocktail("drink3", Set.of(new Ingredient("whiskey", "100ml")));
        Cocktail actual = storage.getCocktail("drink3");

        assertEquals(expected, actual, "Method does not return the correct cocktail by its name");
    }

}